package com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinApplyConnectTokenPrivateRequest
 */
public class KucoinApplyConnectTokenPrivateRequestSerializer extends StdSerializer<KucoinApplyConnectTokenPrivateRequest> {
  public KucoinApplyConnectTokenPrivateRequestSerializer() {
    super(KucoinApplyConnectTokenPrivateRequest.class);
  }
  
  @Override
  public void serialize(KucoinApplyConnectTokenPrivateRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeEndObject();
  }
}
