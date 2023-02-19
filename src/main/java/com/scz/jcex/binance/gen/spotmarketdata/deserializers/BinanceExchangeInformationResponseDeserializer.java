package com.scz.jcex.binance.gen.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponse;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponseSymbols;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.deserialization.json.field.StructListFieldDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;

/**
 * Parses incoming JSON messages into com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponse instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponse
 */
public class BinanceExchangeInformationResponseDeserializer extends AbstractJsonMessageDeserializer<BinanceExchangeInformationResponse> {
  private final BinanceExchangeInformationResponseSymbolsDeserializer binanceExchangeInformationResponseSymbolsDeserializer = new BinanceExchangeInformationResponseSymbolsDeserializer();
  private final StructListFieldDeserializer<BinanceExchangeInformationResponseSymbols> binanceExchangeInformationResponseSymbolsListDeserializer = new StructListFieldDeserializer<BinanceExchangeInformationResponseSymbols>(binanceExchangeInformationResponseSymbolsDeserializer);
  
  @Override
  public BinanceExchangeInformationResponse deserialize(JsonParser parser) throws IOException {
    BinanceExchangeInformationResponse msg = new BinanceExchangeInformationResponse();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "timezone":
        msg.setTimezone(parser.nextTextValue());
      break;
      case "serverTime":
        msg.setServerTime(parser.nextLongValue(0L));
      break;
      case "symbols":
        parser.nextToken();
        msg.setSymbols(binanceExchangeInformationResponseSymbolsListDeserializer.deserialize(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
