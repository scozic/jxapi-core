package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinPositionChangeEventsMessage;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinPositionChangeEventsMessage instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinPositionChangeEventsMessage
 */
public class KucoinPositionChangeEventsMessageDeserializer extends AbstractJsonMessageDeserializer<KucoinPositionChangeEventsMessage> {
  private final KucoinPositionChangeEventsMessageDataDeserializer kucoinPositionChangeEventsMessageDataDeserializer = new KucoinPositionChangeEventsMessageDataDeserializer();
  
  @Override
  public KucoinPositionChangeEventsMessage deserialize(JsonParser parser) throws IOException {
    KucoinPositionChangeEventsMessage msg = new KucoinPositionChangeEventsMessage();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "type":
        msg.setType(parser.nextTextValue());
      break;
      case "userId":
        msg.setUserId(parser.nextTextValue());
      break;
      case "channelType":
        msg.setChannelType(parser.nextTextValue());
      break;
      case "topic":
        msg.setTopic(parser.nextTextValue());
      break;
      case "subject":
        msg.setSubject(parser.nextTextValue());
      break;
      case "data":
        parser.nextToken();
        msg.setData(kucoinPositionChangeEventsMessageDataDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
