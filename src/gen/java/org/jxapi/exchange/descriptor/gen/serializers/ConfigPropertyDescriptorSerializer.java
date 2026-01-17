package org.jxapi.exchange.descriptor.gen.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor;
import org.jxapi.netutils.serialization.json.AbstractJsonValueSerializer;
import org.jxapi.netutils.serialization.json.ListJsonValueSerializer;
import static org.jxapi.util.JsonUtil.writeCustomSerializerField;
import static org.jxapi.util.JsonUtil.writeObjectField;
import static org.jxapi.util.JsonUtil.writeStringField;

/**
 * Jackson JSON Serializer for org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor
 * @see ConfigPropertyDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class ConfigPropertyDescriptorSerializer extends AbstractJsonValueSerializer<ConfigPropertyDescriptor> {
  
  private static final long serialVersionUID = 2475280454295970865L;
  
  /**
   * Constructor
   */
  public ConfigPropertyDescriptorSerializer() {
    super(ConfigPropertyDescriptor.class);
  }
  
  private ListJsonValueSerializer<ConfigPropertyDescriptor> propertiesSerializer;
  
  @Override
  public void serialize(ConfigPropertyDescriptor value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    writeStringField(gen, "name", value.getName());
    writeStringField(gen, "description", value.getDescription());
    writeStringField(gen, "type", value.getType());
    writeObjectField(gen, "defaultValue", value.getDefaultValue());
    if(propertiesSerializer == null) {
      propertiesSerializer = new ListJsonValueSerializer<>(new ConfigPropertyDescriptorSerializer());
    }
    writeCustomSerializerField(gen, "properties", value.getProperties(), propertiesSerializer, provider);
    gen.writeEndObject();
  }
}
