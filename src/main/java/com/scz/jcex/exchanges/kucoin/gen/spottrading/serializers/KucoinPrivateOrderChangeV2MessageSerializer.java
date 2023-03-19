package com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinPrivateOrderChangeV2Message;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinPrivateOrderChangeV2Message
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinPrivateOrderChangeV2Message
 */
public class KucoinPrivateOrderChangeV2MessageSerializer extends StdSerializer<KucoinPrivateOrderChangeV2Message> {
  public KucoinPrivateOrderChangeV2MessageSerializer() {
    super(KucoinPrivateOrderChangeV2Message.class);
  }
  
  @Override
  public void serialize(KucoinPrivateOrderChangeV2Message value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("type", String.valueOf(value.getType()));
    gen.writeNumberField("topic", value.getTopic());
    gen.writeStringField("subject", String.valueOf(value.getSubject()));
    gen.writeObjectField("data", value.getData());
    gen.writeEndObject();
  }
}
