package com.scz.jxapi.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersResponseDataItems;
import com.scz.jxapi.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersResponseDataItems
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
    if (value.getId() != null){
      gen.writeStringField("id", String.valueOf(value.getId()));
    }
    if (value.getSymbol() != null){
      gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    }
    if (value.getOpType() != null){
      gen.writeStringField("opType", String.valueOf(value.getOpType()));
    }
    if (value.getType() != null){
      gen.writeStringField("type", String.valueOf(value.getType()));
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
    if (value.getFunds() != null){
      gen.writeStringField("funds", EncodingUtil.bigDecimalToString(value.getFunds()));
    }
    if (value.getDealFunds() != null){
      gen.writeStringField("dealFunds", EncodingUtil.bigDecimalToString(value.getDealFunds()));
    }
    if (value.getDealSize() != null){
      gen.writeStringField("dealSize", EncodingUtil.bigDecimalToString(value.getDealSize()));
    }
    if (value.getFee() != null){
      gen.writeStringField("fee", EncodingUtil.bigDecimalToString(value.getFee()));
    }
    if (value.getFeeCurrency() != null){
      gen.writeStringField("feeCurrency", String.valueOf(value.getFeeCurrency()));
    }
    if (value.getStp() != null){
      gen.writeStringField("stp", String.valueOf(value.getStp()));
    }
    if (value.getStop() != null){
      gen.writeStringField("stop", String.valueOf(value.getStop()));
    }
    if (value.isStopTriggered() != null){
      gen.writeBooleanField("stopTriggered", value.isStopTriggered());
    }
    if (value.getStopPrice() != null){
      gen.writeStringField("stopPrice", EncodingUtil.bigDecimalToString(value.getStopPrice()));
    }
    if (value.getTimeInForce() != null){
      gen.writeStringField("timeInForce", String.valueOf(value.getTimeInForce()));
    }
    if (value.isPostOnly() != null){
      gen.writeBooleanField("postOnly", value.isPostOnly());
    }
    if (value.isHidden() != null){
      gen.writeBooleanField("hidden", value.isHidden());
    }
    if (value.isIceberg() != null){
      gen.writeBooleanField("iceberg", value.isIceberg());
    }
    if (value.getVisibleSize() != null){
      gen.writeStringField("visibleSize", EncodingUtil.bigDecimalToString(value.getVisibleSize()));
    }
    if (value.getCancelAfter() != null){
      gen.writeStringField("cancelAfter", EncodingUtil.bigDecimalToString(value.getCancelAfter()));
    }
    if (value.getChannel() != null){
      gen.writeStringField("channel", String.valueOf(value.getChannel()));
    }
    if (value.getClientOid() != null){
      gen.writeStringField("clientOid", String.valueOf(value.getClientOid()));
    }
    if (value.getRemark() != null){
      gen.writeStringField("remark", String.valueOf(value.getRemark()));
    }
    if (value.getTags() != null){
      gen.writeStringField("tags", String.valueOf(value.getTags()));
    }
    if (value.isIsActive() != null){
      gen.writeBooleanField("isActive", value.isIsActive());
    }
    if (value.isCancelExist() != null){
      gen.writeBooleanField("cancelExist", value.isCancelExist());
    }
    if (value.getCreatedAt() != null){
      gen.writeNumberField("createdAt", value.getCreatedAt());
    }
    if (value.getTradeType() != null){
      gen.writeStringField("tradeType", String.valueOf(value.getTradeType()));
    }
    gen.writeEndObject();
  }
}
