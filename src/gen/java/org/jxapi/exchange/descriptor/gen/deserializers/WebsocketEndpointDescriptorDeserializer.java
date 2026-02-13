package org.jxapi.exchange.descriptor.gen.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import static org.jxapi.util.JsonUtil.readNextObject;
import static org.jxapi.util.JsonUtil.readNextString;
import static org.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor instances
 * @see org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoDeserializerGenerator")
public class WebsocketEndpointDescriptorDeserializer extends AbstractJsonMessageDeserializer<WebsocketEndpointDescriptor> {
  private WebsocketTopicMatcherDescriptorDeserializer topicMatcherDeserializer;
  
  @Override
  public WebsocketEndpointDescriptor deserialize(JsonParser parser) throws IOException {
    WebsocketEndpointDescriptor msg = new WebsocketEndpointDescriptor();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.currentName()) {
      case "name":
        msg.setName(readNextString(parser));
      break;
      case "description":
        msg.setDescription(readNextString(parser));
      break;
      case "topic":
        msg.setTopic(readNextString(parser));
      break;
      case "websocketClient":
        msg.setWebsocketClient(readNextString(parser));
      break;
      case "docUrl":
        msg.setDocUrl(readNextString(parser));
      break;
      case "request":
        msg.setRequest(readNextObject(parser, org.jxapi.pojo.descriptor.Field.class));
      break;
      case "message":
        msg.setMessage(readNextObject(parser, org.jxapi.pojo.descriptor.Field.class));
      break;
      case "topicMatcher":
        parser.nextToken();
        if(topicMatcherDeserializer == null) {
          topicMatcherDeserializer = new WebsocketTopicMatcherDescriptorDeserializer();
        }
        msg.setTopicMatcher(topicMatcherDeserializer.deserialize(parser));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
