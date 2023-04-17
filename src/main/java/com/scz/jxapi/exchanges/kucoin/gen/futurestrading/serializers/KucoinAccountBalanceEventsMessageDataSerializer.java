package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinAccountBalanceEventsMessageData;
import com.scz.jxapi.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinAccountBalanceEventsMessageData
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinAccountBalanceEventsMessageData
 */
public class KucoinAccountBalanceEventsMessageDataSerializer extends StdSerializer<KucoinAccountBalanceEventsMessageData> {
  public KucoinAccountBalanceEventsMessageDataSerializer() {
    super(KucoinAccountBalanceEventsMessageData.class);
  }
  
  @Override
  public void serialize(KucoinAccountBalanceEventsMessageData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getOrderMargin() != null){
      gen.writeStringField("orderMargin", EncodingUtil.bigDecimalToString(value.getOrderMargin()));
    }
    if (value.getWithdrawHold() != null){
      gen.writeStringField("withdrawHold", EncodingUtil.bigDecimalToString(value.getWithdrawHold()));
    }
    if (value.getAvailableBalance() != null){
      gen.writeStringField("availableBalance", EncodingUtil.bigDecimalToString(value.getAvailableBalance()));
    }
    if (value.getHoldBalance() != null){
      gen.writeStringField("holdBalance", EncodingUtil.bigDecimalToString(value.getHoldBalance()));
    }
    if (value.getCurrency() != null){
      gen.writeStringField("currency", String.valueOf(value.getCurrency()));
    }
    if (value.getTimestamp() != null){
      gen.writeNumberField("timestamp", value.getTimestamp());
    }
    gen.writeEndObject();
  }
}
