package com.scz.jcex.binance.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jcex.binance.spotmarketdata.pojo.BinanceExchangeInformationResponseSymbolsFilters;
import com.scz.jcex.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Parses incoming JSON messages into com.scz.jcex.binance.spotmarketdata.pojo.BinanceExchangeInformationResponseSymbolsFilters instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jcex.binance.spotmarketdata.pojo.BinanceExchangeInformationResponseSymbolsFilters
 */
public class BinanceExchangeInformationResponseSymbolsFiltersDeserializer extends AbstractJsonMessageDeserializer<BinanceExchangeInformationResponseSymbolsFilters> {
  
  @Override
  public BinanceExchangeInformationResponseSymbolsFilters deserialize(JsonParser parser) throws IOException {
    BinanceExchangeInformationResponseSymbolsFilters msg = new BinanceExchangeInformationResponseSymbolsFilters();
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
      }
    }
    
     return msg;
  }
  
}
