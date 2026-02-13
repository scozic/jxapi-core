package org.jxapi.exchange.descriptor.gen.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor;
import org.jxapi.netutils.serialization.json.AbstractJsonValueSerializer;
import static org.jxapi.util.JsonUtil.writeCustomSerializerField;
import static org.jxapi.util.JsonUtil.writeObjectField;
import static org.jxapi.util.JsonUtil.writeStringField;

/**
 * Jackson JSON Serializer for org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor
 * @see WebsocketEndpointDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class WebsocketEndpointDescriptorSerializer extends AbstractJsonValueSerializer<WebsocketEndpointDescriptor> {
  
  private static final long serialVersionUID = -4331439425826461065L;
  
  /**
   * Constructor
   */
  public WebsocketEndpointDescriptorSerializer() {
    super(WebsocketEndpointDescriptor.class);
  }
  
  private WebsocketTopicMatcherDescriptorSerializer topicMatcherSerializer;
  
  @Override
  public void serialize(WebsocketEndpointDescriptor value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    writeStringField(gen, "name", value.getName());
    writeStringField(gen, "description", value.getDescription());
    writeStringField(gen, "topic", value.getTopic());
    writeStringField(gen, "websocketClient", value.getWebsocketClient());
    writeStringField(gen, "docUrl", value.getDocUrl());
    writeObjectField(gen, "request", value.getRequest());
    writeObjectField(gen, "message", value.getMessage());
    if(topicMatcherSerializer == null) {
      topicMatcherSerializer = new WebsocketTopicMatcherDescriptorSerializer();
    }
    writeCustomSerializerField(gen, "topicMatcher", value.getTopicMatcher(), topicMatcherSerializer, provider);
    gen.writeEndObject();
  }
}
