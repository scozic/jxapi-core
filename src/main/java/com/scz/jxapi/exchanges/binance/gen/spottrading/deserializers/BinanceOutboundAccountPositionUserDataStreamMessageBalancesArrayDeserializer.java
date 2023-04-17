package com.scz.jxapi.exchanges.binance.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamMessageBalancesArray;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;

import static com.scz.jxapi.util.EncodingUtil.readNextBigDecimal;

import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamMessageBalancesArray instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamMessageBalancesArray
 */
public class BinanceOutboundAccountPositionUserDataStreamMessageBalancesArrayDeserializer extends AbstractJsonMessageDeserializer<BinanceOutboundAccountPositionUserDataStreamMessageBalancesArray> {
  
  @Override
  public BinanceOutboundAccountPositionUserDataStreamMessageBalancesArray deserialize(JsonParser parser) throws IOException {
    BinanceOutboundAccountPositionUserDataStreamMessageBalancesArray msg = new BinanceOutboundAccountPositionUserDataStreamMessageBalancesArray();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "a":
        msg.setAsset(parser.nextTextValue());
      break;
      case "f":
        msg.setFree(readNextBigDecimal(parser));
      break;
      case "l":
        msg.setLocked(readNextBigDecimal(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
