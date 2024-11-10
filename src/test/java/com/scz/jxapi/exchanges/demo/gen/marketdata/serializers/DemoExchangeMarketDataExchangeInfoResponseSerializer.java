package com.scz.jxapi.exchanges.demo.gen.marketdata.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponse;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponse
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see DemoExchangeMarketDataExchangeInfoResponse
 */
public class DemoExchangeMarketDataExchangeInfoResponseSerializer extends StdSerializer<DemoExchangeMarketDataExchangeInfoResponse> {
  public DemoExchangeMarketDataExchangeInfoResponseSerializer() {
    super(DemoExchangeMarketDataExchangeInfoResponse.class);
  }
  
  @Override
  public void serialize(DemoExchangeMarketDataExchangeInfoResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getResponseCode() != null){
      gen.writeNumberField("responseCode", value.getResponseCode());
    }
    if (value.getPayload() != null){
      gen.writeObjectField("payload", value.getPayload());
    }
    gen.writeEndObject();
  }
}
