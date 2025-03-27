package org.jxapi.generator.java.exchange.api.ws;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import org.springframework.util.CollectionUtils;

import org.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.Field;
import org.jxapi.exchange.descriptor.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.Type;
import org.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import org.jxapi.generator.java.exchange.ClassesGenerator;
import org.jxapi.generator.java.exchange.api.ExchangeApiGenUtil;
import org.jxapi.generator.java.exchange.api.pojo.JsonMessageDeserializerClassesGenerator;
import org.jxapi.generator.java.exchange.api.pojo.JsonPojoSerializerClassesGenerator;
import org.jxapi.generator.java.exchange.api.pojo.PojoClassesGenerator;

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
	 * @param exchangeDescriptor Exchange descriptor where API with REST endpoint are defined
	 * @param apiDescriptor API group of exchange descriptor defining the REST endpoint
	 * @param websocketEndpointDescriptor REST endpoint descriptor to generate related Java classes for.
	 */
	public WebsocketEndpointClassesGenerator(ExchangeDescriptor exchangeDescriptor, 
			ExchangeApiDescriptor apiDescriptor, 
			WebsocketEndpointDescriptor websocketEndpointDescriptor) {
		this.exchangeDescriptor = exchangeDescriptor;
		this.apiDescriptor = apiDescriptor;
		this.websocketEndpointDescriptor = websocketEndpointDescriptor;
		this.request = ExchangeApiGenUtil.resolveFieldProperties(apiDescriptor, websocketEndpointDescriptor.getRequest());
	}
	
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
					ExchangeApiGenUtil.generateWebsocketEndpointRequestPojoClassName(
							exchangeDescriptor, 
							apiDescriptor, 
							websocketEndpointDescriptor),
							request.getProperties())
			  .generateClasses(outputFolder);
		}
	
		if (shouldGenerateMessagePojo()) {
			new JsonPojoSerializerClassesGenerator( 
					ExchangeApiGenUtil.generateWebsocketEndpointMessagePojoClassName(
							exchangeDescriptor, 
							apiDescriptor, 
							websocketEndpointDescriptor),
					websocketEndpointDescriptor.getMessage().getProperties())
			  .generateClasses(outputFolder);
		}
	}

	private void generateDeserializers(Path outputFolder) throws IOException {
		if (shouldGenerateMessagePojo()) {
			new JsonMessageDeserializerClassesGenerator(
					ExchangeApiGenUtil.generateWebsocketEndpointMessagePojoClassName(
							exchangeDescriptor, 
							apiDescriptor, 
							websocketEndpointDescriptor),
							websocketEndpointDescriptor.getMessage().getProperties()).generateClasses(outputFolder);
		}
	}

	private void generatePojos(Path outputFolder) throws IOException {
		if (shouldGenerateRequestPojo()) {
			new PojoClassesGenerator( 
					ExchangeApiGenUtil.generateWebsocketEndpointRequestPojoClassName(exchangeDescriptor, apiDescriptor, websocketEndpointDescriptor), 
					"Subscription request to" + exchangeDescriptor.getId() 
						+ " " + apiDescriptor.getName() + " API " 
						+ websocketEndpointDescriptor.getName() 
						+ " websocket endpoint<br>\n" 
						+ websocketEndpointDescriptor.getDescription(),
					request.getProperties(), 
					request.getImplementedInterfaces() 
				).generateClasses(outputFolder);
		}
		
		if (shouldGenerateMessagePojo()) {
			new PojoClassesGenerator( 
					ExchangeApiGenUtil.generateWebsocketEndpointMessagePojoClassName(
							exchangeDescriptor, 
							apiDescriptor, 
							websocketEndpointDescriptor), 
					"Message disseminated upon subscription to " 
						+ exchangeDescriptor.getId() + " " 
						+ apiDescriptor.getName() + " API " 
						+ websocketEndpointDescriptor.getName() + " websocket endpoint request<br>\n"
						+ websocketEndpointDescriptor.getDescription(),
					websocketEndpointDescriptor.getMessage().getProperties(), 
					websocketEndpointDescriptor.getMessage().getImplementedInterfaces() 
				).generateClasses(outputFolder);
		}
	}
	
	private boolean shouldGenerateRequestPojo() {
		return shouldGeneratePojo(websocketEndpointDescriptor.getRequest());
	}
	
	private boolean shouldGenerateMessagePojo() {
		return shouldGeneratePojo(websocketEndpointDescriptor.getMessage());
	}
	
	private boolean shouldGeneratePojo(Field field) {
		if (field == null) {
			return false;
		}
		Type type = Optional.ofNullable(field.getType()).orElse(Type.OBJECT);
		if (!type.isObject()) {
			return false;
		}
		return !CollectionUtils.isEmpty(field.getProperties());
	}
	

}
