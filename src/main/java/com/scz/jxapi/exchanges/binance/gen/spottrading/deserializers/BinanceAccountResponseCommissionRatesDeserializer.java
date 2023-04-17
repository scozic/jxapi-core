package com.scz.jxapi.exchanges.binance.gen.spottrading.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceAccountResponseCommissionRates;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;

import static com.scz.jxapi.util.EncodingUtil.readNextBigDecimal;

import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceAccountResponseCommissionRates instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceAccountResponseCommissionRates
 */
public class BinanceAccountResponseCommissionRatesDeserializer extends AbstractJsonMessageDeserializer<BinanceAccountResponseCommissionRates> {
  
  @Override
  public BinanceAccountResponseCommissionRates deserialize(JsonParser parser) throws IOException {
    BinanceAccountResponseCommissionRates msg = new BinanceAccountResponseCommissionRates();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "maker":
        msg.setMaker(readNextBigDecimal(parser));
      break;
      case "taker":
        msg.setTaker(readNextBigDecimal(parser));
      break;
      case "buyer":
        msg.setBuyer(readNextBigDecimal(parser));
      break;
      case "seller":
        msg.setSeller(readNextBigDecimal(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
