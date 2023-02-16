package com.scz.jcex.binance.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponseSymbolsFilters;
import com.scz.jcex.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponseSymbolsFilters
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
    gen.writeStringField("filterType", String.valueOf(value.getFilterType()));
    gen.writeStringField("minPrice", EncodingUtil.bigDecimalToString(value.getMinPrice()));
    gen.writeStringField("maxPrice", EncodingUtil.bigDecimalToString(value.getMaxPrice()));
    gen.writeStringField("tickSize", EncodingUtil.bigDecimalToString(value.getTickSize()));
    gen.writeEndObject();
  }
  
}
