package org.jxapi.generator.java.exchange.api.demo;

import java.util.List;
import java.util.Properties;

import org.jxapi.exchange.ExchangeObserver;
import org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.JavaTypeGenerator;
import org.jxapi.generator.java.exchange.ExchangeGenUtil;
import org.jxapi.generator.java.exchange.api.ExchangeApiGenUtil;
import org.jxapi.generator.java.pojo.PojoGenUtil;
import org.jxapi.netutils.websocket.WebsocketListener;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.DemoProperties;
import org.jxapi.util.DemoUtil;
import org.slf4j.Logger;

/**
 * Generates a demo snippet class for a Websocket endpoint.
 * Such class contains a main method that subscribes to the Websocket endpoint
 * API, logs subscription request sent and messages received, and after when
 * some time has elapsed, unsubscribes from websocket stream and exits. Also:
 * <ul>
 * <li>Has imports declaration with the API interface class, the request class
 * if any, and the {@link DemoUtil} class.</li>
 * <li>Has a SLF4J {@link Logger} declaration.</li>
 * <li>The subscription request is created if the API method has arguments using
 * a generated <code>public static</code> method for creating that request.</li>
 * <li>That request creation method uses sample values provided with each field
 * of the request sample values, see {@link Field#getSampleValue()} and.</li>
 * <li>Logs the subscription and unsubscription actions.</li>
 * <li>Logs the messages received from the websocket stream.</li>
 * <li>Unsubscribes from stream after some delay has elapsed after subscription.
 * This delay is configured using {@link DemoProperties#DEMO_WS_SUBSCRIPTION_DURATION_PROPERTY} .</li>
 * <li>Exits after some delay has elased after unsubiscription. Such delay configured 
 * using {@link DemoProperties#DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION_PROPERTY}</li>
 * </ul>
 */
public class WebsocketEndpointDemoGenerator extends JavaTypeGenerator {
  
  private static final String SUBSCRIPTION_DURATION_VAR_NAME = "subscriptionDuration";
  private static final String DELAY_BEFORE_EXIT_VAR_NAME = "delayBeforeExit";
  private static final String SUBSCRIBE_METHOD_ARGUMENT_INDENT = spaces("public static void subscribe(".length());
  private static final String MAIN_METHOD_SUBSCRIBE_METHOD_CALL_ARGUMENT_INDENT = spaces("subscribe(".length());
  private static final String EXCHANGE_VAR = "exchange";
  private static final String API_VAR = "api";
  private static final String OBSERVER_VAR = "observer";
  
  private static String spaces(int count) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < count; i++) {
      sb.append(' ');
    }
    return sb.toString();
  }
  
  private final ExchangeDescriptor exchangeDescriptor;
  private final boolean hasArguments;
  private final Field request;
  private final String requestClassName;
  private final String requestSimpleClassName;
  private String apiInterfaceClassName;
  private final String simpleApiClassName;
  private final String fullStreamName;
  private final String subscribeMethodName;
  private final String unsubscribeMethodName;
  private final String messageClassSimpleName;
  private final String exchangeClassName;
  private final String exchangeSimpleClassName;
  private final Type requestDataType;
  private final ExchangeApiDescriptor exchangeApi;
  private final WebsocketEndpointDescriptor websocketApi;
  private final List<ConfigPropertyDescriptor> demoProperties;
  
  /**
   * Constructor.
   * 
   * @param exchangeDescriptor the exchange descriptor
   * @param exchangeApiDescriptor the exchange API descriptor
   * @param websocketApi the Websocket endpoint descriptor
   * @param demoProperties the demo configuration properties for the exchange
   */
  public WebsocketEndpointDemoGenerator(ExchangeDescriptor exchangeDescriptor, 
                                        ExchangeApiDescriptor exchangeApiDescriptor, 
                                        WebsocketEndpointDescriptor websocketApi,
                                        List<ConfigPropertyDescriptor> demoProperties) {
    super(EndpointDemoGenUtil.getWebsocketApiDemoClassName(exchangeDescriptor, exchangeApiDescriptor, websocketApi));
    this.exchangeDescriptor = exchangeDescriptor;
    this.exchangeApi = exchangeApiDescriptor;
    this.websocketApi = websocketApi;
    this.demoProperties = demoProperties;
    this.request = ExchangeApiGenUtil.resolveFieldProperties(exchangeApiDescriptor, websocketApi.getRequest());
    this.exchangeClassName = ExchangeGenUtil.getExchangeInterfaceName(exchangeDescriptor);
    this.exchangeSimpleClassName = JavaCodeGenUtil.getClassNameWithoutPackage(exchangeClassName);
    subscribeMethodName = ExchangeApiGenUtil.getWebsocketSubscribeMethodName(websocketApi, exchangeApiDescriptor.getWebsocketEndpoints());
    unsubscribeMethodName = ExchangeApiGenUtil.getWebsocketUnsubscribeMethodName(websocketApi, exchangeApiDescriptor.getWebsocketEndpoints());
    setTypeDeclaration("public class");
    this.hasArguments = ExchangeApiGenUtil.websocketEndpointHasArguments(websocketApi, exchangeApiDescriptor);
    if (hasArguments) {
      this.requestDataType =  PojoGenUtil.getFieldType(request);
      if (this.requestDataType.getCanonicalType().isPrimitive) {
        this.requestClassName = requestDataType.getCanonicalType().typeClass.getName();
      } else {
        this.requestClassName = ExchangeApiGenUtil.generateWebsocketEndpointRequestPojoClassName(exchangeDescriptor, exchangeApiDescriptor, websocketApi);
      }
      
      addImport(requestClassName);
      this.requestSimpleClassName = PojoGenUtil.getClassNameForType(
          requestDataType, 
          getImports(), 
          requestClassName);
    } else {
      this.requestClassName = null;
      this.requestSimpleClassName = null;
      this.requestDataType = null;
    }
    this.apiInterfaceClassName = ExchangeGenUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor);
    this.simpleApiClassName = JavaCodeGenUtil.getClassNameWithoutPackage(apiInterfaceClassName);

    addImport(apiInterfaceClassName);
    setDescription(getClassJavadoc());
    this.fullStreamName = exchangeDescriptor.getId() + " " 
                + exchangeApiDescriptor.getName() 
                + " " + websocketApi.getName();
    Type messageDataType = PojoGenUtil.getFieldType(websocketApi.getMessage());
    messageClassSimpleName = PojoGenUtil.getClassNameForType(
        messageDataType, 
        getImports(), 
        ExchangeApiGenUtil.generateWebsocketEndpointMessagePojoClassName(
            exchangeDescriptor, 
            exchangeApiDescriptor, 
            websocketApi));
    addImport(WebsocketListener.class);
    addImport(DemoUtil.class);
    addImport(Properties.class);
    addImport(ExchangeObserver.class);
    addImport(DemoProperties.class);
    addImport(InterruptedException.class);
    addImport(this.exchangeClassName);
  }
  
  @Override
  public String generate() {
    JavaCodeGenUtil.generateSlf4jLoggerDeclaration(this);
    if (hasArguments) {
      this.appendToBody(EndpointDemoGenUtil.generateFieldCreationMethod(
          request,  
          requestClassName, 
          exchangeDescriptor,
          exchangeApi,
          EndpointDemoGenUtil.WEBSOCKET_DEMO_GROUP_NAME,
          websocketApi.getName(),
          demoProperties,
          getImports()))
        .append("\n");
    }
    generateSubscribeMethod();
    this.appendToBody("\n");
    generateMainMethod();
    return super.generate();
  }
  
  private void generateSubscribeMethod() {
    appendMethod(generateSubscribeMethodSignature(), 
           generateSubscribeMethodBody(), 
           generateSubscribeMethodJavadoc());
  }
  
  private String generateSubscribeMethodSignature() {
    StringBuilder signature = new StringBuilder()
        .append("public static void subscribe(");
    if (hasArguments) {
      signature.append(requestSimpleClassName)
           .append(" request,\n")
           .append(SUBSCRIBE_METHOD_ARGUMENT_INDENT);
    }
    signature.append("WebsocketListener<")
         .append(messageClassSimpleName)
         .append("> messageListener,\n")
         .append(SUBSCRIBE_METHOD_ARGUMENT_INDENT)
         .append("Properties configProperties,\n")
         .append(SUBSCRIBE_METHOD_ARGUMENT_INDENT)
         .append(ExchangeObserver.class.getSimpleName())
         .append(" ")
         .append(OBSERVER_VAR)
         .append(") throws ")
         .append(InterruptedException.class.getSimpleName());
    return signature.toString();
  }
  
  private String getClassJavadoc() {
    return new StringBuilder()
          .append("Snippet to test call to ")
          .append(getApiInterfaceSubscribeMethodJavadocLink())
          .append(".")
          .toString();
        
  }
  
  private String getApiInterfaceSubscribeMethodJavadocLink() {
    StringBuilder javadoc = new StringBuilder()
        .append("{@link ")
        .append(JavaCodeGenUtil.getClassNameWithoutPackage(apiInterfaceClassName))
        .append("#")
        .append(subscribeMethodName)
        .append("(");
    if (hasArguments) {
      javadoc.append(JavaCodeGenUtil.getMethodArgumentJavadoc(requestDataType, requestClassName))
           .append(", ");
    }
    return javadoc.append(WebsocketListener.class.getName())
              .append(")}").toString();
  }
  
  private String generateSubscribeMethodJavadoc() {
    StringBuilder javadoc = new StringBuilder()
         .append(getApiInterfaceSubscribeMethodJavadocLink())
         .append(".\n<br>Websocket endpoint subscription will be performed using given websocket listener ")
         .append("then after waiting for <code>subscriptionDuration</code> delay, unsubscription is performed.\n")
         .append("Finally waits for <code>delayBeforeExitAfterUnsubscription</code> delay before returning to make sure no more messages are dispatched.\n");
    if (hasArguments) {
      javadoc.append("@param request          The subscription request\n");
    }
    javadoc.append("@param messageListener  The listener that will receive messages dispatched while subscription is active\n")
            .append("@param configProperties Exchange configuration properties.\n")
            .append("@param observer      {@link ExchangeObserver} (optional, ignored if <code>null</code>) observer will")
            .append (" be subscribed to Exchange API exposing websocket endpoint that will be notifed of received websocket events.")
            .append("Useful in particular to get notified of websocket errors.\n")
            .append("@throws InterruptedException eventually thrown while sleeping for <code>subscriptionDuration</code> or <code>delayBeforeExitAfterUnsubscription</code> delays");
    return javadoc.toString();
  }
  
  private String generateSubscribeMethodBody() {
    String apiGroupGetterMethodName = ExchangeGenUtil.getApiGroupGetterMethodName(exchangeDescriptor, exchangeApi);
    StringBuilder bodyBuilder = new StringBuilder();
    bodyBuilder.append(EndpointDemoGenUtil.getNewTestExchangeInstruction(
          exchangeClassName, 
          EXCHANGE_VAR, 
          "configProperties"))
        .append("\n")
        .append(EndpointDemoGenUtil.getNewTestApiInstruction(
          EXCHANGE_VAR, 
          API_VAR,
          simpleApiClassName,
          apiGroupGetterMethodName))
        .append("\n")
        .append("long ")
            .append(SUBSCRIPTION_DURATION_VAR_NAME)
            .append(" = ")
            .append(DemoProperties.class.getSimpleName())
            .append(".")
            .append("getWebsocketSubscriptionDuration(configProperties);\n");
    bodyBuilder.append("long ")
            .append(DELAY_BEFORE_EXIT_VAR_NAME)
            .append(" = ")
            .append(DemoProperties.class.getSimpleName())
            .append(".")
            .append("getWebsocketDelayBeforeExit(configProperties);\n");
    bodyBuilder.append("log.info(\"Subscribing to websocket API '")
           .append(fullStreamName)
           .append("' for {} ms");
    if (hasArguments) {
      bodyBuilder.append(" with request:{}\", ").append(SUBSCRIPTION_DURATION_VAR_NAME).append(", request);\n");
    } else {
      bodyBuilder.append("\", ")
             .append(SUBSCRIPTION_DURATION_VAR_NAME)
             .append(");\n");
    }
    bodyBuilder.append("if (")
      .append(OBSERVER_VAR)
      .append(" != null) ")
      .append(JavaCodeGenUtil.generateCodeBlock(EXCHANGE_VAR + ".subscribeObserver(" + OBSERVER_VAR + ");"))
      .append("String subId = api.")
      .append(subscribeMethodName)
      .append("(");
    if (hasArguments) {
      bodyBuilder.append("request, ");
    }

    bodyBuilder.append("messageListener);\n")
      .append("DemoUtil.sleep(")
      .append(SUBSCRIPTION_DURATION_VAR_NAME)
      .append(");\n")
      .append("log.info(\"Unubscribing from '")
      .append(fullStreamName)
      .append("' stream\");\n")
      .append(API_VAR)
      .append(".")
      .append(unsubscribeMethodName)
      .append("(subId);\n")
      .append("DemoUtil.sleep(")
      .append(DELAY_BEFORE_EXIT_VAR_NAME)
      .append(");\n")
      .append("if (")
      .append(OBSERVER_VAR)
      .append(" != null) ")
      .append(JavaCodeGenUtil.generateCodeBlock(EXCHANGE_VAR + ".unsubscribeObserver(" + OBSERVER_VAR + ");"))
      .append(EXCHANGE_VAR)
      .append(".dispose();");
    
    return bodyBuilder.toString();
  }
  
  private void generateMainMethod() {
    String exchangeInterfaceClassName = ExchangeGenUtil.getExchangeInterfaceName(exchangeDescriptor);
    String exchangeImplClassName = ExchangeGenUtil.getExchangeInterfaceImplementationName(exchangeInterfaceClassName);
    addImport(exchangeImplClassName);
    String propsVar = EndpointDemoGenUtil.CREATE_REQUEST_PROPERTIES_ARG_NAME;
    StringBuilder bodyBuilder = new StringBuilder()
      .append("Properties ")
      .append(propsVar)
      .append(" = ")
      .append(EndpointDemoGenUtil.getTestPropertiesInstruction(exchangeSimpleClassName, getImports()))
      .append(";\n")
      .append("subscribe(");
    if (hasArguments) {
      bodyBuilder.append(EndpointDemoGenUtil.generateFieldCreationMethodName(request))
        .append("(")
        .append(propsVar)
        .append("),\n")
        .append(MAIN_METHOD_SUBSCRIBE_METHOD_CALL_ARGUMENT_INDENT);
    }
    bodyBuilder.append(DemoUtil.class.getSimpleName())
           .append("::logWsMessage,\n")
           .append(MAIN_METHOD_SUBSCRIBE_METHOD_CALL_ARGUMENT_INDENT)
           .append(propsVar)
           .append(",\n")
           .append(MAIN_METHOD_SUBSCRIBE_METHOD_CALL_ARGUMENT_INDENT)
           .append(DemoUtil.class.getSimpleName())
           .append("::logWsApiEvent);\n")
           .append("System.exit(0);");
    
    appendMethod("public static void main(String[] args)",
           JavaCodeGenUtil.generateTryBlock(
               bodyBuilder.toString(),  
               "log.error(\"Exception raised from main()\", t);\nSystem.exit(-1);",
               "Throwable t",
               null, null),
           generateMainMethodJavadoc());
    
  }
  
  private String generateMainMethodJavadoc() {
    return  new StringBuilder()
            .append("Runs websocket endpoint '")
            .append(fullStreamName)
            .append("' subscription demo.\n")
            .append("@param args no arguments expected")
            .toString();
  }

}
