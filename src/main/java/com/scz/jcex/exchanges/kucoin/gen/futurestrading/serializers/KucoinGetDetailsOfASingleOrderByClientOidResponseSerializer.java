package com.scz.jcex.exchanges.kucoin.gen.futurestrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetDetailsOfASingleOrderByClientOidResponse;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetDetailsOfASingleOrderByClientOidResponse
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinGetDetailsOfASingleOrderByClientOidResponse
 */
public class KucoinGetDetailsOfASingleOrderByClientOidResponseSerializer extends StdSerializer<KucoinGetDetailsOfASingleOrderByClientOidResponse> {
  public KucoinGetDetailsOfASingleOrderByClientOidResponseSerializer() {
    super(KucoinGetDetailsOfASingleOrderByClientOidResponse.class);
  }
  
  @Override
  public void serialize(KucoinGetDetailsOfASingleOrderByClientOidResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
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
