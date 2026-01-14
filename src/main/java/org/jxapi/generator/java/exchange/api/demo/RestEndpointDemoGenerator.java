package org.jxapi.generator.java.exchange.api.demo;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.jxapi.exchange.ExchangeObserver;
import org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.JavaTypeGenerator;
import org.jxapi.generator.java.exchange.ExchangeGenUtil;
import org.jxapi.generator.java.exchange.api.ExchangeApiGenUtil;
import org.jxapi.generator.java.pojo.PojoGenUtil;
import org.jxapi.netutils.rest.RestResponse;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.DemoUtil;
import org.slf4j.Logger;

/**
 * Generates a demo snippet class for a REST endpoint.
 * Such class contains a main method that calls the REST endpoint API, logs the
 * response and exits. Also:
 * <ul>
 * <li>Has imports declaration with the API interface class, the request class
 * if any, and the {@link DemoUtil} class.</li>
 * <li>Has a SLF4J {@link Logger} declaration.</li>
 * <li>The request is created if the API method has arguments using a generated
 * <code>public static</code> method for creating that request.</li>
 * <li>That request creation method uses sample values provided with each field
 * of the request sample values, see {@link Field#getSampleValue()}.</li>
 * </ul>
 */
public class RestEndpointDemoGenerator extends JavaTypeGenerator {
  
  private static final String EXECUTE_METHOD_ARG_INDENT = "        ";
  private static final String EXCHANGE_VAR = "exchange";
  private static final String API_VAR = "api";
  private static final String OBSERVER_VAR = "observer";
  
  private final RestEndpointDescriptor restApi;
  private final boolean hasArguments;
  private final String requestClassName;
  private final String requestSimpleClassName;
  private String apiInterfaceClassName;
  private final String simpleApiClassName;
  private final String exchangeClassName;
  private final String exchangeSimpleClassName;
  private final Field request;
  private final Type requestDataType;
  private final String exchangeImplClassName;
  private final String responseSimpleClassName;
  private final boolean hasResponse;
  private final Field response;
  private final Type responseDataType;
  private final String apiEndpointMethodJavadocLink;
  private final String apiMethodName;
  private final ExchangeDescriptor exchange;
  private final ExchangeApiDescriptor exchangeApi;
  private final List<ConfigPropertyDescriptor> demoProperties;
  
  /**
   * Constructor.
   * 
   * @param exchangeDescriptor the exchange descriptor
   * @param exchangeApiDescriptor the exchange API descriptor
   * @param restApi the REST endpoint descriptor
   * @param demoProperties the list of demo configuration properties available for demo snippets
   */
  public RestEndpointDemoGenerator(ExchangeDescriptor exchangeDescriptor, 
                   ExchangeApiDescriptor exchangeApiDescriptor, 
                   RestEndpointDescriptor restApi,
                   List<ConfigPropertyDescriptor> demoProperties) {
    super(EndpointDemoGenUtil.getRestApiDemoClassName(exchangeDescriptor, exchangeApiDescriptor, restApi));
    setTypeDeclaration("public class");
    this.exchange = exchangeDescriptor;
    this.exchangeApi = exchangeApiDescriptor;
    boolean hasSomeArguments = ExchangeApiGenUtil.restEndpointHasArguments(restApi, exchangeApiDescriptor);
    boolean requestHasBody = ExchangeApiGenUtil.restEndpointRequestHasBody(restApi);
    if (requestHasBody && !hasSomeArguments) {
      restApi = restApi.deepClone();
      restApi.setRequest(ExchangeApiGenUtil.createDefaultRawBodyRequest());
      hasSomeArguments = true;
    }
    this.hasArguments = hasSomeArguments;
    this.restApi = restApi;
    this.demoProperties = demoProperties;
    this.exchangeClassName = ExchangeGenUtil.getExchangeInterfaceName(exchangeDescriptor);
    this.exchangeSimpleClassName = JavaCodeGenUtil.getClassNameWithoutPackage(exchangeClassName);
    this.request = ExchangeApiGenUtil.resolveFieldProperties(exchangeApiDescriptor, restApi.getRequest());
    this.exchangeImplClassName = ExchangeGenUtil.getExchangeInterfaceImplementationName(exchangeDescriptor);
    if (hasSomeArguments) {
      requestDataType =  PojoGenUtil.getFieldType(request);
      if (requestDataType.getCanonicalType().isPrimitive) {
        requestClassName = requestDataType.getCanonicalType().typeClass.getName();
      } else if (requestDataType.isObject() ){
        requestClassName = ExchangeApiGenUtil.generateRestEnpointRequestPojoClassName(
                    exchangeDescriptor, 
                    exchangeApiDescriptor, 
                    restApi);
      } else {
        requestClassName = Type.getLeafSubType(requestDataType).getCanonicalType().typeClass.getName();
      }
      
      addImport(requestClassName);
      requestSimpleClassName = PojoGenUtil.getClassNameForType(
                    requestDataType, 
                    getImports(), 
                    requestClassName);
    } else {
      requestClassName = null;
      requestSimpleClassName = null;
      requestDataType = null;
    }
    this.apiInterfaceClassName = ExchangeGenUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor);
    this.simpleApiClassName = JavaCodeGenUtil.getClassNameWithoutPackage(apiInterfaceClassName);
    response = restApi.getResponse();
    responseDataType = PojoGenUtil.getFieldType(response);
    hasResponse = ExchangeApiGenUtil.restEndpointHasResponse(restApi, exchangeApiDescriptor);
    if (hasResponse) {
      String restResponseClassName = null;
      if (responseDataType != null && responseDataType.isObject()) {
        restResponseClassName = ExchangeApiGenUtil.generateRestEnpointResponsePojoClassName(
            exchangeDescriptor, 
            exchangeApiDescriptor, 
            restApi);
        addImport(restResponseClassName);
      }
      responseSimpleClassName = PojoGenUtil.getClassNameForType(
          responseDataType, 
          getImports(), 
          restResponseClassName);

    } else {
      responseSimpleClassName = String.class.getSimpleName();
    }
    this.apiMethodName = ExchangeApiGenUtil.getRestApiMethodName(restApi, exchangeApiDescriptor.getRestEndpoints());
    this.apiEndpointMethodJavadocLink = generateApiEndpointMethodJavadocLink();
    addImport(apiInterfaceClassName);
    addImport(exchangeClassName);
    addImport(exchangeImplClassName);
    addImport(DemoUtil.class);
    addImport(ExecutionException.class);
    addImport(InterruptedException.class);
    addImport(RestResponse.class);
    addImport(Properties.class);
    addImport(ExchangeObserver.class);
    setDescription(generateClassJavadoc());
  }
  
  @Override
  public String generate() {
    JavaCodeGenUtil.generateSlf4jLoggerDeclaration(this);
    if (hasArguments) {
      this.appendToBody(EndpointDemoGenUtil.generateFieldCreationMethod(
                request,  
                requestClassName, 
                exchange,
                exchangeApi,
                EndpointDemoGenUtil.REST_DEMO_GROUP_NAME,
                restApi.getName(),
                demoProperties,
                getImports()));
      this.appendToBody("\n");
    }
    generateExecuteMethod();
    this.appendToBody("\n");
    generateMainMethod();
    return super.generate();
  }
  
  private String generateApiEndpointMethodJavadocLink() {
    StringBuilder javadoc = new StringBuilder()
            .append("{@link ")
            .append(JavaCodeGenUtil.getClassNameWithoutPackage(apiInterfaceClassName))
            .append("#")
            .append(apiMethodName)
            .append("(");
    if (hasArguments) {
      javadoc.append(JavaCodeGenUtil.getMethodArgumentJavadoc(requestDataType, requestClassName));
    }
    return javadoc.append(")}").toString();
  }
  
  private String generateClassJavadoc() {
    return new StringBuilder()
        .append("Snippet to test call to ")
        .append(apiEndpointMethodJavadocLink)
        .append(")}<br>")
        .toString();
  }
  
  private void generateExecuteMethod() {
    String apiGroupGetterMethodName = ExchangeGenUtil.getApiGroupGetterMethodName(exchange, exchangeApi);
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
      .append("log.info(\"Calling ")
      .append(apiInterfaceClassName)
      .append(".")
      .append(apiMethodName)
      .append("() API");
    if (hasArguments) {
      bodyBuilder.append(" with request:{}\", request");
    } else {
      bodyBuilder.append("\"");
    }
    
    bodyBuilder.append(");\n")
       .append("if (")
       .append(OBSERVER_VAR)
       .append(" != null) ")
       .append(JavaCodeGenUtil.generateCodeBlock(EXCHANGE_VAR + ".subscribeObserver(" + OBSERVER_VAR + ");"));
    
    StringBuilder tryClause = new StringBuilder();
    tryClause.append("return DemoUtil.checkResponse(")
           .append(API_VAR)
           .append(".")
           .append(apiMethodName)
           .append("(");
    if (hasArguments) {
      tryClause.append("request");
    }
    tryClause.append("));");
         
    StringBuilder finallyClause = new StringBuilder()
        .append("if (")
        .append(OBSERVER_VAR)
        .append(" != null) ")
        .append(JavaCodeGenUtil.generateCodeBlock(EXCHANGE_VAR + ".unsubscribeObserver(" + OBSERVER_VAR + ");"))
        .append(EXCHANGE_VAR)
            .append(".dispose();\n");
    
    
    bodyBuilder.append(JavaCodeGenUtil.generateTryBlock(tryClause.toString(), 
              null, 
              null,
              finallyClause.toString(), 
              null));
    
    StringBuilder signature = new StringBuilder()
        .append("public static RestResponse<");
  
    signature.append(responseSimpleClassName).append("> execute(");
    if (hasArguments) {
      signature.append(requestSimpleClassName)
           .append(" ")
           .append("request, ");
    }
    signature
      .append("Properties configProperties, ExchangeObserver ")
      .append(OBSERVER_VAR)
      .append(") throws InterruptedException, ExecutionException");
    
    appendMethod(signature.toString(), bodyBuilder.toString(), generateExecuteMethodJavadoc());
  }
  
  private String generateExecuteMethodJavadoc() {
    StringBuilder javadoc = new StringBuilder()
        .append("Submits a call to ")
        .append(this.apiEndpointMethodJavadocLink)
        .append("and waits for response.\n");
    if (hasArguments) {
      javadoc.append("@param request     The request to submit\n");
    }
    javadoc.append("@param configProperties  The configuration properties to instantiate exchange with\n")
         .append("@param ")
         .append(OBSERVER_VAR)
         .append(" API observer that will notified of events. Is subscribed before REST API call and unsubscribed right after. Ignored if <code>null</code>\n")
         .append("@return Response data resulting from this API call\n")
         .append("@throws InterruptedException eventually thrown waiting for response\n")
         .append("@throws ExecutionException raised if response is not OK, see {@link RestResponse#isOk()}");
    return javadoc.toString();
  }
  
  private void generateMainMethod() {
    String propsVar = EndpointDemoGenUtil.CREATE_REQUEST_PROPERTIES_ARG_NAME;
    StringBuilder bodyBuilder = new StringBuilder()
      .append("Properties ")
      .append(propsVar)
      .append(" = ")
      .append(EndpointDemoGenUtil.getTestPropertiesInstruction(exchangeSimpleClassName, getImports()))
      .append(";\n")
      .append("execute(");
    if (hasArguments) {
      bodyBuilder.append(EndpointDemoGenUtil.generateFieldCreationMethodName(restApi.getRequest()))
        .append("(")
        .append(propsVar)
        .append("),\n")
        .append(EXECUTE_METHOD_ARG_INDENT);
    }
    bodyBuilder.append(propsVar)
               .append(",\n")
               .append(EXECUTE_METHOD_ARG_INDENT)
               .append(DemoUtil.class.getSimpleName())
               .append("::logRestApiEvent")
               .append(");\nSystem.exit(0);");
    
    appendMethod("public static void main(String[] args)", 
        JavaCodeGenUtil.generateTryBlock(
            bodyBuilder.toString(), 
            "log.error(\"Exception raised from main()\", t);\nSystem.exit(-1);", 
            "Throwable t", 
            null, null),
         "Runs REST endpoint demo snippet calling " 
          + apiEndpointMethodJavadocLink 
          + "\n@param args no argument expected");
  }

}
