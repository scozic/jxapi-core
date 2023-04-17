package com.scz.jcex.exchanges.binance.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamMessageBalancesArray;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jcex.util.EncodingUtil.readNextBigDecimal;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamMessageBalancesArray instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamMessageBalancesArray
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
