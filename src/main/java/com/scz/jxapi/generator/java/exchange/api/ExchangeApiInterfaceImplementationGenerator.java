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

import com.scz.jxapi.exchange.AbstractExchangeApi;
import com.scz.jxapi.exchange.descriptor.CanonicalType;
import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.RestEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.WebsocketMessageTopicMatcherFieldDescriptor;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.JavaTypeGenerator;
import com.scz.jxapi.generator.java.exchange.ExchangeInterfaceGenerator;
import com.scz.jxapi.generator.java.exchange.ExchangeInterfaceImplementationGenerator;
import com.scz.jxapi.generator.java.exchange.ExchangeJavaWrapperGeneratorUtil;
import com.scz.jxapi.netutils.deserialization.MessageDeserializer;
import com.scz.jxapi.netutils.rest.FutureRestResponse;
import com.scz.jxapi.netutils.rest.HttpMethod;
import com.scz.jxapi.netutils.rest.HttpRequest;
import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;
import com.scz.jxapi.netutils.rest.ratelimits.RequestThrottler;
import com.scz.jxapi.netutils.websocket.DefaultWebsocketMessageTopicMatcher;
import com.scz.jxapi.netutils.websocket.WebsocketEndpoint;
import com.scz.jxapi.netutils.websocket.WebsocketListener;
import com.scz.jxapi.netutils.websocket.WebsocketSubscribeRequest;
import com.scz.jxapi.util.EncodingUtil;
import com.scz.jxapi.util.JsonUtil;

public class ExchangeApiInterfaceImplementationGenerator extends JavaTypeGenerator {
	
	private static final String EXCHANGE_NAME_ARGUMENT_NAME = "exchangeName";
	private static final String REQUEST_THROTTLER_VARIABLE_NAME = "requestThrottler";
	private static final String HTTP_REQUEST_INTERCEPTOR_VARIABLE_NAME = "httpRequestInterceptor";
	private static final String HTTP_REQUEST_EXECUTOR_VARIABLE_NAME = "httpRequestExecutor";
	private static final String WEBSOCKET_MANAGER_VARIABLE_NAME = "websocketManager";
	
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
	
	
	public ExchangeApiInterfaceImplementationGenerator(ExchangeDescriptor exchangeDescriptor, ExchangeApiDescriptor exchangeApiDescriptor) {
		super(ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor) + "Impl");
		this.exchangeDescriptor = exchangeDescriptor;
		this.exchangeApiDescriptor = exchangeApiDescriptor;
		setTypeDeclaration("public class");
		String fullInterfaceName = ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor);
		String simpleInterfaceName = JavaCodeGenerationUtil.getClassNameWithoutPackage(fullInterfaceName);
		setImplementedInterfaces(Arrays.asList(fullInterfaceName));
		setParentClassName(AbstractExchangeApi.class.getName());
		setDescription("Actual implementation of {@link " + simpleInterfaceName + "}<br/>\n"
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
		
		String fullInterfaceName = ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor);
		String simpleInterfaceName = JavaCodeGenerationUtil.getClassNameWithoutPackage(fullInterfaceName);
		String simpleImplementationName = simpleInterfaceName + "Impl";
		
		boolean hasExchangeLimits = exchangeDescriptor.getRateLimits() != null 
										&& !exchangeDescriptor.getRateLimits().isEmpty();
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
				&& (exchangeApiDescriptor.getRestEndpoints() != null && !exchangeApiDescriptor.getRestEndpoints().isEmpty())) {
			addImport(RequestThrottler.class);
			
			if (hasExchangeLimits) {
				constructorBody.append(", ").append(REQUEST_THROTTLER_VARIABLE_NAME);
				exchangeDescriptor.getRateLimits().forEach(r -> exchangeRateLimitsVariables.add(ExchangeJavaWrapperGeneratorUtil.generateRateLimitVariableName(r.getId())));
				
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
			if (apiGlobalLimits != null && !apiGlobalLimits.isEmpty()) {
				int rateLimitCounter = 0;
				for (RateLimitRule apiGlobalLimit: apiGlobalLimits) {
					apiGlobalRateLimitVariables.add(generateRateLimitVariable(apiGlobalLimit, exchangeApiDescriptor.getName() + rateLimitCounter++));
				}
			}
		}
		constructorBody.append(");\n");
	
		if (exchangeApiDescriptor.hasRestEndpoints()) {
			String httpRequestExecutorFactoryClassName  = exchangeApiDescriptor.getHttpRequestExecutorFactory();
			constructorBody.append("this.")
			   .append(HTTP_REQUEST_EXECUTOR_VARIABLE_NAME)
			   .append(" = ")
			   .append("createHttpRequestExecutor(")
			   .append(httpRequestExecutorFactoryClassName != null? 
					   	"\"" + httpRequestExecutorFactoryClassName + "\""
					   	: "null")
			   .append(");\n");
			
			String httpRequestInterceptorFactoryFullClassName = exchangeApiDescriptor.getHttpRequestInterceptorFactory();
			if (httpRequestInterceptorFactoryFullClassName != null) {
				constructorBody.append("this.")
							   .append(HTTP_REQUEST_INTERCEPTOR_VARIABLE_NAME)
							   .append(" = ")
							   .append("createHttpRequestInterceptor(\"")
							   .append(httpRequestInterceptorFactoryFullClassName)
							   .append("\");\n");
			}
		}
		
		if (exchangeApiDescriptor.getRestEndpoints() != null) {
			for (RestEndpointDescriptor restApi: exchangeApiDescriptor.getRestEndpoints()) {
				generateRestEndpointMethodDeclaration(restApi);
			}
		}
		
		if (exchangeApiDescriptor.hasWebsocketEndpoints()) {
			constructorBody.append("this.")
						   .append(WEBSOCKET_MANAGER_VARIABLE_NAME)
						   .append(" = createWebsocketManager(")
						   .append(JavaCodeGenerationUtil.getQuotedString(exchangeApiDescriptor.getWebsocketUrl()))
						   .append(", ")
						   .append(JavaCodeGenerationUtil.getQuotedString(exchangeApiDescriptor.getWebsocketFactory()))
						   .append(", ")
						   .append(JavaCodeGenerationUtil.getQuotedString(exchangeApiDescriptor.getWebsocketHookFactory()))
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
		if (finalMembersDeclarations.length() > 0) {
			String finalMembersDeclarationStr = finalMembersDeclarations.toString();
			appendToBody(finalMembersDeclarationStr);
			if (!finalMembersDeclarationStr.endsWith("\n")) {
				appendToBody("\n\n");
			} else {
				appendToBody("\n");
			}
		}
		addImport(Properties.class);
		StringBuilder constructorSignature = new StringBuilder()
							.append("public ")
							.append(simpleImplementationName)
							.append("(String ")
							.append(EXCHANGE_NAME_ARGUMENT_NAME)
							.append(", ")
							.append("Properties properties");
		if (hasExchangeLimits && exchangeApiDescriptor.getRestEndpoints() != null && !exchangeApiDescriptor.getRestEndpoints().isEmpty()) {
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
		staticMembersDeclaration.append("private static final ").append( staticMemberDeclaration);//.append("\n");
	}

	private void generateWebsocketApiMethodsDeclarations(WebsocketEndpointDescriptor websocketApi) {
		Field request = ExchangeApiGeneratorUtil.resolveFieldProperties(exchangeApiDescriptor, websocketApi.getRequest());
		Type requestDataType = ExchangeApiGeneratorUtil.getFieldType(request);
		Field message = ExchangeApiGeneratorUtil.resolveFieldProperties(exchangeApiDescriptor, websocketApi.getMessage());
		Type messageDataType = ExchangeApiGeneratorUtil.getFieldType(message);
		addImport(WebsocketEndpoint.class);
		boolean hasArguments = ExchangeApiGeneratorUtil.websocketEndpointHasArguments(websocketApi, exchangeApiDescriptor);
		String requestSimpleClassName = Object.class.getSimpleName();
		String requestArgName = ExchangeApiGeneratorUtil.DEFAULT_REQUEST_ARG_NAME;
		if (hasArguments) {
			String requestClassName = null;
			if (requestDataType != null && requestDataType.isObject()) {
				requestClassName = ExchangeApiGeneratorUtil.generateWebsocketEndpointRequestPojoClassName(
										exchangeDescriptor, 
										exchangeApiDescriptor, 
										websocketApi);
			}
			requestSimpleClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
													requestDataType, 
													getImports(), 
													requestClassName);
		}
		
		String messageClassObjectName = ExchangeApiGeneratorUtil.generateWebsocketEndpointMessagePojoClassName(
											exchangeDescriptor, 
											exchangeApiDescriptor, 
											websocketApi);
		String messageClassSimpleName = ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
											messageDataType, 
											getImports(), 
											messageClassObjectName);
		String subscribeMethodName = "subscribe" + JavaCodeGenerationUtil.firstLetterToUpperCase(websocketApi.getName());
		String unsubscribeMethodName = "unsubscribe" + JavaCodeGenerationUtil.firstLetterToUpperCase(websocketApi.getName());
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
		addImport(DefaultWebsocketMessageTopicMatcher.class);
		
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
				.append(", topic, ")
				.append(DefaultWebsocketMessageTopicMatcher.class.getSimpleName())
				.append(".create(");
		
		List<String> replacements = new ArrayList<>();
		replacements.add("topic");
		replacements.add("\" + topic + \"");
		if (request != null) {
			replacements.add(ExchangeApiGeneratorUtil.getRequestArgName(request.getName()));
			replacements.add("\" + " + requestArgName + " + \"");
		}
		List<Field> endpointParameters = Optional.ofNullable(request == null? null: request.getParameters()).orElse(List.of());
		endpointParameters.forEach(param -> {
			replacements.add(param.getMsgField() != null? param.getMsgField(): param.getName());
			String parameterClass = ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(param.getType(), getImports(), Optional.ofNullable(param.getObjectName()).orElse(messageClassObjectName));
			if (!parameterClass.startsWith("java.lang") && parameterClass.contains(".")) {
				parameterClass = JavaCodeGenerationUtil.getClassNameWithoutPackage(parameterClass);
			}
			replacements.add("\" + request." 
								 + JavaCodeGenerationUtil.getGetAccessorMethodName(
										 		param.getName(), 
										 		parameterClass, 
										 		endpointParameters
										 		       .stream()
										 		       .map(f1 -> f1.getName())
										 		.collect(Collectors.toList())) 
								 + "() + \"");
		});
		
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
			subscribeMethodBody.append(topicMatcherFieldDeclaration.toString());	
			if (i < websocketApi.getMessageTopicMatcherFields().size() - 1) {
				subscribeMethodBody.append(", ");
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
		Field request = ExchangeApiGeneratorUtil.resolveFieldProperties(exchangeApiDescriptor, websocketApi.getRequest());
		Type requestDataType = ExchangeApiGeneratorUtil.getFieldType(request); 
		String topicSerializerBody = "\"\"";
		if (ExchangeApiGeneratorUtil.websocketEndpointHasArguments(websocketApi, exchangeApiDescriptor)) {
			if (websocketApi.getTopic() != null) {
				topicSerializerBody =	generateUrlParametersOrTopicSerializerBodyFromTemplate(
												websocketApi.getTopic(), 
												request.getParameters(), 
												websocketApi.getTopicParametersListSeparator(),
												websocketApi.getRequest().getName());
			} else if (requestDataType.getCanonicalType().isPrimitive) {
				topicSerializerBody = "request == null? \"\": \"/\" + request";
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
			if (requestDataType != null && requestDataType.isObject()) {
				requestClassName = ExchangeApiGeneratorUtil.generateRestEnpointRequestPojoClassName(exchangeDescriptor, exchangeApiDescriptor, restApi);
			}
			requestSimpleClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
					requestDataType, 
					getImports(), 
					requestClassName);
		}
		boolean hasResponse = ExchangeApiGeneratorUtil.restEndpointHasResponse(restApi, exchangeApiDescriptor);
		String responseSimpleClassName = "String";
		String getResponseDeserializerInstance = null;
		if (hasResponse) {
			String restResponseClassName = null;
			if (responseDataType != null && responseDataType.isObject()) {
				restResponseClassName = ExchangeApiGeneratorUtil.generateRestEnpointResponsePojoClassName(
						exchangeDescriptor, 
						exchangeApiDescriptor, 
						restApi);
			}
			
			responseSimpleClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
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
		
		String apiMethodName = JavaCodeGenerationUtil.firstLetterToLowerCase(restApi.getName());
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
		if  (exchangeDescriptor.getRateLimits() != null) {
			String exchangeClassName = ExchangeInterfaceImplementationGenerator.getExchangeInterfaceName(exchangeDescriptor);
			addImport(exchangeClassName);
			String exchangeSimpleClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(exchangeClassName);
			exchangeDescriptor.getRateLimits().forEach(
					rateLimit -> rateLimitVariables.add(exchangeSimpleClassName + "." + ExchangeJavaWrapperGeneratorUtil.generateRateLimitVariableName(rateLimit.getId()))); 
		}
		if (apiGlobalRateLimitVariables != null) {
			rateLimitVariables.addAll(apiGlobalRateLimitVariables);
		}
		if (restApi.getRateLimits() != null) {
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
		StringBuilder createHttpRequestInstruction = new StringBuilder()
				.append("HttpRequest.create(")
				.append(ExchangeApiGeneratorUtil.getRestEndpointNameStaticVariable(restApi.getName()))
				.append(", ")
				.append(JavaCodeGenerationUtil.getQuotedString(restApi.getUrl()));
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
		if (rateLimits != null && rateLimits.size() > 0) {
			return true;
		}
		if (exchangeApiDescriptor.getRestEndpoints() != null) {
			for (RestEndpointDescriptor restEndpoint : exchangeApiDescriptor.getRestEndpoints()) {
				rateLimits = restEndpoint.getRateLimits();
				if (rateLimits != null && rateLimits.size() > 0) {
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
		if (name == null) {
			throw new IllegalArgumentException("rateLimitRule:" + rateLimitRule + " should have an id in API " + exchangeApiDescriptor.getName());
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
														exchangeApiDescriptor, request).getParameters();
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
			} else if (enpointParameters != null && enpointParameters.size() > 0 && hasQueryParams) {
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
	
	private String generateGetUrlParametersBodyUsingQueryParams(List<Field> endpointParameters, boolean singleRequestParam) {
		addImport(EncodingUtil.class);
		StringBuilder s = new StringBuilder().append(EncodingUtil.class.getSimpleName() + ".createUrlQueryParameters(");
		for (int i = 0; i < endpointParameters.size(); i++) {
			if (i > 0) {
				s.append(", ");
			}
			Field param = endpointParameters.get(i);
			String name = param.getName();
			s.append("\"")
			 .append(name)
			 .append("\", ");
			String value = "request";
			if (!singleRequestParam) {
				value += "." 
						+ JavaCodeGenerationUtil.getGetAccessorMethodName(
							name, 
							ExchangeApiGeneratorUtil.getClassNameForField(param, null, param.getObjectName()), 
							endpointParameters.stream().map(p -> p.getName()).collect(Collectors.toList()))
						+ "()";
			}
			if (param.getType().getCanonicalType() == CanonicalType.LIST
				|| param.getType().getCanonicalType() == CanonicalType.MAP
				|| param.getType().isObject()) {
				addImport(JsonUtil.class);
				value = new StringBuilder()
								.append("JsonUtil.pojoToJsonString(")
								.append(value)
								.append(")").toString(); 
			} else if (param.getType().getCanonicalType() == CanonicalType.STRING) {
				value = new StringBuilder()
						.append(value)
						.toString(); 
			}
			s.append(value);
			
		}
		return s.append(")").toString();
	}

	private String generateUrlParametersOrTopicSerializerBodyFromTemplate(String urlParametersTemplate, 
																  List<Field> endpointParameters, 
																  String stringListSeparator,
																  String requestArgName) {
		endpointParameters = Optional.ofNullable(endpointParameters).orElse(List.of());
		if (stringListSeparator == null) {
			stringListSeparator = ExchangeJavaWrapperGeneratorUtil.DEFAULT_STRING_LIST_SEPARATOR;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(EncodingUtil.class.getSimpleName())
		  .append(".substituteArguments(\"")
		  .append(urlParametersTemplate)
		  .append("\"");
		int n = endpointParameters.size();
		for (int i = 0; i < n; i++) {
			Field param = endpointParameters.get(i);
			String name = param.getName();
			if (!urlParametersTemplate.contains("${" + name + "}")) {
				continue;
			}
			String value = "request." 
						   + JavaCodeGenerationUtil.getGetAccessorMethodName(
								name, 
								ExchangeApiGeneratorUtil.getClassNameForField(param, null, param.getObjectName()), 
								endpointParameters.stream().map(p -> p.getName()).collect(Collectors.toList()))
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
}
