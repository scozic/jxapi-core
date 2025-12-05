package org.jxapi.exchange.descriptor.gen.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor;

/**
 * Jackson JSON Serializer for org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor
 * @see WebsocketEndpointDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class WebsocketEndpointDescriptorSerializer extends StdSerializer<WebsocketEndpointDescriptor> {
  /**
   * Constructor
   */
  public WebsocketEndpointDescriptorSerializer() {
    super(WebsocketEndpointDescriptor.class);
  }
  
  @Override
  public void serialize(WebsocketEndpointDescriptor value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getName() != null){
      gen.writeStringField("name", String.valueOf(value.getName()));
    }
    if (value.getDescription() != null){
      gen.writeStringField("description", String.valueOf(value.getDescription()));
    }
    if (value.getTopic() != null){
      gen.writeStringField("topic", String.valueOf(value.getTopic()));
    }
    if (value.getTopicParametersListSeparator() != null){
      gen.writeStringField("topicParametersListSeparator", String.valueOf(value.getTopicParametersListSeparator()));
    }
    if (value.getDocUrl() != null){
      gen.writeStringField("docUrl", String.valueOf(value.getDocUrl()));
    }
    if (value.getRequest() != null){
      gen.writeObjectField("request", value.getRequest());
    }
    if (value.getMessage() != null){
      gen.writeObjectField("message", value.getMessage());
    }
    if (value.getTopicMatcher() != null){
      gen.writeObjectField("topicMatcher", value.getTopicMatcher());
    }
    gen.writeEndObject();
  }
}
