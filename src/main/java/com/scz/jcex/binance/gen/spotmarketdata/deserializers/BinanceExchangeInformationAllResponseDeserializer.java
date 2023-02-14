package com.scz.jcex.binance.gen.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationAllResponse;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationAllResponseSymbols;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.deserialization.json.field.StructListFieldDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationAllResponse instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationAllResponse
 */
public class BinanceExchangeInformationAllResponseDeserializer extends AbstractJsonMessageDeserializer<BinanceExchangeInformationAllResponse> {
  private final BinanceExchangeInformationAllResponseSymbolsDeserializer binanceExchangeInformationAllResponseSymbolsDeserializer = new BinanceExchangeInformationAllResponseSymbolsDeserializer();
  private final StructListFieldDeserializer<BinanceExchangeInformationAllResponseSymbols> binanceExchangeInformationAllResponseSymbolsListDeserializer = new StructListFieldDeserializer<BinanceExchangeInformationAllResponseSymbols>(binanceExchangeInformationAllResponseSymbolsDeserializer);
  
  @Override
  public BinanceExchangeInformationAllResponse deserialize(JsonParser parser) throws IOException {
    BinanceExchangeInformationAllResponse msg = new BinanceExchangeInformationAllResponse();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "timezone":
        msg.setTimezone(parser.nextTextValue());
      break;
      case "serverTime":
        msg.setServerTime(parser.nextLongValue(0L));
      break;
      case "symbols":
        msg.setSymbols(binanceExchangeInformationAllResponseSymbolsListDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
  
}
