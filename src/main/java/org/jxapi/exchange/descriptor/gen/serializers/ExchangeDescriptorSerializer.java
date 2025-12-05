package org.jxapi.exchange.descriptor.gen.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;

/**
 * Jackson JSON Serializer for org.jxapi.exchange.descriptor.gen.ExchangeDescriptor
 * @see ExchangeDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class ExchangeDescriptorSerializer extends StdSerializer<ExchangeDescriptor> {
  /**
   * Constructor
   */
  public ExchangeDescriptorSerializer() {
    super(ExchangeDescriptor.class);
  }
  
  @Override
  public void serialize(ExchangeDescriptor value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getId() != null){
      gen.writeStringField("id", String.valueOf(value.getId()));
    }
    if (value.getJxapi() != null){
      gen.writeStringField("jxapi", String.valueOf(value.getJxapi()));
    }
    if (value.getVersion() != null){
      gen.writeStringField("version", String.valueOf(value.getVersion()));
    }
    if (value.getDescription() != null){
      gen.writeStringField("description", String.valueOf(value.getDescription()));
    }
    if (value.getDocUrl() != null){
      gen.writeStringField("docUrl", String.valueOf(value.getDocUrl()));
    }
    if (value.getBasePackage() != null){
      gen.writeStringField("basePackage", String.valueOf(value.getBasePackage()));
    }
    if (value.getHttpRequestExecutorFactory() != null){
      gen.writeStringField("httpRequestExecutorFactory", String.valueOf(value.getHttpRequestExecutorFactory()));
    }
    if (value.getHttpRequestInterceptorFactory() != null){
      gen.writeStringField("httpRequestInterceptorFactory", String.valueOf(value.getHttpRequestInterceptorFactory()));
    }
    if (value.getHttpUrl() != null){
      gen.writeStringField("httpUrl", String.valueOf(value.getHttpUrl()));
    }
    if (value.getWebsocketUrl() != null){
      gen.writeStringField("websocketUrl", String.valueOf(value.getWebsocketUrl()));
    }
    if (value.getWebsocketFactory() != null){
      gen.writeStringField("websocketFactory", String.valueOf(value.getWebsocketFactory()));
    }
    if (value.getWebsocketHookFactory() != null){
      gen.writeStringField("websocketHookFactory", String.valueOf(value.getWebsocketHookFactory()));
    }
    if (value.getHttpRequestTimeout() != null){
      gen.writeNumberField("httpRequestTimeout", value.getHttpRequestTimeout());
    }
    if (value.getAfterInitHookFactory() != null){
      gen.writeStringField("afterInitHookFactory", String.valueOf(value.getAfterInitHookFactory()));
    }
    if (value.getProperties() != null){
      gen.writeObjectField("properties", value.getProperties());
    }
    if (value.getConstants() != null){
      gen.writeObjectField("constants", value.getConstants());
    }
    if (value.getRateLimits() != null){
      gen.writeObjectField("rateLimits", value.getRateLimits());
    }
    if (value.getApis() != null){
      gen.writeObjectField("apis", value.getApis());
    }
    gen.writeEndObject();
  }
}
