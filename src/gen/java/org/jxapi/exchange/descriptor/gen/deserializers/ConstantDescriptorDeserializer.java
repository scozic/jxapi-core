package org.jxapi.exchange.descriptor.gen.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.ConstantDescriptor;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import org.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import static org.jxapi.util.JsonUtil.readNextObject;
import static org.jxapi.util.JsonUtil.readNextString;
import static org.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into org.jxapi.exchange.descriptor.gen.ConstantDescriptor instances
 * @see org.jxapi.exchange.descriptor.gen.ConstantDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoDeserializerGenerator")
public class ConstantDescriptorDeserializer extends AbstractJsonMessageDeserializer<ConstantDescriptor> {
  private ListJsonFieldDeserializer<ConstantDescriptor> constantsDeserializer;
  
  @Override
  public ConstantDescriptor deserialize(JsonParser parser) throws IOException {
    ConstantDescriptor msg = new ConstantDescriptor();
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
      case "value":
        msg.setValue(readNextObject(parser));
      break;
      case "constants":
        parser.nextToken();
        if(constantsDeserializer == null) {
          constantsDeserializer = new ListJsonFieldDeserializer<>(new ConstantDescriptorDeserializer());
        }
        msg.setConstants(constantsDeserializer.deserialize(parser));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
