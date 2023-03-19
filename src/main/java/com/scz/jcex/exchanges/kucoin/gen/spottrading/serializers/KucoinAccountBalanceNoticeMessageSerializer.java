package com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinAccountBalanceNoticeMessage;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinAccountBalanceNoticeMessage
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinAccountBalanceNoticeMessage
 */
public class KucoinAccountBalanceNoticeMessageSerializer extends StdSerializer<KucoinAccountBalanceNoticeMessage> {
  public KucoinAccountBalanceNoticeMessageSerializer() {
    super(KucoinAccountBalanceNoticeMessage.class);
  }
  
  @Override
  public void serialize(KucoinAccountBalanceNoticeMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("type", String.valueOf(value.getType()));
    gen.writeStringField("topic", String.valueOf(value.getTopic()));
    gen.writeStringField("subject", String.valueOf(value.getSubject()));
    gen.writeObjectField("data", value.getData());
    gen.writeEndObject();
  }
}
