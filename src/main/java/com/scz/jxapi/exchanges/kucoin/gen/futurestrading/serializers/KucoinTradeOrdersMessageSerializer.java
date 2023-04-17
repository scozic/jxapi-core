package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinTradeOrdersMessage;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinTradeOrdersMessage
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinTradeOrdersMessage
 */
public class KucoinTradeOrdersMessageSerializer extends StdSerializer<KucoinTradeOrdersMessage> {
  public KucoinTradeOrdersMessageSerializer() {
    super(KucoinTradeOrdersMessage.class);
  }
  
  @Override
  public void serialize(KucoinTradeOrdersMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getType() != null){
      gen.writeStringField("type", String.valueOf(value.getType()));
    }
    if (value.getSubject() != null){
      gen.writeStringField("subject", String.valueOf(value.getSubject()));
    }
    if (value.getTopic() != null){
      gen.writeStringField("topic", String.valueOf(value.getTopic()));
    }
    if (value.getChannelType() != null){
      gen.writeStringField("channelType", String.valueOf(value.getChannelType()));
    }
    if (value.getData() != null){
      gen.writeObjectField("data", value.getData());
    }
    gen.writeEndObject();
  }
}
