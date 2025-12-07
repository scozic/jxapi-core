package org.jxapi.exchange.descriptor.gen.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.ConstantDescriptor;

/**
 * Jackson JSON Serializer for org.jxapi.exchange.descriptor.gen.ConstantDescriptor
 * @see ConstantDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class ConstantDescriptorSerializer extends StdSerializer<ConstantDescriptor> {
  /**
   * Constructor
   */
  public ConstantDescriptorSerializer() {
    super(ConstantDescriptor.class);
  }
  
  @Override
  public void serialize(ConstantDescriptor value, JsonGenerator gen, SerializerProvider provider) throws IOException {
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
    if (value.getValue() != null){
      gen.writeObjectField("value", value.getValue());
    }
    if (value.getConstants() != null){
      gen.writeObjectField("constants", value.getConstants());
    }
    gen.writeEndObject();
  }
}
