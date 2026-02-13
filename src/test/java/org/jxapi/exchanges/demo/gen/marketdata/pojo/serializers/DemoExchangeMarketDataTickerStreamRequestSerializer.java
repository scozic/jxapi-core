package org.jxapi.exchanges.demo.gen.marketdata.pojo.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest;
import org.jxapi.netutils.serialization.json.AbstractJsonValueSerializer;
import static org.jxapi.util.JsonUtil.writeStringField;

/**
 * Jackson JSON Serializer for org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest
 * @see DemoExchangeMarketDataTickerStreamRequest
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class DemoExchangeMarketDataTickerStreamRequestSerializer extends AbstractJsonValueSerializer<DemoExchangeMarketDataTickerStreamRequest> {
  
  private static final long serialVersionUID = -6657596609460857888L;
  
  /**
   * Constructor
   */
  public DemoExchangeMarketDataTickerStreamRequestSerializer() {
    super(DemoExchangeMarketDataTickerStreamRequest.class);
  }
  
  
  @Override
  public void serialize(DemoExchangeMarketDataTickerStreamRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    writeStringField(gen, "symbol", value.getSymbol());
    gen.writeEndObject();
  }
}
