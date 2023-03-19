package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicResponseDataInstanceServers;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicResponseDataInstanceServers
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinApplyConnectTokenPublicResponseDataInstanceServers
 */
public class KucoinApplyConnectTokenPublicResponseDataInstanceServersSerializer extends StdSerializer<KucoinApplyConnectTokenPublicResponseDataInstanceServers> {
  public KucoinApplyConnectTokenPublicResponseDataInstanceServersSerializer() {
    super(KucoinApplyConnectTokenPublicResponseDataInstanceServers.class);
  }
  
  @Override
  public void serialize(KucoinApplyConnectTokenPublicResponseDataInstanceServers value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("endpoint", String.valueOf(value.getEndpoint()));
    gen.writeStringField("protocol", String.valueOf(value.getProtocol()));
    gen.writeBooleanField("encrypt", value.isEncrypt());
    gen.writeNumberField("pingInterval", value.getPingInterval());
    gen.writeNumberField("pingTimeout", value.getPingTimeout());
    gen.writeEndObject();
  }
}
