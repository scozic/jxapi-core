package com.scz.jcex.binance.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamMessage;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamMessageBalancesArray;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.deserialization.json.field.StructListFieldDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamMessage instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamMessage
 */
public class BinanceOutboundAccountPositionUserDataStreamMessageDeserializer extends AbstractJsonMessageDeserializer<BinanceOutboundAccountPositionUserDataStreamMessage> {
  private final BinanceOutboundAccountPositionUserDataStreamMessageBalancesArrayDeserializer binanceOutboundAccountPositionUserDataStreamMessageBalancesArrayDeserializer = new BinanceOutboundAccountPositionUserDataStreamMessageBalancesArrayDeserializer();
  private final StructListFieldDeserializer<BinanceOutboundAccountPositionUserDataStreamMessageBalancesArray> binanceOutboundAccountPositionUserDataStreamMessageBalancesArrayListDeserializer = new StructListFieldDeserializer<BinanceOutboundAccountPositionUserDataStreamMessageBalancesArray>(binanceOutboundAccountPositionUserDataStreamMessageBalancesArrayDeserializer);
  
  @Override
  public BinanceOutboundAccountPositionUserDataStreamMessage deserialize(JsonParser parser) throws IOException {
    BinanceOutboundAccountPositionUserDataStreamMessage msg = new BinanceOutboundAccountPositionUserDataStreamMessage();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "e":
        msg.setEventType(parser.nextTextValue());
      break;
      case "E":
        msg.setEventTime(parser.nextLongValue(0L));
      break;
      case "u":
        msg.setLastAccountUpdateTime(parser.nextLongValue(0L));
      break;
      case "B":
        parser.nextToken();
        msg.setBalancesArray(binanceOutboundAccountPositionUserDataStreamMessageBalancesArrayListDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
