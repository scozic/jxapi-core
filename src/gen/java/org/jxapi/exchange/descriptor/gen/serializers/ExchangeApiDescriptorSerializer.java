package org.jxapi.exchange.descriptor.gen.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor;
import org.jxapi.netutils.serialization.json.AbstractJsonValueSerializer;
import org.jxapi.netutils.serialization.json.ListJsonValueSerializer;
import static org.jxapi.util.JsonUtil.writeCustomSerializerField;
import static org.jxapi.util.JsonUtil.writeStringField;

/**
 * Jackson JSON Serializer for org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor
 * @see ExchangeApiDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class ExchangeApiDescriptorSerializer extends AbstractJsonValueSerializer<ExchangeApiDescriptor> {
  
  private static final long serialVersionUID = -549713475401761102L;
  
  /**
   * Constructor
   */
  public ExchangeApiDescriptorSerializer() {
    super(ExchangeApiDescriptor.class);
  }
  
  private ListJsonValueSerializer<RestEndpointDescriptor> restEndpointsSerializer;
  private ListJsonValueSerializer<WebsocketEndpointDescriptor> websocketEndpointsSerializer;
  
  @Override
  public void serialize(ExchangeApiDescriptor value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    writeStringField(gen, "name", value.getName());
    writeStringField(gen, "description", value.getDescription());
    writeStringField(gen, "httpUrl", value.getHttpUrl());
    writeStringField(gen, "defaultHttpClient", value.getDefaultHttpClient());
    writeStringField(gen, "defaultWebsocketClient", value.getDefaultWebsocketClient());
    if(restEndpointsSerializer == null) {
      restEndpointsSerializer = new ListJsonValueSerializer<>(new RestEndpointDescriptorSerializer());
    }
    writeCustomSerializerField(gen, "restEndpoints", value.getRestEndpoints(), restEndpointsSerializer, provider);
    if(websocketEndpointsSerializer == null) {
      websocketEndpointsSerializer = new ListJsonValueSerializer<>(new WebsocketEndpointDescriptorSerializer());
    }
    writeCustomSerializerField(gen, "websocketEndpoints", value.getWebsocketEndpoints(), websocketEndpointsSerializer, provider);
    gen.writeEndObject();
  }
}
