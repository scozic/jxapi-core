package com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetRealTimeTickerRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetRealTimeTickerRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinGetRealTimeTickerRequest
 */
public class KucoinGetRealTimeTickerRequestSerializer extends StdSerializer<KucoinGetRealTimeTickerRequest> {
  public KucoinGetRealTimeTickerRequestSerializer() {
    super(KucoinGetRealTimeTickerRequest.class);
  }
  
  @Override
  public void serialize(KucoinGetRealTimeTickerRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getSymbol() != null){
      gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    }
    gen.writeEndObject();
  }
}
