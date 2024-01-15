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
		String requestClassName = ExchangeJavaWrapperGeneratorUtil.generateWebsocketEndpointRequestClassName(exchangeDescriptor, exchangeApiDescriptor, websocketApi);
		String requestClassSimpleName = JavaCodeGenerationUtil.getClassNameWithoutPackage(requestClassName);
		addImport(requestClassName);
		String messageClassSimpleName = ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
				EndpointParameterType.fromTypeName(websocketApi.getResponseDataType()), 
				getImports(), 
				ExchangeJavaWrapperGeneratorUtil.generateWebsocketEndpointMessageClassName(
						exchangeDescriptor, 
						exchangeApiDescriptor, 
						websocketApi));
		String subscribeMethodName = "subscribe" + JavaCodeGenerationUtil.firstLetterToUpperCase(websocketApi.getName());
		String unsubscribeMethodName = "unsubscribe" + JavaCodeGenerationUtil.firstLetterToUpperCase(websocketApi.getName());
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
		String responseSimpleClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
				EndpointParameterType.fromTypeName(restApi.getResponseDataType()), 
				getImports(), 
				ExchangeJavaWrapperGeneratorUtil.generateRestEnpointResponseClassName(
						exchangeDescriptor, 
						exchangeApiDescriptor, 
						restApi));
		String apiMethodName = JavaCodeGenerationUtil.firstLetterToLowerCase(restApi.getName());
		String apiMethodSignature = FutureRestResponse.class.getSimpleName() + "<" + responseSimpleClassName + "> " + apiMethodName + "(" + requestSimpleClassName + " request)"; 
		appendToBody(JavaCodeGenerationUtil.generateJavaDoc(restApi.getDescription()) + "\n");
		appendToBody(apiMethodSignature + ";\n");
		addImport(FutureRestResponse.class);
	}

}
