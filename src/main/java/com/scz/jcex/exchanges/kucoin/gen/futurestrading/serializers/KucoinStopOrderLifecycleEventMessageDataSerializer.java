package com.scz.jcex.exchanges.kucoin.gen.futurestrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinStopOrderLifecycleEventMessageData;
import com.scz.jcex.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinStopOrderLifecycleEventMessageData
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinStopOrderLifecycleEventMessageData
 */
public class KucoinStopOrderLifecycleEventMessageDataSerializer extends StdSerializer<KucoinStopOrderLifecycleEventMessageData> {
  public KucoinStopOrderLifecycleEventMessageDataSerializer() {
    super(KucoinStopOrderLifecycleEventMessageData.class);
  }
  
  @Override
  public void serialize(KucoinStopOrderLifecycleEventMessageData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getOrderId() != null){
      gen.writeStringField("orderId", String.valueOf(value.getOrderId()));
    }
    if (value.getSymbol() != null){
      gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    }
    if (value.getType() != null){
      gen.writeStringField("type", String.valueOf(value.getType()));
    }
    if (value.getStatus() != null){
      gen.writeStringField("status", String.valueOf(value.getStatus()));
    }
    if (value.getOrderType() != null){
      gen.writeStringField("orderType", String.valueOf(value.getOrderType()));
    }
    if (value.getSide() != null){
      gen.writeStringField("side", String.valueOf(value.getSide()));
    }
    if (value.getOrderPrice() != null){
      gen.writeStringField("orderPrice", EncodingUtil.bigDecimalToString(value.getOrderPrice()));
    }
    if (value.getSize() != null){
      gen.writeStringField("size", EncodingUtil.bigDecimalToString(value.getSize()));
    }
    if (value.getStop() != null){
      gen.writeStringField("stop", String.valueOf(value.getStop()));
    }
    if (value.getStopPrice() != null){
      gen.writeStringField("stopPrice", EncodingUtil.bigDecimalToString(value.getStopPrice()));
    }
    if (value.getStopPriceType() != null){
      gen.writeStringField("stopPriceType", String.valueOf(value.getStopPriceType()));
    }
    if (value.isTriggerSuccess() != null){
      gen.writeBooleanField("triggerSuccess", value.isTriggerSuccess());
    }
    if (value.getError() != null){
      gen.writeStringField("error", String.valueOf(value.getError()));
    }
    if (value.getTs() != null){
      gen.writeNumberField("ts", value.getTs());
    }
    gen.writeEndObject();
  }
}
