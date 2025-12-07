package org.jxapi.exchange.descriptor.gen.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.WebsocketTopicMatcherDescriptor;

/**
 * Jackson JSON Serializer for org.jxapi.exchange.descriptor.gen.WebsocketTopicMatcherDescriptor
 * @see WebsocketTopicMatcherDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class WebsocketTopicMatcherDescriptorSerializer extends StdSerializer<WebsocketTopicMatcherDescriptor> {
  /**
   * Constructor
   */
  public WebsocketTopicMatcherDescriptorSerializer() {
    super(WebsocketTopicMatcherDescriptor.class);
  }
  
  @Override
  public void serialize(WebsocketTopicMatcherDescriptor value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getFieldName() != null){
      gen.writeStringField("fieldName", String.valueOf(value.getFieldName()));
    }
    if (value.getFieldValue() != null){
      gen.writeObjectField("fieldValue", value.getFieldValue());
    }
    if (value.getFieldRegexp() != null){
      gen.writeStringField("fieldRegexp", String.valueOf(value.getFieldRegexp()));
    }
    if (value.getAnd() != null){
      gen.writeObjectField("and", value.getAnd());
    }
    if (value.getOr() != null){
      gen.writeObjectField("or", value.getOr());
    }
    gen.writeEndObject();
  }
}
