package org.jxapi.exchange.descriptor.gen.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.WebsocketTopicMatcherDescriptor;
import org.jxapi.netutils.serialization.json.AbstractJsonValueSerializer;
import org.jxapi.netutils.serialization.json.ListJsonValueSerializer;
import static org.jxapi.util.JsonUtil.writeCustomSerializerField;
import static org.jxapi.util.JsonUtil.writeObjectField;
import static org.jxapi.util.JsonUtil.writeStringField;

/**
 * Jackson JSON Serializer for org.jxapi.exchange.descriptor.gen.WebsocketTopicMatcherDescriptor
 * @see WebsocketTopicMatcherDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class WebsocketTopicMatcherDescriptorSerializer extends AbstractJsonValueSerializer<WebsocketTopicMatcherDescriptor> {
  
  private static final long serialVersionUID = -4147049876199877260L;
  
  /**
   * Constructor
   */
  public WebsocketTopicMatcherDescriptorSerializer() {
    super(WebsocketTopicMatcherDescriptor.class);
  }
  
  private ListJsonValueSerializer<WebsocketTopicMatcherDescriptor> andSerializer;
  private ListJsonValueSerializer<WebsocketTopicMatcherDescriptor> orSerializer;
  
  @Override
  public void serialize(WebsocketTopicMatcherDescriptor value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    writeStringField(gen, "fieldName", value.getFieldName());
    writeObjectField(gen, "fieldValue", value.getFieldValue());
    writeStringField(gen, "fieldRegexp", value.getFieldRegexp());
    if(andSerializer == null) {
      andSerializer = new ListJsonValueSerializer<>(new WebsocketTopicMatcherDescriptorSerializer());
    }
    writeCustomSerializerField(gen, "and", value.getAnd(), andSerializer, provider);
    if(orSerializer == null) {
      orSerializer = new ListJsonValueSerializer<>(new WebsocketTopicMatcherDescriptorSerializer());
    }
    writeCustomSerializerField(gen, "or", value.getOr(), orSerializer, provider);
    gen.writeEndObject();
  }
}
