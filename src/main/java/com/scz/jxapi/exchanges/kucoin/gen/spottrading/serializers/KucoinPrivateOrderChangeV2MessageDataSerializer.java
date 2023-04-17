package com.scz.jxapi.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinPrivateOrderChangeV2MessageData;
import com.scz.jxapi.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinPrivateOrderChangeV2MessageData
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinPrivateOrderChangeV2MessageData
 */
public class KucoinPrivateOrderChangeV2MessageDataSerializer extends StdSerializer<KucoinPrivateOrderChangeV2MessageData> {
  public KucoinPrivateOrderChangeV2MessageDataSerializer() {
    super(KucoinPrivateOrderChangeV2MessageData.class);
  }
  
  @Override
  public void serialize(KucoinPrivateOrderChangeV2MessageData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getSymbol() != null){
      gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    }
    if (value.getOrderType() != null){
      gen.writeStringField("orderType", String.valueOf(value.getOrderType()));
    }
    if (value.getSide() != null){
      gen.writeStringField("side", String.valueOf(value.getSide()));
    }
    if (value.getOrderId() != null){
      gen.writeStringField("orderId", String.valueOf(value.getOrderId()));
    }
    if (value.getLiquidity() != null){
      gen.writeStringField("liquidity", String.valueOf(value.getLiquidity()));
    }
    if (value.getType() != null){
      gen.writeStringField("type", String.valueOf(value.getType()));
    }
    if (value.getOrderTime() != null){
      gen.writeNumberField("orderTime", value.getOrderTime());
    }
    if (value.getSize() != null){
      gen.writeStringField("size", EncodingUtil.bigDecimalToString(value.getSize()));
    }
    if (value.getFilledSize() != null){
      gen.writeStringField("filledSize", EncodingUtil.bigDecimalToString(value.getFilledSize()));
    }
    if (value.getPrice() != null){
      gen.writeStringField("price", EncodingUtil.bigDecimalToString(value.getPrice()));
    }
    if (value.getMatchPrice() != null){
      gen.writeStringField("matchPrice", EncodingUtil.bigDecimalToString(value.getMatchPrice()));
    }
    if (value.getMatchSize() != null){
      gen.writeStringField("matchSize", EncodingUtil.bigDecimalToString(value.getMatchSize()));
    }
    if (value.getTradeId() != null){
      gen.writeStringField("tradeId", String.valueOf(value.getTradeId()));
    }
    if (value.getClientOid() != null){
      gen.writeStringField("clientOid", String.valueOf(value.getClientOid()));
    }
    if (value.getRemainSize() != null){
      gen.writeStringField("remainSize", EncodingUtil.bigDecimalToString(value.getRemainSize()));
    }
    if (value.getStatus() != null){
      gen.writeStringField("status", String.valueOf(value.getStatus()));
    }
    if (value.getCanceledSize() != null){
      gen.writeStringField("canceledSize", EncodingUtil.bigDecimalToString(value.getCanceledSize()));
    }
    if (value.getCanceledFunds() != null){
      gen.writeStringField("canceledFunds", EncodingUtil.bigDecimalToString(value.getCanceledFunds()));
    }
    if (value.getOriginSize() != null){
      gen.writeStringField("originSize", EncodingUtil.bigDecimalToString(value.getOriginSize()));
    }
    if (value.getOriginFunds() != null){
      gen.writeStringField("originFunds", EncodingUtil.bigDecimalToString(value.getOriginFunds()));
    }
    if (value.getTs() != null){
      gen.writeNumberField("ts", value.getTs());
    }
    gen.writeEndObject();
  }
}
