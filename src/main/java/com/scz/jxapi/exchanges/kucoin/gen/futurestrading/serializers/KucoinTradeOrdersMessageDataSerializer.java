package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinTradeOrdersMessageData;
import com.scz.jxapi.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinTradeOrdersMessageData
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinTradeOrdersMessageData
 */
public class KucoinTradeOrdersMessageDataSerializer extends StdSerializer<KucoinTradeOrdersMessageData> {
  public KucoinTradeOrdersMessageDataSerializer() {
    super(KucoinTradeOrdersMessageData.class);
  }
  
  @Override
  public void serialize(KucoinTradeOrdersMessageData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
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
    if (value.getMatchSize() != null){
      gen.writeStringField("matchSize", EncodingUtil.bigDecimalToString(value.getMatchSize()));
    }
    if (value.getMatchPrice() != null){
      gen.writeStringField("matchPrice", EncodingUtil.bigDecimalToString(value.getMatchPrice()));
    }
    if (value.getOrderType() != null){
      gen.writeStringField("orderType", String.valueOf(value.getOrderType()));
    }
    if (value.getSide() != null){
      gen.writeStringField("side", String.valueOf(value.getSide()));
    }
    if (value.getPrice() != null){
      gen.writeStringField("price", EncodingUtil.bigDecimalToString(value.getPrice()));
    }
    if (value.getSize() != null){
      gen.writeStringField("size", EncodingUtil.bigDecimalToString(value.getSize()));
    }
    if (value.getRemainSize() != null){
      gen.writeStringField("remainSize", EncodingUtil.bigDecimalToString(value.getRemainSize()));
    }
    if (value.getFilledSize() != null){
      gen.writeStringField("filledSize", EncodingUtil.bigDecimalToString(value.getFilledSize()));
    }
    if (value.getCanceledSize() != null){
      gen.writeStringField("canceledSize", EncodingUtil.bigDecimalToString(value.getCanceledSize()));
    }
    if (value.getTradeId() != null){
      gen.writeStringField("tradeId", String.valueOf(value.getTradeId()));
    }
    if (value.getClientOId() != null){
      gen.writeStringField("clientOId", String.valueOf(value.getClientOId()));
    }
    if (value.getOrderTime() != null){
      gen.writeNumberField("orderTime", value.getOrderTime());
    }
    if (value.getOldSize() != null){
      gen.writeStringField("oldSize", EncodingUtil.bigDecimalToString(value.getOldSize()));
    }
    if (value.getLiquidity() != null){
      gen.writeStringField("liquidity", String.valueOf(value.getLiquidity()));
    }
    if (value.getTs() != null){
      gen.writeNumberField("ts", value.getTs());
    }
    gen.writeEndObject();
  }
}
