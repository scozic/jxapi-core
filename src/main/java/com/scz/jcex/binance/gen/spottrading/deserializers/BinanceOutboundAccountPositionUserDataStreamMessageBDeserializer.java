package com.scz.jcex.binance.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamMessageB;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Parses incoming JSON messages into com.scz.jcex.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamMessageB instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamMessageB
 */
public class BinanceOutboundAccountPositionUserDataStreamMessageBDeserializer extends AbstractJsonMessageDeserializer<BinanceOutboundAccountPositionUserDataStreamMessageB> {
  
  @Override
  public BinanceOutboundAccountPositionUserDataStreamMessageB deserialize(JsonParser parser) throws IOException {
    BinanceOutboundAccountPositionUserDataStreamMessageB msg = new BinanceOutboundAccountPositionUserDataStreamMessageB();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "a":
        msg.setA(parser.nextTextValue());
      break;
      case "f":
        msg.setF(new BigDecimal(parser.nextTextValue()));
      break;
      case "l":
        msg.setL(new BigDecimal(parser.nextTextValue()));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
