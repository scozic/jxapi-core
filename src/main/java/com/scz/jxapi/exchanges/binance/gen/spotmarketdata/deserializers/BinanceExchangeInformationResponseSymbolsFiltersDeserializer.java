package com.scz.jxapi.exchanges.binance.gen.spotmarketdata.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.scz.jxapi.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponseSymbolsFilters;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import com.scz.jxapi.netutils.serialization.json.JsonParserUtil;
import java.io.IOException;
import static com.scz.jxapi.util.EncodingUtil.readNextBigDecimal;

/**
 * Parses incoming JSON messages into com.scz.jxapi.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponseSymbolsFilters instances
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see com.scz.jxapi.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponseSymbolsFilters
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
        msg.setMinPrice(readNextBigDecimal(parser));
      break;
      case "maxPrice":
        msg.setMaxPrice(readNextBigDecimal(parser));
      break;
      case "tickSize":
        msg.setTickSize(readNextBigDecimal(parser));
      break;
      default:
        JsonParserUtil.skipNextValue(parser);
      }
    }
    
     return msg;
  }
}
