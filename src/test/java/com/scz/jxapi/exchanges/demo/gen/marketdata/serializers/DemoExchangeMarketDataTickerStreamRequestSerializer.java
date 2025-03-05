package com.scz.jxapi.exchanges.demo.gen.marketdata.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest;
import javax.annotation.processing.Generated;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest
 * @see DemoExchangeMarketDataTickerStreamRequest
 */
@Generated("com.scz.jxapi.generator.java.exchange.api.pojo.JsonPojoSerializerGenerator")
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
