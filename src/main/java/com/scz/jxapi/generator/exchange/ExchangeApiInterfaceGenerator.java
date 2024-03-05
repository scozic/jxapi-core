package com.scz.jxapi.generator.exchange;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.JavaTypeGenerator;
import com.scz.jxapi.netutils.rest.FutureRestResponse;
import com.scz.jxapi.netutils.websocket.WebsocketListener;
import com.scz.jxapi.util.HasProperties;

public class ExchangeApiInterfaceGenerator extends JavaTypeGenerator {
	
	private final ExchangeDescriptor exchangeDescriptor;
	
	private final ExchangeApiDescriptor exchangeApiDescriptor;
	
	public ExchangeApiInterfaceGenerator(ExchangeDescriptor exchangeDescriptor, ExchangeApiDescriptor exchangeApiDescriptor) {
		super(ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor));
		this.exchangeDescriptor = exchangeDescriptor;
		this.exchangeApiDescriptor = exchangeApiDescriptor;
		setDescription(exchangeApiDescriptor.getName() + " CEX " + exchangeApiDescriptor.getName() + " API</br>\n" 
				+ exchangeApiDescriptor.getDescription() + "\n" 
				+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING);
		setTypeDeclaration("public interface");
		this.setParentClassName(HasProperties.class.getName());
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
		String websocketEndpointFactoryFullClassName = exchangeApiDescriptor.getWebsocketEndpointFactory();
		if (websocketEndpointFactoryFullClassName == null) {
			throw new IllegalStateException("No 'websocketEndpointFactory' defined on " + exchangeApiDescriptor.getName());
		}
		EndpointParameterType requestDataType = websocketApi.getRequest() == null? null: websocketApi.getRequest().getEndpointParameterType();
		boolean hasArguments = ExchangeJavaWrapperGeneratorUtil.websocketEndpointHasArguments(websocketApi, exchangeApiDescriptor);
		String requestSimpleClassName = Object.class.getSimpleName();
		if (hasArguments) {
			String requestClassName = ExchangeJavaWrapperGeneratorUtil.generateWebsocketEndpointRequestClassName(
													exchangeDescriptor, 
													exchangeApiDescriptor, 
													websocketApi);
			requestSimpleClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
													requestDataType, 
													getImports(), 
													requestClassName);
		}
		
		EndpointParameterType messageDataType = websocketApi.getMessage() == null? null: websocketApi.getMessage().getEndpointParameterType();
		String messageClassSimpleName = ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
				messageDataType, 
				getImports(), 
				ExchangeJavaWrapperGeneratorUtil.generateWebsocketEndpointMessageClassName(
						exchangeDescriptor, 
						exchangeApiDescriptor, 
						websocketApi));
		String subscribeMethodName = "subscribe" + JavaCodeGenerationUtil.firstLetterToUpperCase(websocketApi.getName());
		String unsubscribeMethodName = "unsubscribe" + JavaCodeGenerationUtil.firstLetterToUpperCase(websocketApi.getName());
		addImport(WebsocketListener.class);
		String subscribeMethodSignature = new StringBuilder()
				.append("String ")
				.append(subscribeMethodName)
				.append("(")
				.append(hasArguments? requestSimpleClassName 
										+ " " + ExchangeJavaWrapperGeneratorUtil.getRequestArgName(websocketApi.getRequestArgName()) 
										+ ", "
									: "")
				.append("WebsocketListener<")
				.append(messageClassSimpleName)
				.append("> listener)").toString();
		appendToBody("\n" 
		  			+ JavaCodeGenerationUtil.generateJavaDoc(
					"Subscribe to " + websocketApi.getName() + " stream.<br/>\n" 
			    	+ websocketApi.getDescription() 
					+ "\n"
					+ "\n@return client subscriptionId to use for unsubscription")
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
		EndpointParameterType requestDataType = request == null? null: request.getEndpointParameterType(); //EndpointParameterType.fromTypeName(restApi.getRequestDataType());
		EndpointParameter response = restApi.getResponse();
		EndpointParameterType responseDataType = response == null? null: response.getEndpointParameterType();
		String requestSimpleClassName = "Object";
		if (hasArguments) {
			String requestClassName = ExchangeJavaWrapperGeneratorUtil.generateRestEnpointRequestClassName(exchangeDescriptor, exchangeApiDescriptor, restApi);
			requestSimpleClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
					requestDataType, 
					getImports(), 
					requestClassName);
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
									   ExchangeJavaWrapperGeneratorUtil.getRequestArgName(request.getName())
									 : "")
				.append(")").toString(); 
		appendToBody(JavaCodeGenerationUtil.generateJavaDoc(restApi.getDescription()) + "\n");
		appendToBody(apiMethodSignature + ";\n");
		addImport(FutureRestResponse.class);
	}

}
