package com.scz.jxapi.generator.java.exchange.api.demo;

import java.util.Optional;
import java.util.Properties;

import org.slf4j.Logger;

import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.exchange.ExchangeApiObserver;
import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import com.scz.jxapi.generator.java.exchange.ExchangeJavaWrapperGeneratorUtil;
import com.scz.jxapi.generator.java.exchange.api.ExchangeApiGeneratorUtil;
import com.scz.jxapi.netutils.websocket.WebsocketListener;
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
	private final String websocketListenerSimpleClassName;
	private final String exchangeClassName;
	private final String exchangeSimpleClassName;
	
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
		this.request = ExchangeApiGeneratorUtil.resolveFieldProperties(exchangeApiDescriptor, websocketApi.getRequest());
		this.exchangeClassName = ExchangeJavaWrapperGeneratorUtil.getExchangeInterfaceName(exchangeDescriptor);
		this.exchangeSimpleClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(exchangeClassName);
		subscribeMethodName = ExchangeApiGeneratorUtil.getWebsocketSubscribeMethodName(websocketApi);
		unsubscribeMethodName = ExchangeApiGeneratorUtil.getWebsocketUnsubscribeMethodName(websocketApi);
		setTypeDeclaration("public class");
		this.hasArguments = ExchangeApiGeneratorUtil.websocketEndpointHasArguments(websocketApi, exchangeApiDescriptor);
		if (hasArguments) {
			Type requestDataType =  Optional.ofNullable(request.getType()).orElse(Type.OBJECT);
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
		Type messageDataType = ExchangeJavaWrapperGeneratorUtil.getFieldType(websocketApi.getMessage());
		messageClassSimpleName = ExchangeJavaWrapperGeneratorUtil.getClassNameForType(
				messageDataType, 
				getImports(), 
				ExchangeApiGeneratorUtil.generateWebsocketEndpointMessagePojoClassName(
						exchangeDescriptor, 
						exchangeApiDescriptor, 
						websocketApi));
		websocketListenerSimpleClassName = WebsocketListener.class.getSimpleName() + "<" + messageClassSimpleName + ">";
		addImport(WebsocketListener.class);
		addImport(DemoUtil.class);
		addImport(Properties.class);
		addImport(ExchangeApiObserver.class);
		addImport(TestJXApiProperties.class);
	}
	
	@Override
	public String generate() {
		generateStaticVariables();
		this.appendToBody("\n");
		if (hasArguments) {
			this.appendToBody(EndpointDemoGeneratorUtil.generateFieldCreationMethod(
								request,  
								requestClassName, 
								getImports()))
				.append("\n");
		}
		generateSubscribeMethod();
		this.appendToBody("\n");
		generateMainMethod();
		return super.generate();
	}
	
	private void generateStaticVariables() {
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
			.append(".DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION;\n");
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
					 .append(" request, ");
		}
		signature.append("WebsocketListener<")
				 .append(messageClassSimpleName)
				 .append("> messageListener, Properties configProperties, ExchangeApiObserver apiObserver, ")
				 .append("long subscriptionDuration, long delayBeforeExitAfterUnsubscription) throws InterruptedException");
		return signature.toString();
	}
	
	private String generateSubscribeMethodJavadoc() {
		StringBuilder javadoc = new StringBuilder()
				.append("Tests websocket stream subscription to {@link ")
				.append(apiInterfaceClassName)
				.append("#")
				.append(subscribeMethodName)
				.append("(");
		if (hasArguments) {
			javadoc.append(requestSimpleClassName)
				   .append(", ");
		}
		javadoc.append(websocketListenerSimpleClassName)
			   .append(")}.\n<br>Websocket endpoint subscription will be performed using given websocket listener ")
			   .append("then after waiting for <code>subscriptionDuration</code> delay, unsubscription is performed.\n")
			   .append("Finally waits for <code>delayBeforeExitAfterUnsubscription</code> delay before returning to make sure no more messages are dispatched.\n");
		if (hasArguments) {
			javadoc
			   .append("@param request                            The subscription request\n");
		}
		javadoc.append("@param messageListener                    The listener that will receive messages dispatched while subscription is active\n")
		   	   .append("@param configProperties                   Exchange configuration properties.\n")
		   	   .append("@param apiObservver                       {@link ExchangeApiObserver} (optional, ignored if <code>null</code>) observer will")
		   	   .append (" be subscribed to Exchange API exposing websocket endpoint that will be notifed of received websocket events.")
		   	   .append("Useful in particular to get notified of websocket errors.\n")
		   	   .append("@param subscriptionDuration               Delay to wait for after subscription before unsubscribing.\n")
		   	   .append("@param delayBeforeExitAfterUnsubscription Delay to wait before returning after unsubscribing.\n")
		   	   .append("@throws InterruptedException eventually thrown while sleeping for <code>subscriptionDuration</code> or <code>delayBeforeExitAfterUnsubscription</code> delays");
		return javadoc.toString();
	}
	
	private String generateSubscribeMethodBody() {
		String exchangeName = JavaCodeGenerationUtil.firstLetterToLowerCase(exchangeDescriptor.getName());
		StringBuilder bodyBuilder = new StringBuilder();
		bodyBuilder.append(EndpointDemoGeneratorUtil.getNewTestApiInstruction(
				exchangeName, 
				exchangeClassName, 
				simpleApiClassName, 
				"configProperties"));
		bodyBuilder.append("\nlog.info(\"Subscribing to websocket API '")
		.append(fullStreamName)
		.append("' for \" + ")
		.append(SUBSCRIPTION_DURATION_STATIC_VAR_NAME)
		.append(" + \"ms");
		if (hasArguments) {
			bodyBuilder.append(" with request:\" + request);\n");
		} else {
			bodyBuilder.append("\");\n");
		}
	
		bodyBuilder.append("String subId = api.")
			.append(subscribeMethodName)
			.append("(");
		if (hasArguments) {
			bodyBuilder.append("request, ");
		}

		bodyBuilder.append("messageListener);\n")
			.append("Thread.sleep(subscriptionDuration);\n")
			.append("log.info(\"Unubscribing from '")
			.append(fullStreamName)
			.append("' stream\");\n")
			.append("api.")
			.append(unsubscribeMethodName)
			.append("(subId);\n")
			.append("Thread.sleep(delayBeforeExitAfterUnsubscription);\n");
		return bodyBuilder.toString();
	}
	
	private void generateMainMethod() {
		StringBuilder bodyBuilder = new StringBuilder();
		String exchangeInterfaceClassName = ExchangeJavaWrapperGeneratorUtil.getExchangeInterfaceName(exchangeDescriptor);
		String exchangeImplClassName = ExchangeJavaWrapperGeneratorUtil.getExchangeInterfaceImplementationName(exchangeInterfaceClassName);
		addImport(exchangeImplClassName);

		
		bodyBuilder.append("subscribe(");
		if (hasArguments) {
			bodyBuilder.append(EndpointDemoGeneratorUtil.generateFieldCreationMethodName(request))
					   .append("(), ");
		}
		bodyBuilder.append(DemoUtil.class.getSimpleName())
				   .append("::logWsMessage, ")
				   .append(EndpointDemoGeneratorUtil.getTestPropertiesInstruction(exchangeSimpleClassName, getImports()))
				   .append(", DemoUtil::logWsApiEvent, ")
				   .append(SUBSCRIPTION_DURATION_STATIC_VAR_NAME)
				   .append(", ")
				   .append(DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION_VAR_NAME)
				   .append(");\nSystem.exit(0);");
		appendMethod("public static void main(String[] args)", 
				"try {\n" 
					+ JavaCodeGenerationUtil.indent(bodyBuilder.toString(), JavaCodeGenerationUtil.INDENTATION) 
					+ "\n} catch (Throwable t) {\n"
					+ JavaCodeGenerationUtil.indent("log.error(\"Exception raised from main()\", t);\nSystem.exit(-1);", JavaCodeGenerationUtil.INDENTATION)
					+ "\n}",
					generateMainMethodJavadoc());
		
	}
	
	private String generateMainMethodJavadoc() {
		return  new StringBuilder()
						.append("Runs websocket endpoint '")
						.append(fullStreamName)
						.append("' subscription demo.").toString();
	}

}
