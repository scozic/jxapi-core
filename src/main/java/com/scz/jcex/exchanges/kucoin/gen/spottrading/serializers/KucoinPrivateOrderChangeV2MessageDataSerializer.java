package com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinPrivateOrderChangeV2MessageData;
import com.scz.jcex.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinPrivateOrderChangeV2MessageData
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
    gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    gen.writeStringField("orderType", String.valueOf(value.getOrderType()));
    gen.writeStringField("side", String.valueOf(value.getSide()));
    gen.writeStringField("orderId", String.valueOf(value.getOrderId()));
    gen.writeStringField("liquidity", String.valueOf(value.getLiquidity()));
    gen.writeStringField("type", String.valueOf(value.getType()));
    gen.writeNumberField("orderTime", value.getOrderTime());
    gen.writeStringField("size", EncodingUtil.bigDecimalToString(value.getSize()));
    gen.writeStringField("filledSize", EncodingUtil.bigDecimalToString(value.getFilledSize()));
    gen.writeStringField("price", EncodingUtil.bigDecimalToString(value.getPrice()));
    gen.writeStringField("matchPrice", EncodingUtil.bigDecimalToString(value.getMatchPrice()));
    gen.writeStringField("matchSize", EncodingUtil.bigDecimalToString(value.getMatchSize()));
    gen.writeStringField("tradeId", String.valueOf(value.getTradeId()));
    gen.writeStringField("clientOid", String.valueOf(value.getClientOid()));
    gen.writeStringField("remainSize", EncodingUtil.bigDecimalToString(value.getRemainSize()));
    gen.writeStringField("status", String.valueOf(value.getStatus()));
    gen.writeStringField("canceledSize", EncodingUtil.bigDecimalToString(value.getCanceledSize()));
    gen.writeStringField("canceledFunds", EncodingUtil.bigDecimalToString(value.getCanceledFunds()));
    gen.writeStringField("originSize", EncodingUtil.bigDecimalToString(value.getOriginSize()));
    gen.writeStringField("originFunds", EncodingUtil.bigDecimalToString(value.getOriginFunds()));
    gen.writeNumberField("ts", value.getTs());
    gen.writeEndObject();
  }
}
