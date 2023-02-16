package com.scz.jcex.binance.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceAccountResponseBalances;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Parses incoming JSON messages into com.scz.jcex.binance.gen.spottrading.pojo.BinanceAccountResponseBalances instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.binance.gen.spottrading.pojo.BinanceAccountResponseBalances
 */
public class BinanceAccountResponseBalancesDeserializer extends AbstractJsonMessageDeserializer<BinanceAccountResponseBalances> {
  
  @Override
  public BinanceAccountResponseBalances deserialize(JsonParser parser) throws IOException {
    BinanceAccountResponseBalances msg = new BinanceAccountResponseBalances();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "asset":
        msg.setAsset(parser.nextTextValue());
      break;
      case "free":
        msg.setFree(new BigDecimal(parser.nextTextValue()));
      break;
      case "locked":
        msg.setLocked(new BigDecimal(parser.nextTextValue()));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
  
}
