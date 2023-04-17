package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetDetailsOfASingleOrderByClientOidResponseData;
import com.scz.jxapi.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetDetailsOfASingleOrderByClientOidResponseData
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinGetDetailsOfASingleOrderByClientOidResponseData
 */
public class KucoinGetDetailsOfASingleOrderByClientOidResponseDataSerializer extends StdSerializer<KucoinGetDetailsOfASingleOrderByClientOidResponseData> {
  public KucoinGetDetailsOfASingleOrderByClientOidResponseDataSerializer() {
    super(KucoinGetDetailsOfASingleOrderByClientOidResponseData.class);
  }
  
  @Override
  public void serialize(KucoinGetDetailsOfASingleOrderByClientOidResponseData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getId() != null){
      gen.writeStringField("id", String.valueOf(value.getId()));
    }
    if (value.getSymbol() != null){
      gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
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
    if (value.getLeverage() != null){
      gen.writeStringField("leverage", EncodingUtil.bigDecimalToString(value.getLeverage()));
    }
    if (value.isForceHold() != null){
      gen.writeBooleanField("forceHold", value.isForceHold());
    }
    if (value.isCloseOrder() != null){
      gen.writeBooleanField("closeOrder", value.isCloseOrder());
    }
    if (value.getVisibleSize() != null){
      gen.writeStringField("visibleSize", EncodingUtil.bigDecimalToString(value.getVisibleSize()));
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
    if (value.getUpdatedAt() != null){
      gen.writeNumberField("updatedAt", value.getUpdatedAt());
    }
    if (value.getEndAt() != null){
      gen.writeNumberField("endAt", value.getEndAt());
    }
    if (value.getOrderTime() != null){
      gen.writeNumberField("orderTime", value.getOrderTime());
    }
    if (value.getSettleCurrency() != null){
      gen.writeStringField("settleCurrency", String.valueOf(value.getSettleCurrency()));
    }
    if (value.getStatus() != null){
      gen.writeStringField("status", String.valueOf(value.getStatus()));
    }
    if (value.getTradeType() != null){
      gen.writeStringField("tradeType", String.valueOf(value.getTradeType()));
    }
    if (value.getFilledValue() != null){
      gen.writeStringField("filledValue", EncodingUtil.bigDecimalToString(value.getFilledValue()));
    }
    if (value.getFilledSize() != null){
      gen.writeStringField("filledSize", EncodingUtil.bigDecimalToString(value.getFilledSize()));
    }
    if (value.isReduceOnly() != null){
      gen.writeBooleanField("reduceOnly", value.isReduceOnly());
    }
    gen.writeEndObject();
  }
}
