package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetDetailsOfASingleOrderByClientOidRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetDetailsOfASingleOrderByClientOidRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinGetDetailsOfASingleOrderByClientOidRequest
 */
public class KucoinGetDetailsOfASingleOrderByClientOidRequestSerializer extends StdSerializer<KucoinGetDetailsOfASingleOrderByClientOidRequest> {
  public KucoinGetDetailsOfASingleOrderByClientOidRequestSerializer() {
    super(KucoinGetDetailsOfASingleOrderByClientOidRequest.class);
  }
  
  @Override
  public void serialize(KucoinGetDetailsOfASingleOrderByClientOidRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getClientOid() != null){
      gen.writeStringField("clientOid", String.valueOf(value.getClientOid()));
    }
    gen.writeEndObject();
  }
}
