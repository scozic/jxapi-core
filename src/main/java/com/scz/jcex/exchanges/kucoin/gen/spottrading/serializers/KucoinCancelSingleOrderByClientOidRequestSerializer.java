package com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelSingleOrderByClientOidRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelSingleOrderByClientOidRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinCancelSingleOrderByClientOidRequest
 */
public class KucoinCancelSingleOrderByClientOidRequestSerializer extends StdSerializer<KucoinCancelSingleOrderByClientOidRequest> {
  public KucoinCancelSingleOrderByClientOidRequestSerializer() {
    super(KucoinCancelSingleOrderByClientOidRequest.class);
  }
  
  @Override
  public void serialize(KucoinCancelSingleOrderByClientOidRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getClientOid() != null){
      gen.writeStringField("clientOid", String.valueOf(value.getClientOid()));
    }
    gen.writeEndObject();
  }
}
