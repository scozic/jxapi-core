package com.scz.jcex.exchanges.binance.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponseSymbolsFilters;
import com.scz.jcex.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponseSymbolsFilters
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceExchangeInformationResponseSymbolsFilters
 */
public class BinanceExchangeInformationResponseSymbolsFiltersSerializer extends StdSerializer<BinanceExchangeInformationResponseSymbolsFilters> {
  public BinanceExchangeInformationResponseSymbolsFiltersSerializer() {
    super(BinanceExchangeInformationResponseSymbolsFilters.class);
  }
  
  @Override
  public void serialize(BinanceExchangeInformationResponseSymbolsFilters value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getFilterType() != null){
      gen.writeStringField("filterType", String.valueOf(value.getFilterType()));
    }
    if (value.getMinPrice() != null){
      gen.writeStringField("minPrice", EncodingUtil.bigDecimalToString(value.getMinPrice()));
    }
    if (value.getMaxPrice() != null){
      gen.writeStringField("maxPrice", EncodingUtil.bigDecimalToString(value.getMaxPrice()));
    }
    if (value.getTickSize() != null){
      gen.writeStringField("tickSize", EncodingUtil.bigDecimalToString(value.getTickSize()));
    }
    gen.writeEndObject();
  }
}
