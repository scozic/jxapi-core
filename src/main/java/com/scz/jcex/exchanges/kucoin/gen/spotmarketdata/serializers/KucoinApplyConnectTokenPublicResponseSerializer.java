package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicResponse;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicResponse
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinApplyConnectTokenPublicResponse
 */
public class KucoinApplyConnectTokenPublicResponseSerializer extends StdSerializer<KucoinApplyConnectTokenPublicResponse> {
  public KucoinApplyConnectTokenPublicResponseSerializer() {
    super(KucoinApplyConnectTokenPublicResponse.class);
  }
  
  @Override
  public void serialize(KucoinApplyConnectTokenPublicResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
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
