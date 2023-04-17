package com.scz.jcex.exchanges.kucoin.gen.futurestrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinTradeOrdersMessage;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinTradeOrdersMessage instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinTradeOrdersMessage
 */
public class KucoinTradeOrdersMessageDeserializer extends AbstractJsonMessageDeserializer<KucoinTradeOrdersMessage> {
  private final KucoinTradeOrdersMessageDataDeserializer kucoinTradeOrdersMessageDataDeserializer = new KucoinTradeOrdersMessageDataDeserializer();
  
  @Override
  public KucoinTradeOrdersMessage deserialize(JsonParser parser) throws IOException {
    KucoinTradeOrdersMessage msg = new KucoinTradeOrdersMessage();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "type":
        msg.setType(parser.nextTextValue());
      break;
      case "subject":
        msg.setSubject(parser.nextTextValue());
      break;
      case "topic":
        msg.setTopic(parser.nextTextValue());
      break;
      case "channelType":
        msg.setChannelType(parser.nextTextValue());
      break;
      case "data":
        parser.nextToken();
        msg.setData(kucoinTradeOrdersMessageDataDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
