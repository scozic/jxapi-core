package com.scz.jxapi.exchanges.demo.gen.marketdata.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 * @see DemoExchangeMarketDataTickerStreamRequest
 */
public class DemoExchangeMarketDataTickerStreamRequestSerializer extends StdSerializer<DemoExchangeMarketDataTickerStreamRequest> {
  public DemoExchangeMarketDataTickerStreamRequestSerializer() {
    super(DemoExchangeMarketDataTickerStreamRequest.class);
  }
  
  @Override
  public void serialize(DemoExchangeMarketDataTickerStreamRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getSymbol() != null){
      gen.writeStringField("symbol", String.valueOf(value.getSymbol()));
    }
    gen.writeEndObject();
  }
}
