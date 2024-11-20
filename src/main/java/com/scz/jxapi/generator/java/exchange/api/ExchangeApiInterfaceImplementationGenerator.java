package com.scz.jxapi.generator.java.exchange.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.util.CollectionUtils;

import com.scz.jxapi.exchange.AbstractExchangeApi;
import com.scz.jxapi.exchange.descriptor.CanonicalType;
import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.RestEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.WebsocketMessageTopicMatcherFieldDescriptor;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.JavaTypeGenerator;
import com.scz.jxapi.generator.java.exchange.ExchangeInterfaceGenerator;
import com.scz.jxapi.generator.java.exchange.ExchangeJavaWrapperGeneratorUtil;
import com.scz.jxapi.netutils.deserialization.MessageDeserializer;
import com.scz.jxapi.netutils.rest.FutureRestResponse;
import com.scz.jxapi.netutils.rest.HttpMethod;
import com.scz.jxapi.netutils.rest.HttpRequest;
import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;
import com.scz.jxapi.netutils.rest.ratelimits.RequestThrottler;
import com.scz.jxapi.netutils.websocket.WebsocketEndpoint;
import com.scz.jxapi.netutils.websocket.WebsocketListener;
import com.scz.jxapi.netutils.websocket.WebsocketSubscribeRequest;
import com.scz.jxapi.util.CollectionUtil;
import com.scz.jxapi.util.EncodingUtil;
import com.scz.jxapi.util.JsonUtil;

/**
 * Generates the Java source code for the implementation class of an API
 * interface for an exchange, as described in {@link ExchangeApiDescriptor}.
 * <p>
 * This class generates the actual implementation of the API interface, with
 * methods for every REST endpoint call and subscribe/unsubscribe methods for
 * every websocket stream.
 * <ul>
 * <li>The generated class extends {@link AbstractExchangeApi} and implements
 * the API interface.
 * <li>It contains a constructor that initializes the API implementation with
 * the exchange name, properties, and eventual rate limit rules.
 * <li>It contains a method for every REST endpoint call and
 * subscribe/unsubscribe methods for every websocket stream.
 * <li>It contains final deserializers (see {@link MessageDeserializer}) final
 * properties declarations for every REST endpoint response and websocket
 * message.
 * <li>When rate limits are defined for the exchange or the API, the generated
 * class contains final list of rate limit rule declarations for evenry rule
 * used in REST APIs exposed by this interface. These lists contain both
 * exchange wide, exchange API wide, and REST endpoint specific rules e.g. all
 * rules that must be enforced by calling the corresponding API.
 * <li>A {@link Logger} declaration generated.
 * <li>Base URL for REST endpoints and websocket URL are declared as static variables. If the base URL is not defined in the API descriptor, it is concatenated to URL.
 * </ul>
 * <p>
 * Regarding REST endpoint call methods generation:
 * <ul>
 * <li>For every REST endpoint, a method is generated with the following
 * signature:
 * 
 * <pre>
 * {@code
 * FutureRestResponse<ResponseType> methodName(RequestType request);
 * }
 * </pre>
 * 
 * <li>For every REST endpoint, a final deserializer is generated for the
 * response type.
 * <li>For every REST endpoint, a final rate limit rule declaration is generated
 * if rate limits are defined for the endpoint.
 * <li>For every REST endpoint, a public static final field is generated with
 * URL of REST endpoint as value. This field is used in the {@link HttpRequest}
 * creation. It has value of the endpoint URL (see
 * RestEndpointDescriptor#getHttpUrl()) if that URL is absolute, or
 * concatenation of API group base URL with it if it is a relative URI.
 * <li>A REST endpoint call method body contains the following steps:
 * <ul>
 * <li>If method has arguments and expects them to be serialized as URL
 * parameters, generate a URL parameters serializer instruction if the endpoint
 * has arguments and expects them to be serialized as URL parameters using
 * {@link EncodingUtil#createUrlQueryParameters(Object...)} method..
 * <li>Generate a DEBUG log statement with the HTTP method, endpoint name and
 * eventual request content.
 * <li>Generate a {@link HttpRequest} using
 * {@link HttpRequest#create(String, String, HttpMethod, Object, List, int)}
 * method.
 * <li>Set the url of the request using the endpoint URL static variable.
 * <li>Generate a submit request instruction using
 * {@link AbstractExchangeApi#submit(HttpRequest, MessageDeserializer)} method.
 * </ul>
 * </ul>
 * <p>
 * Regarding websocket stream subscribe/unsubscribe methods generation:
 * <ul>
 * <li>For every websocket endpoint, a subscribe method is generated with the
 * following signature:
 * 
 * <pre>
 * {@code
 * String subscribeWebsocketEndpointName(RequestType request, WebsocketListener<ResponseType> listener);
 * }
 * </pre>
 * 
 * <li>For every websocket endpoint, an unsubscribe method is generated with the
 * following signature:
 * 
 * <pre>
 * {@code
 * boolean unsubscribeWebsocketEndpointName(String subscriptionId);
 * }
 * </pre>
 * 
 * <li>A websocket endpoint subscribe method body contains the following steps:
 * <ul>
 * <li>Generate a topic string using
 * {@link EncodingUtil#substituteArguments(String, Object...)} method with
 * endpoint specific topic template and eventual request data.
 * <li>Generate a DEBUG log statement with the endpoint name and eventual
 * request
 * <li>Generate a {@link WebsocketSubscribeRequest} using
 * {@link WebsocketSubscribeRequest#create(String, Object, String, com.scz.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatcherFactory)}
 * method.
 * <li>Set the request object in the {@link WebsocketSubscribeRequest} if the
 * endpoint has arguments.
 * <li>Submit the {@link WebsocketSubscribeRequest} using
 * {@link WebsocketEndpoint#subscribe(WebsocketSubscribeRequest, WebsocketListener)}
 * method.
 * </ul>
 * <li>For every websocket endpoint, a final deserializer is generated for the
 * message type.
 * <li>For every websocket endpoint, a final {@link WebsocketEndpoint} instance
 * is created and initialized in the constructor using
 * {@link AbstractExchangeApi#createWebsocketEndpoint(String, MessageDeserializer)}
 * method.
 * <li>For every websocket endpoint, a {@link WebsocketSubscribeRequest} is
 * created and submitted using
 * {@link WebsocketEndpoint#subscribe(WebsocketSubscribeRequest, WebsocketListener)}
 * method.
 * </ul>
 * 
 * @see ExchangeApiDescriptor
 * @see AbstractExchangeApi
 */
public class ExchangeApiInterfaceImplementationGenerator extends JavaTypeGenerator {
	
	private static final String EXCHANGE_NAME_ARGUMENT_NAME = "exchangeName";
	private static final String REQUEST_THROTTLER_VARIABLE_NAME = "requestThrottler";
	
	private final ExchangeDescriptor exchangeDescriptor;
	
	private final ExchangeApiDescriptor exchangeApiDescriptor;
	
	private StringBuilder staticMembersDeclaration;
	
	private StringBuilder finalMembersDeclarations;
	
	private StringBuilder constructorBody;
	
	private Map<String, String> methods;
	private final boolean hasRateLimits;
	private List<String> apiGlobalRateLimitVariables;
	private Set<String> endpointSpecificRateLimitIds;
	private List<String> methodSignatures;
	private boolean hasBaseHttpUrl = false;
	
	
	/**
	 * Constructor.
	 * 
	 * @param exchangeDescriptor    the exchange descriptor where the API is defined
	 * @param exchangeApiDescriptor the API descriptor to generate interface implementation for
	 */
	public ExchangeApiInterfaceImplementationGenerator(ExchangeDescriptor exchangeDescriptor, 
													   ExchangeApiDescriptor exchangeApiDescriptor) {
		super(ExchangeJavaWrapperGeneratorUtil.getApiInterfaceImplementationClassName(exchangeDescriptor, exchangeApiDescriptor));
		this.exchangeDescriptor = exchangeDescriptor;
		this.exchangeApiDescriptor = exchangeApiDescriptor;
		setTypeDeclaration("public class");
		String fullInterfaceName = ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor);
		String simpleInterfaceName = JavaCodeGenerationUtil.getClassNameWithoutPackage(fullInterfaceName);
		setImplementedInterfaces(Arrays.asList(fullInterfaceName));
		setParentClassName(AbstractExchangeApi.class.getName());
		setDescription("Actual implementation of {@link " + simpleInterfaceName + "}<br>\n"
						+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING);
		hasRateLimits = checkHasRateLimits();
	}

	@Override
	public String generate() {
		staticMembersDeclaration = new StringBuilder();
		finalMembersDeclarations = new StringBuilder();
		constructorBody = new StringBuilder();
		methodSignatures = new ArrayList<>();
		methods = new HashMap<>();
		apiGlobalRateLimitVariables = new ArrayList<>();
		endpointSpecificRateLimitIds = new HashSet<>();
		List<String> exchangeRateLimitsVariables = new ArrayList<>();
		String simpleImplementationName =  JavaCodeGenerationUtil.getClassNameWithoutPackage(getName());
		List<RateLimitRule> exchangeRateLimits = exchangeDescriptor.getRateLimits();
		boolean hasExchangeLimits = !CollectionUtils.isEmpty(exchangeRateLimits);
		boolean hasRestEnpoints = !CollectionUtils.isEmpty(exchangeApiDescriptor.getRestEndpoints());
		boolean hasWsEnpoints = !CollectionUtils.isEmpty(exchangeApiDescriptor.getWebsocketEndpoints());
		String exchangeClassName = ExchangeJavaWrapperGeneratorUtil.getExchangeInterfaceName(exchangeDescriptor);
		addImport(exchangeClassName);
		constructorBody.append("super(")
					   .append(ExchangeApiInterfaceGenerator.EXCHANGE_API_NAME_VARIABLE)
					   .append(", ")
					   .append(EXCHANGE_NAME_ARGUMENT_NAME)
					   .append(", ")
					   .append(JavaCodeGenerationUtil.getClassNameWithoutPackage(exchangeClassName))
					   .append(".")
					   .append(ExchangeInterfaceGenerator.EXCHANGE_ID_VARIABLE)
					   .append(", ")
					   .append("properties");
		if ((hasRateLimits || hasExchangeLimits) 
				&& (hasRestEnpoints)) {
			addImport(RequestThrottler.class);
			
			if (hasExchangeLimits) {
				constructorBody.append(", ").append(REQUEST_THROTTLER_VARIABLE_NAME);
				exchangeRateLimits.forEach(r -> exchangeRateLimitsVariables.add(ExchangeJavaWrapperGeneratorUtil.generateRateLimitVariableName(r.getId())));
			} else {
				constructorBody
					.append(", new ")
					.append(RequestThrottler.class.getSimpleName())
					.append("(\"")
					.append(exchangeDescriptor.getName())
					.append(exchangeApiDescriptor.getName())
					.append("\")")
					.toString();
			}
			
			List<RateLimitRule> apiGlobalLimits = exchangeApiDescriptor.getRateLimits();
			if (!CollectionUtil.isEmpty(apiGlobalLimits)) {
				int rateLimitCounter = 0;
				for (RateLimitRule apiGlobalLimit: apiGlobalLimits) {
					apiGlobalRateLimitVariables.add(generateRateLimitVariable(apiGlobalLimit, exchangeApiDescriptor.getName() + rateLimitCounter++));
				}
			}
		}
		constructorBody.append(");\n");
	
		if (hasRestEnpoints) {
			String httpUrlVariableDeclaration = ExchangeApiGeneratorUtil.getHttpUrlVariableDeclaration(exchangeDescriptor, exchangeApiDescriptor, getImports());
			this.hasBaseHttpUrl = httpUrlVariableDeclaration != null;
			if (hasBaseHttpUrl) {
				staticMembersDeclaration
					.append("\n")
					.append(httpUrlVariableDeclaration)
					.append("\n");
			}
			String httpRequestExecutorFactoryClassName  = getHttpRequestExecutorFactory();
			String httpRequestTimeout = getHttpRequestTimeout() + "L";
			constructorBody
			   .append("createHttpRequestExecutor(")
			   .append(httpRequestExecutorFactoryClassName != null? 
					   	"\"" + httpRequestExecutorFactoryClassName + "\""
					   	: "null")
			   .append(", ")
			   .append(httpRequestTimeout)
			   .append(");\n");
			
			String httpRequestInterceptorFactoryFullClassName = getHttpRequestInterceptorFactory();
			if (httpRequestInterceptorFactoryFullClassName != null) {
				constructorBody.append("createHttpRequestInterceptor(\"")
							   .append(httpRequestInterceptorFactoryFullClassName)
							   .append("\");\n");
			}
		}
		
		if (exchangeApiDescriptor.getRestEndpoints() != null) {
			for (RestEndpointDescriptor restApi: exchangeApiDescriptor.getRestEndpoints()) {
				generateRestEndpointMethodDeclaration(restApi);
			}
		}
		
		if (hasWsEnpoints) {
			String websocketUrlVariableDeclaration = ExchangeApiGeneratorUtil.getWebsocketUrlVariableDeclaration(exchangeDescriptor, 
				    															exchangeApiDescriptor,  
				    															getImports());
			if (websocketUrlVariableDeclaration == null) {
				throw new IllegalArgumentException("No valid websocket URL for API with websocket endpoints:" 
													+ exchangeApiDescriptor);
			}
			staticMembersDeclaration.append("\n")
									.append(websocketUrlVariableDeclaration);
			constructorBody.append("createWebsocketManager(")
						   .append(ExchangeJavaWrapperGeneratorUtil.WEBSOCKET_URL_STATIC_VARIABLE)
						   .append(", ")
						   .append(JavaCodeGenerationUtil.getQuotedString(getWebsocketFactory()))
						   .append(", ")
						   .append(JavaCodeGenerationUtil.getQuotedString(getWebsocketHookFactory()))
						   .append(");\n");
			for (WebsocketEndpointDescriptor websocketApi : exchangeApiDescriptor.getWebsocketEndpoints()) {
				generateWebsocketApiMethodsDeclarations(websocketApi);
			}
		}
		
		appendToBody("\n");
		JavaCodeGenerationUtil.generateSlf4jLoggerDeclaration(this);
		if (staticMembersDeclaration.length() > 0) {
			appendToBody(staticMembersDeclaration.toString());
			appendToBody("\n");
		}
		String finalMembersDeclarationStr = finalMembersDeclarations.toString();
		appendToBody(finalMembersDeclarationStr);
		appendToBody("\n");
		addImport(Properties.class);
		StringBuilder constructorSignature = new StringBuilder()
							.append("public ")
							.append(simpleImplementationName)
							.append("(String ")
							.append(EXCHANGE_NAME_ARGUMENT_NAME)
							.append(", ")
							.append("Properties properties");
		if (hasExchangeLimits && hasRestEnpoints) {
			addImport(RequestThrottler.class);
			constructorSignature.append(", ")
								.append(RequestThrottler.class.getSimpleName())
								.append(" ")
								.append(REQUEST_THROTTLER_VARIABLE_NAME);
		}
		constructorSignature.append(")");
		appendMethod(constructorSignature.toString(), constructorBody.toString());
		for (String methodSignature : methodSignatures) {
			appendMethod(methodSignature, methods.get(methodSignature));
			appendToBody("\n");
		}
		
		return super.generate();
	}
	
	private void addMethod(String methodDeclaration, String methodBody) {
		methodSignatures.add(methodDeclaration);
		methods.put(methodDeclaration, methodBody);
	}
	
	private void addPrivateStaticFinalMember(String staticMemberDeclaration) {
		if (staticMembersDeclaration.length() > 0) {
			staticMembersDeclaration.append("\n");
		}
		staticMembersDeclaration.append("private static final ").append( staticMemberDeclaration);
	}

	private void generateWebsocketApiMethodsDeclarations(WebsocketEndpointDescriptor websocketApi) {
		Field request = ExchangeApiGeneratorUtil.resolveFieldProperties(exchangeApiDescriptor, websocketApi.getRequest());
		Type requestDataType = ExchangeApiGeneratorUtil.getFieldType(request);
		Field message = ExchangeApiGeneratorUtil.resolveFieldProperties(exchangeApiDescriptor, websocketApi.getMessage());
		Type messageDataType = ExchangeApiGeneratorUtil.getFieldType(message);
		addImport(WebsocketEndpoint.class);
		boolean hasArguments = ExchangeApiGeneratorUtil.websocketEndpointHasArguments(websocketApi, exchangeApiDescriptor);
		String requestClassName = null;
		String requestSimpleClassName = Object.class.getSimpleName();
		String requestArgName = ExchangeApiGeneratorUtil.DEFAULT_REQUEST_ARG_NAME;
		if (hasArguments) {
			if (requestDataType.isObject()) {
				requestClassName = ExchangeApiGeneratorUtil.generateWebsocketEndpointRequestPojoClassName(
										exchangeDescriptor, 
										exchangeApiDescriptor, 
										websocketApi);
			}
			requestSimpleClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForType(
													requestDataType, 
													getImports(), 
													requestClassName);
		}
		
		String messageClassObjectName = ExchangeApiGeneratorUtil.generateWebsocketEndpointMessagePojoClassName(
											exchangeDescriptor, 
											exchangeApiDescriptor, 
											websocketApi);
		String messageClassSimpleName = ExchangeJavaWrapperGeneratorUtil.getClassNameForType(
											messageDataType, 
											getImports(), 
											messageClassObjectName);
		String subscribeMethodName = ExchangeApiGeneratorUtil.getWebsocketSubscribeMethodName(websocketApi);
		String unsubscribeMethodName = ExchangeApiGeneratorUtil.getWebsocketUnsubscribeMethodName(websocketApi);
		String websocketEndpointVariableName = JavaCodeGenerationUtil.firstLetterToLowerCase(websocketApi.getName()) + "Ws";
		
		String getResponseDeserializerInstance = ExchangeApiGeneratorUtil.getNewMessageDeserializerInstruction(messageDataType, messageClassObjectName, getImports());
		
		finalMembersDeclarations.append("private final WebsocketEndpoint<")
								.append(messageClassSimpleName)
								.append("> ")
								.append(websocketEndpointVariableName)
								.append(";\n");
		constructorBody.append("this.")
					   .append(websocketEndpointVariableName)
					   .append(" = ")
					   .append("createWebsocketEndpoint(")
					   .append(ExchangeApiGeneratorUtil.getWebsocketEndpointNameStaticVariable(websocketApi.getName()))
					   .append(", ")
					   .append(getResponseDeserializerInstance)
					   .append(");\n");
		addImport(WebsocketListener.class);
		String subscribeMethodSignature = new StringBuilder()
											.append("String ")
											.append(subscribeMethodName)
											.append("(")
											.append(hasArguments? requestSimpleClassName + " " + requestArgName + ", ": "")
											.append("WebsocketListener<")
											.append(messageClassSimpleName)
											.append("> listener)").toString();
		
		String unsubscribeMethodSignature = "boolean " + unsubscribeMethodName + "(String subscriptionId)";
		addImport(WebsocketSubscribeRequest.class);
		
		StringBuilder subscribeMethodBody = new StringBuilder()
				.append("String topic = ")
				.append(generateWebsocketTopicSerializerDeclaration(websocketApi))
				.append("if (log.isDebugEnabled())\n")
				.append(JavaCodeGenerationUtil.INDENTATION)
				.append("log.debug(\"")
				.append(subscribeMethodName)
				.append(hasArguments? ":" + ExchangeApiGeneratorUtil.getRequestArgName(request.getName()) + ":\" + " + requestArgName: "\"")
				.append(");\n")
				.append(WebsocketSubscribeRequest.class.getSimpleName())
				.append(" websocketSubscribeRequest = ")
				.append(WebsocketSubscribeRequest.class.getSimpleName())
				.append(".create(")
				.append(hasArguments? requestArgName: "null")
				.append(", topic, topicMatcher(");
		
		List<String> replacements = new ArrayList<>();
		replacements.add("topic");
		replacements.add("\" + topic + \"");
		if (request != null) {
			replacements.add(ExchangeApiGeneratorUtil.getRequestArgName(request.getName()));
			replacements.add("\" + " + requestArgName + " + \"");
		}
		List<Field> requestFields = Optional.ofNullable(request == null? null: request.getProperties()).orElse(List.of());
		for (Field param: requestFields) {
			replacements.add(param.getMsgField() != null? param.getMsgField(): param.getName());
			String fieldClass = ExchangeJavaWrapperGeneratorUtil.getClassNameForType(param.getType(), getImports(), requestClassName);
			replacements.add("\" + request." 
								 + JavaCodeGenerationUtil.getGetAccessorMethodName(
										 		param.getName(), 
										 		fieldClass, 
										 		requestFields
										 		       .stream()
										 		       .map(Field::getName)
										 		.collect(Collectors.toList())) 
								 + "() + \"");
		}
		
		if (!CollectionUtil.isEmpty(websocketApi.getMessageTopicMatcherFields())) {
			for (int i = 0; i < websocketApi.getMessageTopicMatcherFields().size(); i++) {
				WebsocketMessageTopicMatcherFieldDescriptor topicMatcherField = websocketApi.getMessageTopicMatcherFields().get(i);
				String topicMatcherFieldDeclaration = new StringBuilder()
						.append("\"")
						.append(topicMatcherField.getName())
						.append("\", ")
						.append(EncodingUtil.substituteArguments("\"" + topicMatcherField.getValue() + "\"", (Object[]) replacements.toArray(new String[replacements.size()])))
						.toString();
				String emtpyStrAppend = " + \"\"";
				if (topicMatcherFieldDeclaration.endsWith(emtpyStrAppend)) {
					topicMatcherFieldDeclaration = topicMatcherFieldDeclaration.substring(0, topicMatcherFieldDeclaration.length() - emtpyStrAppend.length());
				}
				subscribeMethodBody.append(topicMatcherFieldDeclaration);	
				if (i < websocketApi.getMessageTopicMatcherFields().size() - 1) {
					subscribeMethodBody.append(", ");
				}
			}
		}
		subscribeMethodBody.append("));\n")
			.append(hasArguments? "websocketSubscribeRequest.setRequest(request);\n" : "")
			.append("return ")
			.append(websocketEndpointVariableName)
			.append (".subscribe(websocketSubscribeRequest, listener);");
		addMethod("@Override\npublic " + subscribeMethodSignature, subscribeMethodBody.toString());
		
		
		StringBuilder unsubscribeMethodBody = new StringBuilder()
				.append("if (log.isDebugEnabled())\n")
				.append(JavaCodeGenerationUtil.INDENTATION)
				.append("log.debug(\"")
				.append(unsubscribeMethodName)
				.append(": subscriptionId:\" + subscriptionId);\n")
				.append("return ")
				.append(websocketEndpointVariableName)
				.append(".unsubscribe(subscriptionId);\n");
		addMethod("@Override\npublic " + unsubscribeMethodSignature, unsubscribeMethodBody.toString());
	}
	
	private String generateWebsocketTopicSerializerDeclaration(WebsocketEndpointDescriptor websocketApi) {
		String topicSerializerBody = "\"\"";
		Field request = ExchangeApiGeneratorUtil.resolveFieldProperties(exchangeApiDescriptor, websocketApi.getRequest());
		if (ExchangeApiGeneratorUtil.websocketEndpointHasArguments(websocketApi, exchangeApiDescriptor)) {
			if (websocketApi.getTopic() != null) {
				topicSerializerBody =	generateUrlParametersOrTopicSerializerBodyFromTemplate(
												websocketApi.getTopic(), 
												request.getProperties(), 
												websocketApi.getTopicParametersListSeparator(),
												websocketApi.getRequest().getName());
			} else {
				topicSerializerBody = "request == null? \"\": \"\" + JsonUtil.pojoToJsonString(request)";
			}			
		} else if (websocketApi.getTopic() != null) {
			topicSerializerBody = "\"" + websocketApi.getTopic() + "\"";
		}
		
		return topicSerializerBody + ";\n";
	}
	
	private void generateRestEndpointMethodDeclaration(RestEndpointDescriptor restApi) {
		Field request = restApi.getRequest();
		Type requestDataType = ExchangeApiGeneratorUtil.getFieldType(request);
		Field response = restApi.getResponse();
		Type responseDataType =  ExchangeApiGeneratorUtil.getFieldType(response);
		boolean hasArguments = ExchangeApiGeneratorUtil.restEndpointHasArguments(restApi, exchangeApiDescriptor);
		String requestSimpleClassName = "Object";
		String requestArgName = ExchangeApiGeneratorUtil.DEFAULT_REQUEST_ARG_NAME;
		if (hasArguments) {
			String requestClassName = null;
			if (requestDataType.isObject()) {
				requestClassName = ExchangeApiGeneratorUtil.generateRestEnpointRequestPojoClassName(exchangeDescriptor, exchangeApiDescriptor, restApi);
			}
			requestSimpleClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForType(
					requestDataType, 
					getImports(), 
					requestClassName);
		}
		boolean hasResponse = ExchangeApiGeneratorUtil.restEndpointHasResponse(restApi, exchangeApiDescriptor);
		String responseSimpleClassName = "String";
		String getResponseDeserializerInstance = null;
		if (hasResponse) {
			String restResponseClassName = null;
			if (responseDataType.isObject()) {
				restResponseClassName = ExchangeApiGeneratorUtil.generateRestEnpointResponsePojoClassName(
						exchangeDescriptor, 
						exchangeApiDescriptor, 
						restApi);
			}
			
			responseSimpleClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForType(
					responseDataType, 
					getImports(), 
					restResponseClassName);
			getResponseDeserializerInstance = ExchangeApiGeneratorUtil.getNewMessageDeserializerInstruction(
					responseDataType, 
					restResponseClassName,
					getImports());
		} else {
			getResponseDeserializerInstance = ExchangeApiGeneratorUtil.getNewMessageDeserializerInstruction(null, null, getImports());
		}
		
		String deserializerVariableName = JavaCodeGenerationUtil.firstLetterToLowerCase(restApi.getName()) + "ResponseDeserializer";
		addImport(MessageDeserializer.class);
		finalMembersDeclarations
			.append("private final MessageDeserializer<")
			.append(responseSimpleClassName)
			.append("> ")
			.append(deserializerVariableName)
			.append(" = ")
			.append(getResponseDeserializerInstance)
			.append(";\n");
		
		String apiMethodName = ExchangeApiGeneratorUtil.getRestApiMethodName(restApi);
		String apiMethodSignature =  new StringBuilder()
											.append(FutureRestResponse.class.getSimpleName())
											.append("<")
											.append(responseSimpleClassName)
											.append("> ")
											.append(apiMethodName)
											.append("(")
											.append(hasArguments? requestSimpleClassName + " " + requestArgName : "")
											.append(")").toString(); 
		
		addImport(FutureRestResponse.class);
		String urlParametersSerializerVariableName = "urlParameters";
		String urlParametersSerializerDeclaration = generateUrlParametersSerializerDeclaration(restApi);
		if (urlParametersSerializerDeclaration != null) {
			urlParametersSerializerDeclaration = "String " + urlParametersSerializerVariableName + " = " + urlParametersSerializerDeclaration;
		}
		StringBuilder apiMethodBody = new StringBuilder()
				.append(Optional.ofNullable(urlParametersSerializerDeclaration).orElse(""))
				.append("if (log.isDebugEnabled())\n")
				.append(JavaCodeGenerationUtil.INDENTATION)
				.append("log.debug(\"")
				.append(restApi.getHttpMethod().name())
				.append(" ")
				.append(restApi.getName())
				.append(" >")
				.append(hasArguments? " \" + " + requestArgName : "\"")
				.append(");\n");
		
		List<String> rateLimitVariables = new ArrayList<>();
		if  (!CollectionUtils.isEmpty(exchangeDescriptor.getRateLimits())) {
			String exchangeClassName = ExchangeJavaWrapperGeneratorUtil.getExchangeInterfaceImplementationName(exchangeDescriptor);
			addImport(exchangeClassName);
			String exchangeSimpleClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(exchangeClassName);
			exchangeDescriptor.getRateLimits().forEach(
					rateLimit -> rateLimitVariables.add(exchangeSimpleClassName + "." + ExchangeJavaWrapperGeneratorUtil.generateRateLimitVariableName(rateLimit.getId()))); 
		}
		rateLimitVariables.addAll(apiGlobalRateLimitVariables);
		if (!CollectionUtils.isEmpty(restApi.getRateLimits())) {
			int rateLimitCounter = 1;
			for (RateLimitRule rateLimit: restApi.getRateLimits()) {
				rateLimitVariables.add(generateRateLimitVariable(rateLimit, restApi.getName() + rateLimitCounter++));
			}
		}
		
		apiMethodBody.append("return ");
		
		String rateLimitsVariable = "null";
		int requestWeight = Optional.ofNullable(restApi.getRequestWeight()).orElse(0);
		
		if (!rateLimitVariables.isEmpty()) {
			addImport(RateLimitRule.class);
			rateLimitsVariable = "RATE_LIMITS_" + JavaCodeGenerationUtil.getStaticVariableName(restApi.getName());
			generateRateLimitListVariable(rateLimitsVariable, rateLimitVariables);
		}
		
		addImport(HttpRequest.class);
		staticMembersDeclaration
				.append("\n")
				.append(ExchangeApiGeneratorUtil.getRestEndpointUrlVariableDeclaration(hasBaseHttpUrl, restApi));
		String endpointUrlVar = ExchangeApiGeneratorUtil.getRestEndpointUrlStaticVariableName(restApi);
		StringBuilder createHttpRequestInstruction = new StringBuilder()
				.append("HttpRequest.create(")
				.append(ExchangeApiGeneratorUtil.getRestEndpointNameStaticVariable(restApi.getName()))
				.append(", ")
				.append(endpointUrlVar);
		if (urlParametersSerializerDeclaration != null) {
			createHttpRequestInstruction
				.append(" + ")
				.append(urlParametersSerializerVariableName);
		}
		addImport(HttpMethod.class);
		createHttpRequestInstruction.append(", HttpMethod.")
				.append(restApi.getHttpMethod().name())
				.append(", ")
				.append(hasArguments? requestArgName: "null")
				.append(", ")
				.append(rateLimitsVariable)
				.append(", ")
				.append(requestWeight)
				.append(")")
				.toString();
				
		StringBuilder sumbitRequestInstruction = new StringBuilder();
		sumbitRequestInstruction
		.append("submit(")
		.append(createHttpRequestInstruction)
		.append(", ")
		.append(deserializerVariableName)
		.append(");\n");
		apiMethodBody.append(sumbitRequestInstruction.toString());
		
		addMethod("@Override\npublic " + apiMethodSignature, apiMethodBody.toString());
	}

	private boolean checkHasRateLimits() {
		List<RateLimitRule> rateLimits = exchangeApiDescriptor.getRateLimits();
		if (rateLimits != null && !rateLimits.isEmpty()) {
			return true;
		}
		if (exchangeApiDescriptor.getRestEndpoints() != null) {
			for (RestEndpointDescriptor restEndpoint : exchangeApiDescriptor.getRestEndpoints()) {
				rateLimits = restEndpoint.getRateLimits();
				if (rateLimits != null && !rateLimits.isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}
	
	private String generateRateLimitVariable(RateLimitRule rateLimitRule, String defaultName) {
		String name = rateLimitRule.getId();
		if (name == null) {
			name = defaultName;
		}
		String variableName = ExchangeJavaWrapperGeneratorUtil.generateRateLimitVariableName(name);
		// Add new rule definition if no one exists with same name. Otherwise, rule is expected to be a reference to existing one.
		if (!endpointSpecificRateLimitIds.contains(name)) {
			endpointSpecificRateLimitIds.add(name);
			String declaration = RateLimitRule.class.getSimpleName() + " " + variableName + " = ";
			addImport(RateLimitRule.class);
			if (rateLimitRule.getMaxTotalWeight() >= 0) {
				declaration +=  "RateLimitRule.createWeightedRule(\"" + name + "\", " + rateLimitRule.getTimeFrame()+ ", " + rateLimitRule.getMaxTotalWeight() + ");";
			} else {
				declaration +=  "RateLimitRule.createRule(\"" + name + "\", " + rateLimitRule.getTimeFrame()+ ", " + rateLimitRule.getMaxRequestCount() + ");";
			}
			addPrivateStaticFinalMember(declaration);
		}
		
		return variableName;
	}
	
	private void generateRateLimitListVariable(String variableName, List<String> rateLimitRuleVariableNames) {
		addImport(List.class);
		StringBuilder declaration = new StringBuilder()
										.append("List<")
										.append(RateLimitRule.class.getSimpleName())
										.append("> ")
										.append(variableName)
										.append(" = List.of(");
		for (int i = 0; i < rateLimitRuleVariableNames.size(); i++) {
			declaration.append(rateLimitRuleVariableNames.get(i));
			if (i < rateLimitRuleVariableNames.size() - 1) {
				declaration.append(", ");
			}
		}
		declaration.append(");");
		addPrivateStaticFinalMember(declaration.toString());
	}
	
	private String generateUrlParametersSerializerDeclaration(RestEndpointDescriptor restApi) {
		Field request = restApi.getRequest();
		String getUrlParametersBody = null;
		List<Field> enpointParameters = request == null? null:
													ExchangeApiGeneratorUtil.resolveFieldProperties(
														exchangeApiDescriptor, request).getProperties();
		if (ExchangeApiGeneratorUtil.restEndpointHasArguments(restApi, exchangeApiDescriptor)) {
			Type requestDataType = ExchangeApiGeneratorUtil.getFieldType(request);
			boolean hasQueryParams = restApi.isQueryParams()	
					|| !restApi.getHttpMethod().requestHasBody;
			if (restApi.getUrlParameters() != null) {
				getUrlParametersBody =	generateUrlParametersOrTopicSerializerBodyFromTemplate(
												restApi.getUrlParameters(), 
												enpointParameters, 
												restApi.getUrlParametersListSeparator(),
												request.getName());
			} else if (!CollectionUtil.isEmpty(enpointParameters) && hasQueryParams) {
				getUrlParametersBody = generateGetUrlParametersBodyUsingQueryParams(enpointParameters, false);
			} else if (!requestDataType.isObject() && hasQueryParams) {
				getUrlParametersBody = generateGetUrlParametersBodyUsingQueryParams(List.of(request), true);
			} // else object type with 0 properties, no url parameter
		} else if (restApi.getUrlParameters() != null) {
			getUrlParametersBody = "\"" + restApi.getUrlParameters() + "\"";
		}
		if (getUrlParametersBody != null) {
			getUrlParametersBody = getUrlParametersBody + ";\n";
		}
		return getUrlParametersBody;
	}
	
	private String generateGetUrlParametersBodyUsingQueryParams(List<Field> requestFields, boolean singleRequestParam) {
		addImport(EncodingUtil.class);
		StringBuilder s = new StringBuilder().append(EncodingUtil.class.getSimpleName() + ".createUrlQueryParameters(");
		for (int i = 0; i < requestFields.size(); i++) {
			if (i > 0) {
				s.append(", ");
			}
			Field f = requestFields.get(i);
			String name = Optional.ofNullable(f.getMsgField()).orElse(f.getName());
			s.append("\"")
			 .append(name)
			 .append("\", ");
			String value = "request";
			if (!singleRequestParam) {
				value += "." 
						+ JavaCodeGenerationUtil.getGetAccessorMethodName(
							f.getName(), 
							ExchangeApiGeneratorUtil.getClassNameForField(f, null, f.getObjectName()), 
							requestFields.stream().map(Field::getName).collect(Collectors.toList()))
						+ "()";
			}
			if (f.getType().getCanonicalType() == CanonicalType.LIST
				|| f.getType().getCanonicalType() == CanonicalType.MAP
				|| f.getType().isObject()) {
				addImport(JsonUtil.class);
				value = new StringBuilder()
								.append("JsonUtil.pojoToJsonString(")
								.append(value)
								.append(")").toString(); 
			} else if (f.getType().getCanonicalType() == CanonicalType.STRING) {
				value = new StringBuilder()
						.append(value)
						.toString(); 
			}
			s.append(value);
			
		}
		return s.append(")").toString();
	}

	private String generateUrlParametersOrTopicSerializerBodyFromTemplate(String urlParametersTemplate, 
																  		  List<Field> fields, 
																  		  String stringListSeparator,
																  		  String requestArgName) {
		fields = Optional.ofNullable(fields).orElse(List.of());
		if (stringListSeparator == null) {
			stringListSeparator = ExchangeJavaWrapperGeneratorUtil.DEFAULT_STRING_LIST_SEPARATOR;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(EncodingUtil.class.getSimpleName())
		  .append(".substituteArguments(\"")
		  .append(urlParametersTemplate)
		  .append("\"");
		int n = fields.size();
		for (int i = 0; i < n; i++) {
			Field param = fields.get(i);
			String name = param.getName();
			if (!urlParametersTemplate.contains("${" + name + "}")) {
				continue;
			}
			String value = "request." 
						   + JavaCodeGenerationUtil.getGetAccessorMethodName(
								name, 
								ExchangeApiGeneratorUtil.getClassNameForField(param, null, param.getObjectName()), 
								fields.stream().map(Field::getName).collect(Collectors.toList()))
						   + "()";
			if (param.getType().getCanonicalType() == CanonicalType.LIST) {
				value = EncodingUtil.class.getSimpleName() + ".listToString(" + value + ", \"" + stringListSeparator + "\")"; 
			}
			sb.append(", \"").append(name).append("\", ").append(value);
		}
		if (urlParametersTemplate.contains("${" + requestArgName + "}")) {
			n++;
			sb.append(", \"")
			  .append(requestArgName)
			  .append("\", ")
			  .append(ExchangeApiGeneratorUtil.DEFAULT_REQUEST_ARG_NAME);
		} 
		if (n <= 0) {
			return JavaCodeGenerationUtil.getQuotedString(urlParametersTemplate);
		}
		addImport(EncodingUtil.class);
		sb.append(")");
		return sb.toString();
	}
	
	private String getHttpRequestInterceptorFactory() {
		return Optional.ofNullable(exchangeApiDescriptor.getHttpRequestInterceptorFactory())
					   .orElse(exchangeDescriptor.getHttpRequestInterceptorFactory());
	}

	private String getHttpRequestExecutorFactory() {
		return Optional.ofNullable(exchangeApiDescriptor.getHttpRequestExecutorFactory())
					   .orElse(exchangeDescriptor.getHttpRequestExecutorFactory());
	}

	private long getHttpRequestTimeout() {
		long timeout = exchangeApiDescriptor.getHttpRequestTimeout();
		if (timeout >= 0) {
			return timeout;
		}
		timeout = exchangeDescriptor.getHttpRequestTimeout();
		if (timeout >= 0) {
			return timeout;
		}
		return -1L;
	}


	private String getWebsocketFactory() {
		return Optional.ofNullable(exchangeApiDescriptor.getWebsocketFactory())
					   .orElse(exchangeDescriptor.getWebsocketFactory());
	}

	private String getWebsocketHookFactory() {
		return Optional.ofNullable(exchangeApiDescriptor.getWebsocketHookFactory())
					   .orElse(exchangeDescriptor.getWebsocketHookFactory());
	}
	
	
}
