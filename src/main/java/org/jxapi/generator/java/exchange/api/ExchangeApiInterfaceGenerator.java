package org.jxapi.generator.java.exchange.api;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jxapi.exchange.ExchangeApi;
import org.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.JavaTypeGenerator;
import org.jxapi.generator.java.exchange.ExchangeConstantValuePlaceholderResolverFactory;
import org.jxapi.generator.java.exchange.ExchangeGenUtil;
import org.jxapi.generator.java.pojo.PojoGenUtil;
import org.jxapi.netutils.rest.FutureRestResponse;
import org.jxapi.netutils.rest.ratelimits.RateLimitRule;
import org.jxapi.netutils.websocket.WebsocketListener;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.PlaceHolderResolver;

/**
 * Generates source code for a Java interface for an
 * {@link ExchangeApiDescriptor} defined in an exchange descriptor file.
 * This interface contains:
 * <ul>
 * <li>A function for every REST endpoint call, and subscribe and unsubscribe to
 * websocket stream methods for every websocket endpoint.
 * <li>A static variable for every REST and Websocket endpoint name.
 * <li>A static variable for the API name.
 * </ul>
 * The static variables are used to identify the API and its endpoints in the
 * API wrapper implementation, as it is provided in observability events.
 * 
 * @see ExchangeApiDescriptor
 */
public class ExchangeApiInterfaceGenerator extends JavaTypeGenerator {
  
  private static final String STRING = "String ";

  /**
   * Name of generated static final property for the API name.
   */
  public static final String EXCHANGE_API_NAME_VARIABLE = "ID";
  
  private final ExchangeDescriptor exchangeDescriptor;
  
  private final ExchangeApiDescriptor exchangeApiDescriptor;
  
  private final PlaceHolderResolver docPlaceHolderResolver;
  
  /**
   * Constructor.
   * 
   * @param exchangeDescriptor    the exchange descriptor where the API is defined
   * @param exchangeApiDescriptor the API descriptor to generate interface for
   * @param docPlaceHolderResolver the place holder resolver to use for resolving placeholders in descriptions.
   */
  public ExchangeApiInterfaceGenerator(ExchangeDescriptor exchangeDescriptor, 
                                       ExchangeApiDescriptor exchangeApiDescriptor,
                                       PlaceHolderResolver docPlaceHolderResolver) {
    super(ExchangeGenUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor));
    this.exchangeDescriptor = exchangeDescriptor;
    this.exchangeApiDescriptor = exchangeApiDescriptor;
    this.docPlaceHolderResolver = Optional.ofNullable(docPlaceHolderResolver)
                                          .orElse(PlaceHolderResolver.NO_OP);
    setDescription(exchangeDescriptor.getId() 
                    + " " 
                    + exchangeApiDescriptor.getName() 
                    + " API<br>\n" 
                    + this.docPlaceHolderResolver.resolve(exchangeApiDescriptor.getDescription()));
    setTypeDeclaration("public interface");
    this.setParentClassName(ExchangeApi.class.getName());
  }
  
  /**
   * {@inheritDoc}
   * <p>
   * Generates the whole interface source code, including:
   * <ul>
   * <li>Static variables for the API name and every REST and Websocket endpoint
   * name.</li>
   * <li>Method declarations for every REST endpoint call, and subscribe and
   * unsubscribe to websocket stream methods for every websocket endpoint.</li>
   * </ul>
   */
  @Override
  public String generate() {
    appendToBody("\n")
        .append(JavaCodeGenUtil.generateJavaDoc("Name of '" + exchangeApiDescriptor.getName() + "' API group."))
        .append("\nString ")
      .append(EXCHANGE_API_NAME_VARIABLE)
      .append(" = ")
      .append(JavaCodeGenUtil.getQuotedString(exchangeApiDescriptor.getName()))
      .append(";\n");
    List<RestEndpointDescriptor> restApis = CollectionUtil.emptyIfNull(exchangeApiDescriptor.getRestEndpoints());
    List<WebsocketEndpointDescriptor> wsApis = CollectionUtil.emptyIfNull(exchangeApiDescriptor.getWebsocketEndpoints());
    ExchangeApiGenUtil.generateEndpointNameStaticVariablesDeclaration(
        restApis.stream().map(RestEndpointDescriptor::getName).collect(Collectors.toList()), 
        "RestApi", 
        body);
    ExchangeApiGenUtil.generateEndpointNameStaticVariablesDeclaration(
        wsApis.stream().map(WebsocketEndpointDescriptor::getName).collect(Collectors.toList()), 
        "WsApi", 
        body);
    
    generateEndpointDefaultValuesStaticFieldDeclarations();
    
    if (exchangeApiDescriptor.getRestEndpoints() != null) {
      for (RestEndpointDescriptor restApi: exchangeApiDescriptor.getRestEndpoints()) {
          appendToBody("\n");
        generateRestEndpointMethodDeclaration(restApi);
      }
    }
    
    if (exchangeApiDescriptor.getWebsocketEndpoints() != null) {
      for (WebsocketEndpointDescriptor websocketApi : exchangeApiDescriptor.getWebsocketEndpoints()) {
        generateWebsocketApiMethodsDeclarations(websocketApi);
      }
    }
    
    generateRateLimitRuleMethodDeclarations();
    
    return super.generate();
  }
  
  private void generateEndpointDefaultValuesStaticFieldDeclarations() {
    PlaceHolderResolver constantInDefaultValuesPlaceHolderResolverFactory = 
        new ExchangeConstantValuePlaceholderResolverFactory(exchangeDescriptor).createConstantValuePlaceholderResolver(getImports());
    ExchangeApiGenUtil.generateRestEndpointRequestDefaultValuesStaticFieldDeclarations(
        CollectionUtil.emptyIfNull(exchangeApiDescriptor.getRestEndpoints()), 
        getImports(), 
        docPlaceHolderResolver,
        constantInDefaultValuesPlaceHolderResolverFactory,
        body);
    ExchangeApiGenUtil.generateWebsocketEndpointRequestDefaultValuesStaticFieldDeclarations(
        CollectionUtil.emptyIfNull(exchangeApiDescriptor.getWebsocketEndpoints()), 
        getImports(), 
        docPlaceHolderResolver,
        constantInDefaultValuesPlaceHolderResolverFactory,
        body);
  }
  
  private void generateRateLimitRuleMethodDeclarations() {
    for (RateLimitRule rateLimit : CollectionUtil.emptyIfNull(exchangeApiDescriptor.getRateLimits())) {
      String rateLimitName = rateLimit.getId();
      addImport(RateLimitRule.class);
      appendToBody(ExchangeGenUtil.generateRateLimitRuleInterfaceMethodDeclaration(rateLimitName));
    }
  }

  private void generateWebsocketApiMethodsDeclarations(WebsocketEndpointDescriptor websocketApi) {
    Type requestDataType = PojoGenUtil.getFieldType(websocketApi.getRequest());
    boolean hasArguments = ExchangeApiGenUtil.websocketEndpointHasArguments(websocketApi, exchangeApiDescriptor);
    String requestSimpleClassName = Object.class.getSimpleName();
    String requestDescription = null;
    String requestArgName = null;
    if (hasArguments) {
      String requestClassName = null;
      if (requestDataType != null && requestDataType.isObject()) {
        requestClassName = ExchangeApiGenUtil.generateWebsocketEndpointRequestPojoClassName(
            exchangeDescriptor, 
            exchangeApiDescriptor, 
            websocketApi);
      }
      requestSimpleClassName = ExchangeGenUtil.getClassNameForType(
                          requestDataType, 
                          getImports(), 
                          requestClassName);
      requestDescription = Optional.ofNullable(docPlaceHolderResolver.resolve(websocketApi.getRequest().getDescription())).orElse("request");
      requestArgName = ExchangeApiGenUtil.getRequestArgName(websocketApi.getRequest().getName());
    }
    
    Type messageDataType = PojoGenUtil.getFieldType(websocketApi.getMessage());
    String messageClassSimpleName = ExchangeGenUtil.getClassNameForType(
        messageDataType, 
        getImports(), 
        ExchangeApiGenUtil.generateWebsocketEndpointMessagePojoClassName(
            exchangeDescriptor, 
            exchangeApiDescriptor, 
            websocketApi));
    String subscribeMethodName = ExchangeApiGenUtil.getWebsocketSubscribeMethodName(websocketApi, exchangeApiDescriptor.getWebsocketEndpoints());
    String unsubscribeMethodName = ExchangeApiGenUtil.getWebsocketUnsubscribeMethodName(websocketApi, exchangeApiDescriptor.getWebsocketEndpoints());
    addImport(WebsocketListener.class);
    String subscribeMethodSignature = new StringBuilder()
        .append(STRING)
        .append(subscribeMethodName)
        .append("(")
        .append(hasArguments? requestSimpleClassName 
                    + " " + requestArgName 
                    + ", "
                  : "")
        .append("WebsocketListener<")
        .append(messageClassSimpleName)
        .append("> listener)").toString();
    
    StringBuilder subscribeMethodDoc = new StringBuilder()
        .append("Subscribe to ")
        .append(websocketApi.getName())
        .append(" stream.<br>\n");

    if (websocketApi.getDescription() != null) {
      subscribeMethodDoc
        .append(docPlaceHolderResolver.resolve(websocketApi.getDescription()))
        .append("\n");  
    }
    subscribeMethodDoc.append("\n");
    if (requestDescription != null) {
      subscribeMethodDoc
        .append("@param ")
        .append(requestArgName)
        .append(" ")
        .append(requestDescription)
        .append("\n");
    }

    subscribeMethodDoc
      .append("@param listener listener that will receive incoming messages\n")
      .append("@return client subscriptionId to use for unsubscription using {@link #")
      .append(unsubscribeMethodName)
      .append("(String)}");
    if (websocketApi.getDocUrl() != null) {
      subscribeMethodDoc
        .append("\n@see ")
        .append(JavaCodeGenUtil.getHtmlLink(websocketApi.getDocUrl(), "Reference documentation"));
    }
    
    appendToBody("\n");
    appendToBody(JavaCodeGenUtil.generateJavaDoc(subscribeMethodDoc.toString()));
    appendToBody("\n");
    appendToBody(subscribeMethodSignature + ";\n\n");
    
    String unsubscribeMethodSignature = "boolean " + unsubscribeMethodName + "(String subscriptionId)";
    StringBuilder unsubscribeMethodDoc = new StringBuilder()
        .append("Unsubscribe from ")
        .append(websocketApi.getName())
        .append(" stream.\n")
        .append("\n@param subscriptionId client subscription ID")
        .append("\n@return <code>true</code> if given <code>subscriptionId</code> was found.")
          .append("\n@see #")
         .append(subscribeMethodName)
         .append("(");
    if (hasArguments) {
      unsubscribeMethodDoc
        .append(requestSimpleClassName)
        .append(", ");
    }
    unsubscribeMethodDoc.append("WebsocketListener)");
    appendToBody(JavaCodeGenUtil.generateJavaDoc(unsubscribeMethodDoc.toString()))
      .append("\n")
      .append(unsubscribeMethodSignature)
      .append(";\n");
  }

  private void generateRestEndpointMethodDeclaration(RestEndpointDescriptor restApi) {
    boolean hasArguments = ExchangeApiGenUtil.restEndpointHasArguments(restApi, exchangeApiDescriptor);
    Field request = restApi.getRequest();
    Type requestDataType = PojoGenUtil.getFieldType(request);
    Field response = restApi.getResponse();
    Type responseDataType = PojoGenUtil.getFieldType(response);
    String requestSimpleClassName = "Object";
    String requestArgName = null;
    if (hasArguments) {
      String requestClassName = null;
      if (requestDataType != null && requestDataType.isObject()) {
        requestClassName = ExchangeApiGenUtil.generateRestEnpointRequestPojoClassName(
                    exchangeDescriptor, 
                    exchangeApiDescriptor, 
                    restApi);
      }
      requestSimpleClassName = ExchangeGenUtil.getClassNameForType(
                    requestDataType, 
                    getImports(), 
                    requestClassName);
      requestArgName = ExchangeApiGenUtil.getRequestArgName(request.getName());
    }
    boolean hasResponse = ExchangeApiGenUtil.restEndpointHasResponse(restApi, exchangeApiDescriptor);
    String responseSimpleClassName = "String";
    if (hasResponse) {
      String restResponseClassName = null;
      if (responseDataType != null && responseDataType.isObject()) {
        restResponseClassName = ExchangeApiGenUtil.generateRestEnpointResponsePojoClassName(
                      exchangeDescriptor, 
                      exchangeApiDescriptor, 
                      restApi);
      }
      responseSimpleClassName = ExchangeGenUtil.getClassNameForType(
          responseDataType, 
          getImports(), 
          restResponseClassName);
    }
    String apiMethodName = ExchangeApiGenUtil.getRestApiMethodName(restApi, exchangeApiDescriptor.getRestEndpoints());
    String apiMethodSignature =  new StringBuilder()
        .append(FutureRestResponse.class.getSimpleName())
        .append("<")
        .append(responseSimpleClassName)
        .append("> ")
        .append(apiMethodName)
        .append("(")
        .append(hasArguments? requestSimpleClassName 
                     + " " +
                     requestArgName
                   : "")
        .append(")").toString(); 
    StringBuilder javaDoc = new StringBuilder();
    if (restApi.getDescription() != null) {
      javaDoc.append(docPlaceHolderResolver.resolve(restApi.getDescription()))
           .append("\n");
    }
    if (hasArguments) {
      String paramDescription = Optional.ofNullable(docPlaceHolderResolver.resolve(request.getDescription())).orElse("request");
      javaDoc.append("@param ")
           .append(requestArgName)
           .append(" ")
           .append(paramDescription)
           .append("\n");
    }
    
    javaDoc.append("@return A {@link FutureRestResponse} that will complete when request submitted asynchronously has been processed.");
    if (hasResponse && response.getDescription() != null) {
      javaDoc.append("If successful, will provide response:")
             .append(docPlaceHolderResolver.resolve(response.getDescription()))
             .append("\n");
    }
    
    if (restApi.getDocUrl() != null) {
      javaDoc.append("\n@see ")
             .append(JavaCodeGenUtil.getHtmlLink(restApi.getDocUrl(), "Reference documentation"))
             .append("\n");
    }
    if (javaDoc.length() > 0) {
      // Remove trailing '\n'
      javaDoc.delete(javaDoc.length() - 1, javaDoc.length());
      appendToBody(JavaCodeGenUtil.generateJavaDoc(javaDoc.toString())).append("\n");
    }
    appendToBody(apiMethodSignature + ";\n");
    addImport(FutureRestResponse.class);
  }

}
