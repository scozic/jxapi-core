package org.jxapi.exchanges.demo.gen.marketdata.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponsePayload;
import org.jxapi.util.EncodingUtil;

/**
 * Jackson JSON Serializer for org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponsePayload
 * @see DemoExchangeMarketDataExchangeInfoResponsePayload
 */
@Generated("org.jxapi.generator.java.exchange.api.pojo.JsonPojoSerializerGenerator")
public class DemoExchangeMarketDataExchangeInfoResponsePayloadSerializer extends StdSerializer<DemoExchangeMarketDataExchangeInfoResponsePayload> {
  /**
   * Constructor
   */
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
