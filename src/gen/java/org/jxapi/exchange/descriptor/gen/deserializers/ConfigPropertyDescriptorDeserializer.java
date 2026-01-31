package org.jxapi.exchange.descriptor.gen.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import org.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import static org.jxapi.util.JsonUtil.readNextObject;
import static org.jxapi.util.JsonUtil.readNextString;
import static org.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor instances
 * @see org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoDeserializerGenerator")
public class ConfigPropertyDescriptorDeserializer extends AbstractJsonMessageDeserializer<ConfigPropertyDescriptor> {
  private ListJsonFieldDeserializer<ConfigPropertyDescriptor> propertiesDeserializer;
  
  @Override
  public ConfigPropertyDescriptor deserialize(JsonParser parser) throws IOException {
    ConfigPropertyDescriptor msg = new ConfigPropertyDescriptor();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.currentName()) {
      case "name":
        msg.setName(readNextString(parser));
      break;
      case "description":
        msg.setDescription(readNextString(parser));
      break;
      case "type":
        msg.setType(readNextString(parser));
      break;
      case "defaultValue":
        msg.setDefaultValue(readNextObject(parser));
      break;
      case "properties":
        parser.nextToken();
        if(propertiesDeserializer == null) {
          propertiesDeserializer = new ListJsonFieldDeserializer<>(new ConfigPropertyDescriptorDeserializer());
        }
        msg.setProperties(propertiesDeserializer.deserialize(parser));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
