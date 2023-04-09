package com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersResponseDataItems;
import com.scz.jcex.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersResponseDataItems
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinListOrdersResponseDataItems
 */
public class KucoinListOrdersResponseDataItemsSerializer extends StdSerializer<KucoinListOrdersResponseDataItems> {
  public KucoinListOrdersResponseDataItemsSerializer() {
    super(KucoinListOrdersResponseDataItems.class);
  }
  
  @Override
  public void serialize(KucoinListOrdersResponseDataItems value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    gen.writeStringField("id", String.valueOf(value.getId()));
    gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    gen.writeStringField("opType", String.valueOf(value.getOpType()));
    gen.writeStringField("type", String.valueOf(value.getType()));
    gen.writeStringField("side", String.valueOf(value.getSide()));
    gen.writeStringField("price", EncodingUtil.bigDecimalToString(value.getPrice()));
    gen.writeStringField("size", EncodingUtil.bigDecimalToString(value.getSize()));
    gen.writeStringField("funds", EncodingUtil.bigDecimalToString(value.getFunds()));
    gen.writeStringField("dealFunds", EncodingUtil.bigDecimalToString(value.getDealFunds()));
    gen.writeStringField("dealSize", EncodingUtil.bigDecimalToString(value.getDealSize()));
    gen.writeStringField("fee", EncodingUtil.bigDecimalToString(value.getFee()));
    gen.writeStringField("feeCurrency", String.valueOf(value.getFeeCurrency()));
    gen.writeStringField("stp", String.valueOf(value.getStp()));
    gen.writeStringField("stop", String.valueOf(value.getStop()));
    gen.writeBooleanField("stopTriggered", value.isStopTriggered());
    gen.writeStringField("stopPrice", EncodingUtil.bigDecimalToString(value.getStopPrice()));
    gen.writeStringField("timeInForce", String.valueOf(value.getTimeInForce()));
    gen.writeBooleanField("postOnly", value.isPostOnly());
    gen.writeBooleanField("hidden", value.isHidden());
    gen.writeBooleanField("iceberg", value.isIceberg());
    gen.writeStringField("visibleSize", EncodingUtil.bigDecimalToString(value.getVisibleSize()));
    gen.writeStringField("cancelAfter", EncodingUtil.bigDecimalToString(value.getCancelAfter()));
    gen.writeStringField("channel", String.valueOf(value.getChannel()));
    gen.writeStringField("clientOid", String.valueOf(value.getClientOid()));
    gen.writeStringField("remark", String.valueOf(value.getRemark()));
    gen.writeStringField("tags", String.valueOf(value.getTags()));
    gen.writeBooleanField("isActive", value.isIsActive());
    gen.writeBooleanField("cancelExist", value.isCancelExist());
    gen.writeNumberField("createdAt", value.getCreatedAt());
    gen.writeStringField("tradeType", String.valueOf(value.getTradeType()));
    gen.writeEndObject();
  }
}
