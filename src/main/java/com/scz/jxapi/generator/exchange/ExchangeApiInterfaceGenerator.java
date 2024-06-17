package com.scz.jxapi.generator.exchange;

import java.util.Optional;

import com.scz.jxapi.exchange.ExchangeApi;
import com.scz.jxapi.generator.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.JavaTypeGenerator;
import com.scz.jxapi.netutils.rest.FutureRestResponse;
import com.scz.jxapi.netutils.websocket.WebsocketListener;

public class ExchangeApiInterfaceGenerator extends JavaTypeGenerator {
	
	private static EndpointParameterType getEndpointParameterType(EndpointParameter parameter) {
		return parameter == null? null: Optional.ofNullable(parameter.getEndpointParameterType()).orElse(EndpointParameterType.OBJECT);
	}
	
	private final ExchangeDescriptor exchangeDescriptor;
	
	private final ExchangeApiDescriptor exchangeApiDescriptor;
	
	public ExchangeApiInterfaceGenerator(ExchangeDescriptor exchangeDescriptor, ExchangeApiDescriptor exchangeApiDescriptor) {
		super(ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor));
		this.exchangeDescriptor = exchangeDescriptor;
		this.exchangeApiDescriptor = exchangeApiDescriptor;
		setDescription(exchangeDescriptor.getName() + " " + exchangeApiDescriptor.getName() + " API</br>\n" 
				+ exchangeApiDescriptor.getDescription() + "\n" 
				+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING);
		setTypeDeclaration("public interface");
		this.setParentClassName(ExchangeApi.class.getName());
	}
	
	@Override
	public String generate() {
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
		
		return super.generate();
	}

	private void generateWebsocketApiMethodsDeclarations(WebsocketEndpointDescriptor websocketApi) {
		EndpointParameterType requestDataType = getEndpointParameterType(websocketApi.getRequest());
		boolean hasArguments = ExchangeJavaWrapperGeneratorUtil.websocketEndpointHasArguments(websocketApi, exchangeApiDescriptor);
		String requestSimpleClassName = Object.class.getSimpleName();
		String requestDescription = null;
		String requestArgName = null;
		if (hasArguments) {
			String requestClassName = ExchangeJavaWrapperGeneratorUtil.generateWebsocketEndpointRequestClassName(
													exchangeDescriptor, 
													exchangeApiDescriptor, 
													websocketApi);
			requestSimpleClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
													requestDataType, 
													getImports(), 
													requestClassName);
			requestDescription = websocketApi.getRequest().getDescription();
			requestArgName = ExchangeJavaWrapperGeneratorUtil.getRequestArgName(websocketApi.getRequest().getName());
		}
		
		EndpointParameterType messageDataType = getEndpointParameterType(websocketApi.getMessage());
		String messageClassSimpleName = ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
				messageDataType, 
				getImports(), 
				ExchangeJavaWrapperGeneratorUtil.generateWebsocketEndpointMessageClassName(
						exchangeDescriptor, 
						exchangeApiDescriptor, 
						websocketApi));
		String subscribeMethodName = ExchangeJavaWrapperGeneratorUtil.getWebsocketSubscribeMethodName(websocketApi);
		String unsubscribeMethodName = ExchangeJavaWrapperGeneratorUtil.getWebsocketUnsubscribeMethodName(websocketApi);
		addImport(WebsocketListener.class);
		String subscribeMethodSignature = new StringBuilder()
				.append("String ")
				.append(subscribeMethodName)
				.append("(")
				.append(hasArguments? requestSimpleClassName 
										+ " " + requestArgName 
										+ ", "
									: "")
				.append("WebsocketListener<")
				.append(messageClassSimpleName)
				.append("> listener)").toString();
		appendToBody("\n")
			.append(JavaCodeGenerationUtil.generateJavaDoc(
						"Subscribe to " + websocketApi.getName() + " stream.<br/>\n" 
						+ (websocketApi.getDescription() != null? websocketApi.getDescription() + "\n": "")
						+ "\n"
						+ (requestDescription != null? "@param " + requestArgName + " " + requestDescription + "\n": "") 
						+ "@return client subscriptionId to use for unsubscription")
						+ "\n");
		appendToBody(subscribeMethodSignature + ";\n");
		
		String unsubscribeMethodSignature = "boolean " + unsubscribeMethodName + "(String subscriptionId)";
		appendToBody("\n" 
		    		+ JavaCodeGenerationUtil.generateJavaDoc(
		    			"Unsubscribe from " 
						+ websocketApi.getName() 
						+ " stream.\n"
						+ "\n@param subscriptionId ID of subscription returned by #" 
						+ subscribeMethodName 
						+ "()") 
						+ "\n");
		appendToBody(unsubscribeMethodSignature + ";\n");
	}

	private void generateRestEndpointMethodDeclaration(RestEndpointDescriptor restApi) {
		boolean hasArguments = ExchangeJavaWrapperGeneratorUtil.restEndpointHasArguments(restApi, exchangeApiDescriptor);
		EndpointParameter request = restApi.getRequest();
		EndpointParameterType requestDataType = getEndpointParameterType(request);
		EndpointParameter response = restApi.getResponse();
		EndpointParameterType responseDataType = getEndpointParameterType(response);
		String requestSimpleClassName = "Object";
		String requestArgName = null;
		if (hasArguments) {
			String requestClassName = ExchangeJavaWrapperGeneratorUtil.generateRestEnpointRequestClassName(exchangeDescriptor, exchangeApiDescriptor, restApi);
			requestSimpleClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
					requestDataType, 
					getImports(), 
					requestClassName);
			requestArgName = ExchangeJavaWrapperGeneratorUtil.getRequestArgName(request.getName());
		}
		boolean hasResponse = ExchangeJavaWrapperGeneratorUtil.restEndpointHasResponse(restApi, exchangeApiDescriptor);
		String restResponseClassName = null;
		String responseSimpleClassName = "String";
		if (hasResponse) {
			restResponseClassName = ExchangeJavaWrapperGeneratorUtil.generateRestEnpointResponseClassName(
					exchangeDescriptor, 
					exchangeApiDescriptor, 
					restApi);
			
			responseSimpleClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
					responseDataType, 
					getImports(), 
					restResponseClassName);
		}
		String apiMethodName = JavaCodeGenerationUtil.firstLetterToLowerCase(restApi.getName());
		String apiMethodSignature =  new StringBuilder()
				.append(FutureRestResponse.class.getSimpleName())
				.append("<")
				.append(responseSimpleClassName)
				.append("> ")
				.append(apiMethodName)
				.append("(")
				.append(hasArguments? requestSimpleClassName 
									   + " " +
									   requestArgName
									 : "")
				.append(")").toString(); 
		StringBuilder javaDoc = new StringBuilder();
		if (restApi.getDescription() != null) {
			javaDoc.append(restApi.getDescription())
				   .append("\n");
		}
		if (hasArguments && request.getDescription() != null) {
			javaDoc.append("@param ")
				   .append(requestArgName)
				   .append(" ")
				   .append(request.getDescription())
				   .append("\n");
		}
		if (hasResponse && response.getDescription() != null) {
			javaDoc.append("@return ")
			   .append(response.getDescription())
			   .append("\n");
		}
		if (javaDoc.length() > 0) {
			javaDoc.deleteCharAt(javaDoc.length() - 1);
			appendToBody(JavaCodeGenerationUtil.generateJavaDoc(javaDoc.toString())).append("\n");
		}
		appendToBody(apiMethodSignature + ";\n");
		addImport(FutureRestResponse.class);
	}

}
