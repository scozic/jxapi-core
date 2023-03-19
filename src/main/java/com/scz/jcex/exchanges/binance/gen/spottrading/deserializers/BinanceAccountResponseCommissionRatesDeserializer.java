package com.scz.jcex.exchanges.binance.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceAccountResponseCommissionRates;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jcex.util.EncodingUtil.toBigDecimal;

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
        msg.setMaker(toBigDecimal(parser.nextTextValue()));
      break;
      case "taker":
        msg.setTaker(toBigDecimal(parser.nextTextValue()));
      break;
      case "buyer":
        msg.setBuyer(toBigDecimal(parser.nextTextValue()));
      break;
      case "seller":
        msg.setSeller(toBigDecimal(parser.nextTextValue()));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
