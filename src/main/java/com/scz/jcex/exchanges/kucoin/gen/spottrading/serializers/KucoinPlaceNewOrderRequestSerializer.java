package com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinPlaceNewOrderRequest;
import java.io.IOException;

/**
 * Jackson JSON Serializer for com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinPlaceNewOrderRequest
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
    gen.writeStringField("clientOid", String.valueOf(value.getClientOid()));
    gen.writeStringField("side", String.valueOf(value.getSide()));
    gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    gen.writeStringField("type", String.valueOf(value.getType()));
    gen.writeStringField("remark", String.valueOf(value.getRemark()));
    gen.writeStringField("stp", String.valueOf(value.getStp()));
    gen.writeStringField("tradeType", String.valueOf(value.getTradeType()));
    gen.writeStringField("price", String.valueOf(value.getPrice()));
    gen.writeStringField("size", String.valueOf(value.getSize()));
    gen.writeStringField("timeInForce", String.valueOf(value.getTimeInForce()));
    gen.writeNumberField("cancelAfter", value.getCancelAfter());
    gen.writeBooleanField("postOnly", value.isPostOnly());
    gen.writeBooleanField("hidden", value.isHidden());
    gen.writeBooleanField("iceberg", value.isIceberg());
    gen.writeStringField("visibleSize", String.valueOf(value.getVisibleSize()));
    gen.writeStringField("funds", String.valueOf(value.getFunds()));
    gen.writeEndObject();
  }
}
