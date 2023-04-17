package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinAccountBalanceEventsMessage;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;

import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinAccountBalanceEventsMessage instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinAccountBalanceEventsMessage
 */
public class KucoinAccountBalanceEventsMessageDeserializer extends AbstractJsonMessageDeserializer<KucoinAccountBalanceEventsMessage> {
  private final KucoinAccountBalanceEventsMessageDataDeserializer kucoinAccountBalanceEventsMessageDataDeserializer = new KucoinAccountBalanceEventsMessageDataDeserializer();
  
  @Override
  public KucoinAccountBalanceEventsMessage deserialize(JsonParser parser) throws IOException {
    KucoinAccountBalanceEventsMessage msg = new KucoinAccountBalanceEventsMessage();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "userId":
        msg.setUserId(parser.nextTextValue());
      break;
      case "topic":
        msg.setTopic(parser.nextTextValue());
      break;
      case "subject":
        msg.setSubject(parser.nextTextValue());
      break;
      case "data":
        parser.nextToken();
        msg.setData(kucoinAccountBalanceEventsMessageDataDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
