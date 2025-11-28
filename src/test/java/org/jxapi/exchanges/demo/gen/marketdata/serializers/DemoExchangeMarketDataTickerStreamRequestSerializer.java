package org.jxapi.exchanges.demo.gen.marketdata.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest;

/**
 * Jackson JSON Serializer for org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest
 * @see DemoExchangeMarketDataTickerStreamRequest
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class DemoExchangeMarketDataTickerStreamRequestSerializer extends StdSerializer<DemoExchangeMarketDataTickerStreamRequest> {
  /**
   * Constructor
   */
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
