package org.jxapi.exchange.descriptor.gen.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.ConstantDescriptor;
import org.jxapi.netutils.serialization.json.AbstractJsonValueSerializer;
import org.jxapi.netutils.serialization.json.ListJsonValueSerializer;
import static org.jxapi.util.JsonUtil.writeCustomSerializerField;
import static org.jxapi.util.JsonUtil.writeObjectField;
import static org.jxapi.util.JsonUtil.writeStringField;

/**
 * Jackson JSON Serializer for org.jxapi.exchange.descriptor.gen.ConstantDescriptor
 * @see ConstantDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class ConstantDescriptorSerializer extends AbstractJsonValueSerializer<ConstantDescriptor> {
  
  private static final long serialVersionUID = -5162948237972625778L;
  
  /**
   * Constructor
   */
  public ConstantDescriptorSerializer() {
    super(ConstantDescriptor.class);
  }
  
  private ListJsonValueSerializer<ConstantDescriptor> constantsSerializer;
  
  @Override
  public void serialize(ConstantDescriptor value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    writeStringField(gen, "name", value.getName());
    writeStringField(gen, "description", value.getDescription());
    writeStringField(gen, "type", value.getType());
    writeObjectField(gen, "value", value.getValue());
    if(constantsSerializer == null) {
      constantsSerializer = new ListJsonValueSerializer<>(new ConstantDescriptorSerializer());
    }
    writeCustomSerializerField(gen, "constants", value.getConstants(), constantsSerializer, provider);
    gen.writeEndObject();
  }
}
