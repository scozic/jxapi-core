package org.jxapi.exchange.descriptor.gen.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.WebsocketTopicMatcherDescriptor;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import org.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import static org.jxapi.util.JsonUtil.readNextObject;
import static org.jxapi.util.JsonUtil.readNextString;
import static org.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into org.jxapi.exchange.descriptor.gen.WebsocketTopicMatcherDescriptor instances
 * @see org.jxapi.exchange.descriptor.gen.WebsocketTopicMatcherDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoDeserializerGenerator")
public class WebsocketTopicMatcherDescriptorDeserializer extends AbstractJsonMessageDeserializer<WebsocketTopicMatcherDescriptor> {
  private ListJsonFieldDeserializer<WebsocketTopicMatcherDescriptor> andDeserializer;
  private ListJsonFieldDeserializer<WebsocketTopicMatcherDescriptor> orDeserializer;
  
  @Override
  public WebsocketTopicMatcherDescriptor deserialize(JsonParser parser) throws IOException {
    WebsocketTopicMatcherDescriptor msg = new WebsocketTopicMatcherDescriptor();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.currentName()) {
      case "fieldName":
        msg.setFieldName(readNextString(parser));
      break;
      case "fieldValue":
        msg.setFieldValue(readNextObject(parser));
      break;
      case "fieldRegexp":
        msg.setFieldRegexp(readNextString(parser));
      break;
      case "and":
        parser.nextToken();
        if(andDeserializer == null) {
          andDeserializer = new ListJsonFieldDeserializer<>(new WebsocketTopicMatcherDescriptorDeserializer());
        }
        msg.setAnd(andDeserializer.deserialize(parser));
      break;
      case "or":
        parser.nextToken();
        if(orDeserializer == null) {
          orDeserializer = new ListJsonFieldDeserializer<>(new WebsocketTopicMatcherDescriptorDeserializer());
        }
        msg.setOr(orDeserializer.deserialize(parser));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
