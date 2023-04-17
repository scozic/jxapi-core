package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinPlaceAnOrderRequest;
import com.scz.jxapi.util.EncodingUtil;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo.KucoinPlaceAnOrderRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinPlaceAnOrderRequest
 */
public class KucoinPlaceAnOrderRequestSerializer extends StdSerializer<KucoinPlaceAnOrderRequest> {
  public KucoinPlaceAnOrderRequestSerializer() {
    super(KucoinPlaceAnOrderRequest.class);
  }
  
  @Override
  public void serialize(KucoinPlaceAnOrderRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getClientOid() != null){
      gen.writeStringField("clientOid", String.valueOf(value.getClientOid()));
    }
    if (value.getSide() != null){
      gen.writeStringField("side", String.valueOf(value.getSide()));
    }
    if (value.getSymbol() != null){
      gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    }
    if (value.getType() != null){
      gen.writeStringField("type", String.valueOf(value.getType()));
    }
    if (value.getLeverage() != null){
      gen.writeStringField("leverage", EncodingUtil.bigDecimalToString(value.getLeverage()));
    }
    if (value.getRemark() != null){
      gen.writeStringField("remark", String.valueOf(value.getRemark()));
    }
    if (value.getStop() != null){
      gen.writeStringField("stop", String.valueOf(value.getStop()));
    }
    if (value.getStopPriceType() != null){
      gen.writeStringField("stopPriceType", String.valueOf(value.getStopPriceType()));
    }
    if (value.getStopPrice() != null){
      gen.writeStringField("stopPrice", EncodingUtil.bigDecimalToString(value.getStopPrice()));
    }
    if (value.isReduceOnly() != null){
      gen.writeBooleanField("reduceOnly", value.isReduceOnly());
    }
    if (value.isCloseOrder() != null){
      gen.writeBooleanField("closeOrder", value.isCloseOrder());
    }
    if (value.isForceHold() != null){
      gen.writeBooleanField("forceHold", value.isForceHold());
    }
    if (value.getPrice() != null){
      gen.writeStringField("price", EncodingUtil.bigDecimalToString(value.getPrice()));
    }
    if (value.getSize() != null){
      gen.writeNumberField("size", value.getSize());
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
    gen.writeEndObject();
  }
}
