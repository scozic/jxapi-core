package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicResponseData;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicResponseData
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinApplyConnectTokenPublicResponseData
 */
public class KucoinApplyConnectTokenPublicResponseDataSerializer extends StdSerializer<KucoinApplyConnectTokenPublicResponseData> {
  public KucoinApplyConnectTokenPublicResponseDataSerializer() {
    super(KucoinApplyConnectTokenPublicResponseData.class);
  }
  
  @Override
  public void serialize(KucoinApplyConnectTokenPublicResponseData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
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
