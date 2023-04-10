package com.scz.jcex.exchanges.kucoin.gen.futurestrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinStopOrderLifecycleEventRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinStopOrderLifecycleEventRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinStopOrderLifecycleEventRequest
 */
public class KucoinStopOrderLifecycleEventRequestSerializer extends StdSerializer<KucoinStopOrderLifecycleEventRequest> {
  public KucoinStopOrderLifecycleEventRequestSerializer() {
    super(KucoinStopOrderLifecycleEventRequest.class);
  }
  
  @Override
  public void serialize(KucoinStopOrderLifecycleEventRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeEndObject();
  }
}
