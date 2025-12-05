package org.jxapi.exchange.descriptor.gen.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import javax.annotation.processing.Generated;
import org.jxapi.exchange.descriptor.gen.WebsocketTopicMatcherDescriptor;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import org.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import static org.jxapi.util.JsonUtil.readNextString;
import static org.jxapi.util.JsonUtil.skipNextValue;

/**
 * Parses incoming JSON messages into org.jxapi.exchange.descriptor.gen.WebsocketTopicMatcherDescriptor instances
 * @see org.jxapi.exchange.descriptor.gen.WebsocketTopicMatcherDescriptor
 */
@Generated("org.jxapi.generator.java.pojo.JsonMessageDeserializerGenerator")
public class WebsocketTopicMatcherDescriptorDeserializer extends AbstractJsonMessageDeserializer<WebsocketTopicMatcherDescriptor> {
  private final ListJsonFieldDeserializer<WebsocketTopicMatcherDescriptor> andDeserializer = new ListJsonFieldDeserializer<>(new WebsocketTopicMatcherDescriptorDeserializer());
  private final ListJsonFieldDeserializer<WebsocketTopicMatcherDescriptor> orDeserializer = new ListJsonFieldDeserializer<>(new WebsocketTopicMatcherDescriptorDeserializer());
  
  @Override
  public WebsocketTopicMatcherDescriptor deserialize(JsonParser parser) throws IOException {
    WebsocketTopicMatcherDescriptor msg = new WebsocketTopicMatcherDescriptor();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "fieldName":
        msg.setFieldName(readNextString(parser));
      break;
      case "fieldValue":
        msg.setFieldValue(readNextString(parser));
      break;
      case "fieldRegexp":
        msg.setFieldRegexp(readNextString(parser));
      break;
      case "and":
        parser.nextToken();
        msg.setAnd(andDeserializer.deserialize(parser));
      break;
      case "or":
        parser.nextToken();
        msg.setOr(orDeserializer.deserialize(parser));
      break;
      default:
        skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
