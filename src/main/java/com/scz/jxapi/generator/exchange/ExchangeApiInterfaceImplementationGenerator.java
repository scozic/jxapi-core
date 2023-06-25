package com.scz.jxapi.generator.exchange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.JavaTypeGenerator;
import com.scz.jxapi.generator.JsonMessageDeserializerGenerator;
import com.scz.jxapi.netutils.deserialization.RawStringMessageDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.ObjectListFieldDeserializer;
import com.scz.jxapi.netutils.rest.FutureRestResponse;
import com.scz.jxapi.netutils.rest.RestEndpoint;
import com.scz.jxapi.netutils.rest.RestRequest;
import com.scz.jxapi.netutils.websocket.DefaultWebsocketMessageTopicMatcher;
import com.scz.jxapi.netutils.websocket.WebsocketEndpoint;
import com.scz.jxapi.netutils.websocket.WebsocketListener;
import com.scz.jxapi.netutils.websocket.WebsocketMessageTopicMatcherField;
import com.scz.jxapi.netutils.websocket.WebsocketSubscribeRequest;
import com.scz.jxapi.util.EncodingUtil;

public class ExchangeApiInterfaceImplementationGenerator extends JavaTypeGenerator {
	
	private final ExchangeDescriptor exchangeDescriptor;
	
	private final ExchangeApiDescriptor exchangeApiDescriptor;
	
	private StringBuilder staticMembersDeclaration;
	
	private StringBuilder finalMembersDeclarations;
	
	private StringBuilder constructorBody;
	
	private List<String> methodSignatures;
	private Map<String, String> methods;
	private final String restApiFactoryVariableName = "restEndpointFactory";
	private final String websocketEndpointFactoryVariableName = "websocketEndpointFactory";
	private final String websocketEndpointFactoryFullClassName;
	
	
	public ExchangeApiInterfaceImplementationGenerator(ExchangeDescriptor exchangeDescriptor, ExchangeApiDescriptor exchangeApiDescriptor) {
		super(ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor) + "Impl");
		this.exchangeDescriptor = exchangeDescriptor;
		this.exchangeApiDescriptor = exchangeApiDescriptor;
		setTypeDeclaration("public class ");
		String fullInterfaceName = ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor);
		String simpleInterfaceName = JavaCodeGenerationUtil.getClassNameWithoutPackage(fullInterfaceName);
		setImplementedInterfaces(Arrays.asList(fullInterfaceName));
		setDescription("Actual implementation of {@link " + simpleInterfaceName + "}<br/>\n"
				   + JavaCodeGenerationUtil.GENERATED_CODE_WARNING);
		websocketEndpointFactoryFullClassName = exchangeApiDescriptor.getWebsocketEndpointFactory();
	}

	@Override
	public String generate() {
		staticMembersDeclaration = new StringBuilder();
		finalMembersDeclarations = new StringBuilder();
		constructorBody = new StringBuilder();
		methodSignatures = new ArrayList<>();
		methods = new HashMap<>();
		
		
		String fullInterfaceName = ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor);
		String simpleInterfaceName = JavaCodeGenerationUtil.getClassNameWithoutPackage(fullInterfaceName);
		String simpleImplementationName = simpleInterfaceName + "Impl";
		

		String restApiFactoryFullClassName = exchangeApiDescriptor.getRestEndpointFactory();
		addImport(restApiFactoryFullClassName);
		String restApiFactorySimpleClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(restApiFactoryFullClassName);
		
		finalMembersDeclarations.append("\nprivate final "
				  							 + restApiFactorySimpleClassName
				  							 + " "
				  							 + restApiFactoryVariableName
				  							 + " = new "
				  							 + restApiFactorySimpleClassName + "();\n");
		
		String websocketEndpointFactoryFullClassName = exchangeApiDescriptor.getWebsocketEndpointFactory();
		
		if (websocketEndpointFactoryFullClassName != null) {
			addImport(websocketEndpointFactoryFullClassName);
			String websocketEndpointFactorySimpleClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(websocketEndpointFactoryFullClassName);
			finalMembersDeclarations.append("\nprivate final "
						 + websocketEndpointFactorySimpleClassName
						 + " "
						 + websocketEndpointFactoryVariableName
						 + " = new "
						 + websocketEndpointFactorySimpleClassName + "();\n");
		}
		
		constructorBody.append("this." + restApiFactoryVariableName + ".setProperties(properties);\n");
		if (websocketEndpointFactoryFullClassName != null) {
			constructorBody.append("this." + websocketEndpointFactoryVariableName + ".setProperties(properties);\n");
		}
		
		for (RestEndpointDescriptor restApi: exchangeApiDescriptor.getRestEndpoints()) {
			generateRestEndpointMethodDeclaration(restApi);
		}
		
		for (WebsocketEndpointDescriptor websocketApi : exchangeApiDescriptor.getWebsocketEndpoints()) {
			generateWebsocketApiMethodsDeclarations(websocketApi);
		}
		
		JavaCodeGenerationUtil.generateLoggerDeclaration(this);
		
		appendToBody(staticMembersDeclaration.toString());
		appendToBody(finalMembersDeclarations.toString());
		appendToBody("\n");
		
		addImport(Properties.class);
		appendMethod("public " + simpleImplementationName + "(Properties properties)", constructorBody.toString());
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

	private void generateWebsocketApiMethodsDeclarations(WebsocketEndpointDescriptor websocketApi) {
		if (websocketEndpointFactoryFullClassName == null) {
			throw new IllegalStateException("No 'websocketEndpointFactory' defined on " + exchangeApiDescriptor.getName());
		}
		addImport(WebsocketEndpoint.class);
		String requestClassName = ExchangeJavaWrapperGeneratorUtil.generateWebsocketEndpointRequestClassName(exchangeDescriptor, exchangeApiDescriptor, websocketApi);
		String requestClassSimpleName = JavaCodeGenerationUtil.getClassNameWithoutPackage(requestClassName);
		addImport(requestClassName);
		String messageClassName = ExchangeJavaWrapperGeneratorUtil.generateWebsocketEndpointMessageClassName(exchangeDescriptor, exchangeApiDescriptor, websocketApi);
		String messageClassSimpleName = JavaCodeGenerationUtil.getClassNameWithoutPackage(messageClassName);
		String subscribeMethodName = "subscribe" + JavaCodeGenerationUtil.firstLetterToUpperCase(websocketApi.getName());
		String unsubscribeMethodName = "unsubscribe" + JavaCodeGenerationUtil.firstLetterToUpperCase(websocketApi.getName());
		String websocketEndpointVariableName = JavaCodeGenerationUtil.firstLetterToLowerCase(websocketApi.getName()) + "Ws";
		
		String messageDeserializerClassName = null;
		String getResponseDeserializerInstance = null;
		switch (websocketApi.getResponseDataType()) {
		case JSON_OBJECT:
			addImport(messageClassName);
			messageDeserializerClassName = JsonMessageDeserializerGenerator.getJsonMessageDeserializerClassName(messageClassName);
			getResponseDeserializerInstance = "new " + JavaCodeGenerationUtil.getClassNameWithoutPackage(messageDeserializerClassName) + "()";
			break;
		case JSON_OBJECT_LIST:
			addImport(messageClassName);
			messageDeserializerClassName = JsonMessageDeserializerGenerator.getJsonMessageDeserializerClassName(messageClassName);
			addImport(ObjectListFieldDeserializer.class);
			addImport(List.class);
			getResponseDeserializerInstance = "new ObjectListFieldDeserializer<" + messageClassSimpleName + ">(new " + JavaCodeGenerationUtil.getClassNameWithoutPackage(messageDeserializerClassName) + "())";
			messageClassSimpleName = "List<" + messageClassSimpleName + ">";
			break;
		case STRING:
			messageDeserializerClassName = RawStringMessageDeserializer.class.getName();
			getResponseDeserializerInstance = "RawStringMessageDeserializer.INSTANCE";
			break;
		default:
			throw new IllegalArgumentException("Unexpected responseDataType" + websocketApi.getResponseDataType() + " for:" + websocketApi);
		}
		addImport(messageDeserializerClassName);
		
		finalMembersDeclarations.append("\nprivate final WebsocketEndpoint<" + requestClassSimpleName + ", " + messageClassSimpleName + "> " + websocketEndpointVariableName + ";\n");
		constructorBody.append("this." + websocketEndpointVariableName + " = "  
											 		 + websocketEndpointFactoryVariableName + ".createWebsocketEndpoint(" 
											 		 + getResponseDeserializerInstance 
											 		 + ");\n");
		addImport(WebsocketListener.class);
		String subscribeMethodSignature = "String " 
										  + subscribeMethodName 
										  + "(" + requestClassSimpleName 
										  + " request, WebsocketListener<" 
										  + messageClassSimpleName  
										  + "> listener)";
		
		String unsubscribeMethodSignature = "boolean " + unsubscribeMethodName + "(String subscriptionId)";
		addImport(WebsocketSubscribeRequest.class);
		addImport(DefaultWebsocketMessageTopicMatcher.class);
		addImport(WebsocketMessageTopicMatcherField.class);
		
		StringBuilder subscribeMethodBody = new StringBuilder()
			.append("if (log.isDebugEnabled())\n")
			.append(JavaCodeGenerationUtil.INDENTATION)
			.append("log.debug(\"")
			.append(subscribeMethodName)
			.append(":request:\" + request);\n")
			.append(WebsocketSubscribeRequest.class.getSimpleName())
			.append("<")
			.append(requestClassSimpleName)
			.append(">")
			.append(" websocketSubscribeRequest = new ")
			.append(WebsocketSubscribeRequest.class.getSimpleName())
			.append("<>();\n")
			.append("websocketSubscribeRequest.setMessageTopicMatcher(new ")
			.append(DefaultWebsocketMessageTopicMatcher.class.getSimpleName())
			.append("(")
			.append(WebsocketMessageTopicMatcherField.class.getSimpleName())
			.append(".createList(");
		List<String> replacements = new ArrayList<>();
		replacements.add("topic");
		replacements.add("\" + request.getTopic() + \"");
		websocketApi.getParameters().forEach(param -> {
			replacements.add(param.getMsgField() != null? param.getMsgField(): param.getName());
			String parameterClass = ExchangeJavaWrapperGeneratorUtil.PARAMETER_TYPE_CLASSES.get(param.getType());
			if (!parameterClass.startsWith("java.lang") && parameterClass.contains(".")) {
				parameterClass = JavaCodeGenerationUtil.getClassNameWithoutPackage(parameterClass);
			}
			replacements.add("\" + request." 
								 + JavaCodeGenerationUtil.getGetAccessorMethodName(
										 		param.getName(), 
										 		parameterClass, 
										 		websocketApi.getResponse().stream()
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
				.append(EncodingUtil.substituteArguments("\"" + topicMatcherField.getValue() + "\"", (Object[]) replacements.toArray(new String[replacements.size()])));
				
			if (i < websocketApi.getMessageTopicMatcherFields().size() - 1) {
				subscribeMethodBody.append(", ");
			}
		}
		subscribeMethodBody.append(")));\n")
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

	private void generateRestEndpointMethodDeclaration(RestEndpointDescriptor restApi) {
		addImport(RestEndpoint.class);
		String requestClassName = ExchangeJavaWrapperGeneratorUtil.generateRestEnpointRequestClassName(exchangeDescriptor, exchangeApiDescriptor, restApi);
		String requestSimpleClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(requestClassName);
		addImport(requestClassName);
		String responseClassName = ExchangeJavaWrapperGeneratorUtil.generateRestEnpointResponseClassName(exchangeDescriptor, exchangeApiDescriptor, restApi);
		String responseSimpleClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(responseClassName);
		String responseDeserializerClassName = null;
		String getResponseDeserializerInstance = null;
		switch (restApi.getResponseDataType()) {
		case JSON_OBJECT:
			addImport(responseClassName);
			responseDeserializerClassName = JsonMessageDeserializerGenerator.getJsonMessageDeserializerClassName(responseClassName);
			getResponseDeserializerInstance = "new " + JavaCodeGenerationUtil.getClassNameWithoutPackage(responseDeserializerClassName) + "()";
			break;
		case JSON_OBJECT_LIST:
			addImport(responseClassName);
			responseDeserializerClassName = JsonMessageDeserializerGenerator.getJsonMessageDeserializerClassName(responseClassName);
			addImport(ObjectListFieldDeserializer.class);
			addImport(List.class);
			getResponseDeserializerInstance = "new ObjectListFieldDeserializer<" + responseSimpleClassName + ">(new " + JavaCodeGenerationUtil.getClassNameWithoutPackage(responseDeserializerClassName) + "())";
			responseSimpleClassName = "List<" + responseSimpleClassName + ">";
			break;
		case STRING:
			responseDeserializerClassName = RawStringMessageDeserializer.class.getName();
			getResponseDeserializerInstance = "RawStringMessageDeserializer.INSTANCE";
			break;
		default:
			throw new IllegalArgumentException("Unexpected responseDataType" + restApi.getResponseDataType() + " for:" + restApi);
		}
		addImport(responseDeserializerClassName);
		
		String apiMethodName = JavaCodeGenerationUtil.firstLetterToLowerCase(restApi.getName());
		String restEndpointVariableName = apiMethodName + "Api";
		
		finalMembersDeclarations.append("\nprivate final RestEndpoint<" + requestSimpleClassName + ", " + responseSimpleClassName + "> " + restEndpointVariableName + ";\n");
		constructorBody.append("this." + restEndpointVariableName + " = "  + restApiFactoryVariableName + ".createRestEndpoint(" + getResponseDeserializerInstance + ");\n");
		
		String apiMethodSignature = FutureRestResponse.class.getSimpleName() + "<" + responseSimpleClassName + "> " + apiMethodName + "(" + requestSimpleClassName + " request)"; 
		
		addImport(RestRequest.class);
		addImport(FutureRestResponse.class);
		StringBuilder apiMethodBody = new StringBuilder()
				.append("if (log.isDebugEnabled())\n")
				.append(JavaCodeGenerationUtil.INDENTATION)
				.append("log.debug(\"")
				.append(restApi.getHttpMethod().toUpperCase())
				.append(" ")
				.append(restApi.getName())
				.append(" > \" + request);\n")
				.append(" return ")
				.append(restEndpointVariableName)
				.append(".call(RestRequest.create(\"")
				.append(restApi.getUrl())
				.append("\", \"")
				.append(restApi.getHttpMethod().toUpperCase())
				.append("\", request));\n");
		addMethod("@Override\npublic " + apiMethodSignature, apiMethodBody.toString());
	}
}
