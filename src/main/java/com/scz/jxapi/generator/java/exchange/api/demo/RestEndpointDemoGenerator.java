package com.scz.jxapi.generator.java.exchange.api.demo;

import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;

import com.scz.jxapi.exchange.ExchangeApiObserver;
import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.RestEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.JavaTypeGenerator;
import com.scz.jxapi.generator.java.exchange.ExchangeJavaWrapperGeneratorUtil;
import com.scz.jxapi.generator.java.exchange.api.ExchangeApiGeneratorUtil;
import com.scz.jxapi.netutils.rest.RestResponse;
import com.scz.jxapi.util.DemoUtil;
import com.scz.jxapi.util.TestJXApiProperties;

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
 * of the request sample values, see {@link Field#getSampleValue()} and
 * {@link Field#getSampleMapKeyValue()}.</li>
 * </ul>
 */
public class RestEndpointDemoGenerator extends JavaTypeGenerator {
	
	private final RestEndpointDescriptor restApi;
	private final boolean hasArguments;
	private final String requestClassName;
	private final String requestSimpleClassName;
	private String apiInterfaceClassName;
	private final String simpleApiClassName;
	private final String exchangeName;
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
	
	/**
	 * Constructor.
	 * 
	 * @param exchangeDescriptor the exchange descriptor
	 * @param exchangeApiDescriptor the exchange API descriptor
	 * @param restApi the REST endpoint descriptor
	 */
	public RestEndpointDemoGenerator(ExchangeDescriptor exchangeDescriptor, 
									 ExchangeApiDescriptor exchangeApiDescriptor, 
									 RestEndpointDescriptor restApi) {
		super(EndpointDemoGeneratorUtil.getRestApiDemoClassName(exchangeDescriptor, exchangeApiDescriptor, restApi));
		setTypeDeclaration("public class");
		this.restApi = restApi;
		this.exchangeName = exchangeDescriptor.getName();
		this.exchangeClassName = ExchangeJavaWrapperGeneratorUtil.getExchangeInterfaceName(exchangeDescriptor);
		this.exchangeSimpleClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(exchangeClassName);
		this.hasArguments = ExchangeApiGeneratorUtil.restEndpointHasArguments(restApi, exchangeApiDescriptor);
		this.request = ExchangeApiGeneratorUtil.resolveFieldProperties(exchangeApiDescriptor, restApi.getRequest());
		this.exchangeImplClassName = ExchangeJavaWrapperGeneratorUtil.getExchangeInterfaceImplementationName(exchangeDescriptor);
		if (hasArguments) {
			requestDataType =  Optional.ofNullable(request.getType()).orElse(Type.OBJECT);
			if (requestDataType.getCanonicalType().isPrimitive) {
				requestClassName = requestDataType.getCanonicalType().typeClass.getName();
			} else if (requestDataType.isObject() ){
				requestClassName = ExchangeApiGeneratorUtil.generateRestEnpointRequestPojoClassName(
										exchangeDescriptor, 
										exchangeApiDescriptor, 
										restApi);
			} else {
				requestClassName = Type.getLeafSubType(requestDataType).getCanonicalType().typeClass.getName();
			}
			
			addImport(requestClassName);
			requestSimpleClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForType(
										requestDataType, 
										getImports(), 
										requestClassName);
		} else {
			requestClassName = null;
			requestSimpleClassName = null;
			requestDataType = null;
		}
		this.apiInterfaceClassName = ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor);
		this.simpleApiClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(apiInterfaceClassName);
		response = restApi.getResponse();
		responseDataType = ExchangeJavaWrapperGeneratorUtil.getFieldType(response);
		hasResponse = ExchangeApiGeneratorUtil.restEndpointHasResponse(restApi, exchangeApiDescriptor);
		if (hasResponse) {
			String restResponseClassName = null;
			if (responseDataType != null && responseDataType.isObject()) {
				restResponseClassName = ExchangeApiGeneratorUtil.generateRestEnpointResponsePojoClassName(
						exchangeDescriptor, 
						exchangeApiDescriptor, 
						restApi);
				addImport(restResponseClassName);
			}
			responseSimpleClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForType(
					responseDataType, 
					getImports(), 
					restResponseClassName);

		} else {
			responseSimpleClassName = null;
		}
		this.apiMethodName = ExchangeApiGeneratorUtil.getRestApiMethodName(restApi);
		this.apiEndpointMethodJavadocLink = generateApiEndpointMethodJavadocLink();
		addImport(apiInterfaceClassName);
		addImport(exchangeClassName);
		addImport(exchangeImplClassName);
		addImport(TestJXApiProperties.class);
		addImport(DemoUtil.class);
		addImport(ExecutionException.class);
		addImport(InterruptedException.class);
		addImport(RestResponse.class);
		addImport(Properties.class);
		addImport(ExchangeApiObserver.class);
		setDescription(generateClassJavadoc());
	}
	
	@Override
	public String generate() {
		JavaCodeGenerationUtil.generateSlf4jLoggerDeclaration(this);
		if (hasArguments) {
			this.appendToBody(EndpointDemoGeneratorUtil.generateFieldCreationMethod(
								request,  
								requestClassName, 
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
						.append(JavaCodeGenerationUtil.getClassNameWithoutPackage(apiInterfaceClassName))
						.append("#")
						.append(apiMethodName)
						.append("(");
		if (hasArguments) {
			javadoc.append(requestSimpleClassName);
		}
		return javadoc.append(")}").toString();
	}
	
	private String generateClassJavadoc() {
		return new StringBuilder()
				.append("Snippet to test call to ")
				.append(apiEndpointMethodJavadocLink)
				.append(")}<br>\n")
				.append(JavaCodeGenerationUtil.GENERATED_CODE_WARNING)
				.toString();
	}
	
	private void generateExecuteMethod() {
		StringBuilder bodyBuilder = new StringBuilder();
		bodyBuilder.append(EndpointDemoGeneratorUtil.getNewTestApiInstruction(
														exchangeName, 
														exchangeClassName, 
														simpleApiClassName, 
														"configProperties"));
		bodyBuilder.append("\n");
		bodyBuilder.append("log.info(\"Calling ")
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
		   .append("if (apiObserver != null) ")
		   .append(JavaCodeGenerationUtil.generateCodeBlock("api.subscribeObserver(apiObserver);"));
		
		StringBuilder tryClause = new StringBuilder();
		tryClause.append("return DemoUtil.checkResponse(api.")
			     .append(apiMethodName)
			     .append("(");
		if (hasArguments) {
			tryClause.append("request");
		}
		tryClause.append("));\n");
		
		StringBuilder finallyClause = new StringBuilder()
				.append("if (apiObserver != null) ")
				.append(JavaCodeGenerationUtil.generateCodeBlock("api.unsubscribeObserver(apiObserver);"));
		
		bodyBuilder.append(JavaCodeGenerationUtil.generateTryBlock(tryClause.toString(), 
						  null, 
						  null,
						  finallyClause.toString(), 
						  null));;
		
		StringBuilder signature = new StringBuilder()
				.append("public static RestResponse<");
	
		signature.append(responseSimpleClassName).append("> execute(");
		if (hasArguments) {
			signature.append(requestSimpleClassName)
					 .append(" ")
					 .append("request, ");
		}
		signature.append("Properties configProperties, ExchangeApiObserver apiObserver) throws InterruptedException, ExecutionException");
		
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
		javadoc.append("@param properties  The configuration properties to instantiate exchange with\n")
			   .append("@param apiObserver API observer that will notified of events. Is subscribed before REST API call and unsubscribed right after. Ignored if <code>null</code>\n")
			   .append("@return Response data resulting from this API call\n")
			   .append("@throws InterruptedException eventually thrown waiting for response")
			   .append("@throws ExecutionException raised if response is not OK, see {@link RestResponse#isOk()}");
		return javadoc.toString();
	}
	
	private void generateMainMethod() {
		StringBuilder bodyBuilder = new StringBuilder().append("execute(");
		if (hasArguments) {
			bodyBuilder.append(EndpointDemoGeneratorUtil.generateFieldCreationMethodName(restApi.getRequest()))
					   .append("(), ");
		}
		bodyBuilder.append(EndpointDemoGeneratorUtil.getTestPropertiesInstruction(exchangeSimpleClassName, getImports()))
				   .append(", ")
				   .append(DemoUtil.class.getSimpleName())
				   .append("::logRestApiEvent")
				   .append(");\nSystem.exit(0);");
		
		appendMethod("public static void main(String[] args)", 
				JavaCodeGenerationUtil.generateTryBlock(
						bodyBuilder.toString(), 
						"log.error(\"Exception raised from main()\", t);\nSystem.exit(-1);", 
						"Throwable t", 
						null, null),
				 "Runs REST endpoint demo snippet calling " 
					+ apiEndpointMethodJavadocLink 
					+ "\n@param args no argument expected");
	}

}
