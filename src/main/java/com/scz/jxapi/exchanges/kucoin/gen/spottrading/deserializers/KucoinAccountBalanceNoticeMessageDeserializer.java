package com.scz.jxapi.exchanges.kucoin.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinAccountBalanceNoticeMessage;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinAccountBalanceNoticeMessage instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinAccountBalanceNoticeMessage
 */
public class KucoinAccountBalanceNoticeMessageDeserializer extends AbstractJsonMessageDeserializer<KucoinAccountBalanceNoticeMessage> {
  private final KucoinAccountBalanceNoticeMessageDataDeserializer kucoinAccountBalanceNoticeMessageDataDeserializer = new KucoinAccountBalanceNoticeMessageDataDeserializer();
  
  @Override
  public KucoinAccountBalanceNoticeMessage deserialize(JsonParser parser) throws IOException {
    KucoinAccountBalanceNoticeMessage msg = new KucoinAccountBalanceNoticeMessage();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "type":
        msg.setType(parser.nextTextValue());
      break;
      case "topic":
        msg.setTopic(parser.nextTextValue());
      break;
      case "subject":
        msg.setSubject(parser.nextTextValue());
      break;
      case "data":
        parser.nextToken();
        msg.setData(kucoinAccountBalanceNoticeMessageDataDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
