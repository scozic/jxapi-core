package com.scz.jxapi.exchanges.demo.gen.marketdata.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponsePayload;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponsePayload
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see DemoExchangeMarketDataExchangeInfoResponsePayload
 */
public class DemoExchangeMarketDataExchangeInfoResponsePayloadSerializer extends StdSerializer<DemoExchangeMarketDataExchangeInfoResponsePayload> {
  public DemoExchangeMarketDataExchangeInfoResponsePayloadSerializer() {
    super(DemoExchangeMarketDataExchangeInfoResponsePayload.class);
  }
  
  @Override
  public void serialize(DemoExchangeMarketDataExchangeInfoResponsePayload value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getSymbol() != null){
      gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    }
    if (value.getMinOrderSize() != null){
      gen.writeStringField("minOrderSize", EncodingUtil.bigDecimalToString(value.getMinOrderSize()));
    }
    if (value.getOrderTickSize() != null){
      gen.writeStringField("orderTickSize", EncodingUtil.bigDecimalToString(value.getOrderTickSize()));
    }
    gen.writeEndObject();
  }
}
