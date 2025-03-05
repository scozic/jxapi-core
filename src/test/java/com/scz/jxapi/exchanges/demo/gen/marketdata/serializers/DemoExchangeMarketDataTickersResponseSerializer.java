package com.scz.jxapi.exchanges.demo.gen.marketdata.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponse;
import javax.annotation.processing.Generated;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponse
 * @see DemoExchangeMarketDataTickersResponse
 */
@Generated("com.scz.jxapi.generator.java.exchange.api.pojo.JsonPojoSerializerGenerator")
public class DemoExchangeMarketDataTickersResponseSerializer extends StdSerializer<DemoExchangeMarketDataTickersResponse> {
  public DemoExchangeMarketDataTickersResponseSerializer() {
    super(DemoExchangeMarketDataTickersResponse.class);
  }
  
  @Override
  public void serialize(DemoExchangeMarketDataTickersResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
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
