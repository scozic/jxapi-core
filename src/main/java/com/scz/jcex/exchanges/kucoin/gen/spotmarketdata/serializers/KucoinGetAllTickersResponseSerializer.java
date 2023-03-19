package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetAllTickersResponse;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetAllTickersResponse
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinGetAllTickersResponse
 */
public class KucoinGetAllTickersResponseSerializer extends StdSerializer<KucoinGetAllTickersResponse> {
  public KucoinGetAllTickersResponseSerializer() {
    super(KucoinGetAllTickersResponse.class);
  }
  
  @Override
  public void serialize(KucoinGetAllTickersResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("code", String.valueOf(value.getCode()));
    gen.writeStringField("msg", String.valueOf(value.getMsg()));
    gen.writeObjectField("data", value.getData());
    gen.writeEndObject();
  }
}
