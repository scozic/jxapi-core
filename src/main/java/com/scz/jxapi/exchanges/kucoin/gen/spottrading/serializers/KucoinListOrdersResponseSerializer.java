package com.scz.jxapi.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersResponse;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersResponse
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinListOrdersResponse
 */
public class KucoinListOrdersResponseSerializer extends StdSerializer<KucoinListOrdersResponse> {
  public KucoinListOrdersResponseSerializer() {
    super(KucoinListOrdersResponse.class);
  }
  
  @Override
  public void serialize(KucoinListOrdersResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
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
