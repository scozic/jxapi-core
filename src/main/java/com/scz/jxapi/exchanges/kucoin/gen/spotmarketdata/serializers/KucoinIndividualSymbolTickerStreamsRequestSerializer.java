package com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinIndividualSymbolTickerStreamsRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinIndividualSymbolTickerStreamsRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinIndividualSymbolTickerStreamsRequest
 */
public class KucoinIndividualSymbolTickerStreamsRequestSerializer extends StdSerializer<KucoinIndividualSymbolTickerStreamsRequest> {
  public KucoinIndividualSymbolTickerStreamsRequestSerializer() {
    super(KucoinIndividualSymbolTickerStreamsRequest.class);
  }
  
  @Override
  public void serialize(KucoinIndividualSymbolTickerStreamsRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getSymbol() != null){
      gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    }
    gen.writeEndObject();
  }
}
