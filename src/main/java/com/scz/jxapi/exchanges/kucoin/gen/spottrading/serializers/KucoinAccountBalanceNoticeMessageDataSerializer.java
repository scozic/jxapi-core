package com.scz.jxapi.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinAccountBalanceNoticeMessageData;
import com.scz.jxapi.util.EncodingUtil;

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
    if (value.getTotal() != null){
      gen.writeStringField("total", EncodingUtil.bigDecimalToString(value.getTotal()));
    }
    if (value.getAvailable() != null){
      gen.writeStringField("available", EncodingUtil.bigDecimalToString(value.getAvailable()));
    }
    if (value.getAvailableChange() != null){
      gen.writeStringField("availableChange", EncodingUtil.bigDecimalToString(value.getAvailableChange()));
    }
    if (value.getCurrency() != null){
      gen.writeStringField("currency", String.valueOf(value.getCurrency()));
    }
    if (value.getHold() != null){
      gen.writeStringField("hold", EncodingUtil.bigDecimalToString(value.getHold()));
    }
    if (value.getHoldChange() != null){
      gen.writeStringField("holdChange", EncodingUtil.bigDecimalToString(value.getHoldChange()));
    }
    if (value.getRelationEvent() != null){
      gen.writeStringField("relationEvent", String.valueOf(value.getRelationEvent()));
    }
    if (value.getRelationContext() != null){
      gen.writeObjectField("relationContext", value.getRelationContext());
    }
    if (value.getTime() != null){
      gen.writeNumberField("time", value.getTime());
    }
    gen.writeEndObject();
  }
}
