package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinStopOrderLifecycleEventMessage;

import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinStopOrderLifecycleEventMessage
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinStopOrderLifecycleEventMessage
 */
public class KucoinStopOrderLifecycleEventMessageSerializer extends StdSerializer<KucoinStopOrderLifecycleEventMessage> {
  public KucoinStopOrderLifecycleEventMessageSerializer() {
    super(KucoinStopOrderLifecycleEventMessage.class);
  }
  
  @Override
  public void serialize(KucoinStopOrderLifecycleEventMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getUserId() != null){
      gen.writeStringField("userId", String.valueOf(value.getUserId()));
    }
    if (value.getTopic() != null){
      gen.writeStringField("topic", String.valueOf(value.getTopic()));
    }
    if (value.getSubject() != null){
      gen.writeStringField("subject", String.valueOf(value.getSubject()));
    }
    if (value.getData() != null){
      gen.writeObjectField("data", value.getData());
    }
    gen.writeEndObject();
  }
}
