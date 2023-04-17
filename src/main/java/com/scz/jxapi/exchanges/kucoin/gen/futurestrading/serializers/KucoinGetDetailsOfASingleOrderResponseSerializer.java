package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetDetailsOfASingleOrderResponse;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetDetailsOfASingleOrderResponse
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinGetDetailsOfASingleOrderResponse
 */
public class KucoinGetDetailsOfASingleOrderResponseSerializer extends StdSerializer<KucoinGetDetailsOfASingleOrderResponse> {
  public KucoinGetDetailsOfASingleOrderResponseSerializer() {
    super(KucoinGetDetailsOfASingleOrderResponse.class);
  }
  
  @Override
  public void serialize(KucoinGetDetailsOfASingleOrderResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
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
