package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinAccountBalanceEventsRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinAccountBalanceEventsRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinAccountBalanceEventsRequest
 */
public class KucoinAccountBalanceEventsRequestSerializer extends StdSerializer<KucoinAccountBalanceEventsRequest> {
  public KucoinAccountBalanceEventsRequestSerializer() {
    super(KucoinAccountBalanceEventsRequest.class);
  }
  
  @Override
  public void serialize(KucoinAccountBalanceEventsRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeEndObject();
  }
}
