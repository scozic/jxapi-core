package org.jxapi.exchanges.demo.gen.marketdata.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoRequest;

/**
 * Jackson JSON Serializer for org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoRequest
 * @see DemoExchangeMarketDataExchangeInfoRequest
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class DemoExchangeMarketDataExchangeInfoRequestSerializer extends StdSerializer<DemoExchangeMarketDataExchangeInfoRequest> {
  /**
   * Constructor
   */
  public DemoExchangeMarketDataExchangeInfoRequestSerializer() {
    super(DemoExchangeMarketDataExchangeInfoRequest.class);
  }
  
  @Override
  public void serialize(DemoExchangeMarketDataExchangeInfoRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getSymbols() != null){
      gen.writeObjectField("symbols", value.getSymbols());
    }
    gen.writeEndObject();
  }
}
