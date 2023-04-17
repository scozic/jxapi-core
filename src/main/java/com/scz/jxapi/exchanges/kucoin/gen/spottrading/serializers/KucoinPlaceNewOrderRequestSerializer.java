package com.scz.jxapi.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinPlaceNewOrderRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinPlaceNewOrderRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see KucoinPlaceNewOrderRequest
 */
public class KucoinPlaceNewOrderRequestSerializer extends StdSerializer<KucoinPlaceNewOrderRequest> {
  public KucoinPlaceNewOrderRequestSerializer() {
    super(KucoinPlaceNewOrderRequest.class);
  }
  
  @Override
  public void serialize(KucoinPlaceNewOrderRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
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
    if (value.getRemark() != null){
      gen.writeStringField("remark", String.valueOf(value.getRemark()));
    }
    if (value.getStp() != null){
      gen.writeStringField("stp", String.valueOf(value.getStp()));
    }
    if (value.getTradeType() != null){
      gen.writeStringField("tradeType", String.valueOf(value.getTradeType()));
    }
    if (value.getPrice() != null){
      gen.writeStringField("price", String.valueOf(value.getPrice()));
    }
    if (value.getSize() != null){
      gen.writeStringField("size", String.valueOf(value.getSize()));
    }
    if (value.getTimeInForce() != null){
      gen.writeStringField("timeInForce", String.valueOf(value.getTimeInForce()));
    }
    if (value.getCancelAfter() != null){
      gen.writeNumberField("cancelAfter", value.getCancelAfter());
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
      gen.writeStringField("visibleSize", String.valueOf(value.getVisibleSize()));
    }
    if (value.getFunds() != null){
      gen.writeStringField("funds", String.valueOf(value.getFunds()));
    }
    gen.writeEndObject();
  }
}
