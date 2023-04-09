package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetTickerResponse;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetTickerResponse
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinGetTickerResponse
 */
public class KucoinGetTickerResponseSerializer extends StdSerializer<KucoinGetTickerResponse> {
  public KucoinGetTickerResponseSerializer() {
    super(KucoinGetTickerResponse.class);
  }
  
  @Override
  public void serialize(KucoinGetTickerResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getCode() != null){
      gen.writeStringField("code", String.valueOf(value.getCode()));
    }
    if (value.getMsg() != null){
      gen.writeStringField("msg", String.valueOf(value.getMsg()));
    }
    if (value.getData() != null){
      gen.writeObjectField("data", value.getData());
    }
    gen.writeEndObject();
  }
}
