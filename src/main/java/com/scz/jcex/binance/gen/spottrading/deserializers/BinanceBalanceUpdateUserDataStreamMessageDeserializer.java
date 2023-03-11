package com.scz.jcex.binance.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceBalanceUpdateUserDataStreamMessage;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Parses incoming JSON messages into com.scz.jcex.binance.gen.spottrading.pojo.BinanceBalanceUpdateUserDataStreamMessage instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.binance.gen.spottrading.pojo.BinanceBalanceUpdateUserDataStreamMessage
 */
public class BinanceBalanceUpdateUserDataStreamMessageDeserializer extends AbstractJsonMessageDeserializer<BinanceBalanceUpdateUserDataStreamMessage> {
  
  @Override
  public BinanceBalanceUpdateUserDataStreamMessage deserialize(JsonParser parser) throws IOException {
    BinanceBalanceUpdateUserDataStreamMessage msg = new BinanceBalanceUpdateUserDataStreamMessage();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "e":
        msg.setEventType(parser.nextTextValue());
      break;
      case "E":
        msg.setEventTime(parser.nextLongValue(0L));
      break;
      case "a":
        msg.setAsset(parser.nextTextValue());
      break;
      case "d":
        msg.setBalanceDelta(new BigDecimal(parser.nextTextValue()));
      break;
      case "T":
        msg.setClearTime(parser.nextLongValue(0L));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
