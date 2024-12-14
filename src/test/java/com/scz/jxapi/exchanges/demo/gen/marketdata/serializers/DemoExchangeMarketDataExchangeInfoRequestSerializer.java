package com.scz.jxapi.exchanges.demo.gen.marketdata.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoRequest;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see DemoExchangeMarketDataExchangeInfoRequest
 */
public class DemoExchangeMarketDataExchangeInfoRequestSerializer extends StdSerializer<DemoExchangeMarketDataExchangeInfoRequest> {
  public DemoExchangeMarketDataExchangeInfoRequestSerializer() {
    super(DemoExchangeMarketDataExchangeInfoRequest.class);
  }
  
  @Override
  public void serialize(DemoExchangeMarketDataExchangeInfoRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getSymbols() != null) {
      gen.writeObjectField("symbols", value.getSymbols());
    }
    gen.writeEndObject();
  }
}
