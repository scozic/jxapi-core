package com.scz.jxapi.generator.exchange;

import java.util.Optional;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.JavaTypeGenerator;
import com.scz.jxapi.util.DemoUtil;
import com.scz.jxapi.util.TestJXApiProperties;

public class RestEndpointDemoGenerator extends JavaTypeGenerator {
	
	private final ExchangeDescriptor exchangeDescriptor;
	private final ExchangeApiDescriptor exchangeApiDescriptor;
	private final RestEndpointDescriptor restApi;
	private StringBuilder body;
	private final boolean hasArguments;
	private final String requestClassName;
	private final String requestSimpleClassName;
	private String apiInterfaceClassName;
	private final String simpleApiClassName;
	
	public RestEndpointDemoGenerator(ExchangeDescriptor exchangeDescriptor, 
									 ExchangeApiDescriptor exchangeApiDescriptor, 
									 RestEndpointDescriptor restApi) {
		super(EndpointDemoGeneratorUtil.getRestApiDemoClassName(exchangeDescriptor, exchangeApiDescriptor, restApi));
		this.exchangeDescriptor = exchangeDescriptor;
		this.exchangeApiDescriptor = exchangeApiDescriptor;
		this.restApi = restApi;
		setTypeDeclaration("public class");
		this.hasArguments = ExchangeJavaWrapperGeneratorUtil.restEndpointHasArguments(restApi, exchangeApiDescriptor);
		if (hasArguments) {
			EndpointParameterType requestDataType =  Optional.ofNullable(restApi.getRequest().getEndpointParameterType()).orElse(EndpointParameterType.OBJECT);
			if (requestDataType.getCanonicalType().isPrimitive) {
				requestClassName = requestDataType.getCanonicalType().typeClass.getName();
			} else {
				requestClassName = ExchangeJavaWrapperGeneratorUtil.generateRestEnpointRequestClassName(exchangeDescriptor, exchangeApiDescriptor, restApi);
			}
			
			addImport(requestClassName);
			requestSimpleClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
					requestDataType, 
					getImports(), 
					requestClassName);
		} else {
			requestClassName = null;
			requestSimpleClassName = null;
		}
		this.apiInterfaceClassName = ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor);
		this.simpleApiClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(apiInterfaceClassName);
		addImport(apiInterfaceClassName);
		String apiMethodName = JavaCodeGenerationUtil.firstLetterToLowerCase(restApi.getName());
		setDescription("Snippet to test call to {@link " 
				+ ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor) 
				+ "#" 
				+ apiMethodName 
				+ "(" + (requestClassName == null? "": requestSimpleClassName) + ")}<br/>\n"
				+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING);
		
	}
	
	public String generate() {
		JavaCodeGenerationUtil.generateSlf4jLoggerDeclaration(this);
		generateMainMethodBody();
		return super.generate();
	}
	
	private void generateMainMethodBody() {
		body = new StringBuilder();
		String exchangeInterfaceClassName = ExchangeJavaWrapperGeneratorUtil.getExchangeInterfaceName(exchangeDescriptor);
		String exchangeName = JavaCodeGenerationUtil.firstLetterToLowerCase(exchangeDescriptor.getName());
		EndpointParameter request = ExchangeJavaWrapperGeneratorUtil.resolveEndpointParameters(exchangeApiDescriptor, restApi.getRequest());
		String exchangeImplClassName = exchangeInterfaceClassName + "Impl";
		addImport(exchangeImplClassName);
		addImport(TestJXApiProperties.class);
		addImport(DemoUtil.class);
		body.append(EndpointDemoGeneratorUtil.getNewTestApiInstruction(exchangeName, exchangeImplClassName, simpleApiClassName));
		if (hasArguments) {
			this.appendToBody(EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(
								request,  
								requestClassName, 
								getImports()));
			body.append(requestSimpleClassName)
				.append(" request = ")
				.append(EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethodName(request))
				.append("();\n");
		}
			
		String apiMethodName = JavaCodeGenerationUtil.firstLetterToLowerCase(restApi.getName());
		body.append("log.info(\"Calling ")
			.append(apiInterfaceClassName)
			.append(".")
			.append(apiMethodName)
			.append("() API");
		if (hasArguments) {
			body.append(" with request:\" + request");
		} else {
			body.append("\"");
		}
		body.append(");\n");
			
		body.append("DemoUtil.checkResponse(api.")
			.append(apiMethodName)
			.append("(");
		if (hasArguments) {
			body.append("request");
		}
		body.append("));\nSystem.exit(0);");
		
		appendMethod("public static void main(String[] args)", 
					"try {\n" 
						+ JavaCodeGenerationUtil.indent(body.toString(), JavaCodeGenerationUtil.INDENTATION) 
						+ "\n} catch (Throwable t) {\n"
						+ JavaCodeGenerationUtil.indent("log.error(\"Exception raised from main()\", t);\nSystem.exit(-1);", JavaCodeGenerationUtil.INDENTATION)
						+ "\n}");
	}

}
