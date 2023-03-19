package com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateResponseDataInstanceServers;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateResponseDataInstanceServers
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinApplyConnectTokenPrivateResponseDataInstanceServers
 */
public class KucoinApplyConnectTokenPrivateResponseDataInstanceServersSerializer extends StdSerializer<KucoinApplyConnectTokenPrivateResponseDataInstanceServers> {
  public KucoinApplyConnectTokenPrivateResponseDataInstanceServersSerializer() {
    super(KucoinApplyConnectTokenPrivateResponseDataInstanceServers.class);
  }
  
  @Override
  public void serialize(KucoinApplyConnectTokenPrivateResponseDataInstanceServers value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("endpoint", String.valueOf(value.getEndpoint()));
    gen.writeStringField("protocol", String.valueOf(value.getProtocol()));
    gen.writeBooleanField("encrypt", value.isEncrypt());
    gen.writeNumberField("pingInterval", value.getPingInterval());
    gen.writeNumberField("pingTimeout", value.getPingTimeout());
    gen.writeEndObject();
  }
}
