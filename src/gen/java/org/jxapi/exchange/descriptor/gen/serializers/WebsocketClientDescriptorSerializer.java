package org.jxapi.exchange.descriptor.gen.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.WebsocketClientDescriptor;
import org.jxapi.netutils.serialization.json.AbstractJsonValueSerializer;
import static org.jxapi.util.JsonUtil.writeStringField;

/**
 * Jackson JSON Serializer for org.jxapi.exchange.descriptor.gen.WebsocketClientDescriptor
 * @see WebsocketClientDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class WebsocketClientDescriptorSerializer extends AbstractJsonValueSerializer<WebsocketClientDescriptor> {
  
  private static final long serialVersionUID = 3615036968754342043L;
  
  /**
   * Constructor
   */
  public WebsocketClientDescriptorSerializer() {
    super(WebsocketClientDescriptor.class);
  }
  
  
  @Override
  public void serialize(WebsocketClientDescriptor value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    writeStringField(gen, "name", value.getName());
    writeStringField(gen, "websocketUrl", value.getWebsocketUrl());
    writeStringField(gen, "websocketFactory", value.getWebsocketFactory());
    writeStringField(gen, "websocketHookFactory", value.getWebsocketHookFactory());
    gen.writeEndObject();
  }
}
