package com.scz.jxapi.generator.java.exchange.api.ws;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.RestEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.exchange.ClassesGenerator;
import com.scz.jxapi.generator.java.exchange.api.ExchangeApiGeneratorUtil;
import com.scz.jxapi.generator.java.exchange.api.pojo.EndpointPojoClassesGenerator;
import com.scz.jxapi.generator.java.exchange.api.pojo.JsonMessageDeserializerClassesGenerator;
import com.scz.jxapi.generator.java.exchange.api.pojo.JsonPojoSerializerClassesGenerator;

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
	private final Field request;
	
	/**
	 * 
	 */
	public WebsocketEndpointClassesGenerator(ExchangeDescriptor exchangeDescriptor, 
			ExchangeApiDescriptor apiDescriptor, 
			WebsocketEndpointDescriptor websocketEndpointDescriptor) {
		this.exchangeDescriptor = exchangeDescriptor;
		this.apiDescriptor = apiDescriptor;
		this.websocketEndpointDescriptor = websocketEndpointDescriptor;
		this.request = ExchangeApiGeneratorUtil.resolveFieldProperties(apiDescriptor, websocketEndpointDescriptor.getRequest());
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
					ExchangeApiGeneratorUtil.generateWebsocketEndpointRequestClassName(
							exchangeDescriptor, 
							apiDescriptor, 
							websocketEndpointDescriptor),
							request.getParameters())
			  .generateClasses(outputFolder);
		}
	
		if (shouldGenerateMessagePojo()) {
			new JsonPojoSerializerClassesGenerator( 
					ExchangeApiGeneratorUtil.generateWebsocketEndpointMessageClassName(
							exchangeDescriptor, 
							apiDescriptor, 
							websocketEndpointDescriptor),
					websocketEndpointDescriptor.getMessage().getParameters())
			  .generateClasses(outputFolder);;
		}
	}

	private void generateDeserializers(Path outputFolder) throws IOException {
//		List<EndpointParameter> response = websocketEndpointDescriptor.getResponse();
		if (shouldGenerateMessagePojo()) {
			new JsonMessageDeserializerClassesGenerator(
					ExchangeApiGeneratorUtil.generateWebsocketEndpointMessageClassName(
							exchangeDescriptor, 
							apiDescriptor, 
							websocketEndpointDescriptor),
							websocketEndpointDescriptor.getMessage().getParameters()).generateClasses(outputFolder);
		}
	}

	private void generatePojos(Path outputFolder) throws IOException {
//		List<EndpointParameter> requestParameters = websocketEndpointDescriptor.getParameters();
		if (shouldGenerateRequestPojo()) {
			new EndpointPojoClassesGenerator( 
					ExchangeApiGeneratorUtil.generateWebsocketEndpointRequestClassName(exchangeDescriptor, apiDescriptor, websocketEndpointDescriptor), 
					"Subscription request to" + exchangeDescriptor.getName() 
						+ " " + apiDescriptor.getName() + " API " 
						+ websocketEndpointDescriptor.getName() 
						+ " websocket endpoint<br/>\n" 
						+ websocketEndpointDescriptor.getDescription()
						+ "\n"
						+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING,
					request.getParameters(), 
					request.getImplementedInterfaces(), 
					null).generateClasses(outputFolder);
		}
		
		if (shouldGenerateMessagePojo()) {
			new EndpointPojoClassesGenerator( 
					ExchangeApiGeneratorUtil.generateWebsocketEndpointMessageClassName(
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
					websocketEndpointDescriptor.getMessage().getParameters(), 
					websocketEndpointDescriptor.getMessage().getImplementedInterfaces(), 
					null).generateClasses(outputFolder);
		}
	}
	
	private boolean shouldGenerateRequestPojo() {
		return shouldGeneratePojo(websocketEndpointDescriptor.getRequest());
	}
	
	private boolean shouldGenerateMessagePojo() {
		return shouldGeneratePojo(websocketEndpointDescriptor.getMessage());
	}
	
	private boolean shouldGeneratePojo(Field param) {
		if (param == null) {
			return false;
		}
		Type type = Optional.ofNullable(param.getType()).orElse(Type.OBJECT);
		if (!type.isObject()) {
			return false;
		}
		List<Field> parameters = param.getParameters();
		return parameters != null && !parameters.isEmpty();
	}
	

}
