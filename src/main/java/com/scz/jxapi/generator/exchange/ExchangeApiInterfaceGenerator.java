package com.scz.jxapi.generator.exchange;

import java.util.List;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.JavaTypeGenerator;
import com.scz.jxapi.netutils.rest.FutureRestResponse;
import com.scz.jxapi.netutils.websocket.WebsocketListener;

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
	}
	
	@Override
	public String generate() {
		for (RestEndpointDescriptor restApi: exchangeApiDescriptor.getRestEndpoints()) {
			generateRestEndpointMethodDeclaration(restApi);
		}
		
		for (WebsocketEndpointDescriptor websocketApi : exchangeApiDescriptor.getWebsocketEndpoints()) {
			generateWebsocketApiMethodsDeclarations(websocketApi);
		}
		
		return super.generate();
	}

	private void generateWebsocketApiMethodsDeclarations(WebsocketEndpointDescriptor websocketApi) {
		String websocketEndpointFactoryFullClassName = exchangeApiDescriptor.getWebsocketEndpointFactory();
		if (websocketEndpointFactoryFullClassName == null) {
			throw new IllegalStateException("No 'websocketEndpointFactory' defined on " + exchangeApiDescriptor.getName());
		}
		String requestClassName = ExchangeJavaWrapperGeneratorUtil.generateWebsocketEndpointRequestClassName(exchangeDescriptor, exchangeApiDescriptor, websocketApi);
		String requestClassSimpleName = JavaCodeGenerationUtil.getClassNameWithoutPackage(requestClassName);
		addImport(requestClassName);
		String messageClassName = ExchangeJavaWrapperGeneratorUtil.generateWebsocketEndpointMessageClassName(exchangeDescriptor, exchangeApiDescriptor, websocketApi);
		String messageClassSimpleName = JavaCodeGenerationUtil.getClassNameWithoutPackage(messageClassName);
		String subscribeMethodName = "subscribe" + JavaCodeGenerationUtil.firstLetterToUpperCase(websocketApi.getName());
		String unsubscribeMethodName = "unsubscribe" + JavaCodeGenerationUtil.firstLetterToUpperCase(websocketApi.getName());
		switch (websocketApi.getResponseDataType()) {
		case JSON_OBJECT:
			addImport(messageClassName);
			break;
		case JSON_OBJECT_LIST:
			addImport(messageClassName);
			addImport(List.class);
			messageClassSimpleName = "List<" + messageClassSimpleName + ">";
			break;
		case STRING:
			break;
		default:
			throw new IllegalArgumentException("Unexpected responseDataType" + websocketApi.getResponseDataType() + " for:" + websocketApi);
		}
		addImport(WebsocketListener.class);
		String subscribeMethodSignature = "String " 
										  + subscribeMethodName 
										  + "(" + requestClassSimpleName 
										  + " request, WebsocketListener<" 
										  + messageClassSimpleName  
										  + "> listener)";
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
		String requestClassName = ExchangeJavaWrapperGeneratorUtil.generateRestEnpointRequestClassName(exchangeDescriptor, exchangeApiDescriptor, restApi);
		String requestSimpleClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(requestClassName);
		addImport(requestClassName);
		String responseClassName = ExchangeJavaWrapperGeneratorUtil.generateRestEnpointResponseClassName(exchangeDescriptor, exchangeApiDescriptor, restApi);
		String responseSimpleClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(responseClassName);
		switch (restApi.getResponseDataType()) {
		case JSON_OBJECT:
			addImport(responseClassName);
			break;
		case JSON_OBJECT_LIST:
			addImport(responseClassName);
			addImport(List.class);
			responseSimpleClassName = "List<" + responseSimpleClassName + ">";
			break;
		case STRING:
			break;
		default:
			throw new IllegalArgumentException("Unexpected responseDataType" + restApi.getResponseDataType() + " for:" + restApi);
		}
		String apiMethodName = JavaCodeGenerationUtil.firstLetterToLowerCase(restApi.getName());
		String apiMethodSignature = FutureRestResponse.class.getSimpleName() + "<" + responseSimpleClassName + "> " + apiMethodName + "(" + requestSimpleClassName + " request)"; 
		appendToBody(JavaCodeGenerationUtil.generateJavaDoc(restApi.getDescription()) + "\n");
		appendToBody(apiMethodSignature + ";\n");
		addImport(FutureRestResponse.class);
	}

}
