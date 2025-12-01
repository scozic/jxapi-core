package org.jxapi.exchange.descriptor.gen.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor;

/**
 * Jackson JSON Serializer for org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor
 * @see ConfigPropertyDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class ConfigPropertyDescriptorSerializer extends StdSerializer<ConfigPropertyDescriptor> {
  /**
   * Constructor
   */
  public ConfigPropertyDescriptorSerializer() {
    super(ConfigPropertyDescriptor.class);
  }
  
  @Override
  public void serialize(ConfigPropertyDescriptor value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getName() != null){
      gen.writeStringField("name", String.valueOf(value.getName()));
    }
    if (value.getDescription() != null){
      gen.writeStringField("description", String.valueOf(value.getDescription()));
    }
    if (value.getType() != null){
      gen.writeStringField("type", String.valueOf(value.getType()));
    }
    if (value.getDefaultValue() != null){
      gen.writeObjectField("defaultValue", value.getDefaultValue());
    }
    if (value.getProperties() != null){
      gen.writeObjectField("properties", value.getProperties());
    }
    gen.writeEndObject();
  }
}
