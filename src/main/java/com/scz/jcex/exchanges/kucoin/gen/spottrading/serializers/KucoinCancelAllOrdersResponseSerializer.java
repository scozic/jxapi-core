package com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelAllOrdersResponse;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelAllOrdersResponse
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinCancelAllOrdersResponse
 */
public class KucoinCancelAllOrdersResponseSerializer extends StdSerializer<KucoinCancelAllOrdersResponse> {
  public KucoinCancelAllOrdersResponseSerializer() {
    super(KucoinCancelAllOrdersResponse.class);
  }
  
  @Override
  public void serialize(KucoinCancelAllOrdersResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("code", String.valueOf(value.getCode()));
    gen.writeStringField("msg", String.valueOf(value.getMsg()));
    gen.writeObjectField("data", value.getData());
    gen.writeEndObject();
  }
}
