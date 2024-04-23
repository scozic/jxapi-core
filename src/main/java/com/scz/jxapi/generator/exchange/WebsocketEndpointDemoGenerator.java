package com.scz.jxapi.generator.exchange;

import java.util.Optional;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.JavaTypeGenerator;
import com.scz.jxapi.util.DemoUtil;
import com.scz.jxapi.util.TestJXApiProperties;

public class WebsocketEndpointDemoGenerator extends JavaTypeGenerator {
	
	private static final String SUBSCRIPTION_DURATION_STATIC_VAR_NAME = "SUBSCRIPTION_DURATION";
	private static final String DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION_VAR_NAME = "DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION";
	
	private final ExchangeDescriptor exchangeDescriptor;
	private final ExchangeApiDescriptor exchangeApiDescriptor;
	private final WebsocketEndpointDescriptor websocketApi;
	private StringBuilder body;
	private final boolean hasArguments;
	private final String requestClassName;
	private final String requestSimpleClassName;
	private String apiInterfaceClassName;
	private final String simpleApiClassName;
	private final String fullStreamName;
	
	public WebsocketEndpointDemoGenerator(ExchangeDescriptor exchangeDescriptor, 
										  ExchangeApiDescriptor exchangeApiDescriptor, 
										  WebsocketEndpointDescriptor websocketApi) {
		super(EndpointDemoGeneratorUtil.getWebsocketApiDemoClassName(exchangeDescriptor, exchangeApiDescriptor, websocketApi));
		this.exchangeDescriptor = exchangeDescriptor;
		this.exchangeApiDescriptor = exchangeApiDescriptor;
		this.websocketApi = websocketApi;
		setTypeDeclaration("public class");
		this.hasArguments = ExchangeJavaWrapperGeneratorUtil.websocketEndpointHasArguments(websocketApi, exchangeApiDescriptor);
		if (hasArguments) {
			EndpointParameterType requestDataType =  Optional.ofNullable(websocketApi.getRequest().getEndpointParameterType()).orElse(EndpointParameterType.OBJECT);
			if (requestDataType.getCanonicalType().isPrimitive) {
				requestClassName = requestDataType.getCanonicalType().typeClass.getName();
			} else {
				requestClassName = ExchangeJavaWrapperGeneratorUtil.generateWebsocketEndpointRequestClassName(exchangeDescriptor, exchangeApiDescriptor, websocketApi);
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
		String apiMethodName = JavaCodeGenerationUtil.firstLetterToLowerCase(websocketApi.getName());
		setDescription("Snippet to test call to {@link " 
				+ ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor) 
				+ "#" 
				+ apiMethodName 
				+ "(" + (requestClassName == null? "": requestSimpleClassName) + ")}<br/>\n"
				+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING);
		this.fullStreamName = exchangeDescriptor.getName() + " " 
								+ exchangeApiDescriptor.getName() 
								+ " " + websocketApi.getName();
	}
	
	public String generate() {
		JavaCodeGenerationUtil.generateSlf4jLoggerDeclaration(this);
		this.appendToBody("private static final long ")
			.append(SUBSCRIPTION_DURATION_STATIC_VAR_NAME)
			.append(" = ")
			.append(TestJXApiProperties.class.getSimpleName())
			.append(".DEMO_WS_SUBSCRIPTION_DURATION;\n")
			.append("private static final long ")
			.append(DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION_VAR_NAME)
			.append(" = ")
			.append(TestJXApiProperties.class.getSimpleName())
			.append(".DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION;\n\n");
		generateMainMethodBody();
		return super.generate();
	}
	
	private void generateMainMethodBody() {
		body = new StringBuilder();
		String exchangeInterfaceClassName = ExchangeJavaWrapperGeneratorUtil.getExchangeInterfaceName(exchangeDescriptor);
		String exchangeName = JavaCodeGenerationUtil.firstLetterToLowerCase(exchangeDescriptor.getName());
		EndpointParameter request = ExchangeJavaWrapperGeneratorUtil.resolveEndpointParameters(exchangeApiDescriptor, websocketApi.getRequest());
		String exchangeImplClassName = exchangeInterfaceClassName + "Impl";
		addImport(exchangeImplClassName);
		addImport(TestJXApiProperties.class);
		addImport(DemoUtil.class);
		
		body.append(simpleApiClassName)
			.append(" api = new ")
			.append(JavaCodeGenerationUtil.getClassNameWithoutPackage(exchangeImplClassName))
			.append("(TestJXApiProperties.filterProperties(\"")
			.append(exchangeName)
			.append("\", true)).get")
			.append(simpleApiClassName)
			.append("();\n");
			
		if (hasArguments) {
			this.appendToBody(EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethod(
								request,  
								requestClassName, 
								getImports()))
				.append("\n");
			
			body.append(requestSimpleClassName)
				.append(" request = ")
				.append(EndpointDemoGeneratorUtil.generateEndpointParameterCreationMethodName(request))
				.append("();\n");
		}
		
		String subscribeMethodName = ExchangeJavaWrapperGeneratorUtil.getWebsocketSubscribeMethodName(websocketApi);
		String unsubscribeMethodName = ExchangeJavaWrapperGeneratorUtil.getWebsocketUnsubscribeMethodName(websocketApi);
		body.append("log.info(\"Subscribing to websocket API '")
			.append(fullStreamName)
			.append("' for \" + ")
			.append(SUBSCRIPTION_DURATION_STATIC_VAR_NAME)
			.append(" + \"ms");
		if (hasArguments) {
			body.append(" with request:\" + request);\n");
		} else {
			body.append("\");\n");
		}
		
		body.append("String subId = api.")
			.append(subscribeMethodName)
			.append("(");
		if (hasArguments) {
			body.append("request, ");
		}
		body.append("m -> log.info(\"Received message:\" + m));\n")
			.append("Thread.sleep(")
			.append(SUBSCRIPTION_DURATION_STATIC_VAR_NAME)
			.append(");\n")
			.append("log.info(\"Unubscribing from '")
			.append(fullStreamName)
			.append("' stream\");\n")
			.append("api.")
			.append(unsubscribeMethodName)
			.append("(subId);\n")
			.append("Thread.sleep(")
			.append(DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION_VAR_NAME)
			.append(");\n")
			.append("System.exit(0);");
		
		appendMethod("public static void main(String[] args)", 
				"try {\n" 
					+ JavaCodeGenerationUtil.indent(body.toString(), JavaCodeGenerationUtil.INDENTATION) 
					+ "\n} catch (Throwable t) {\n"
					+ JavaCodeGenerationUtil.indent("log.error(\"Exception raised from main()\", t);\nSystem.exit(-1);", JavaCodeGenerationUtil.INDENTATION)
					+ "\n}");
		
	}

}
