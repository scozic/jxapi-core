package com.scz.jxapi.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateResponseData;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateResponseData
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinApplyConnectTokenPrivateResponseData
 */
public class KucoinApplyConnectTokenPrivateResponseDataSerializer extends StdSerializer<KucoinApplyConnectTokenPrivateResponseData> {
  public KucoinApplyConnectTokenPrivateResponseDataSerializer() {
    super(KucoinApplyConnectTokenPrivateResponseData.class);
  }
  
  @Override
  public void serialize(KucoinApplyConnectTokenPrivateResponseData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getToken() != null){
      gen.writeStringField("token", String.valueOf(value.getToken()));
    }
    if (value.getInstanceServers() != null){
      gen.writeObjectField("instanceServers", value.getInstanceServers());
    }
    gen.writeEndObject();
  }
}
