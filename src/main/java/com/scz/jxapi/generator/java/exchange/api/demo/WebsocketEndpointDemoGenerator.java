package com.scz.jxapi.generator.java.exchange.api.demo;

import java.util.Optional;

import org.slf4j.Logger;

import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import com.scz.jxapi.generator.java.exchange.ExchangeJavaWrapperGeneratorUtil;
import com.scz.jxapi.generator.java.exchange.api.ExchangeApiGeneratorUtil;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.JavaTypeGenerator;
import com.scz.jxapi.util.DemoUtil;
import com.scz.jxapi.util.TestJXApiProperties;

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
 * of the request sample values, see {@link Field#getSampleValue()} and
 * {@link Field#getSampleMapKeyValue()}.</li>
 * <li>Logs the subscription and unsubscription actions.</li>
 * <li>Logs the messages received from the websocket stream.</li>
 * <li>Unsubscribes from stream after some delay has elapsed after subscription.
 * Such delay is set in a <code>public static</code> variable named
 * <code>SUBSCRIPTION_DURATION</code>. Value of this variable is
 * {@link TestJXApiProperties#DEMO_WS_SUBSCRIPTION_DURATION}.</li>
 * <li>Exits after some delay has elased after unsubiscription. Such delay is
 * set in a <code>public static</code> variable named
 * <code>DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION</code>. Value of this variable
 * is
 * {@link TestJXApiProperties#DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION}.</li>
 * </ul>
 */
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
	
	/**
	 * Constructor.
	 * 
	 * @param exchangeDescriptor the exchange descriptor
	 * @param exchangeApiDescriptor the exchange API descriptor
	 * @param websocketApi the Websocket endpoint descriptor
	 */
	public WebsocketEndpointDemoGenerator(ExchangeDescriptor exchangeDescriptor, 
										  ExchangeApiDescriptor exchangeApiDescriptor, 
										  WebsocketEndpointDescriptor websocketApi) {
		super(EndpointDemoGeneratorUtil.getWebsocketApiDemoClassName(exchangeDescriptor, exchangeApiDescriptor, websocketApi));
		this.exchangeDescriptor = exchangeDescriptor;
		this.exchangeApiDescriptor = exchangeApiDescriptor;
		this.websocketApi = websocketApi;
		setTypeDeclaration("public class");
		this.hasArguments = ExchangeApiGeneratorUtil.websocketEndpointHasArguments(websocketApi, exchangeApiDescriptor);
		if (hasArguments) {
			Type requestDataType =  Optional.ofNullable(websocketApi.getRequest().getType()).orElse(Type.OBJECT);
			if (requestDataType.getCanonicalType().isPrimitive) {
				requestClassName = requestDataType.getCanonicalType().typeClass.getName();
			} else {
				requestClassName = ExchangeApiGeneratorUtil.generateWebsocketEndpointRequestPojoClassName(exchangeDescriptor, exchangeApiDescriptor, websocketApi);
			}
			
			addImport(requestClassName);
			requestSimpleClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForType(
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
				+ "(" + (requestClassName == null? "": requestSimpleClassName) + ")}<br>\n"
				+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING);
		this.fullStreamName = exchangeDescriptor.getName() + " " 
								+ exchangeApiDescriptor.getName() 
								+ " " + websocketApi.getName();
	}
	
	@Override
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
		Field request = ExchangeApiGeneratorUtil.resolveFieldProperties(exchangeApiDescriptor, websocketApi.getRequest());
		String exchangeImplClassName = exchangeInterfaceClassName + "Impl";
		addImport(exchangeImplClassName);
		addImport(TestJXApiProperties.class);
		
		body.append(EndpointDemoGeneratorUtil.getNewTestApiInstruction(exchangeName, exchangeImplClassName, simpleApiClassName));
			
		if (hasArguments) {
			this.appendToBody(EndpointDemoGeneratorUtil.generateFieldCreationMethod(
								request,  
								requestClassName, 
								getImports()))
				.append("\n");
			
			body.append(requestSimpleClassName)
				.append(" request = ")
				.append(EndpointDemoGeneratorUtil.generateFieldCreationMethodName(request))
				.append("();\n");
		}
		
		String subscribeMethodName = ExchangeApiGeneratorUtil.getWebsocketSubscribeMethodName(websocketApi);
		String unsubscribeMethodName = ExchangeApiGeneratorUtil.getWebsocketUnsubscribeMethodName(websocketApi);
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
		addImport(DemoUtil.class);
		body.append("m -> ")
			.append(DemoUtil.class.getSimpleName())
			.append(".logWsMessage(m));\n")
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
