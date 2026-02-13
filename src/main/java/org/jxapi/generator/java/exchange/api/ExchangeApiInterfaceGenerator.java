package org.jxapi.generator.java.exchange.api;

import java.util.List;
import java.util.Optional;

import org.jxapi.exchange.ExchangeApi;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.JavaTypeGenerator;
import org.jxapi.generator.java.exchange.ExchangeGenUtil;
import org.jxapi.generator.java.pojo.PojoGenUtil;
import org.jxapi.netutils.rest.FutureRestResponse;
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
    ExchangeGenUtil.generateNamesStaticVariablesDeclarations(
        restApis.stream().map(RestEndpointDescriptor::getName).toList(), 
        "RestApi", 
        body,
        "");
    ExchangeGenUtil.generateNamesStaticVariablesDeclarations(
        wsApis.stream().map(WebsocketEndpointDescriptor::getName).toList(), 
        "WsApi", 
        body,
        "");
    for (RestEndpointDescriptor restApi: restApis) {
      appendToBody("\n");
      generateRestEndpointMethodDeclaration(restApi);
    }
    
    for (WebsocketEndpointDescriptor websocketApi : wsApis) {
      generateWebsocketApiMethodsDeclarations(websocketApi);
    }
    
    return super.generate();
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
      requestSimpleClassName = PojoGenUtil.getClassNameForType(
                          requestDataType, 
                          getImports(), 
                          requestClassName);
      requestDescription = Optional.ofNullable(docPlaceHolderResolver.resolve(websocketApi.getRequest().getDescription())).orElse("request");
      requestArgName = ExchangeApiGenUtil.getRequestArgName(websocketApi.getRequest().getName());
    }
    
    Type messageDataType = PojoGenUtil.getFieldType(websocketApi.getMessage());
    String messageClassSimpleName = PojoGenUtil.getClassNameForType(
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
    boolean requestHasBody = ExchangeApiGenUtil.restEndpointRequestHasBody(restApi);

    if (requestHasBody && !hasArguments) {
        restApi = restApi.deepClone();
        restApi.setRequest(ExchangeApiGenUtil.createDefaultRawBodyRequest());
    }
  
    String requestSimpleClassName = "Object";
    String requestArgName = null;
  
    if (ExchangeApiGenUtil.restEndpointHasArguments(restApi, exchangeApiDescriptor)) {
        requestSimpleClassName = getRequestSimpleClassName(restApi);
        requestArgName = ExchangeApiGenUtil.getRequestArgName(restApi.getRequest().getName());
    } else if (ExchangeApiGenUtil.restEndpointRequestHasBody(restApi)) {
        requestArgName = "body";
    }
  
    String responseSimpleClassName = getRestEndpointResponseSimpleClassName(restApi);
    String apiMethodName = ExchangeApiGenUtil.getRestApiMethodName(restApi, exchangeApiDescriptor.getRestEndpoints());
    String apiMethodSignature = buildRestEnpointMethodSignature(requestSimpleClassName, requestArgName, responseSimpleClassName, apiMethodName);
  
    StringBuilder javaDoc = buildRestEndpointJavaDoc(restApi, requestArgName);
    appendToBody(JavaCodeGenUtil.generateJavaDoc(javaDoc.toString())).append("\n");
    appendToBody(apiMethodSignature + ";\n");
  
    addImport(FutureRestResponse.class);
  }
  
  private String getRequestSimpleClassName(RestEndpointDescriptor restApi) {
    Field request = restApi.getRequest();
    Type requestDataType = PojoGenUtil.getFieldType(request);
    String requestClassName = null;

    if (requestDataType != null && requestDataType.isObject()) {
      requestClassName = ExchangeApiGenUtil.generateRestEnpointRequestPojoClassName(
          exchangeDescriptor,
          exchangeApiDescriptor, 
          restApi);
    }
    return PojoGenUtil.getClassNameForType(requestDataType, getImports(), requestClassName);
  }
  
  private String getRestEndpointResponseSimpleClassName(RestEndpointDescriptor restApi) {
    if (!ExchangeApiGenUtil.restEndpointHasResponse(restApi, exchangeApiDescriptor)) {
      return "String";
    }

    Field response = restApi.getResponse();
    Type responseDataType = PojoGenUtil.getFieldType(response);
    String restResponseClassName = null;

    if (responseDataType != null && responseDataType.isObject()) {
      restResponseClassName = ExchangeApiGenUtil.generateRestEnpointResponsePojoClassName(exchangeDescriptor,
          exchangeApiDescriptor, restApi);
    }
    return PojoGenUtil.getClassNameForType(responseDataType, getImports(), restResponseClassName);
  }
  
  private String buildRestEnpointMethodSignature(String requestSimpleClassName, String requestArgName, String responseSimpleClassName, String apiMethodName) {
    return new StringBuilder()
              .append(FutureRestResponse.class.getSimpleName())
              .append("<")
              .append(responseSimpleClassName)
              .append("> ")
              .append(apiMethodName)
              .append("(")
              .append(requestArgName != null ? requestSimpleClassName + " " + requestArgName : "")
              .append(")")
              .toString();
  }
  
  private StringBuilder buildRestEndpointJavaDoc(RestEndpointDescriptor restApi, String requestArgName) {
    StringBuilder javaDoc = new StringBuilder();
  
    if (restApi.getDescription() != null) {
        javaDoc.append(docPlaceHolderResolver.resolve(restApi.getDescription())).append("\n");
    }
  
    if (requestArgName != null) {
        String paramDescription = Optional.ofNullable(docPlaceHolderResolver.resolve(restApi.getRequest().getDescription())).orElse("request");
        javaDoc.append("@param ").append(requestArgName).append(" ").append(paramDescription).append("\n");
    }
  
    javaDoc.append("@return A {@link FutureRestResponse} that will complete when request submitted asynchronously has been processed.");
    if (ExchangeApiGenUtil.restEndpointHasResponse(restApi, exchangeApiDescriptor) && restApi.getResponse().getDescription() != null) {
        javaDoc.append(" If successful, will provide response: ")
               .append(docPlaceHolderResolver.resolve(restApi.getResponse().getDescription()))
               .append("\n");
    }
  
    if (restApi.getDocUrl() != null) {
        javaDoc.append("\n@see ").append(JavaCodeGenUtil.getHtmlLink(restApi.getDocUrl(), "Reference documentation")).append("\n");
    }
  
    if (javaDoc.length() > 0) {
        javaDoc.delete(javaDoc.length() - 1, javaDoc.length()); // Remove trailing '\n'
    }
  
    return javaDoc;
  }

}
