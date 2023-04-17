package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinPositionChangeEventsMessage;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinPositionChangeEventsMessage
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinPositionChangeEventsMessage
 */
public class KucoinPositionChangeEventsMessageSerializer extends StdSerializer<KucoinPositionChangeEventsMessage> {
  public KucoinPositionChangeEventsMessageSerializer() {
    super(KucoinPositionChangeEventsMessage.class);
  }
  
  @Override
  public void serialize(KucoinPositionChangeEventsMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getType() != null){
      gen.writeStringField("type", String.valueOf(value.getType()));
    }
    if (value.getUserId() != null){
      gen.writeStringField("userId", String.valueOf(value.getUserId()));
    }
    if (value.getChannelType() != null){
      gen.writeStringField("channelType", String.valueOf(value.getChannelType()));
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
