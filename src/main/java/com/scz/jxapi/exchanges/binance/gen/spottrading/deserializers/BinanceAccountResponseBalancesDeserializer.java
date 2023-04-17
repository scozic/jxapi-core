package com.scz.jxapi.exchanges.binance.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceAccountResponseBalances;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jxapi.util.EncodingUtil.readNextBigDecimal;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceAccountResponseBalances instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceAccountResponseBalances
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
        msg.setFree(readNextBigDecimal(parser));
      break;
      case "locked":
        msg.setLocked(readNextBigDecimal(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
