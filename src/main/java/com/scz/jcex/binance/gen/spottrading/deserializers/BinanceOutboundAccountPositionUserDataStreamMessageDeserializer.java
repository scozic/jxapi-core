package com.scz.jcex.binance.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamMessage;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamMessageB;
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
  private final BinanceOutboundAccountPositionUserDataStreamMessageBDeserializer binanceOutboundAccountPositionUserDataStreamMessageBDeserializer = new BinanceOutboundAccountPositionUserDataStreamMessageBDeserializer();
  private final StructListFieldDeserializer<BinanceOutboundAccountPositionUserDataStreamMessageB> binanceOutboundAccountPositionUserDataStreamMessageBListDeserializer = new StructListFieldDeserializer<BinanceOutboundAccountPositionUserDataStreamMessageB>(binanceOutboundAccountPositionUserDataStreamMessageBDeserializer);
  
  @Override
  public BinanceOutboundAccountPositionUserDataStreamMessage deserialize(JsonParser parser) throws IOException {
    BinanceOutboundAccountPositionUserDataStreamMessage msg = new BinanceOutboundAccountPositionUserDataStreamMessage();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "e":
        msg.sete(parser.nextTextValue());
      break;
      case "E":
        msg.setE(parser.nextLongValue(0L));
      break;
      case "u":
        msg.setU(parser.nextLongValue(0L));
      break;
      case "B":
        parser.nextToken();
        msg.setB(binanceOutboundAccountPositionUserDataStreamMessageBListDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
