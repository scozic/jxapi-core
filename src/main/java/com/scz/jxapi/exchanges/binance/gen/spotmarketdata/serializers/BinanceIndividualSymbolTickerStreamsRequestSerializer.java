package com.scz.jxapi.exchanges.binance.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.binance.gen.spotmarketdata.pojo.BinanceIndividualSymbolTickerStreamsRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.binance.gen.spotmarketdata.pojo.BinanceIndividualSymbolTickerStreamsRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see BinanceIndividualSymbolTickerStreamsRequest
 */
public class BinanceIndividualSymbolTickerStreamsRequestSerializer extends StdSerializer<BinanceIndividualSymbolTickerStreamsRequest> {
  public BinanceIndividualSymbolTickerStreamsRequestSerializer() {
    super(BinanceIndividualSymbolTickerStreamsRequest.class);
  }
  
  @Override
  public void serialize(BinanceIndividualSymbolTickerStreamsRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getSymbol() != null){
      gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    }
    gen.writeEndObject();
  }
}
