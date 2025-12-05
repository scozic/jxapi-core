package org.jxapi.exchange.descriptor.gen.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;

/**
 * Jackson JSON Serializer for org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor
 * @see ExchangeApiDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class ExchangeApiDescriptorSerializer extends StdSerializer<ExchangeApiDescriptor> {
  /**
   * Constructor
   */
  public ExchangeApiDescriptorSerializer() {
    super(ExchangeApiDescriptor.class);
  }
  
  @Override
  public void serialize(ExchangeApiDescriptor value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getName() != null){
      gen.writeStringField("name", String.valueOf(value.getName()));
    }
    if (value.getDescription() != null){
      gen.writeStringField("description", String.valueOf(value.getDescription()));
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
    if (value.getHttpRequestTimeout() != null){
      gen.writeNumberField("httpRequestTimeout", value.getHttpRequestTimeout());
    }
    if (value.getWebsocketFactory() != null){
      gen.writeStringField("websocketFactory", String.valueOf(value.getWebsocketFactory()));
    }
    if (value.getWebsocketHookFactory() != null){
      gen.writeStringField("websocketHookFactory", String.valueOf(value.getWebsocketHookFactory()));
    }
    if (value.getWebsocketUrl() != null){
      gen.writeStringField("websocketUrl", String.valueOf(value.getWebsocketUrl()));
    }
    if (value.getRestEndpoints() != null){
      gen.writeObjectField("restEndpoints", value.getRestEndpoints());
    }
    if (value.getWebsocketEndpoints() != null){
      gen.writeObjectField("websocketEndpoints", value.getWebsocketEndpoints());
    }
    gen.writeEndObject();
  }
}
