package com.scz.jcex.exchanges.binance.gen.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationAllResponseSymbolsFilters;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jcex.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Parses incoming JSON messages into com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationAllResponseSymbolsFilters instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationAllResponseSymbolsFilters
 */
public class BinanceExchangeInformationAllResponseSymbolsFiltersDeserializer extends AbstractJsonMessageDeserializer<BinanceExchangeInformationAllResponseSymbolsFilters> {
  
  @Override
  public BinanceExchangeInformationAllResponseSymbolsFilters deserialize(JsonParser parser) throws IOException {
    BinanceExchangeInformationAllResponseSymbolsFilters msg = new BinanceExchangeInformationAllResponseSymbolsFilters();
    while(parser.nextToken() != JsonToken.END_OBJECT) {
      switch(parser.getCurrentName()) {
      case "filterType":
        msg.setFilterType(parser.nextTextValue());
      break;
      case "minPrice":
        msg.setMinPrice(new BigDecimal(parser.nextTextValue()));
      break;
      case "maxPrice":
        msg.setMaxPrice(new BigDecimal(parser.nextTextValue()));
      break;
      case "tickSize":
        msg.setTickSize(new BigDecimal(parser.nextTextValue()));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
