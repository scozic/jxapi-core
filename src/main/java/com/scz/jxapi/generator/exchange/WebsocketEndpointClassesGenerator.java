package com.scz.jxapi.generator.exchange;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;

/**
 * Generates all classes used by a particular Websocket endpoint defined in an  
 * exchange descriptor file. This covers:
 * <ul>
 * <li>POJOs for endpoint request and response
 * <li>JSON deserializers for those POJOs
 * <li>JSON serializers for those POJOs
 * </ul>
 * @see RestEndpointDescriptor
 */
public class WebsocketEndpointClassesGenerator implements ClassesGenerator {
	
	private final ExchangeDescriptor exchangeDescriptor;
	private final ExchangeApiDescriptor apiDescriptor;
	private final WebsocketEndpointDescriptor websocketEndpointDescriptor;
	
	/**
	 * 
	 */
	public WebsocketEndpointClassesGenerator(ExchangeDescriptor exchangeDescriptor, 
			ExchangeApiDescriptor apiDescriptor, 
			WebsocketEndpointDescriptor websocketEndpointDescriptor) {
		this.exchangeDescriptor = exchangeDescriptor;
		this.apiDescriptor = apiDescriptor;
		this.websocketEndpointDescriptor = websocketEndpointDescriptor;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void generateClasses(Path outputFolder) throws IOException {
		// Generate POJOs for request and response
		generatePojos(outputFolder);
		
		// Generate deserializers for request/response pojos
		generateDeserializers(outputFolder);
		
		// Generate serializers
		generateSerializers(outputFolder);
	}

	private void generateSerializers(Path outputFolder) throws IOException {
		if (shouldGenerateRequestPojo()) {
			new JsonPojoSerializerClassesGenerator( 
					ExchangeJavaWrapperGeneratorUtil.generateWebsocketEndpointRequestClassName(
							exchangeDescriptor, 
							apiDescriptor, 
							websocketEndpointDescriptor),
					websocketEndpointDescriptor.getParameters()).generateClasses(outputFolder);
		}
	
		if (shouldGenerateMessagePojo()) {
			new JsonPojoSerializerClassesGenerator( 
					ExchangeJavaWrapperGeneratorUtil.generateWebsocketEndpointMessageClassName(
							exchangeDescriptor, 
							apiDescriptor, 
							websocketEndpointDescriptor),
							websocketEndpointDescriptor.getResponse()).generateClasses(outputFolder);;
		}
	}

	private void generateDeserializers(Path outputFolder) throws IOException {
		List<EndpointParameter> response = websocketEndpointDescriptor.getResponse();
		if (shouldGenerateMessagePojo()) {
			new JsonMessageDeserializerClassesGenerator(
					ExchangeJavaWrapperGeneratorUtil.generateWebsocketEndpointMessageClassName(
							exchangeDescriptor, 
							apiDescriptor, 
							websocketEndpointDescriptor),
							response).generateClasses(outputFolder);
		}
	}

	private void generatePojos(Path outputFolder) throws IOException {
		List<EndpointParameter> requestParameters = websocketEndpointDescriptor.getParameters();
		if (shouldGenerateRequestPojo()) {
			new EndpointPojoClassesGenerator( 
					ExchangeJavaWrapperGeneratorUtil.generateWebsocketEndpointRequestClassName(exchangeDescriptor, apiDescriptor, websocketEndpointDescriptor), 
					"Subscription request to" + exchangeDescriptor.getName() 
						+ " " + apiDescriptor.getName() + " API " 
						+ websocketEndpointDescriptor.getName() 
						+ " websocket endpoint<br/>\n" 
						+ websocketEndpointDescriptor.getDescription()
						+ "\n"
						+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING,
						requestParameters, 
					websocketEndpointDescriptor.getRequestInterfaces(), 
					null).generateClasses(outputFolder);
		}
		
		List<EndpointParameter> responseParameters = websocketEndpointDescriptor.getResponse();
		if (shouldGenerateMessagePojo()) {
			new EndpointPojoClassesGenerator( 
					ExchangeJavaWrapperGeneratorUtil.generateWebsocketEndpointMessageClassName(
							exchangeDescriptor, 
							apiDescriptor, 
							websocketEndpointDescriptor), 
					"Message disseminated upon subscription to " 
						+ exchangeDescriptor.getName() + " " 
						+ apiDescriptor.getName() + " API " 
						+ websocketEndpointDescriptor.getName() + " websocket endpoint request<br/>\n"
						+ websocketEndpointDescriptor.getDescription()
						+ "\n"
						+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING,
					responseParameters, 
					websocketEndpointDescriptor.getResponseInterfaces(), 
					null).generateClasses(outputFolder);
		}
	}
	
	private boolean shouldGenerateRequestPojo() {
		return shouldGeneratePojo(websocketEndpointDescriptor.getParameters(), websocketEndpointDescriptor.getRequestDataType());
	}
	
	private boolean shouldGenerateMessagePojo() {
		return shouldGeneratePojo(websocketEndpointDescriptor.getResponse(), websocketEndpointDescriptor.getMessageDataType());
	}
	
	private boolean shouldGeneratePojo(List<EndpointParameter> parameters, String dataType) {
		return parameters != null && 
			  !(parameters.isEmpty() && websocketEndpointDescriptor.getRequestDataType().equals(CanonicalEndpointParameterTypes.OBJECT.name()));
	}
	

}
