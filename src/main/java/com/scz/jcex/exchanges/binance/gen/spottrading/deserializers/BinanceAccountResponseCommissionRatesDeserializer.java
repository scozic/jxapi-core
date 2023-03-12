package com.scz.jcex.exchanges.binance.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceAccountResponseCommissionRates;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceAccountResponseCommissionRates instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceAccountResponseCommissionRates
 */
public class BinanceAccountResponseCommissionRatesDeserializer extends AbstractJsonMessageDeserializer<BinanceAccountResponseCommissionRates> {
  
  @Override
  public BinanceAccountResponseCommissionRates deserialize(JsonParser parser) throws IOException {
    BinanceAccountResponseCommissionRates msg = new BinanceAccountResponseCommissionRates();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "maker":
        msg.setMaker(new BigDecimal(parser.nextTextValue()));
      break;
      case "taker":
        msg.setTaker(new BigDecimal(parser.nextTextValue()));
      break;
      case "buyer":
        msg.setBuyer(new BigDecimal(parser.nextTextValue()));
      break;
      case "seller":
        msg.setSeller(new BigDecimal(parser.nextTextValue()));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
