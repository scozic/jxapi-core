package org.jxapi.generator.java.exchange.api.ws;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import org.springframework.util.CollectionUtils;

import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor;
import org.jxapi.generator.java.exchange.ClassesGenerator;
import org.jxapi.generator.java.exchange.ConstantValuePlaceholderResolverFactory;
import org.jxapi.generator.java.exchange.ExchangeConstantValuePlaceholderResolverFactory;
import org.jxapi.generator.java.exchange.api.ExchangeApiGenUtil;
import org.jxapi.generator.java.pojo.JsonMessageDeserializerClassesGenerator;
import org.jxapi.generator.java.pojo.JsonPojoSerializerClassesGenerator;
import org.jxapi.generator.java.pojo.PojoClassesGenerator;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.PlaceHolderResolver;

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
  private final PlaceHolderResolver docPlaceHolderResolver;
  private final ConstantValuePlaceholderResolverFactory constantInDefaultValuesPlaceHolderResolverFactory;
  
  /**
   * @param exchangeDescriptor Exchange descriptor where API with REST endpoint are defined
   * @param apiDescriptor API group of exchange descriptor defining the REST endpoint
   * @param websocketEndpointDescriptor REST endpoint descriptor to generate related Java classes for.
   * @param docPlaceHolderResolver Place holder resolver for resolution of placeholders in descriptions.
   */
  public WebsocketEndpointClassesGenerator(ExchangeDescriptor exchangeDescriptor, 
                                           ExchangeApiDescriptor apiDescriptor, 
                                           WebsocketEndpointDescriptor websocketEndpointDescriptor,
                                           PlaceHolderResolver docPlaceHolderResolver) {
    this.exchangeDescriptor = exchangeDescriptor;
    this.apiDescriptor = apiDescriptor;
    this.websocketEndpointDescriptor = websocketEndpointDescriptor;
    this.docPlaceHolderResolver = docPlaceHolderResolver;
    this.request = ExchangeApiGenUtil.resolveFieldProperties(apiDescriptor, websocketEndpointDescriptor.getRequest());
    this.constantInDefaultValuesPlaceHolderResolverFactory = new ExchangeConstantValuePlaceholderResolverFactory(exchangeDescriptor);
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
    if (shouldGenerateRequestPojo()) {
      new JsonMessageDeserializerClassesGenerator( 
          ExchangeApiGenUtil.generateWebsocketEndpointRequestPojoClassName(
              exchangeDescriptor, 
              apiDescriptor, 
              websocketEndpointDescriptor),
              request.getProperties())
        .generateClasses(outputFolder);
    }
    
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
          request.getImplementedInterfaces(),
          docPlaceHolderResolver,
          constantInDefaultValuesPlaceHolderResolverFactory
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
          websocketEndpointDescriptor.getMessage().getImplementedInterfaces(),
          docPlaceHolderResolver,
          constantInDefaultValuesPlaceHolderResolverFactory
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
