package org.jxapi.generator.java.exchange.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.jxapi.exchange.AbstractExchangeApi;
import org.jxapi.exchange.ExchangeObserver;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.gen.HttpClientDescriptor;
import org.jxapi.exchange.descriptor.gen.NetworkDescriptor;
import org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketClientDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.JavaTypeGenerator;
import org.jxapi.generator.java.exchange.ExchangeConstantValuePlaceholderResolverFactory;
import org.jxapi.generator.java.exchange.ExchangeGenUtil;
import org.jxapi.generator.java.pojo.PojoGenUtil;
import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.netutils.rest.FutureRestResponse;
import org.jxapi.netutils.rest.HttpMethod;
import org.jxapi.netutils.rest.HttpRequest;
import org.jxapi.netutils.rest.HttpRequestUrlParamsSerializer;
import org.jxapi.netutils.rest.RestEndpoint;
import org.jxapi.netutils.rest.ratelimits.RateLimitRule;
import org.jxapi.netutils.rest.ratelimits.RequestThrottler;
import org.jxapi.netutils.serialization.MessageSerializer;
import org.jxapi.netutils.websocket.WebsocketEndpoint;
import org.jxapi.netutils.websocket.WebsocketListener;
import org.jxapi.netutils.websocket.WebsocketSubscribeRequest;
import org.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatcherFactory;
import org.jxapi.pojo.descriptor.CanonicalType;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.pojo.descriptor.UrlParameterType;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.JsonUtil;
import org.jxapi.util.PlaceHolderResolver;
import org.slf4j.Logger;
import org.springframework.util.CollectionUtils;

/**
 * Generates the Java source code for the implementation class of an API
 * interface for an exchange API group, as described in
 * {@link ExchangeApiDescriptor}.
 * <p>
 * This class generates the actual implementation of the API interface, with
 * methods for every REST endpoint call and subscribe/unsubscribe methods for
 * every websocket stream.
 * <ul>
 * <li>The generated class extends {@link AbstractExchangeApi} and implements
 * the API interface.
 * <li>It contains a constructor that initializes the API implementation with
 * the exchange name, properties, and eventual rate limit rules.
 * <li>It contains a method for every REST endpoint call and
 * subscribe/unsubscribe methods for every websocket stream.
 * <li>It contains final deserializers (see {@link MessageDeserializer}) final
 * properties declarations for every REST endpoint response and websocket
 * message.
 * <li>When rate limits are defined for the API group, a final field is
 * generated for this {@link RateLimitRule} with a public getter method.
 * <li>If any of the defined REST endpoints has a rate limit, a
 * {@link RequestThrottler} property is generated and passed to the constructor
 * of the API interface. The generated code for rest endpoint call methods will
 * use this throttler to submit the request, passing it all applicable rate
 * limit rules that should be referenced in REST endpoint descriptor, see
 * {@link RestEndpointDescriptor#getRateLimits()}. Remark: An
 * IllegalArgumentException will be thrown if one if the rate limit rule IDs
 * defined in REST endpoint does not match one of an existing rate limit rule
 * defined in enclosing API group or exchange.
 * <li>A {@link Logger} declaration generated.
 * <li>Base URL for REST endpoints and websocket URL are declared as final
 * members and computed by from API group descriptor baseUrl (see
 * {@link ExchangeApiDescriptor#getHttpUrl()} and properties, replacing
 * eventual placeholders using API group constants, exchange level constants, or
 * configuration properties, and concatenating to the exchange base URL (see
 * {@link EncodingUtil#buildUrl(String...)}.
 * </ul>
 * <p>
 * Regarding REST endpoint call methods generation:
 * <ul>
 * <li>For every REST endpoint, a method is generated with the following
 * signature:
 * 
 * <pre>
 * {@code
 * FutureRestResponse<ResponseType> methodName(RequestType request);
 * }
 * </pre>
 * 
 * <li>For every REST endpoint, a final deserializer is generated for the
 * response type.
 * <li>For every REST endpoint, a final rate limit rule declaration is generated
 * if rate limits are defined for the endpoint.
 * <li>For every REST endpoint, a public static final field is generated with
 * URL of REST endpoint as value. This field is used in the {@link HttpRequest}
 * creation. It has value of the endpoint URL (see
 * RestEndpointDescriptor#getHttpUrl()) if that URL is absolute, or
 * concatenation of API group base URL with it if it is a relative URI.
 * <li>A REST endpoint call method body contains the following steps:
 * <ul>
 * <li>If method has arguments and expects them to be serialized as URL
 * parameters, generate a URL parameters serializer instruction if the endpoint
 * has arguments and expects them to be serialized as URL parameters using
 * {@link EncodingUtil#createUrlQueryParameters(Object...)} method..
 * <li>Generate a DEBUG log statement with the HTTP method, endpoint name and
 * eventual request content.
 * <li>Generate a {@link HttpRequest} using
 * {@link HttpRequest#create(String, String, HttpMethod, Object, List, int, String)}
 * method.
 * <li>Set the url of the request using the endpoint URL static variable.
 * <li>Generate a submit request instruction using protected <code>submit(</code> method.
 * </ul>
 * </ul>
 * <p>
 * Regarding websocket stream subscribe/unsubscribe methods generation:
 * <ul>
 * <li>For every websocket endpoint, a subscribe method is generated with the
 * following signature:
 * 
 * <pre>
 * {@code
 * String subscribeWebsocketEndpointName(RequestType request, WebsocketListener<ResponseType> listener);
 * }
 * </pre>
 * 
 * <li>For every websocket endpoint, an unsubscribe method is generated with the
 * following signature:
 * 
 * <pre>
 * {@code
 * boolean unsubscribeWebsocketEndpointName(String subscriptionId);
 * }
 * </pre>
 * 
 * <li>A websocket endpoint subscribe method body contains the following steps:
 * message type.
 * <li>For every websocket endpoint, a final {@link WebsocketEndpoint} instance
 * is created and initialized in the constructor.
 * <li>For every websocket endpoint, subscribe/unsubscribe methods are generated. 
 * Their implementation uses the corresponding websocket endpoint instance. 
 * </ul>
 * 
 * @see ExchangeApiDescriptor
 * @see AbstractExchangeApi
 */
public class ExchangeApiInterfaceImplementationGenerator extends JavaTypeGenerator {
  
  private static final String PRIVATE_STATIC_FINAL2 = "private static final ";
  private static final String GET_PROPERTIES = ".getProperties()";
  private static final String RETURN = "return ";
  private static final String THIS = "this.";
  private static final String EXCHANGE_ARGUMENT_NAME = "exchange";
  private static final String PRIVATE_FINAL = "private final ";
  private static final String PRIVATE_STATIC_FINAL = PRIVATE_STATIC_FINAL2;
  private static final String OVERRIDE_PUBLIC = "@Override\npublic ";
  private static final String EXCHANGE_OBSERVER_ARG_NAME = "exchangeObserver";
  
  private final ExchangeDescriptor exchangeDescriptor;
  private final PlaceHolderResolver docPlaceHolderResolver;
  
  private final ExchangeApiDescriptor exchangeApiDescriptor;
  private final String simpleInterfaceName;
  
  private Map<String, String> restMethods;
  private Map<String, Map<String, String>> wsMethods;
  private final String simpleImplementationName;
  private final boolean hasRestEnpoints;
  private final boolean hasWsEnpoints;
  
  private List<String> websocketEndpointDeclarations;
  private List<String> restEndpointVariablesDeclarations;
  private List<String> restEndpointUrParamSerializerlDeclarations;
  private List<String> messageSerializerVariablesDeclarations;
  private List<String> messageDeserializerVariablesDeclarations;
  private Map<String, String> restEndpointVariables;
  private Map<String, String> wsEndpointVariables;
  private Map<String, String> restEndpointNamesStaticVariables;
  private Map<String, String> wsEndpointNamesStaticVariables;
  private Map<String, String> restClientStaticVariables;
  private Map<String, String> wsClientStaticVariables;
  
  /**
   * Constructor.
   * 
   * @param exchangeDescriptor    the exchange descriptor where the API is defined
   * @param exchangeApiDescriptor the API descriptor to generate interface implementation for
   */
  public ExchangeApiInterfaceImplementationGenerator(
    ExchangeDescriptor exchangeDescriptor, 
    ExchangeApiDescriptor exchangeApiDescriptor,
    PlaceHolderResolver docPlaceHolderResolver) {
    super(ExchangeGenUtil.getApiInterfaceImplementationClassName(exchangeDescriptor, exchangeApiDescriptor));
    this.exchangeDescriptor = exchangeDescriptor;
    this.exchangeApiDescriptor = exchangeApiDescriptor;
    this.docPlaceHolderResolver = docPlaceHolderResolver;
    setTypeDeclaration("public class");
    String fullInterfaceName = ExchangeGenUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor);
    addImport(fullInterfaceName);
    this.simpleInterfaceName = JavaCodeGenUtil.getClassNameWithoutPackage(fullInterfaceName);
    setImplementedInterfaces(Arrays.asList(fullInterfaceName));
    setParentClassName(AbstractExchangeApi.class.getName());
    setDescription("Actual implementation of {@link " + simpleInterfaceName + "}<br>");
    simpleImplementationName =  JavaCodeGenUtil.getClassNameWithoutPackage(getName());
    hasRestEnpoints = !CollectionUtils.isEmpty(exchangeApiDescriptor.getRestEndpoints());
    hasWsEnpoints = !CollectionUtils.isEmpty(exchangeApiDescriptor.getWebsocketEndpoints());
    
  }

  @Override
  public String generate() {
    websocketEndpointDeclarations = CollectionUtil.createList();
    messageDeserializerVariablesDeclarations = CollectionUtil.createList();
    messageSerializerVariablesDeclarations = CollectionUtil.createList();
    restEndpointVariablesDeclarations = CollectionUtil.createList();
    restEndpointUrParamSerializerlDeclarations = CollectionUtil.createList();
    restMethods = CollectionUtil.createMap();
    wsMethods = CollectionUtil.createMap();
    List<RestEndpointDescriptor> restApis = CollectionUtil.emptyIfNull(exchangeApiDescriptor.getRestEndpoints());
    List<WebsocketEndpointDescriptor> wsApis = CollectionUtil.emptyIfNull(exchangeApiDescriptor.getWebsocketEndpoints());
    NetworkDescriptor network = exchangeDescriptor.getNetwork();
    restClientStaticVariables = ExchangeGenUtil.generateHttpClientNamesStaticVariablesDeclarations(network, null);
    wsClientStaticVariables = ExchangeGenUtil.generateWebsocketClientNamesStaticVariablesDeclarations(network, null);
    
    restEndpointVariables = JavaCodeGenUtil.getUniqueCamelCaseVariableNames(
          restApis.stream().map(RestEndpointDescriptor::getName).toList(),
          false);
    
    wsEndpointVariables = JavaCodeGenUtil.getUniqueCamelCaseVariableNames(
          wsApis.stream().map(WebsocketEndpointDescriptor::getName).toList(),
          false);
    
    restEndpointNamesStaticVariables = ExchangeGenUtil.generateNamesStaticVariablesDeclarations(
        restApis.stream().map(RestEndpointDescriptor::getName).toList(), 
        "RestApi", 
        null,
        "");
    
    wsEndpointNamesStaticVariables = ExchangeGenUtil.generateNamesStaticVariablesDeclarations(
        wsApis.stream().map(WebsocketEndpointDescriptor::getName).toList(), 
        "WsApi", 
        null,
        "");
    
    PlaceHolderResolver constantInDefaultValuesPlaceHolderResolverFactory = 
        new ExchangeConstantValuePlaceholderResolverFactory(exchangeDescriptor).createConstantValuePlaceholderResolver(getImports());

    Map<String, String> restRequestDefaultValuesStaticVariables = appendStaticVariablesDeclarations(
        "REST endpoint request default values", 
        s -> ExchangeApiGenUtil.generateRestEndpointRequestDefaultValuesStaticFieldDeclarations(
          CollectionUtil.emptyIfNull(exchangeApiDescriptor.getRestEndpoints()), 
          getImports(), 
          docPlaceHolderResolver,
          constantInDefaultValuesPlaceHolderResolverFactory,
          s));

    Map<String, String> wsRequestDefaultValuesStaticVariables = appendStaticVariablesDeclarations(
        "Websocket endpoint request default values", 
        s ->  ExchangeApiGenUtil.generateWebsocketEndpointRequestDefaultValuesStaticFieldDeclarations(
          CollectionUtil.emptyIfNull(exchangeApiDescriptor.getWebsocketEndpoints()), 
          getImports(), 
          docPlaceHolderResolver,
          constantInDefaultValuesPlaceHolderResolverFactory,
          s));

    for (RestEndpointDescriptor restApi: restApis) {
        generateRestEndpointMethodDeclaration(restApi, restRequestDefaultValuesStaticVariables);
    }
    
    for (WebsocketEndpointDescriptor wsApi : wsApis) {
      generateWebsocketApiMethodsDeclarations(wsApi, wsRequestDefaultValuesStaticVariables);
    }
    
    appendVariablesToBody(restEndpointUrParamSerializerlDeclarations, "REST endpoints URL parameter serializers");
    appendVariablesToBody(messageSerializerVariablesDeclarations, "Request serializers");
    appendVariablesToBody(messageDeserializerVariablesDeclarations, "Message deserializers");
    appendVariablesToBody(restEndpointVariablesDeclarations, "REST endpoints");
    appendVariablesToBody(websocketEndpointDeclarations, "Websocket endpoints");
    appendSeparatorCommentLine("Constructor");
    appendMethod(generateConstructorSignature(), generateConstructorBody(), generateConstructorJavadoc());
    appendRestMethods();
    appendWsMethods();
    return super.generate();
  }
  
  private Map<String, String> appendStaticVariablesDeclarations(
      String variablesDescription, 
      Function<StringBuilder, Map<String, String>> staticVariablesDeclarationGenerator) {
    StringBuilder staticVariablesDeclarations = new StringBuilder();
    Map<String, String> staticVariables = staticVariablesDeclarationGenerator.apply(staticVariablesDeclarations);
    String decl = staticVariablesDeclarations.toString();
    if (!StringUtils.isBlank(decl)) {
      appendSeparatorCommentLine(variablesDescription);
    }
    appendToBody(staticVariablesDeclarations.toString());
    return staticVariables;
  }
  
  private String generateConstructorJavadoc() {
    return new StringBuilder()
        .append("Constructor\n")
        .append("\n")
        .append("@param exchange the exchange instance\n")
        .append("@param ")
        .append(EXCHANGE_OBSERVER_ARG_NAME)
        .append(" the exchange API observer to dispatch endpoint events to\n")
        .toString();
  }

  private void appendRestMethods() {
    if (!restMethods.isEmpty()) {
      appendSeparatorCommentLine("REST endpoint method call implementations");
      for (Entry<String, String> method : restMethods.entrySet()) {
        appendMethod(method.getKey(), method.getValue());
        appendToBody("\n");
      }
    }
  }
  
  private void appendWsMethods() {
    if (!wsMethods.isEmpty()) {
      appendSeparatorCommentLine("Websocket endpoint subscribe/unsubscribe methods implementations");
      for (Map<String, String> wsStreamMethods : wsMethods.values()) {
        for (Entry<String, String> method : wsStreamMethods.entrySet()) {
          appendMethod(method.getKey(), method.getValue());
          appendToBody("\n");
        }
      }
    }
  }
  
  private void appendVariablesToBody(List<String> variablesDeclaration, String comment) {
    if (!variablesDeclaration.isEmpty()) {
      appendSeparatorCommentLine(comment);
      variablesDeclaration.forEach(v -> appendToBody(v).append("\n"));
    }
  }
  
  private void appendSeparatorCommentLine(String comment) {
    appendToBody("\n// ")
      .append(comment)
      .append("\n");
  }
  
  private String generateConstructorSignature() {
    String exchangeClassName = ExchangeGenUtil.getExchangeInterfaceName(exchangeDescriptor);
    addImport(exchangeClassName);
    addImport(ExchangeObserver.class);
    return new StringBuilder()
        .append("public ")
        .append(simpleImplementationName)
        .append("(")
        .append(JavaCodeGenUtil.getClassNameWithoutPackage(exchangeClassName))
        .append(" ")
        .append(EXCHANGE_ARGUMENT_NAME)
        .append(", ")
        .append(ExchangeObserver.class.getSimpleName())
        .append(" ")
        .append(EXCHANGE_OBSERVER_ARG_NAME)
        .append(")")
        .toString();
  }
  
  private String generateConstructorBody() {
    StringBuilder constructorBody = new StringBuilder();
    constructorBody.append("super(")
       .append(ExchangeApiInterfaceGenerator.EXCHANGE_API_NAME_VARIABLE)
       .append(JavaCodeGenUtil.SUPER_ARG_SEPARATOR)
       .append(EXCHANGE_ARGUMENT_NAME)
      .append(JavaCodeGenUtil.SUPER_ARG_SEPARATOR)
      .append(EXCHANGE_OBSERVER_ARG_NAME)
      .append(JavaCodeGenUtil.SUPER_ARG_SEPARATOR)
      .append(ExchangeGenUtil.generateSubstitutionInstructionDeclaration(
        exchangeApiDescriptor.getHttpUrl(), 
        exchangeDescriptor, 
        List.of(),
        EXCHANGE_ARGUMENT_NAME + GET_PROPERTIES,
        getImports()));
    constructorBody.append(");\n");
    
    if (hasRestEnpoints) {
      generateConstructorBodyRestEndpointDeclarations(constructorBody);
    }
    if (hasWsEnpoints) {
      generateConstructorBodyWebsocketEndpointDeclarations(constructorBody);
    }
    return constructorBody.toString();
  }
  
  private void generateConstructorBodyRestEndpointDeclarations(StringBuilder constructorBody) {
    CollectionUtil.emptyIfNull(exchangeApiDescriptor.getRestEndpoints())
      .forEach(restApi -> constructorBody.append(generateRestEndpointCreationInstruction(restApi)));
  }
  
  private void generateConstructorBodyWebsocketEndpointDeclarations(StringBuilder constructorBody) {
    addImport(EncodingUtil.class);
             
    for (WebsocketEndpointDescriptor wsApi: exchangeApiDescriptor.getWebsocketEndpoints()) {
      String websocketEndpointVariableName = getWebsocketEndpointVariable(wsApi);

      String exchangeClassName = ExchangeGenUtil.getExchangeInterfaceName(exchangeDescriptor);
      addImport(exchangeClassName);
      String wsClientNameVar = getWebsocketClientVariable(wsApi);
      constructorBody.append(THIS)
        .append(websocketEndpointVariableName)
        .append(" = ")
        .append("createWebsocketEndpoint(")
        .append(wsEndpointNamesStaticVariables.get(wsApi.getName()))
        .append(", ")
        .append(JavaCodeGenUtil.getClassNameWithoutPackage(exchangeClassName))
        .append(".")
        .append(wsClientNameVar)
        .append(", ")
        .append(getWebsocketEndpoinMessageDeserializerVariable(wsApi))
        .append(");\n");
    }
  }
  
  private void addRestMethod(String methodDeclaration, String methodBody) {
    restMethods.put(methodDeclaration, methodBody);
  }
  
  private void addWebsocketMethod(String wsStreamName, String methodDeclaration, String methodBody) {
    wsMethods.computeIfAbsent(Optional.ofNullable(wsStreamName).orElse(""), k -> new TreeMap<>())
         .put(methodDeclaration, methodBody);
  }

  private void generateWebsocketApiMethodsDeclarations(
      WebsocketEndpointDescriptor websocketApi, 
      Map<String, String> wsRequestDefaultValuesStaticVariables) {
    Field request = ExchangeApiGenUtil.resolveFieldProperties(exchangeApiDescriptor, websocketApi.getRequest());
    Type requestDataType = PojoGenUtil.getFieldType(request);
    Field message = ExchangeApiGenUtil.resolveFieldProperties(exchangeApiDescriptor, websocketApi.getMessage());
    Type messageDataType = PojoGenUtil.getFieldType(message);
    addImport(WebsocketEndpoint.class);
    boolean hasArguments = ExchangeApiGenUtil.websocketEndpointHasArguments(websocketApi, exchangeApiDescriptor);
    String requestClassName = null;
    String requestSimpleClassName = Object.class.getSimpleName();
    String requestArgName = ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME;
    if (hasArguments) {
      if (requestDataType.isObject()) {
        requestClassName = ExchangeApiGenUtil.generateWebsocketEndpointRequestPojoClassName(
          exchangeDescriptor, 
          exchangeApiDescriptor, 
          websocketApi);
      }
      requestSimpleClassName = PojoGenUtil.getClassNameForType(
        requestDataType, 
        getImports(), 
        requestClassName);
    }
    
    String messageClassObjectName = ExchangeApiGenUtil.generateWebsocketEndpointMessagePojoClassName(
                      exchangeDescriptor, 
                      exchangeApiDescriptor, 
                      websocketApi);
    String messageClassSimpleName = PojoGenUtil.getClassNameForType(
                      messageDataType, 
                      getImports(), 
                      messageClassObjectName);
    String subscribeMethodName = ExchangeApiGenUtil.getWebsocketSubscribeMethodName(websocketApi, exchangeApiDescriptor.getWebsocketEndpoints());
    String unsubscribeMethodName = ExchangeApiGenUtil.getWebsocketUnsubscribeMethodName(websocketApi, exchangeApiDescriptor.getWebsocketEndpoints());
    String websocketEndpointVariableName = getWebsocketEndpointVariable(websocketApi);
    websocketEndpointDeclarations.add(new StringBuilder()
        .append(PRIVATE_FINAL)  
        .append("WebsocketEndpoint<")
        .append(messageClassSimpleName)
        .append("> ")
        .append(websocketEndpointVariableName)
        .append(";").toString());
    addImport(WebsocketListener.class);
    String subscribeMethodSignature = new StringBuilder()
        .append("String ")
        .append(subscribeMethodName)
        .append("(")
        .append(hasArguments? requestSimpleClassName + " " + requestArgName + ", ": "")
        .append("WebsocketListener<")
        .append(messageClassSimpleName)
        .append("> listener)").toString();
    
    String unsubscribeMethodSignature = "boolean " + unsubscribeMethodName + "(String subscriptionId)";
    addImport(WebsocketSubscribeRequest.class);
    
    addImport(WebsocketMessageTopicMatcherFactory.class);
    
    StringBuilder subscribeMethodBody = new StringBuilder()
      .append(generateRequestAsDefaultValueIfNullInstruction(
         wsRequestDefaultValuesStaticVariables.get(ExchangeApiGenUtil.getWebsocketEndpointRequestDefaultValueVariableName(websocketApi))))  
      .append("String topic = ")
      .append(generateWebsocketTopicSerializerDeclaration(websocketApi))
      .append(generateWebsocketTopicMatcherDeclaration(websocketApi))
      .append("\n")
      .append(WebsocketSubscribeRequest.class.getSimpleName())
      .append(" subscribeRequest = ")
      .append(WebsocketSubscribeRequest.class.getSimpleName())
      .append(".create(")
      .append(hasArguments? requestArgName: "null")
      .append(", topic, ");
    
    subscribeMethodBody
      .append(ExchangeApiGenUtil.WEBSOCKET_TOPIC_MATCHER_FACTORY_VAR_NAME)
      .append(");\n")
      .append(RETURN)
      .append(websocketEndpointVariableName)
      .append (".subscribe(subscribeRequest, listener);\n");
    addWebsocketMethod(websocketApi.getName(), OVERRIDE_PUBLIC + subscribeMethodSignature, subscribeMethodBody.toString());
    
    StringBuilder unsubscribeMethodBody = new StringBuilder()
        .append(RETURN)
        .append(websocketEndpointVariableName)
        .append(".unsubscribe(subscriptionId);\n");

    String getResponseDeserializerInstance = PojoGenUtil.getNewJsonFieldDeserializerInstruction(
        messageDataType, 
        messageClassObjectName,
        PojoGenUtil.isExternalClassObjectField(message),
        getImports());
    this.messageDeserializerVariablesDeclarations.add(
        new StringBuilder().append(PRIVATE_STATIC_FINAL)
          .append("MessageDeserializer<")
          .append(messageClassSimpleName)
          .append("> ")
          .append(getWebsocketEndpoinMessageDeserializerVariable(websocketApi))
          .append(" = ")
          .append(getResponseDeserializerInstance)
          .append(";").toString());
    
    addWebsocketMethod(websocketApi.getName(), OVERRIDE_PUBLIC + unsubscribeMethodSignature, unsubscribeMethodBody.toString());
  }
  
  private String generateWebsocketTopicMatcherDeclaration(WebsocketEndpointDescriptor websocketApi) {
    return ExchangeApiGenUtil.generateWebsocketTopicMatcherFactoryDeclaration(
        exchangeDescriptor, 
        exchangeApiDescriptor, 
        websocketApi, 
        getImports());
  }
  
  private String generateWebsocketTopicSerializerDeclaration(WebsocketEndpointDescriptor websocketApi) {
    String defaultTopic = "\"" + websocketApi.getName() + "\"";
    Field request = ExchangeApiGenUtil.resolveFieldProperties(exchangeApiDescriptor, websocketApi.getRequest());
    String declaration = ExchangeApiGenUtil.generateTopicValueSubstitutionInstructionDeclaration(
        websocketApi.getTopic(),
        exchangeDescriptor, 
        request, 
        getImports());
    return Optional.ofNullable(declaration).orElse(defaultTopic) + ";\n";
  }
  
  private void generateRestEndpointMethodDeclaration(
      RestEndpointDescriptor restApi, 
      Map<String, String> restRequestDefaultStaticVariables) {
    Field request = restApi.getRequest();
    Type requestDataType = PojoGenUtil.getFieldType(request);
    Field response = restApi.getResponse();
    Type responseDataType =  PojoGenUtil.getFieldType(response);
    boolean hasArguments = ExchangeApiGenUtil.restEndpointHasArguments(restApi, exchangeApiDescriptor);
    String requestSimpleClassName = "Void";
    String requestArgName = null;
    String getRequestSerializerInstance = null;
    boolean requestHasBody = ExchangeApiGenUtil.restEndpointRequestHasBody(restApi);
    String requestClassName = null;
    boolean externalClassObjectRequestType = false;
    if (hasArguments) {
      requestArgName = ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME;
      if (requestDataType.isObject()) {
        externalClassObjectRequestType = PojoGenUtil.isExternalClassObjectField(request);
        requestClassName = ExchangeApiGenUtil.generateRestEnpointRequestPojoClassName(exchangeDescriptor, exchangeApiDescriptor, restApi);
      }
      requestSimpleClassName = PojoGenUtil.getClassNameForType(
          requestDataType, 
          getImports(), 
          requestClassName); 
      generateRestEndpointUrParameterSerializerlVariableDeclarations(restApi, requestSimpleClassName);
    } else if (requestHasBody) {
      // If request has body but no arguments, the request argument is generated as
      // String in method signature
      requestArgName = ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME;
      requestSimpleClassName = String.class.getSimpleName();
    }
    
    String serializerVariableName = null;
    if (hasArguments && requestHasBody) {
      getRequestSerializerInstance = PojoGenUtil.getNewJsonValueSerializerInstruction(
          requestDataType,
          requestClassName, 
          externalClassObjectRequestType, 
          getImports());
      serializerVariableName = getRestEndpointResponseSerializerVariable(restApi, hasArguments);
      addImport(MessageSerializer.class);
      messageSerializerVariablesDeclarations.add(new StringBuilder()
          .append(PRIVATE_STATIC_FINAL)
          .append(MessageSerializer.class.getSimpleName())
          .append("<")
          .append(requestSimpleClassName)
          .append("> ")
          .append(serializerVariableName)
          .append(" = ")
          .append(getRequestSerializerInstance)
          .append(";")
          .toString());
    }
    
    boolean hasResponse = ExchangeApiGenUtil.restEndpointHasResponse(restApi, exchangeApiDescriptor);
    String responseSimpleClassName = "String";
    String getResponseDeserializerInstance = null;
    
    if (hasResponse) {
      boolean externalClassObjectResponseType = false;
      String restResponseClassName = null;
      if (responseDataType.isObject()) {
        externalClassObjectResponseType = PojoGenUtil.isExternalClassObjectField(response);
        restResponseClassName = ExchangeApiGenUtil.generateRestEnpointResponsePojoClassName(
            exchangeDescriptor, 
            exchangeApiDescriptor, 
            restApi);
      }
      
      responseSimpleClassName = PojoGenUtil.getClassNameForType(
          responseDataType, 
          getImports(), 
          restResponseClassName);
      getResponseDeserializerInstance = PojoGenUtil.getNewJsonFieldDeserializerInstruction(
          responseDataType, 
          restResponseClassName,
          externalClassObjectResponseType,
          getImports());
    } else {
      getResponseDeserializerInstance = PojoGenUtil.getNewJsonFieldDeserializerInstruction(null, null, false, getImports());
    }
    
    addImport(RestEndpoint.class);
    restEndpointVariablesDeclarations.add(new StringBuilder()
        .append(PRIVATE_FINAL)
        .append(RestEndpoint.class.getSimpleName())
        .append("<")
        .append(requestSimpleClassName)
        .append(", ")
        .append(responseSimpleClassName)
        .append("> ")
        .append(getRestEndpointVariable(restApi))
        .append(";")
        .toString());
    
    String deserializerVariableName = getRestEndpointResponseDeserializerVariable(restApi);
    addImport(MessageDeserializer.class);
    messageDeserializerVariablesDeclarations.add(new StringBuilder()
        .append(PRIVATE_STATIC_FINAL)
        .append("MessageDeserializer<")
        .append(responseSimpleClassName)
        .append("> ")
        .append(deserializerVariableName)
        .append(" = ")
        .append(getResponseDeserializerInstance)
        .append(";").toString());
    
    String apiMethodName = ExchangeApiGenUtil.getRestApiMethodName(restApi, exchangeApiDescriptor.getRestEndpoints());
    String apiMethodSignature =  new StringBuilder()
        .append(FutureRestResponse.class.getSimpleName())
        .append("<")
        .append(responseSimpleClassName)
        .append("> ")
        .append(apiMethodName)
        .append("(")
        .append(requestArgName != null? requestSimpleClassName + " " + requestArgName : "")
        .append(")").toString(); 
    
    addImport(FutureRestResponse.class);
    
    StringBuilder apiMethodBody = new StringBuilder()
        .append(generateRequestAsDefaultValueIfNullInstruction(
            restRequestDefaultStaticVariables.get(ExchangeApiGenUtil.getRestEndpointRequestDefaultValueVariableName(restApi))));
    
    apiMethodBody.append(RETURN);
    apiMethodBody.append(createSubmitRequestInstruction(restApi, requestArgName));
    
    addRestMethod(OVERRIDE_PUBLIC + apiMethodSignature, apiMethodBody.toString());
  }
  
  private String createSubmitRequestInstruction(
      RestEndpointDescriptor restApi, 
      String requestVaribale) {
    return new StringBuilder()
        .append(getRestEndpointVariable(restApi))
        .append(".submit(")
        .append(requestVaribale)
        .append(");\n").toString();
  }
  
  private String generateRequestAsDefaultValueIfNullInstruction(String defaultValueStaticVariableName) {
    if (defaultValueStaticVariableName != null) {
      return new StringBuilder()
        .append(ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME)
        .append(" = ")
        .append(JavaCodeGenUtil.generateOptionalOfNullableStatement(
           ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME, 
           defaultValueStaticVariableName, 
           getImports(), 
           false))
        .append("\n")
        .toString();
    }
    return "";
  }
  
  private List<String> getRateLimitsVariables(RestEndpointDescriptor restApi) {
    List<String> rateLimitVariables = new ArrayList<>();
    for (String rateLimitId : CollectionUtil.emptyIfNull(restApi.getRateLimits())) {
      String  pfx = EXCHANGE_ARGUMENT_NAME + ".";
      rateLimitVariables.add(pfx + ExchangeGenUtil.generateRateLimitGetterMethodName(rateLimitId) + "()");
    }
    
    return rateLimitVariables;
  }
   
  private String getRestEndpointResponseDeserializerVariable(RestEndpointDescriptor restApi) {
    return restEndpointNamesStaticVariables.get(restApi.getName()) + "_RESPONSE_DESERIALIZER";
  }
  
  private String getRestEndpointResponseSerializerVariable(RestEndpointDescriptor restApi, boolean hasArguments) {
    if (!hasArguments) {
      return null;
    }
    return restEndpointNamesStaticVariables.get(restApi.getName()) + "_REQUEST_SERIALIZER";
  }
  
  private String getRestEndpointUrlParamsSerializerVariable(RestEndpointDescriptor restApi, boolean hasArguments) {
    if (!hasArguments) {
      return null;
    }
    return restEndpointNamesStaticVariables.get(restApi.getName()) + "_URL_PARAMS_SERIALIZER";
  }
  
  private String getRestEndpointVariable(RestEndpointDescriptor restApi) {
    return restEndpointVariables.get(restApi.getName()) + "RestEndpoint";
  }
  
  private String getWebsocketEndpointVariable(WebsocketEndpointDescriptor websocketApi) {
    return wsEndpointVariables.get(websocketApi.getName()) + "WsEndpoint";
  }
  
  private String getWebsocketEndpoinMessageDeserializerVariable(WebsocketEndpointDescriptor websocketApi) {
    return wsEndpointNamesStaticVariables.get(websocketApi.getName()) + "_MESSAGE_DESERIALIZER";
  }
  
  private String getHttpClientVariable(RestEndpointDescriptor restApi) {
    String httpClientId = Optional
        .ofNullable(restApi.getHttpClient())
        .orElse(exchangeApiDescriptor.getDefaultHttpClient());
    NetworkDescriptor network = getNetworkDescriptor();
    Stream<String> httpClientNamesStream = CollectionUtil.emptyIfNull(network.getHttpClients()).stream()
        .map(HttpClientDescriptor::getName);
    if (httpClientId == null) {
      httpClientId = httpClientNamesStream
          .findFirst()
          .orElseThrow(() -> new IllegalStateException("Cannot resolve HTTP client for REST API '" + restApi.getName()
              + "' since no HTTP client is defined at API or exchange level and exchange network has no HTTP clients defined"));
    } else {
      final String httpClientIdFinal = httpClientId;
      boolean clientExists = httpClientNamesStream.anyMatch(name -> name.equals(httpClientIdFinal));
      if (!clientExists) {
        throw new IllegalStateException("Cannot resolve HTTP client for REST API '" + restApi.getName()
            + "' since HTTP client '" + httpClientId + "' is not defined in exchange network");
      }
    }
    return restClientStaticVariables.get(httpClientId);
  }
  
  private NetworkDescriptor getNetworkDescriptor() {
    NetworkDescriptor network = exchangeDescriptor.getNetwork();
    if (network == null) {
      throw new IllegalStateException("No network defined in '" + exchangeDescriptor);
    }
    return network;
  }
  
  private String getWebsocketClientVariable(WebsocketEndpointDescriptor websocketApi) {
    String wsClientId = Optional
        .ofNullable(websocketApi.getWebsocketClient())
        .orElse( exchangeApiDescriptor.getDefaultWebsocketClient());
    NetworkDescriptor network = getNetworkDescriptor();
    Stream<String> wsClientNamesStream = CollectionUtil.emptyIfNull(network.getWebsocketClients()).stream()
        .map(WebsocketClientDescriptor::getName);
    if (wsClientId == null) {
      wsClientId = wsClientNamesStream
          .findFirst()
          .orElseThrow(() -> new IllegalStateException("Cannot resolve websocket client for websocket API '"
              + websocketApi.getName()
              + "' since no websocket client is defined at API or exchange level and exchange network has no websocket clients defined"));
    } else {
      final String wsClientIdFinal = wsClientId;
      boolean clientExists = wsClientNamesStream.anyMatch(name -> name.equals(wsClientIdFinal));
      if (!clientExists) {
        throw new IllegalStateException("Cannot resolve websocket client for websocket API '" + websocketApi.getName()
            + "' since websocket client '" + wsClientId + "' is not defined in exchange network");
      }
    }
    return wsClientStaticVariables.get(wsClientId);
  }
  
  private String generateRateLimitListVariable(List<String> rateLimitRuleVariableNames) {
    addImport(List.class);
    return new StringBuilder()
        .append("List.of(")
        .append(StringUtils.join(rateLimitRuleVariableNames, ", "))
        .append(")")
        .toString();
  }
  
  private String generateRestEndpointUrlVariableInstantiationInstruction(RestEndpointDescriptor restApi) {
    addImport(EncodingUtil.class);
    StringBuilder sb = new StringBuilder()
        .append(EncodingUtil.class.getSimpleName())
        .append(".buildUrl(this.httpUrl, ")
        .append(ExchangeGenUtil.generateSubstitutionInstructionDeclaration(
            restApi.getUrl(), 
            exchangeDescriptor, 
            List.of(),
            EXCHANGE_ARGUMENT_NAME + GET_PROPERTIES, 
            getImports()))
        .append(")");
    return sb.toString();
  }
  
  private void generateRestEndpointUrParameterSerializerlVariableDeclarations(RestEndpointDescriptor restApi, String requestSimpleClassName) {
      String urlDeclaration = generateRestEndpointUrlVariableDeclaration(restApi);
      addImport(HttpRequestUrlParamsSerializer.class);
      StringBuilder sb = new StringBuilder()
        .append(PRIVATE_STATIC_FINAL2)
        .append(HttpRequestUrlParamsSerializer.class.getSimpleName())
        .append("<")
        .append(requestSimpleClassName)
        .append("> ")
        .append(getRestEndpointUrlParamsSerializerVariable(restApi, true));
      if (urlDeclaration == null) {
        sb.append(" = HttpRequestUrlParamsSerializer.noParams();");
      } else {
        sb.append(" = (request, url) -> ")
          .append(urlDeclaration)
          .append(".toString();");
      }
      restEndpointUrParamSerializerlDeclarations.add(sb.toString());
  }
  
  /**
   * Generates the URL variable declaration for the given REST endpoint.
   * @param restApi the REST endpoint descriptor. Is expected to have arguments.
   * @return the URL variable declaration, or null if the endpoint has no URL parameters
   */
  private String generateRestEndpointUrlVariableDeclaration(RestEndpointDescriptor restApi) {
    Field request = ExchangeApiGenUtil.resolveFieldProperties(
          exchangeApiDescriptor, restApi.getRequest());
    List<String> pathParamArgs = new ArrayList<>();
    collectUrlParameterArguments(
        restApi, 
        request, 
        null, 
        UrlParameterType.PATH, 
        ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME, 
        pathParamArgs);
    List<String> queryParamArgs = new ArrayList<>();
    collectUrlParameterArguments(
        restApi, 
        request, 
        null,
        UrlParameterType.QUERY, 
        ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME,
        queryParamArgs);
    if (pathParamArgs.isEmpty() && queryParamArgs.isEmpty()) {
      return null;
    }
    StringBuilder sb = new StringBuilder()
      .append("new StringBuilder(128).append(url)");
    if (!pathParamArgs.isEmpty()) {
      addImport(EncodingUtil.class);
      sb.append("\n")
        .append(JavaCodeGenUtil.INDENTATION)
        .append(".append(")
        .append(EncodingUtil.class.getSimpleName())
        .append(".createUrlPathParameters(")
        .append(String.join(", ", pathParamArgs))
        .append("))");    
    }

    if (!queryParamArgs.isEmpty()) {
      addImport(EncodingUtil.class);
      sb.append("\n")
        .append(JavaCodeGenUtil.INDENTATION)
        .append(".append(")
        .append(EncodingUtil.class.getSimpleName())
        .append(".createUrlQueryParameters(")
        .append(String.join(", ", queryParamArgs))
        .append("))");
    }
    return sb.toString();  
  }
  
  private void collectUrlParameterArguments(
      RestEndpointDescriptor 
      restApi, 
      Field f, 
      List<Field> sieblings, 
      UrlParameterType urlParameterType, 
      String valuePrefix, 
      List<String> res) {
    f = ExchangeApiGenUtil.resolveFieldProperties(exchangeApiDescriptor, f);
    UrlParameterType defUrlParamType = (Optional
          .ofNullable(restApi.isRequestHasBody())
          .orElse(false) 
        || HttpMethod.valueOf(restApi.getHttpMethod()).requestHasBody)?
         null: 
         UrlParameterType.QUERY;
    UrlParameterType fieldUrlParamType = f.getIn();
    Type type = PojoGenUtil.getFieldType(f);
    List<Field> properties = ExchangeApiGenUtil.resolveFieldProperties(exchangeApiDescriptor, f).getProperties();
    if (sieblings != null) {
      valuePrefix += "." 
        + JavaCodeGenUtil.getGetAccessorMethodName(
          f.getName(),
          type,
          sieblings.stream().map(Field::getName).toList()) 
        + "()";
    }
    if (fieldUrlParamType == null 
        && !CollectionUtil.isEmpty(properties) 
        && type.getCanonicalType() == CanonicalType.OBJECT) { 
      for (Field p : properties) {
        collectUrlParameterArguments(restApi, p, properties, urlParameterType, valuePrefix, res);
      }
    } else if (Optional
        .ofNullable(fieldUrlParamType)
        .orElse(defUrlParamType) == urlParameterType) {
      if (urlParameterType == UrlParameterType.QUERY) {
        String name = Optional 
            .ofNullable(Optional
                .ofNullable(f.getMsgField())
                .orElse(f.getName()))
            .orElse(ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME);
        res.add(JavaCodeGenUtil.getQuotedString(name));
      }
      String value = valuePrefix;
      if (type.getCanonicalType() == CanonicalType.LIST 
          || type.getCanonicalType() == CanonicalType.MAP
          || type.isObject()) {
        addImport(JsonUtil.class);
        value = new StringBuilder()
            .append("JsonUtil.pojoToJsonString(")
            .append(value)
            .append(")")
            .toString();
      }
      res.add(value);
    }
  }
  
  private String generateRestEndpointCreationInstruction(RestEndpointDescriptor restApi) {
    addImport(HttpMethod.class);
    addImport(RestEndpoint.class);
    String name = restApi.getName();
    String restEndpointVariable = getRestEndpointVariable(restApi);
    boolean hasArguments = ExchangeApiGenUtil.restEndpointHasArguments(restApi, exchangeApiDescriptor);
    boolean requestHasBody = ExchangeApiGenUtil.restEndpointRequestHasBody(restApi);
    boolean endpointHasRateLimits = !getRateLimitsVariables(restApi).isEmpty();
    String urlParamsSerializer = getRestEndpointUrlParamsSerializerVariable(restApi, hasArguments);
    if (urlParamsSerializer == null) {
      addImport(HttpRequestUrlParamsSerializer.class);
      urlParamsSerializer = HttpRequestUrlParamsSerializer.class.getSimpleName() + ".noParams()";
    }
    String createRestEndpointMethod = null;
    if (Boolean.TRUE.equals(restApi.isPaginated())) {
      createRestEndpointMethod = "createPaginatedRestEndpoint";
    } else {
      createRestEndpointMethod = "createRestEndpoint";
    }
    String exchangeClassName = ExchangeGenUtil.getExchangeInterfaceName(exchangeDescriptor);
    addImport(exchangeClassName);
    
    String httpClientNameVar = getHttpClientVariable(restApi);
    Integer weight = restApi.getRequestWeight();
    String thisRestEndpointVariable = THIS + restEndpointVariable;
    StringBuilder res = new StringBuilder()
      .append(thisRestEndpointVariable)
      .append(" = ")
      .append(createRestEndpointMethod)
      .append("(")
      .append(restEndpointNamesStaticVariables.get(name))
      .append(", ")
      .append(JavaCodeGenUtil.getClassNameWithoutPackage(exchangeClassName))
      .append(".")
      .append(httpClientNameVar)
      .append(");\n")
      .append(thisRestEndpointVariable)
      .append(".setHttpMethod(")
      .append(HttpMethod.class.getSimpleName())
      .append(".")
      .append(restApi.getHttpMethod())
      .append(");\n")
      .append(thisRestEndpointVariable)
      .append(".setUrl(")
      .append(generateRestEndpointUrlVariableInstantiationInstruction(restApi))
      .append(");\n")
      .append(thisRestEndpointVariable)
      .append(".setUrlParamsSerializer(")
      .append(urlParamsSerializer)
      .append(");\n");
    
    if (requestHasBody) {
      res.append(thisRestEndpointVariable)
         .append(".setSerializer(")
         .append(getRestEndpointResponseSerializerVariable(restApi, hasArguments))
         .append(");\n");
    }
    
    res.append(thisRestEndpointVariable)
      .append(".setDeserializer(")
      .append(getRestEndpointResponseDeserializerVariable(restApi))
      .append(");\n");
    
    if (endpointHasRateLimits) {
      res.append(thisRestEndpointVariable)
        .append(".setRateLimitRules(")
        .append(generateRateLimitListVariable(getRateLimitsVariables(restApi)))
        .append(");\n");
    }
    
    if (weight != null) {
      res.append(thisRestEndpointVariable)
        .append(".setWeight(")
        .append(weight)
        .append(");\n");
    }
    return res.append("\n").toString();
  }
}
