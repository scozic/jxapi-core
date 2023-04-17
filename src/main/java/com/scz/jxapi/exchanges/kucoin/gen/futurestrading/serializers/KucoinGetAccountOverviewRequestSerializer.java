package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetAccountOverviewRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetAccountOverviewRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinGetAccountOverviewRequest
 */
public class KucoinGetAccountOverviewRequestSerializer extends StdSerializer<KucoinGetAccountOverviewRequest> {
  public KucoinGetAccountOverviewRequestSerializer() {
    super(KucoinGetAccountOverviewRequest.class);
  }
  
  @Override
  public void serialize(KucoinGetAccountOverviewRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getCurrency() != null){
      gen.writeStringField("currency", String.valueOf(value.getCurrency()));
    }
    gen.writeEndObject();
  }
}
