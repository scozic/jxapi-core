package com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinAccountBalanceNoticeMessageData;
import com.scz.jcex.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinAccountBalanceNoticeMessageData
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinAccountBalanceNoticeMessageData
 */
public class KucoinAccountBalanceNoticeMessageDataSerializer extends StdSerializer<KucoinAccountBalanceNoticeMessageData> {
  public KucoinAccountBalanceNoticeMessageDataSerializer() {
    super(KucoinAccountBalanceNoticeMessageData.class);
  }
  
  @Override
  public void serialize(KucoinAccountBalanceNoticeMessageData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("total", EncodingUtil.bigDecimalToString(value.getTotal()));
    gen.writeStringField("available", EncodingUtil.bigDecimalToString(value.getAvailable()));
    gen.writeStringField("availableChange", EncodingUtil.bigDecimalToString(value.getAvailableChange()));
    gen.writeStringField("currency", String.valueOf(value.getCurrency()));
    gen.writeStringField("hold", EncodingUtil.bigDecimalToString(value.getHold()));
    gen.writeStringField("holdChange", EncodingUtil.bigDecimalToString(value.getHoldChange()));
    gen.writeStringField("relationEvent", String.valueOf(value.getRelationEvent()));
    gen.writeObjectField("relationContext", value.getRelationContext());
    gen.writeNumberField("time", value.getTime());
    gen.writeEndObject();
  }
}
