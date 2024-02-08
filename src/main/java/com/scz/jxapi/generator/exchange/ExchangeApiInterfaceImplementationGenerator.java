package com.scz.jxapi.generator.exchange;

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

import com.scz.jxapi.generator.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.JavaTypeGenerator;
import com.scz.jxapi.netutils.deserialization.RawBigDecimalMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.RawBooleanMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.RawIntegerMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.RawLongMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.RawStringMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.BigDecimalJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.BooleanJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.IntegerJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.LongJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.MapJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.StringJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.TimestampJsonFieldDeserializer;
import com.scz.jxapi.netutils.rest.FutureRestResponse;
import com.scz.jxapi.netutils.rest.RestEndpoint;
import com.scz.jxapi.netutils.rest.RestRequest;
import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;
import com.scz.jxapi.netutils.rest.ratelimits.RequestThrottler;
import com.scz.jxapi.netutils.websocket.DefaultWebsocketMessageTopicMatcher;
import com.scz.jxapi.netutils.websocket.WebsocketEndpoint;
import com.scz.jxapi.netutils.websocket.WebsocketListener;
import com.scz.jxapi.netutils.websocket.WebsocketMessageTopicMatcherField;
import com.scz.jxapi.netutils.websocket.WebsocketSubscribeRequest;
import com.scz.jxapi.util.EncodingUtil;

public class ExchangeApiInterfaceImplementationGenerator extends JavaTypeGenerator {
	
	private static final String REQUEST_THROTTLER_VARIABLE_NAME = "requestThrottler";
	private static final String REST_API_FACTORY_VARIABLE_NAME = "restEndpointFactory";
	private final String WEBSOCKET_ENDPOINT_FACTORY_VARIABLE_NAME = "websocketEndpointFactory";
	
	private final ExchangeDescriptor exchangeDescriptor;
	
	private final ExchangeApiDescriptor exchangeApiDescriptor;
	
	private StringBuilder staticMembersDeclaration;
	
	private StringBuilder finalMembersDeclarations;
	
	private StringBuilder constructorBody;
	
	private List<String> methodSignatures;
	private Map<String, String> methods;
	private final String websocketEndpointFactoryFullClassName;
	private final boolean hasRateLimits;
	private List<String> apiGlobalRateLimitVariables;
	private Set<String> endpointSpecificRateLimitIds;
	
	
	public ExchangeApiInterfaceImplementationGenerator(ExchangeDescriptor exchangeDescriptor, ExchangeApiDescriptor exchangeApiDescriptor) {
		super(ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor) + "Impl");
		this.exchangeDescriptor = exchangeDescriptor;
		this.exchangeApiDescriptor = exchangeApiDescriptor;
		setTypeDeclaration("public class");
		String fullInterfaceName = ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor);
		String simpleInterfaceName = JavaCodeGenerationUtil.getClassNameWithoutPackage(fullInterfaceName);
		setImplementedInterfaces(Arrays.asList(fullInterfaceName));
		setDescription("Actual implementation of {@link " + simpleInterfaceName + "}<br/>\n"
				   + JavaCodeGenerationUtil.GENERATED_CODE_WARNING);
		websocketEndpointFactoryFullClassName = exchangeApiDescriptor.getWebsocketEndpointFactory();
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
		
		finalMembersDeclarations.append("\nprivate final Properties properties;");
		constructorBody.append("this.properties = properties;\n");
		
		if ((hasRateLimits || hasExchangeLimits) 
				&& (exchangeApiDescriptor.getRestEndpoints() != null && !exchangeApiDescriptor.getRestEndpoints().isEmpty())) {
			addImport(RequestThrottler.class);
			StringBuilder requestThrottlerDeclaration = new StringBuilder().append("\nprivate final ")
					.append(RequestThrottler.class.getSimpleName())
					.append(" ")
					.append(REQUEST_THROTTLER_VARIABLE_NAME);
			if (hasExchangeLimits) {
				requestThrottlerDeclaration.append(";");
				constructorBody
					.append("this.")
					.append(REQUEST_THROTTLER_VARIABLE_NAME)
					.append(" = ")
					.append(REQUEST_THROTTLER_VARIABLE_NAME).append(";\n");
				exchangeDescriptor.getRateLimits().forEach(r -> exchangeRateLimitsVariables.add(ExchangeJavaWrapperGeneratorUtil.generateRateLimitVariableName(r.getId())));
				
			} else {
				requestThrottlerDeclaration
					.append(" = new ")
					.append(RequestThrottler.class.getSimpleName())
					.append("(\"")
					.append(exchangeDescriptor.getName())
					.append(exchangeApiDescriptor.getName())
					.append("\");");
			}
			finalMembersDeclarations.append(requestThrottlerDeclaration.toString());
			
			List<RateLimitRule> apiGlobalLimits = exchangeApiDescriptor.getRateLimits();
			if (apiGlobalLimits != null && !apiGlobalLimits.isEmpty()) {
				int rateLimitCounter = 0;
				for (RateLimitRule apiGlobalLimit: apiGlobalLimits) {
					apiGlobalRateLimitVariables.add(generateRateLimitVariable(apiGlobalLimit, exchangeApiDescriptor.getName() + rateLimitCounter++));
				}
			}
		}

		String restApiFactoryFullClassName = exchangeApiDescriptor.getRestEndpointFactory();
		if (restApiFactoryFullClassName != null) {
			addImport(restApiFactoryFullClassName);
			String restApiFactorySimpleClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(restApiFactoryFullClassName);	
			finalMembersDeclarations.append("\nprivate final "
					  							 + restApiFactorySimpleClassName
					  							 + " "
					  							 + REST_API_FACTORY_VARIABLE_NAME
					  							 + " = new "
					  							 + restApiFactorySimpleClassName + "();\n");
		}
		
		String websocketEndpointFactoryFullClassName = exchangeApiDescriptor.getWebsocketEndpointFactory();
		
		if (websocketEndpointFactoryFullClassName != null) {
			addImport(websocketEndpointFactoryFullClassName);
			String websocketEndpointFactorySimpleClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(websocketEndpointFactoryFullClassName);
			finalMembersDeclarations.append("\nprivate final "
						 + websocketEndpointFactorySimpleClassName
						 + " "
						 + WEBSOCKET_ENDPOINT_FACTORY_VARIABLE_NAME
						 + " = new "
						 + websocketEndpointFactorySimpleClassName + "();\n");
		}
		
		if (restApiFactoryFullClassName != null) {
			constructorBody.append("this." + REST_API_FACTORY_VARIABLE_NAME + ".setProperties(properties);\n");
		}
		if (websocketEndpointFactoryFullClassName != null) {
			constructorBody.append("this." + WEBSOCKET_ENDPOINT_FACTORY_VARIABLE_NAME + ".setApi(this);\n");
		}
		
		if (exchangeApiDescriptor.getRestEndpoints() != null) {
			for (RestEndpointDescriptor restApi: exchangeApiDescriptor.getRestEndpoints()) {
				generateRestEndpointMethodDeclaration(restApi);
			}
		}
		
		if (exchangeApiDescriptor.getWebsocketEndpoints() != null) {
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
							.append("(Properties properties");
		if (hasExchangeLimits && exchangeApiDescriptor.getRestEndpoints() != null && !exchangeApiDescriptor.getRestEndpoints().isEmpty()) {
			addImport(RequestThrottler.class);
			constructorSignature.append(", ")
								.append(RequestThrottler.class.getSimpleName())
								.append(" ")
								.append(REQUEST_THROTTLER_VARIABLE_NAME);
		}
		constructorSignature.append(")");
		appendMethod(constructorSignature.toString(), constructorBody.toString());
		appendToBody("\n");
		appendMethod("@Override\npublic Properties getProperties()", "return this.properties;");
		appendToBody("\n");
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
		if (websocketEndpointFactoryFullClassName == null) {
			throw new IllegalStateException("No 'websocketEndpointFactory' defined on " + exchangeApiDescriptor.getName());
		}
		addImport(WebsocketEndpoint.class);
		String requestClassName = ExchangeJavaWrapperGeneratorUtil.generateWebsocketEndpointRequestClassName(exchangeDescriptor, exchangeApiDescriptor, websocketApi);
		String requestSimpleClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
				EndpointParameterType.fromTypeName(websocketApi.getRequestDataType()), 
				getImports(), 
				requestClassName);
		
		String messageClassObjectName = ExchangeJavaWrapperGeneratorUtil.generateWebsocketEndpointMessageClassName(
											exchangeDescriptor, 
											exchangeApiDescriptor, 
											websocketApi);
		String messageClassSimpleName = ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
											EndpointParameterType.fromTypeName(websocketApi.getMessageDataType()), 
											getImports(), 
											messageClassObjectName);
		String subscribeMethodName = "subscribe" + JavaCodeGenerationUtil.firstLetterToUpperCase(websocketApi.getName());
		String unsubscribeMethodName = "unsubscribe" + JavaCodeGenerationUtil.firstLetterToUpperCase(websocketApi.getName());
		String websocketEndpointVariableName = JavaCodeGenerationUtil.firstLetterToLowerCase(websocketApi.getName()) + "Ws";
		
		String getResponseDeserializerInstance = getNewMessageDeserializerInstruction(EndpointParameterType.fromTypeName(websocketApi.getMessageDataType()), messageClassObjectName);
		
		finalMembersDeclarations.append("private final WebsocketEndpoint<" 
											+ requestSimpleClassName 
											+ ", " + messageClassSimpleName + "> " 
											+ websocketEndpointVariableName + ";\n");
		constructorBody.append("this." + websocketEndpointVariableName + " = "  
											 		 + WEBSOCKET_ENDPOINT_FACTORY_VARIABLE_NAME + ".createWebsocketEndpoint(" 
											 		 + getResponseDeserializerInstance 
											 		 + ");\n");
		addImport(WebsocketListener.class);
		String subscribeMethodSignature = "String " 
										  + subscribeMethodName 
										  + "(" + requestSimpleClassName 
										  + " request, WebsocketListener<" 
										  + messageClassSimpleName  
										  + "> listener)";
		
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
				.append(":request:\" + request);\n")
				.append(WebsocketSubscribeRequest.class.getSimpleName())
				.append("<")
				.append(requestSimpleClassName)
				.append(">")
				.append(" websocketSubscribeRequest = ")
				.append(WebsocketSubscribeRequest.class.getSimpleName())
				.append(".create(request, topic, ")
				.append(DefaultWebsocketMessageTopicMatcher.class.getSimpleName())
				.append(".create(");
		
		List<String> replacements = new ArrayList<>();
		replacements.add("topic");
		// replacements.add("\"+ topic + \"");
		replacements.add("topic");
		getEndpointParameters(websocketApi.getParameters(), websocketApi.getRequestObjectName()).forEach(param -> {
			replacements.add(param.getMsgField() != null? param.getMsgField(): param.getName());
			String parameterClass = ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(param.getEndpointParameterType(), getImports(), Optional.ofNullable(param.getObjectName()).orElse(messageClassObjectName));
			if (!parameterClass.startsWith("java.lang") && parameterClass.contains(".")) {
				parameterClass = JavaCodeGenerationUtil.getClassNameWithoutPackage(parameterClass);
			}
			replacements.add("\"+\" + request." 
								 + JavaCodeGenerationUtil.getGetAccessorMethodName(
										 		param.getName(), 
										 		parameterClass, 
										 		getEndpointParameters(websocketApi.getResponse(), 
										 							  websocketApi.getMessageObjectName()).stream()
										 								.map(f1 -> f1.getName())
										 								.collect(Collectors.toList())) 
								 + "() + \"");
		});
		
		for (int i = 0; i < websocketApi.getMessageTopicMatcherFields().size(); i++) {
			WebsocketMessageTopicMatcherFieldDescriptor topicMatcherField = websocketApi.getMessageTopicMatcherFields().get(i);
			subscribeMethodBody
				.append("\"")
				.append(topicMatcherField.getName())
				.append("\", ")
				.append(EncodingUtil.substituteArguments(topicMatcherField.getValue(), (Object[]) replacements.toArray(new String[replacements.size()])));
				
			if (i < websocketApi.getMessageTopicMatcherFields().size() - 1) {
				subscribeMethodBody.append(", ");
			}
		}
		subscribeMethodBody.append("));\n")
			.append("websocketSubscribeRequest.setParameters(request);\n")
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
		EndpointParameterType requestDataType = EndpointParameterType.fromTypeName(websocketApi.getRequestDataType()); 
		String topicSerializerBody = "\"\"";
		if (websocketApi.getTopic() != null) {
			topicSerializerBody =	generateUrlParametersOrTopicSerializerBodyFromTemplate(
											websocketApi.getTopic(), 
											getEndpointParameters(websocketApi.getParameters(), websocketApi.getRequestObjectName()), 
											websocketApi.getTopicParametersListSeparator());
		} else if (requestDataType.getCanonicalType().isPrimitive) {
			topicSerializerBody = "request == null? \"\": \"/\" + request";
		}
		
		return topicSerializerBody + ";\n";
	}

	private List<EndpointParameter> getEndpointParameters(List<EndpointParameter> parameters, String requestObjectName) {
		if (parameters != null) {
			return parameters;
		} else if (requestObjectName != null) {
			return findParametersForObjectName(requestObjectName);
		} else {
			return List.of();
		}
	}
	
	private List<EndpointParameter> findParametersForObjectName(String requestObjectName) {
		for (RestEndpointDescriptor restEndpointDescriptor: this.exchangeApiDescriptor.getRestEndpoints()) {
			if (requestObjectName.equals(restEndpointDescriptor.getRequestObjectName())) {
				if (restEndpointDescriptor.getParameters() != null) {
					return restEndpointDescriptor.getParameters();
				}
			}
			if (requestObjectName.equals(restEndpointDescriptor.getResponseObjectName())) {
				if (restEndpointDescriptor.getResponse() != null) {
					return restEndpointDescriptor.getResponse();
				}
			}
		}
		
		for (WebsocketEndpointDescriptor restEndpointDescriptor: this.exchangeApiDescriptor.getWebsocketEndpoints()) {
			if (requestObjectName.equals(restEndpointDescriptor.getRequestObjectName())) {
				if (restEndpointDescriptor.getParameters() != null) {
					return restEndpointDescriptor.getParameters();
				}
			}
			if (requestObjectName.equals(restEndpointDescriptor.getMessageObjectName())) {
				if (restEndpointDescriptor.getResponse() != null) {
					return restEndpointDescriptor.getResponse();
				}
			}
		}
		throw new IllegalArgumentException("Found no REST request or response or Websocket request or message with fields defined for objectName:"  + requestObjectName);
	}

	private String getNewMessageDeserializerInstruction(EndpointParameterType messageType, String messageFullClassName) {
		switch (messageType.getCanonicalType()) {
		case BIGDECIMAL:
			addImport(RawBigDecimalMessageDeserializer.class);
			return RawBigDecimalMessageDeserializer.class.getSimpleName() + ".getInstance()";
		case BOOLEAN:
			addImport(RawBooleanMessageDeserializer.class);
			return RawBooleanMessageDeserializer.class.getSimpleName() + ".getInstance()";
		case INT:
			addImport(RawIntegerMessageDeserializer.class);
			return RawIntegerMessageDeserializer.class.getSimpleName() + ".getInstance()";
		case TIMESTAMP:
		case LONG:
			addImport(RawLongMessageDeserializer.class);
			return RawLongMessageDeserializer.class.getSimpleName() + ".getInstance()";
		case STRING:
			addImport(RawStringMessageDeserializer.class);
			return RawStringMessageDeserializer.class.getSimpleName() + ".getInstance()";
		case OBJECT:
		case LIST:
		case MAP:
		default:
			return ExchangeJavaWrapperGeneratorUtil.getNewJsonParameterDeserializerInstruction(messageType, messageFullClassName, getImports());
		}
	}
	
	public static String getNewNonPrimitiveParameterJsonDeserializerInstruction(EndpointParameterType type, String objectClassName, Set<String> imports) {
		switch (type.getCanonicalType()) {
		case BIGDECIMAL:
			imports.add(BigDecimalJsonFieldDeserializer.class.getName());
			return "BigDecimalJsonFieldDeserializer.getInstance()";
		case BOOLEAN:
			imports.add(BooleanJsonFieldDeserializer.class.getName());
			return  BooleanJsonFieldDeserializer.class.getSimpleName() + ".getInstance()";
		case INT:
			imports.add(IntegerJsonFieldDeserializer.class.getName());
			return  IntegerJsonFieldDeserializer.class.getSimpleName() + ".getInstance()";
		case LONG:
			imports.add(LongJsonFieldDeserializer.class.getName());
			return  LongJsonFieldDeserializer.class.getSimpleName() + ".getInstance()";
		case STRING:
			imports.add(StringJsonFieldDeserializer.class.getName());
			return  StringJsonFieldDeserializer.class.getSimpleName() + ".getInstance()";
		case TIMESTAMP:
			imports.add(TimestampJsonFieldDeserializer.class.getName());
			return  TimestampJsonFieldDeserializer.class.getSimpleName() + ".getInstance()";
		case LIST:
			imports.add(ListJsonFieldDeserializer.class.getName());
			return "new " + ListJsonFieldDeserializer.class.getSimpleName() + "<>(" + getNewNonPrimitiveParameterJsonDeserializerInstruction(type.getSubType(), objectClassName, imports) + ")";
		case MAP:
			imports.add(MapJsonFieldDeserializer.class.getName());
			return "new " + MapJsonFieldDeserializer.class.getSimpleName() + "<>(" + getNewNonPrimitiveParameterJsonDeserializerInstruction(type.getSubType(), objectClassName, imports) +")";
		case OBJECT:
			String objectDeserializerClass = ExchangeJavaWrapperGeneratorUtil.getJsonMessageDeserializerClassName(objectClassName);
			imports.add(objectDeserializerClass);
			return "new " +  JavaCodeGenerationUtil.getClassNameWithoutPackage(objectDeserializerClass) + "()";
		default:
			throw new IllegalArgumentException("Unexpected field type:" + type);
		}
	}

	private void generateRestEndpointMethodDeclaration(RestEndpointDescriptor restApi) {
		addImport(RestEndpoint.class);
		EndpointParameterType requestDataType = EndpointParameterType.fromTypeName(restApi.getRequestDataType());
		String requestClassName = ExchangeJavaWrapperGeneratorUtil.generateRestEnpointRequestClassName(exchangeDescriptor, exchangeApiDescriptor, restApi);
		String requestSimpleClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
				requestDataType, 
				getImports(), 
				requestClassName);
//		addImport(requestClassName);
		String restResponseClassName = ExchangeJavaWrapperGeneratorUtil.generateRestEnpointResponseClassName(
				exchangeDescriptor, 
				exchangeApiDescriptor, 
				restApi);
		String responseSimpleClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
				EndpointParameterType.fromTypeName(restApi.getResponseDataType()), 
				getImports(), 
				restResponseClassName);
		String getResponseDeserializerInstance = getNewMessageDeserializerInstruction(EndpointParameterType.fromTypeName(restApi.getResponseDataType()), restResponseClassName);
		
		String apiMethodName = JavaCodeGenerationUtil.firstLetterToLowerCase(restApi.getName());
		String restEndpointVariableName = apiMethodName + "Api";
		
		finalMembersDeclarations.append("private final RestEndpoint<" + requestSimpleClassName + ", " + responseSimpleClassName + "> " + restEndpointVariableName + ";\n");
		constructorBody.append("this." + restEndpointVariableName + " = "  + REST_API_FACTORY_VARIABLE_NAME + ".createRestEndpoint(" + getResponseDeserializerInstance + ");\n");
		
		String apiMethodSignature = FutureRestResponse.class.getSimpleName() + "<" + responseSimpleClassName + "> " + apiMethodName + "(" + requestSimpleClassName + " request)"; 
		
		addImport(RestRequest.class);
		addImport(FutureRestResponse.class);
		StringBuilder apiMethodBody = new StringBuilder()
				.append("String urlParameters = ")
				.append(generateUrlParametersSerializerDeclaration(restApi))
				.append("if (log.isDebugEnabled())\n")
				.append(JavaCodeGenerationUtil.INDENTATION)
				.append("log.debug(\"")
				.append(restApi.getHttpMethod().toUpperCase())
				.append(" ")
				.append(restApi.getName())
				.append(" > \" + request);\n");
		
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
			rateLimitsVariable = "RATE_LIMITS_" + JavaCodeGenerationUtil.getStaticVariableName(restEndpointVariableName);
			generateRateLimitListVariable(rateLimitsVariable, rateLimitVariables);
		}
		
		
		String createRestRequestInstruction = new StringBuilder()
				.append("RestRequest.create(\"")
				.append(restApi.getUrl())
				.append("\", \"")
				.append(restApi.getHttpMethod().toUpperCase())
				.append("\", request, ")
				.append(rateLimitsVariable)
				.append(", ")
				.append(requestWeight)
				.append(", urlParameters)")
				.toString();
				
		StringBuilder restEndpointCallInstruction = new StringBuilder();
		if (rateLimitVariables.isEmpty()) {
			restEndpointCallInstruction.append(restEndpointVariableName)
			.append(".call(")
			.append(createRestRequestInstruction)
			.append(")");
		}
		else {
			restEndpointCallInstruction
				 .append(REQUEST_THROTTLER_VARIABLE_NAME)
				 .append(".submit(")
				 .append(createRestRequestInstruction)
				 .append(", ")
				 .append(restEndpointVariableName)
				 .append(")");
		}
		apiMethodBody.append(restEndpointCallInstruction.toString()).append(";\n");
		
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
		EndpointParameterType requestDataType = EndpointParameterType.fromTypeName(restApi.getRequestDataType());
		String requestClassName = ExchangeJavaWrapperGeneratorUtil.generateRestEnpointRequestClassName(exchangeDescriptor, exchangeApiDescriptor, restApi);
		addImport(requestClassName);
		String getUrlParametersBody = "\"\"";
		List<EndpointParameter> parameters = restApi.getParameters();
		if (restApi.getUrlParameters() != null) {
			getUrlParametersBody =	generateUrlParametersOrTopicSerializerBodyFromTemplate(
											restApi.getUrlParameters(), 
											getEndpointParameters(restApi.getParameters(), restApi.getRequestObjectName()), 
											restApi.getUrlParametersListSeparator());
		} else if (parameters != null && parameters.size() > 0
					&& (restApi.isQueryParams()	|| "GET".equalsIgnoreCase(restApi.getHttpMethod()))) {
			getUrlParametersBody = generateGetUrlParametersBodyUsingQueryParams(restApi.getParameters());
		} else if (requestDataType.getCanonicalType().isPrimitive) {
			getUrlParametersBody = "request == null? \"\": \"/\" + request";
		}
		
		return getUrlParametersBody + ";\n";
	}
	
	private String generateGetUrlParametersBodyUsingQueryParams(List<EndpointParameter> endpointParameters) {
		addImport(EncodingUtil.class);
		StringBuilder s = new StringBuilder().append(EncodingUtil.class.getSimpleName() + ".createUrlQueryParameters(");
		for (int i = 0; i < endpointParameters.size(); i++) {
			if (i > 0) {
				s.append(",");
			}
			EndpointParameter param = endpointParameters.get(i);
			String name = param.getName();
			s.append("\"")
			 .append(name)
			 .append("\", ");

			String value = "request." 
					   + JavaCodeGenerationUtil.getGetAccessorMethodName(
							name, 
							ExchangeJavaWrapperGeneratorUtil.getClassNameForEndpointParameter(param, null, param.getObjectName()), 
							endpointParameters.stream().map(p -> p.getName()).collect(Collectors.toList()))
					   + "()";
			if (param.getEndpointParameterType().getCanonicalType() == CanonicalEndpointParameterTypes.LIST) {
				value = EncodingUtil.class.getSimpleName() + ".listToUrlParamString(" + value + ")"; 
			}
			s.append(value);
		}
		return s.append(")").toString();
	}

	private String generateUrlParametersOrTopicSerializerBodyFromTemplate(String urlParametersTemplate, 
																  List<EndpointParameter> endpointParameters, 
																  String stringListSeparator) {
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
			EndpointParameter param = endpointParameters.get(i);
			String name = param.getName();
			if (!urlParametersTemplate.contains("${" + name + "}")) {
				continue;
			}
			String value = "request." 
						   + JavaCodeGenerationUtil.getGetAccessorMethodName(
								name, 
								ExchangeJavaWrapperGeneratorUtil.getClassNameForEndpointParameter(param, null, param.getObjectName()), 
								endpointParameters.stream().map(p -> p.getName()).collect(Collectors.toList()))
						   + "()";
			if (param.getEndpointParameterType().getCanonicalType() == CanonicalEndpointParameterTypes.LIST) {
				value = EncodingUtil.class.getSimpleName() + ".listToString(" + value + ", \"" + stringListSeparator + "\")"; 
			}
			sb.append(", \"").append(name).append("\", ").append(value);
			if (i < n - 1) {
				sb.append(", ");
			}
		}
		if (urlParametersTemplate.contains("${request}")) {
			if (n > 0) {
				sb.append(", ");
				n++;
			}
			sb.append("\"request\", request");
		}
		if (n <= 0) {
			return "\"" + urlParametersTemplate + "\"";
		} else {
			addImport(EncodingUtil.class);
		}
		
		sb.append(")");
		return sb.toString();
	}

}
