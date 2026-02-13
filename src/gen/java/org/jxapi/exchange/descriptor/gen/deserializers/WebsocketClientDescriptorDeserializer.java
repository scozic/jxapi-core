package org.jxapi.exchange.descriptor.gen.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.WebsocketClientDescriptor;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import static org.jxapi.util.JsonUtil.readNextString;
import static org.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into org.jxapi.exchange.descriptor.gen.WebsocketClientDescriptor instances
 * @see org.jxapi.exchange.descriptor.gen.WebsocketClientDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoDeserializerGenerator")
public class WebsocketClientDescriptorDeserializer extends AbstractJsonMessageDeserializer<WebsocketClientDescriptor> {
  
  @Override
  public WebsocketClientDescriptor deserialize(JsonParser parser) throws IOException {
    WebsocketClientDescriptor msg = new WebsocketClientDescriptor();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.currentName()) {
      case "name":
        msg.setName(readNextString(parser));
      break;
      case "websocketUrl":
        msg.setWebsocketUrl(readNextString(parser));
      break;
      case "websocketFactory":
        msg.setWebsocketFactory(readNextString(parser));
      break;
      case "websocketHookFactory":
        msg.setWebsocketHookFactory(readNextString(parser));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
