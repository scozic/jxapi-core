package org.jxapi.exchange.descriptor.gen.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.Constant;

/**
 * Jackson JSON Serializer for org.jxapi.exchange.descriptor.gen.Constant
 * @see Constant
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class ConstantSerializer extends StdSerializer<Constant> {
  /**
   * Constructor
   */
  public ConstantSerializer() {
    super(Constant.class);
  }
  
  @Override
  public void serialize(Constant value, JsonGenerator gen, SerializerProvider provider) throws IOException {
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
