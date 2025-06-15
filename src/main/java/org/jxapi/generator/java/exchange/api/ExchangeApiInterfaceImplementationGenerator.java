package org.jxapi.generator.java.exchange.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.jxapi.exchange.AbstractExchangeApi;
import org.jxapi.exchange.descriptor.CanonicalType;
import org.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.Field;
import org.jxapi.exchange.descriptor.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.Type;
import org.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import org.jxapi.exchange.descriptor.WebsocketMessageTopicMatcherFieldDescriptor;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.JavaTypeGenerator;
import org.jxapi.generator.java.exchange.ExchangeGenUtil;
import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.netutils.rest.FutureRestResponse;
import org.jxapi.netutils.rest.HttpMethod;
import org.jxapi.netutils.rest.HttpRequest;
import org.jxapi.netutils.rest.ratelimits.RateLimitRule;
import org.jxapi.netutils.rest.ratelimits.RequestThrottler;
import org.jxapi.netutils.websocket.WebsocketEndpoint;
import org.jxapi.netutils.websocket.WebsocketListener;
import org.jxapi.netutils.websocket.WebsocketSubscribeRequest;
import org.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatcherFactory;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.JsonUtil;
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
 * {@link ExchangeApiDescriptor#getHttpUrl()} and
 * {@link ExchangeApiDescriptor#getWebsocketUrl()}) properties, replacing
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
 * <li>Generate a submit request instruction using
 * {@link AbstractExchangeApi#submit(HttpRequest, MessageDeserializer)} method.
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
 * <ul>
 * <li>Generate a topic string using
 * {@link EncodingUtil#substituteArguments(String, Object...)} method with
 * endpoint specific topic template and eventual request data.
 * <li>Generate a DEBUG log statement with the endpoint name and eventual
 * request
 * <li>Generate a {@link WebsocketSubscribeRequest} using
 * {@link WebsocketSubscribeRequest#create(Object, String, org.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatcherFactory)}
 * method.
 * <li>Set the request object in the {@link WebsocketSubscribeRequest} if the
 * endpoint has arguments.
 * <li>Submit the {@link WebsocketSubscribeRequest} using
 * {@link WebsocketEndpoint#subscribe(WebsocketSubscribeRequest, WebsocketListener)}
 * method.
 * </ul>
 * <li>For every websocket endpoint, a final deserializer is generated for the
 * message type.
 * <li>For every websocket endpoint, a final {@link WebsocketEndpoint} instance
 * is created and initialized in the constructor using
 * {@link AbstractExchangeApi#createWebsocketEndpoint(String, MessageDeserializer)}
 * method.
 * <li>For every websocket endpoint, a {@link WebsocketSubscribeRequest} is
 * created and submitted using
 * {@link WebsocketEndpoint#subscribe(WebsocketSubscribeRequest, WebsocketListener)}
 * method.
 * </ul>
 * 
 * @see ExchangeApiDescriptor
 * @see AbstractExchangeApi
 */
public class ExchangeApiInterfaceImplementationGenerator extends JavaTypeGenerator {
  
  private static final String GET_PROPERTIES = ".getProperties()";
  private static final String RETURN = "return ";
  private static final String THIS = "this.";
  private static final String EXCHANGE_ARGUMENT_NAME = "exchange";
  private static final String REQUEST_THROTTLER_VARIABLE_NAME = "requestThrottler";
  private static final String PRIVATE_FINAL = "private final ";
  private static final String OVERRIDE_PUBLIC = "@Override\npublic ";
  private static final String WS_URL_PROPERTY_NAME = "wsUrl";
  
  private final ExchangeDescriptor exchangeDescriptor;
  
  private final ExchangeApiDescriptor exchangeApiDescriptor;
  private final String simpleInterfaceName;
  
  private Map<String, String> restMethods;
  private Map<String, Map<String, String>> wsMethods;
  private final boolean hasRateLimits;
  private final String simpleImplementationName;
  private final List<RateLimitRule> exchangeRateLimits;
  private final boolean hasExchangeLimits;
  private final boolean hasRestEnpoints;
  private final boolean hasWsEnpoints;
  
  private List<String> restEndpointUrlDeclarations;
  private List<String> rateLimitVariablesDeclarations;
  private List<String> rateLimitVariablesInstantiationDeclarations;
  private List<String> websocketEndpointDeclarations;
  private List<String> messageDeserializerVariablesDeclarations;
  
  
  /**
   * Constructor.
   * 
   * @param exchangeDescriptor    the exchange descriptor where the API is defined
   * @param exchangeApiDescriptor the API descriptor to generate interface implementation for
   */
  public ExchangeApiInterfaceImplementationGenerator(ExchangeDescriptor exchangeDescriptor, 
                             ExchangeApiDescriptor exchangeApiDescriptor) {
    super(ExchangeGenUtil.getApiInterfaceImplementationClassName(exchangeDescriptor, exchangeApiDescriptor));
    this.exchangeDescriptor = exchangeDescriptor;
    this.exchangeApiDescriptor = exchangeApiDescriptor;
    setTypeDeclaration("public class");
    String fullInterfaceName = ExchangeGenUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor);
    addImport(fullInterfaceName);
    this.simpleInterfaceName = JavaCodeGenUtil.getClassNameWithoutPackage(fullInterfaceName);
    setImplementedInterfaces(Arrays.asList(fullInterfaceName));
    setParentClassName(AbstractExchangeApi.class.getName());
    setDescription("Actual implementation of {@link " + simpleInterfaceName + "}<br>");
    hasRateLimits = checkHasRateLimits();
    simpleImplementationName =  JavaCodeGenUtil.getClassNameWithoutPackage(getName());
    exchangeRateLimits = exchangeDescriptor.getRateLimits();
    hasExchangeLimits = !CollectionUtils.isEmpty(exchangeRateLimits);
    hasRestEnpoints = !CollectionUtils.isEmpty(exchangeApiDescriptor.getRestEndpoints());
    hasWsEnpoints = !CollectionUtils.isEmpty(exchangeApiDescriptor.getWebsocketEndpoints());
  }

  @Override
  public String generate() {
    restEndpointUrlDeclarations = new ArrayList<>();
    rateLimitVariablesDeclarations = new ArrayList<>();
    rateLimitVariablesInstantiationDeclarations = new ArrayList<>();
    websocketEndpointDeclarations = new ArrayList<>();
    messageDeserializerVariablesDeclarations = new ArrayList<>();
    restMethods = new LinkedHashMap<>();
    wsMethods = new LinkedHashMap<>();
    generateApiGlobalRateLimits();
    
    for (RestEndpointDescriptor restApi: CollectionUtil.emptyIfNull(exchangeApiDescriptor.getRestEndpoints())) {
        generateRestEndpointMethodDeclaration(restApi);
    }
    
    for (WebsocketEndpointDescriptor websocketApi : CollectionUtil.emptyIfNull(exchangeApiDescriptor.getWebsocketEndpoints())) {
      generateWebsocketApiMethodsDeclarations(websocketApi);
    }
    appendVariablesToBody(restEndpointUrlDeclarations, "REST endpoint URLs");
    appendVariablesToBody(rateLimitVariablesDeclarations, "REST endpoints rate limits");
    appendVariablesToBody(websocketEndpointDeclarations, "Websocket endpoints");
    appendVariablesToBody(messageDeserializerVariablesDeclarations, "Message deserializers");
    appendSeparatorCommentLine("Constructor");
    appendMethod(generateConstructorSignature(), generateConstructorBody());
    appendRestMethods();
    appendWsMethods();
    generateRateLimitRuleGetters();
    return super.generate();
  }
  
  private void generateApiGlobalRateLimits() {
    for (RateLimitRule rule : CollectionUtil.emptyIfNull(exchangeApiDescriptor.getRateLimits())) {
      generateRateLimitVariable(rule);
    }
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
    StringBuilder constructorSignature = new StringBuilder()
        .append("public ")
        .append(simpleImplementationName)
        .append("(")
        .append(JavaCodeGenUtil.getClassNameWithoutPackage(exchangeClassName))
        .append(" ")
        .append(EXCHANGE_ARGUMENT_NAME);
    if (hasExchangeLimits && hasRestEnpoints) {
      addImport(RequestThrottler.class);
      constructorSignature.append(", ")
                .append(RequestThrottler.class.getSimpleName())
                .append(" ")
                .append(REQUEST_THROTTLER_VARIABLE_NAME);
    }
    constructorSignature.append(")");
    return constructorSignature.toString();
  }
  
  private String generateConstructorBody() {
    StringBuilder constructorBody = new StringBuilder();
    constructorBody.append("super(")
       .append(ExchangeApiInterfaceGenerator.EXCHANGE_API_NAME_VARIABLE)
       .append(JavaCodeGenUtil.SUPER_ARG_SEPARATOR)
       .append(EXCHANGE_ARGUMENT_NAME);
    if ((hasRateLimits || hasExchangeLimits) 
        && (hasRestEnpoints)) {
      addImport(RequestThrottler.class);
      
      if (hasExchangeLimits) {
        constructorBody
          .append(JavaCodeGenUtil.SUPER_ARG_SEPARATOR)
          .append(REQUEST_THROTTLER_VARIABLE_NAME);
      } else {
        constructorBody
          .append(JavaCodeGenUtil.SUPER_ARG_SEPARATOR)
          .append("new ")
          .append(RequestThrottler.class.getSimpleName())
          .append("(\"")
          .append(exchangeDescriptor.getId())
          .append(exchangeApiDescriptor.getName())
          .append("\")")
          .toString();
      }
    } else {
      constructorBody
          .append(JavaCodeGenUtil.SUPER_ARG_SEPARATOR)
          .append("null");
    }
    constructorBody
      .append(JavaCodeGenUtil.SUPER_ARG_SEPARATOR)
      .append(ExchangeGenUtil.generateSubstitutionInstructionDeclaration(
        exchangeApiDescriptor.getHttpUrl(), 
        exchangeDescriptor, 
        exchangeApiDescriptor, 
        EXCHANGE_ARGUMENT_NAME + GET_PROPERTIES,
        getImports()))
      .append(JavaCodeGenUtil.SUPER_ARG_SEPARATOR)
      .append(ExchangeGenUtil.generateSubstitutionInstructionDeclaration(
        exchangeApiDescriptor.getWebsocketUrl(), 
        exchangeDescriptor, 
        exchangeApiDescriptor, 
        EXCHANGE_ARGUMENT_NAME + GET_PROPERTIES, 
        getImports()));
    constructorBody.append(");\n");
  
    for (String rateLimitVariableInstantationDeclaration : rateLimitVariablesInstantiationDeclarations) {
      constructorBody.append(rateLimitVariableInstantationDeclaration);
    }
    
    if (hasRestEnpoints) {
      generateConstructorBodyRestEndpointDeclarations(constructorBody);
    }
    if (hasWsEnpoints) {
      generateConstructorBodyWebsocketEndpointDeclarations(constructorBody);
    }
    return constructorBody.toString();
  }
  
  private void generateConstructorBodyRestEndpointDeclarations(StringBuilder constructorBody) {
    String httpRequestExecutorFactoryClassName  = getHttpRequestExecutorFactory();
    String httpRequestTimeout = getHttpRequestTimeout() + "L";
    constructorBody
       .append("createHttpRequestExecutor(")
       .append(httpRequestExecutorFactoryClassName != null? 
             "\"" + httpRequestExecutorFactoryClassName + "\""
             : "null")
       .append(", ")
       .append(httpRequestTimeout)
       .append(");\n");
    
    String httpRequestInterceptorFactoryFullClassName = getHttpRequestInterceptorFactory();
    if (httpRequestInterceptorFactoryFullClassName != null) {
      constructorBody.append("createHttpRequestInterceptor(\"")
               .append(httpRequestInterceptorFactoryFullClassName)
               .append("\");\n");
    }
    exchangeApiDescriptor.getRestEndpoints().forEach(restApi -> 
      constructorBody.append(generateRestEndpointUrlVariableInstantiationInstruction(restApi)));
  }
  
  private void generateConstructorBodyWebsocketEndpointDeclarations(StringBuilder constructorBody) {
    addImport(EncodingUtil.class);
    constructorBody.append("createWebsocketManager(")
             .append(THIS)
             .append(WS_URL_PROPERTY_NAME)
             .append(", ")
             .append(JavaCodeGenUtil.getQuotedString(getWebsocketFactory()))
             .append(", ")
             .append(JavaCodeGenUtil.getQuotedString(getWebsocketHookFactory()))
             .append(");\n");
             
    for (WebsocketEndpointDescriptor wsApi: exchangeApiDescriptor.getWebsocketEndpoints()) {
      String websocketEndpointVariableName = getWebsocketEndpointVariableName(wsApi);
      String messageClassObjectName = ExchangeApiGenUtil.generateWebsocketEndpointMessagePojoClassName(
          exchangeDescriptor, 
          exchangeApiDescriptor, 
          wsApi);
      Field message = ExchangeApiGenUtil.resolveFieldProperties(exchangeApiDescriptor, wsApi.getMessage());
      Type messageDataType = ExchangeGenUtil.getFieldType(message);
      String getResponseDeserializerInstance = ExchangeApiGenUtil.getNewMessageDeserializerInstruction(messageDataType, messageClassObjectName, getImports());
      constructorBody.append(THIS)
        .append(websocketEndpointVariableName)
        .append(" = ")
        .append("createWebsocketEndpoint(")
        .append(ExchangeApiGenUtil.getWebsocketEndpointNameStaticVariable(wsApi.getName()))
        .append(", ")
        .append(getResponseDeserializerInstance)
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
  
  private String getWebsocketEndpointVariableName(WebsocketEndpointDescriptor websocketApi) {
    return JavaCodeGenUtil.firstLetterToLowerCase(websocketApi.getName()) + "Ws";
  }

  private void generateWebsocketApiMethodsDeclarations(WebsocketEndpointDescriptor websocketApi) {
    Field request = ExchangeApiGenUtil.resolveFieldProperties(exchangeApiDescriptor, websocketApi.getRequest());
    Type requestDataType = ExchangeGenUtil.getFieldType(request);
    Field message = ExchangeApiGenUtil.resolveFieldProperties(exchangeApiDescriptor, websocketApi.getMessage());
    Type messageDataType = ExchangeGenUtil.getFieldType(message);
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
      requestSimpleClassName = ExchangeGenUtil.getClassNameForType(
                          requestDataType, 
                          getImports(), 
                          requestClassName);
    }
    
    String messageClassObjectName = ExchangeApiGenUtil.generateWebsocketEndpointMessagePojoClassName(
                      exchangeDescriptor, 
                      exchangeApiDescriptor, 
                      websocketApi);
    String messageClassSimpleName = ExchangeGenUtil.getClassNameForType(
                      messageDataType, 
                      getImports(), 
                      messageClassObjectName);
    String subscribeMethodName = ExchangeApiGenUtil.getWebsocketSubscribeMethodName(websocketApi);
    String unsubscribeMethodName = ExchangeApiGenUtil.getWebsocketUnsubscribeMethodName(websocketApi);
    String websocketEndpointVariableName = getWebsocketEndpointVariableName(websocketApi);
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
      .append("String topic = ")
      .append(generateWebsocketTopicSerializerDeclaration(websocketApi))
      .append(WebsocketSubscribeRequest.class.getSimpleName())
      .append(" subscribeRequest = ")
      .append(WebsocketSubscribeRequest.class.getSimpleName())
      .append(".create(")
      .append(hasArguments? requestArgName: "null")
      .append(", topic, ");
    
    subscribeMethodBody
      .append(generateWebsocketTopicMatcherDeclaration(websocketApi, request, message, requestArgName, requestClassName))
      .append(");\n")
      .append(RETURN)
      .append(websocketEndpointVariableName)
      .append (".subscribe(subscribeRequest, listener);\n");
    addWebsocketMethod(websocketApi.getName(), OVERRIDE_PUBLIC + subscribeMethodSignature, subscribeMethodBody.toString());
    
    
    StringBuilder unsubscribeMethodBody = new StringBuilder()
        .append(RETURN)
        .append(websocketEndpointVariableName)
        .append(".unsubscribe(subscriptionId);\n");
    addWebsocketMethod(websocketApi.getName(), OVERRIDE_PUBLIC + unsubscribeMethodSignature, unsubscribeMethodBody.toString());
  }
  
  private String generateWebsocketTopicMatcherDeclaration(WebsocketEndpointDescriptor websocketApi, 
                              Field request, 
                              Field msg,
                              String requestArgName, 
                              String requestClassName) {
    StringBuilder topicMatcherDeclaration = new StringBuilder()
        .append(WebsocketMessageTopicMatcherFactory.class.getSimpleName())
        .append(".create(");
    
    List<String> replacements = new ArrayList<>();
    replacements.add("topic");
    replacements.add("\" + topic + \"");
    if (request != null) {
      replacements.add(ExchangeApiGenUtil.getRequestArgName(request.getName()));
      replacements.add("\" + " + requestArgName + " + \"");
    }
    List<Field> requestFields = CollectionUtil.emptyIfNull(request == null? null: request.getProperties());
    for (Field param: requestFields) {
      replacements.add(param.getMsgField() != null? param.getMsgField(): param.getName());
      String fieldClass = ExchangeGenUtil.getClassNameForType(param.getType(), getImports(), requestClassName);
      replacements.add("\" + request." 
                 + JavaCodeGenUtil.getGetAccessorMethodName(
                         param.getName(), 
                         fieldClass, 
                         requestFields
                                .stream()
                                .map(Field::getName)
                         .collect(Collectors.toList())) 
                 + "() + \"");
    }
    
    if (!CollectionUtil.isEmpty(websocketApi.getMessageTopicMatcherFields())) {
      for (int i = 0; i < websocketApi.getMessageTopicMatcherFields().size(); i++) {
        WebsocketMessageTopicMatcherFieldDescriptor topicMatcherField = websocketApi.getMessageTopicMatcherFields().get(i);
        String topicMatcherFieldDeclaration = new StringBuilder()
            .append("\"")
            .append(getMsgFieldName(topicMatcherField.getName(), msg))
            .append("\", ")
            .append(EncodingUtil.substituteArguments("\"" + topicMatcherField.getValue() + "\"", (Object[]) replacements.toArray(new String[replacements.size()])))
            .toString();
        String emtpyStrAppend = " + \"\"";
        if (topicMatcherFieldDeclaration.endsWith(emtpyStrAppend)) {
          topicMatcherFieldDeclaration = topicMatcherFieldDeclaration.substring(0, topicMatcherFieldDeclaration.length() - emtpyStrAppend.length());
        }
        topicMatcherDeclaration.append(topicMatcherFieldDeclaration);  
        if (i < websocketApi.getMessageTopicMatcherFields().size() - 1) {
          topicMatcherDeclaration.append(", ");
        }
      }
    }
    return topicMatcherDeclaration.append(")").toString();
  }
  
  /**
   * Finds actual field name provided in JSON stream for a field with given name
   * 
   * @param name The field name provided in
   *             {@link WebsocketMessageTopicMatcherFieldDescriptor}
   * @param msg  The websocket stream data type
   * @return The msgFieldName (see {@link Field#getMsgField()}) of provided field
   *         or first of its child fields (see {@link Field#getProperties()} with
   *         name see {@link Field#getName()} matching that field.
   */
  private String getMsgFieldName(String name, Field msg) {
    Type messageDataType = ExchangeGenUtil.getFieldType(msg);
    if (Objects.equals(msg.getName(), name)) {
      return Optional.ofNullable(msg.getMsgField()).orElse(name);
    } else if (messageDataType.isObject()) {
      for (Field c: msg.getProperties()) {
        String res = getMsgFieldName(name, c);
        if (res != null) {
          return res;
        }
      }
    }
    return null;
  }
  
  private String generateWebsocketTopicSerializerDeclaration(WebsocketEndpointDescriptor websocketApi) {
    String topicSerializerBody = "\"\"";
    Field request = ExchangeApiGenUtil.resolveFieldProperties(exchangeApiDescriptor, websocketApi.getRequest());
    Type requestDataType = ExchangeGenUtil.getFieldType(request);
    if (ExchangeApiGenUtil.websocketEndpointHasArguments(websocketApi, exchangeApiDescriptor)) {
      if (websocketApi.getTopic() != null) {
        topicSerializerBody =  generateUrlParametersOrTopicSerializerBodyFromTemplate(
                        websocketApi.getTopic(), 
                        request.getProperties(), 
                        websocketApi.getTopicParametersListSeparator(),
                        websocketApi.getRequest().getName(),
                        requestDataType);
      } else {
        topicSerializerBody = "request == null? \"\": \"\" + JsonUtil.pojoToJsonString(request)";
      }      
    } else if (websocketApi.getTopic() != null) {
      topicSerializerBody = "\"" + websocketApi.getTopic() + "\"";
    }
    
    return topicSerializerBody + ";\n";
  }
  
  private void generateRestEndpointMethodDeclaration(RestEndpointDescriptor restApi) {
    Field request = restApi.getRequest();
    Type requestDataType = ExchangeGenUtil.getFieldType(request);
    Field response = restApi.getResponse();
    Type responseDataType =  ExchangeGenUtil.getFieldType(response);
    boolean hasArguments = ExchangeApiGenUtil.restEndpointHasArguments(restApi, exchangeApiDescriptor);
    String requestSimpleClassName = "Object";
    String requestArgName = ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME;
    
    if (hasArguments) {
      String requestClassName = null;
      if (requestDataType.isObject()) {
        requestClassName = ExchangeApiGenUtil.generateRestEnpointRequestPojoClassName(exchangeDescriptor, exchangeApiDescriptor, restApi);
      }
      requestSimpleClassName = ExchangeGenUtil.getClassNameForType(
          requestDataType, 
          getImports(), 
          requestClassName);
    }
    boolean hasResponse = ExchangeApiGenUtil.restEndpointHasResponse(restApi, exchangeApiDescriptor);
    String responseSimpleClassName = "String";
    String getResponseDeserializerInstance = null;
    if (hasResponse) {
      String restResponseClassName = null;
      if (responseDataType.isObject()) {
        restResponseClassName = ExchangeApiGenUtil.generateRestEnpointResponsePojoClassName(
            exchangeDescriptor, 
            exchangeApiDescriptor, 
            restApi);
      }
      
      responseSimpleClassName = ExchangeGenUtil.getClassNameForType(
          responseDataType, 
          getImports(), 
          restResponseClassName);
      getResponseDeserializerInstance = ExchangeApiGenUtil.getNewMessageDeserializerInstruction(
          responseDataType, 
          restResponseClassName,
          getImports());
    } else {
      getResponseDeserializerInstance = ExchangeApiGenUtil.getNewMessageDeserializerInstruction(null, null, getImports());
    }
    
    String deserializerVariableName = JavaCodeGenUtil.firstLetterToLowerCase(restApi.getName()) + "ResponseDeserializer";
    addImport(MessageDeserializer.class);
    messageDeserializerVariablesDeclarations.add(new StringBuilder()
        .append(PRIVATE_FINAL)
        .append("MessageDeserializer<")
        .append(responseSimpleClassName)
        .append("> ")
        .append(deserializerVariableName)
        .append(" = ")
        .append(getResponseDeserializerInstance)
        .append(";").toString());
    
    String apiMethodName = ExchangeApiGenUtil.getRestApiMethodName(restApi);
    String apiMethodSignature =  new StringBuilder()
                      .append(FutureRestResponse.class.getSimpleName())
                      .append("<")
                      .append(responseSimpleClassName)
                      .append("> ")
                      .append(apiMethodName)
                      .append("(")
                      .append(hasArguments? requestSimpleClassName + " " + requestArgName : "")
                      .append(")").toString(); 
    
    addImport(FutureRestResponse.class);
    String urlParametersSerializerVariableName = "urlParameters";
    String urlParametersSerializerDeclaration = generateUrlParametersSerializerDeclaration(restApi);
    if (urlParametersSerializerDeclaration != null) {
      urlParametersSerializerDeclaration = "String " + urlParametersSerializerVariableName + " = " + urlParametersSerializerDeclaration;
    }
    StringBuilder apiMethodBody = new StringBuilder()
        .append(Optional.ofNullable(urlParametersSerializerDeclaration).orElse(""));
    
    List<String> rateLimitVariables = getRateLimitsVariables(restApi);
    
    apiMethodBody.append(RETURN);
    
    String rateLimitsVariable = "null";
    int requestWeight = Optional.ofNullable(restApi.getRequestWeight()).orElse(0);
    String restEndpointJavadocLink = getRestEndpointJavaDocLink(restApi, hasArguments? requestSimpleClassName: null);
    if (!rateLimitVariables.isEmpty()) {
      addImport(RateLimitRule.class);
      rateLimitsVariable = JavaCodeGenUtil.firstLetterToLowerCase(restApi.getName()) + "RateLimits";
      generateRateLimitListVariable(rateLimitsVariable, restApi.getName(), restEndpointJavadocLink, rateLimitVariables);
    }
    
    addImport(HttpRequest.class);
    addRestEndpointUrlDeclarations(restApi, restEndpointJavadocLink);
    String endpointUrlVar = ExchangeApiGenUtil.getRestEndpointUrlVariableName(restApi);
    StringBuilder createHttpRequestInstruction = new StringBuilder()
        .append("HttpRequest.create(")
        .append(ExchangeApiGenUtil.getRestEndpointNameStaticVariable(restApi.getName()))
        .append(", ")
        .append(endpointUrlVar);
    if (urlParametersSerializerDeclaration != null) {
      createHttpRequestInstruction
        .append(" + ")
        .append(urlParametersSerializerVariableName);
    }
    addImport(HttpMethod.class);
    createHttpRequestInstruction.append(", HttpMethod.")
        .append(restApi.getHttpMethod().name())
        .append(", ")
        .append(hasArguments? requestArgName: "null")
        .append(", ")
        .append(rateLimitsVariable)
        .append(", ")
        .append(requestWeight)
        .append(")");
        
    StringBuilder submitRequestInstruction = new StringBuilder();
    if (restApi.isPaginated()) {
      submitRequestInstruction.append("submitPaginated(");
    } else {
      submitRequestInstruction.append("submit(");
    }
    submitRequestInstruction
      .append(createHttpRequestInstruction.toString())
      .append(", ")
      .append(deserializerVariableName);
    if (restApi.isPaginated()) {
      submitRequestInstruction
        .append(", this::")
        .append(ExchangeApiGenUtil.getRestApiMethodName(restApi));
    }
    submitRequestInstruction.append(");\n");
    apiMethodBody.append(submitRequestInstruction.toString());
    
    addRestMethod(OVERRIDE_PUBLIC + apiMethodSignature, apiMethodBody.toString());
  }
  
  private List<String> getRateLimitsVariables(RestEndpointDescriptor restApi) {
    List<String> rateLimitVariables = new ArrayList<>();
    
    for (String rateLimitId : CollectionUtil.emptyIfNull(restApi.getRateLimits())) {
      String  pfx = "";
      boolean isExchangeLimit = CollectionUtil.emptyIfNull(exchangeDescriptor.getRateLimits())
                                              .stream()
                                              .anyMatch(l -> l.getId().equals(rateLimitId));
      if (isExchangeLimit) {
        pfx = EXCHANGE_ARGUMENT_NAME + ".";
      } else if (CollectionUtil.emptyIfNull(exchangeApiDescriptor.getRateLimits())
                                              .stream()
                                              .noneMatch(l -> l.getId().equals(rateLimitId))) {
          throw new IllegalArgumentException(
              String.format("Rate limit rule with id '%s' is not defined in '%s' exchange descriptor nor in '%s' API group descriptor.",
                            rateLimitId, 
                            exchangeDescriptor.getId(), 
                            exchangeApiDescriptor.getName()));
      }
      rateLimitVariables.add(pfx + ExchangeGenUtil.generateRateLimitGetterMethodName(rateLimitId) + "()");
    }
    
    return rateLimitVariables;
  }
  
  private String getRestEndpointJavaDocLink(RestEndpointDescriptor restApi, 
                        String requestSimpleClassName) {
    return new StringBuilder()
          .append(this.simpleInterfaceName)
          .append("#")
          .append(ExchangeApiGenUtil.getRestApiMethodName(restApi))
          .append("(")
          .append(JavaCodeGenUtil.getMethodJavadocArgumentDeclaration(requestSimpleClassName))
          .append(")").toString();
  }
  
  private void addRestEndpointUrlDeclarations(RestEndpointDescriptor restApi, 
                                              String interfaceJavadocLink) {
    StringBuilder javadoc = new StringBuilder()
        .append("URL for <i>")
        .append(restApi.getName())
        .append("</i> REST endpoint.\n")
        .append("@see ")
        .append(interfaceJavadocLink);
    StringBuilder sb = new StringBuilder()
        .append("\n")
        .append(JavaCodeGenUtil.generateJavaDoc(javadoc.toString()))
        .append("\n")
        .append("protected final String ")
        .append(ExchangeApiGenUtil.getRestEndpointUrlVariableName(restApi))
        .append(";");
    restEndpointUrlDeclarations.add(sb.toString()); 
  }
  
  private boolean checkHasRateLimits() {
    return CollectionUtil.emptyIfNull(exchangeApiDescriptor.getRestEndpoints())
              .stream()
              .anyMatch(restEndpoint -> !CollectionUtil.isEmpty(restEndpoint.getRateLimits()));
  }
  
  private String generateRateLimitVariable(RateLimitRule rateLimitRule) {
    String id = rateLimitRule.getId();
    if (!JavaCodeGenUtil.isValidCamelCaseIdentifier(id)) {
      throw new IllegalArgumentException(String.format(
          "Rate limit rule id '%s' is not valid camel case identifier in '%s' API group.",
            id,
            exchangeApiDescriptor.getName()));
    }
    String variableName = ExchangeGenUtil.generateRateLimitVariableName(id);
    addImport(RateLimitRule.class);
    StringBuilder declaration =  new StringBuilder()
      .append(PRIVATE_FINAL)
      .append(RateLimitRule.class.getSimpleName())
      .append(" ")
      .append(variableName)
      .append(";");
    rateLimitVariablesDeclarations.add(declaration.toString());
    StringBuilder instantiationDeclaration = new StringBuilder()
        .append(THIS)
        .append(variableName)
        .append(" = ");
    if (rateLimitRule.getMaxTotalWeight() >= 0) {
      instantiationDeclaration
        .append("RateLimitRule.createWeightedRule(\"")
        .append(id)
        .append("\", ")
        .append(rateLimitRule.getTimeFrame())
        .append(", ")
        .append(rateLimitRule.getMaxTotalWeight());
    } else {
      instantiationDeclaration
        .append("RateLimitRule.createRule(\"")
        .append(id)
        .append("\", ")
        .append(rateLimitRule.getTimeFrame())
        .append(", ")
        .append(rateLimitRule.getMaxRequestCount());
    }
    instantiationDeclaration.append(");\n");
    rateLimitVariablesInstantiationDeclarations.add(instantiationDeclaration.toString());
    return variableName;
  }
  
  private void generateRateLimitRuleGetters() {
    CollectionUtil.emptyIfNull(exchangeApiDescriptor.getRateLimits()).forEach(rateLimitRule -> 
      appendToBody(ExchangeGenUtil.generateRateLimitGetterImplementationMethodDeclaration(rateLimitRule.getId()))
        .append("\n")
    );
  }
  
  private void generateRateLimitListVariable(String variableName, 
                                             String restApiName,
                                             String restEndpointJavadocLink, 
                                             List<String> rateLimitRuleVariableNames) {
    addImport(List.class);
    StringBuilder javadoc = new StringBuilder()
        .append("List of all rate limits applicable to ")
        .append("<i>")
        .append(restApiName)
        .append("</i> REST API")
        .append("\n@see ")
        .append(restEndpointJavadocLink);
        
    StringBuilder declaration = new StringBuilder()
      .append("\n")
      .append(JavaCodeGenUtil.generateJavaDoc(javadoc.toString()))
      .append("\n")
      .append("protected final List<")
      .append(RateLimitRule.class.getSimpleName())
      .append("> ")
      .append(variableName)
      .append(";");
    addImport(List.class);
    rateLimitVariablesDeclarations.add(declaration.toString());
    
    StringBuilder instantationDeclaration = new StringBuilder()
        .append(THIS)
        .append(variableName)
        .append(" = List.of(");
    for (int i = 0; i < rateLimitRuleVariableNames.size(); i++) {
      instantationDeclaration.append(rateLimitRuleVariableNames.get(i));
      if (i < rateLimitRuleVariableNames.size() - 1) {
        instantationDeclaration.append(", ");
      }
    }
    instantationDeclaration.append(");\n");
    rateLimitVariablesInstantiationDeclarations.add(instantationDeclaration.toString());
  }
  
  private String generateRestEndpointUrlVariableInstantiationInstruction(RestEndpointDescriptor restApi) {
    addImport(EncodingUtil.class);
    StringBuilder sb = new StringBuilder().append(THIS)
        .append(ExchangeApiGenUtil.getRestEndpointUrlVariableName(restApi)).append(" = ")
        .append(EncodingUtil.class.getSimpleName())
        .append(".buildUrl(this.getHttpUrl(), ")
        .append(ExchangeGenUtil.generateSubstitutionInstructionDeclaration(
            restApi.getUrl(), 
            exchangeDescriptor, 
            exchangeApiDescriptor, 
            EXCHANGE_ARGUMENT_NAME + GET_PROPERTIES, 
            getImports()))
        .append(");\n");
    
    
    return sb.toString();
  }
  
  private String generateUrlParametersSerializerDeclaration(RestEndpointDescriptor restApi) {
    Field request = restApi.getRequest();
    String getUrlParametersBody = null;
    List<Field> enpointParameters = request == null? null:
                          ExchangeApiGenUtil.resolveFieldProperties(
                            exchangeApiDescriptor, request).getProperties();
    if (ExchangeApiGenUtil.restEndpointHasArguments(restApi, exchangeApiDescriptor)) {
      String requestName = request == null? null: request.getName();
      Type requestDataType = ExchangeGenUtil.getFieldType(request);
      boolean hasQueryParams = restApi.isQueryParams()  
          || !restApi.getHttpMethod().requestHasBody;
      if (restApi.getUrlParameters() != null) {
        getUrlParametersBody =  generateUrlParametersOrTopicSerializerBodyFromTemplate(
                        restApi.getUrlParameters(), 
                        enpointParameters, 
                        restApi.getUrlParametersListSeparator(),
                        requestName,
                        requestDataType);
      } else if (!CollectionUtil.isEmpty(enpointParameters) && hasQueryParams) {
        getUrlParametersBody = generateGetUrlParametersBodyUsingQueryParams(enpointParameters, false);
      } else if (!requestDataType.isObject() && hasQueryParams) {
        getUrlParametersBody = generateGetUrlParametersBodyUsingQueryParams(List.of(request), true);
      } // else object type with 0 properties, no url parameter
    } else if (restApi.getUrlParameters() != null) {
      getUrlParametersBody = "\"" + restApi.getUrlParameters() + "\"";
    }
    if (getUrlParametersBody != null) {
      getUrlParametersBody = getUrlParametersBody + ";\n";
    }
    return getUrlParametersBody;
  }
  
  private String generateGetUrlParametersBodyUsingQueryParams(List<Field> requestFields, boolean singleRequestParam) {
    addImport(EncodingUtil.class);
    StringBuilder s = new StringBuilder().append(EncodingUtil.class.getSimpleName() + ".createUrlQueryParameters(");
    for (int i = 0; i < requestFields.size(); i++) {
      if (i > 0) {
        s.append(", ");
      }
      Field f = requestFields.get(i);
      String name = Optional.ofNullable(f.getMsgField()).orElse(f.getName());
      s.append("\"")
       .append(name)
       .append("\", ");
      String value = "request";
      if (!singleRequestParam) {
        value += "." 
            + JavaCodeGenUtil.getGetAccessorMethodName(
              f.getName(), 
              ExchangeApiGenUtil.getClassNameForField(f, null, f.getObjectName()), 
              requestFields.stream().map(Field::getName).collect(Collectors.toList()))
            + "()";
      }

      Type type = ExchangeGenUtil.getFieldType(f);
      if (type.getCanonicalType() == CanonicalType.LIST
        || type.getCanonicalType() == CanonicalType.MAP
        || type.isObject()) {
        addImport(JsonUtil.class);
        value = new StringBuilder()
                .append("JsonUtil.pojoToJsonString(")
                .append(value)
                .append(")").toString(); 
      } else if (type.getCanonicalType() == CanonicalType.STRING) {
        value = new StringBuilder()
            .append(value)
            .toString(); 
      }
      s.append(value);
      
    }
    return s.append(")").toString();
  }

  private String generateUrlParametersOrTopicSerializerBodyFromTemplate(String urlParametersTemplate, 
                                        List<Field> fields, 
                                        String stringListSeparator,
                                        String requestArgName,
                                        Type requestType) {
    fields = Optional.ofNullable(fields).orElse(List.of());
    if (stringListSeparator == null) {
      stringListSeparator = ExchangeGenUtil.DEFAULT_STRING_LIST_SEPARATOR;
    }
    StringBuilder sb = new StringBuilder();
    sb.append(EncodingUtil.class.getSimpleName())
      .append(".substituteArguments(\"")
      .append(urlParametersTemplate)
      .append("\"");
    int n = fields.size();
    for (int i = 0; i < n; i++) {
      Field f = fields.get(i);
      Type type = ExchangeGenUtil.getFieldType(f);
      String name = f.getName();
      if (!urlParametersTemplate.contains("${" + name + "}")) {
        continue;
      }
      String value = "request." 
               + JavaCodeGenUtil.getGetAccessorMethodName(
                name, 
                ExchangeApiGenUtil.getClassNameForField(f, null, f.getObjectName()), 
                fields.stream().map(Field::getName).collect(Collectors.toList()))
               + "()";
      value = getListFieldToUrlParameterSerializerInstuction(value, type, stringListSeparator);
      sb.append(", \"").append(name).append("\", ").append(value);
    }
    if (urlParametersTemplate.contains("${" + requestArgName + "}")) {
      n++;
      sb.append(", \"")
        .append(requestArgName)
        .append("\", ")
        .append(getListFieldToUrlParameterSerializerInstuction(ExchangeApiGenUtil.DEFAULT_REQUEST_ARG_NAME, requestType, stringListSeparator));
    } 
    if (n <= 0) {
      return JavaCodeGenUtil.getQuotedString(urlParametersTemplate);
    }
    addImport(EncodingUtil.class);
    sb.append(")");
    return sb.toString();
  }
  
  private String getListFieldToUrlParameterSerializerInstuction(String fieldValue, Type fieldType, String stringListSeparator) {
    if (fieldType.getCanonicalType() != CanonicalType.LIST) {
      return fieldValue;
    }
    StringBuilder sb = new StringBuilder()
        .append(EncodingUtil.class.getSimpleName())
        .append(".listToString(")
        .append(fieldValue)
        .append(", \"")
        .append(stringListSeparator).append("\")");
    return sb.toString();
  }
  
  private String getHttpRequestInterceptorFactory() {
    return Optional.ofNullable(exchangeApiDescriptor.getHttpRequestInterceptorFactory())
             .orElse(exchangeDescriptor.getHttpRequestInterceptorFactory());
  }

  private String getHttpRequestExecutorFactory() {
    return Optional.ofNullable(exchangeApiDescriptor.getHttpRequestExecutorFactory())
             .orElse(exchangeDescriptor.getHttpRequestExecutorFactory());
  }

  private long getHttpRequestTimeout() {
    long timeout = exchangeApiDescriptor.getHttpRequestTimeout();
    if (timeout >= 0) {
      return timeout;
    }
    timeout = exchangeDescriptor.getHttpRequestTimeout();
    if (timeout >= 0) {
      return timeout;
    }
    return -1L;
  }


  private String getWebsocketFactory() {
    return Optional.ofNullable(exchangeApiDescriptor.getWebsocketFactory())
             .orElse(exchangeDescriptor.getWebsocketFactory());
  }

  private String getWebsocketHookFactory() {
    return Optional.ofNullable(exchangeApiDescriptor.getWebsocketHookFactory())
             .orElse(exchangeDescriptor.getWebsocketHookFactory());
  }
}
