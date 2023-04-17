package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetListOfOrdersCompletedIn24hResponse;

import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetListOfOrdersCompletedIn24hResponse
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinGetListOfOrdersCompletedIn24hResponse
 */
public class KucoinGetListOfOrdersCompletedIn24hResponseSerializer extends StdSerializer<KucoinGetListOfOrdersCompletedIn24hResponse> {
  public KucoinGetListOfOrdersCompletedIn24hResponseSerializer() {
    super(KucoinGetListOfOrdersCompletedIn24hResponse.class);
  }
  
  @Override
  public void serialize(KucoinGetListOfOrdersCompletedIn24hResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
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
