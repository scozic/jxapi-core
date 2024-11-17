package com.scz.jxapi.generator.java.exchange.api.demo;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;

import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.RestEndpointDescriptor;
import com.scz.jxapi.generator.java.exchange.ExchangeJavaWrapperGeneratorUtil;
import com.scz.jxapi.generator.java.exchange.api.ExchangeApiGeneratorUtil;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.JavaTypeGenerator;
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
		
		addImport(apiInterfaceClassName);
		addImport(exchangeClassName);
		addImport(exchangeImplClassName);
		addImport(TestJXApiProperties.class);
		addImport(DemoUtil.class);
		addImport(ExecutionException.class);
		addImport(InterruptedException.class);
		String apiMethodName = JavaCodeGenerationUtil.firstLetterToLowerCase(restApi.getName());
		setDescription("Snippet to test call to {@link " 
				+ ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor) 
				+ "#" 
				+ apiMethodName 
				+ "(" + (requestClassName == null? "": requestSimpleClassName) + ")}<br>\n"
				+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING);
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
		this.appendToBody("\n");
		return super.generate();
	}
	
	private void generateExecuteMethod() {
		StringBuilder bodyBuilder = new StringBuilder();
		bodyBuilder.append(EndpointDemoGeneratorUtil.getNewTestApiInstruction(
														exchangeName, 
														exchangeClassName, 
														simpleApiClassName, 
														"configProperties"));
		bodyBuilder.append("\n");
		if (hasArguments) {
			bodyBuilder.append(requestSimpleClassName)
				.append(" request = ")
				.append(EndpointDemoGeneratorUtil.generateFieldCreationMethodName(request))
				.append("();\n");
		}
			
		String apiMethodName = JavaCodeGenerationUtil.firstLetterToLowerCase(restApi.getName());
		bodyBuilder.append("log.info(\"Calling ")
			.append(apiInterfaceClassName)
			.append(".")
			.append(apiMethodName)
			.append("() API");
		if (hasArguments) {
			bodyBuilder.append(" with request:\" + request");
		} else {
			bodyBuilder.append("\"");
		}
		bodyBuilder.append(");\n");
			
		bodyBuilder.append("DemoUtil.checkResponse(api.")
			.append(apiMethodName)
			.append("(");
		if (hasArguments) {
			bodyBuilder.append("request");
		}
		bodyBuilder.append("));");
		
		StringBuilder signature = new StringBuilder()
				.append("public static FutureRestResponse<");
	
		signature.append(responseSimpleClassName).append("> execute(");
		if (hasArguments) {
			signature.append("request, ");
		}
		signature.append("Properties configProperties) throws InterruptedException, ExecutionException");
		
		appendMethod(signature.toString(), bodyBuilder.toString());
	}
	
	private void generateMainMethod() {
		StringBuilder bodyBuilder = new StringBuilder().append("execute(");
		if (hasArguments) {
			bodyBuilder.append(EndpointDemoGeneratorUtil.generateFieldCreationMethodName(restApi.getRequest()))
					   .append("(), ");
		}
		bodyBuilder.append(EndpointDemoGeneratorUtil.getTestPropertiesInstruction(exchangeSimpleClassName, getImports()))
				   .append(");\nSystem.exit(0);");
											
		appendMethod("public static void main(String[] args)", 
				"try {\n" 
					+ JavaCodeGenerationUtil.indent(bodyBuilder.toString(), 
													JavaCodeGenerationUtil.INDENTATION) 
					+ "\n} catch (Throwable t) {\n"
					+ JavaCodeGenerationUtil.indent("log.error(\"Exception raised from main()\", t);\nSystem.exit(-1);", 
													JavaCodeGenerationUtil.INDENTATION)
					+ "\n}");
	}

}
