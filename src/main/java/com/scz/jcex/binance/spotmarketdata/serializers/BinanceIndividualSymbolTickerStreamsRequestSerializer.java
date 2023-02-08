package com.scz.jcex.binance.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.binance.spotmarketdata.pojo.BinanceIndividualSymbolTickerStreamsRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.binance.spotmarketdata.pojo.BinanceIndividualSymbolTickerStreamsRequest
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
    gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    gen.writeEndObject();
  }
  
}
