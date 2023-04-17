package com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinApplyConnectTokenPublicRequest
 */
public class KucoinApplyConnectTokenPublicRequestSerializer extends StdSerializer<KucoinApplyConnectTokenPublicRequest> {
  public KucoinApplyConnectTokenPublicRequestSerializer() {
    super(KucoinApplyConnectTokenPublicRequest.class);
  }
  
  @Override
  public void serialize(KucoinApplyConnectTokenPublicRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeEndObject();
  }
}
