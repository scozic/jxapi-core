package com.scz.jxapi.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinPrivateOrderChangeV2Request;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinPrivateOrderChangeV2Request
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinPrivateOrderChangeV2Request
 */
public class KucoinPrivateOrderChangeV2RequestSerializer extends StdSerializer<KucoinPrivateOrderChangeV2Request> {
  public KucoinPrivateOrderChangeV2RequestSerializer() {
    super(KucoinPrivateOrderChangeV2Request.class);
  }
  
  @Override
  public void serialize(KucoinPrivateOrderChangeV2Request value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeEndObject();
  }
}
