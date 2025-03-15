package org.jxapi.exchanges.demo.gen.marketdata.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponsePayload;
import org.jxapi.util.EncodingUtil;

/**
 * Jackson JSON Serializer for org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponsePayload
 * @see DemoExchangeMarketDataTickersResponsePayload
 */
@Generated("org.jxapi.generator.java.exchange.api.pojo.JsonPojoSerializerGenerator")
public class DemoExchangeMarketDataTickersResponsePayloadSerializer extends StdSerializer<DemoExchangeMarketDataTickersResponsePayload> {
  public DemoExchangeMarketDataTickersResponsePayloadSerializer() {
    super(DemoExchangeMarketDataTickersResponsePayload.class);
  }
  
  @Override
  public void serialize(DemoExchangeMarketDataTickersResponsePayload value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getLast() != null){
      gen.writeStringField("last", EncodingUtil.bigDecimalToString(value.getLast()));
    }
    if (value.getHigh() != null){
      gen.writeStringField("high", EncodingUtil.bigDecimalToString(value.getHigh()));
    }
    if (value.getLow() != null){
      gen.writeStringField("low", EncodingUtil.bigDecimalToString(value.getLow()));
    }
    if (value.getVolume() != null){
      gen.writeStringField("volume", EncodingUtil.bigDecimalToString(value.getVolume()));
    }
    if (value.getTime() != null){
      gen.writeNumberField("time", value.getTime());
    }
    gen.writeEndObject();
  }
}
