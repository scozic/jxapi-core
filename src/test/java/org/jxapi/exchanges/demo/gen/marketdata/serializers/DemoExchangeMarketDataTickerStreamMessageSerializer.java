package org.jxapi.exchanges.demo.gen.marketdata.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamMessage;
import org.jxapi.util.EncodingUtil;

/**
 * Jackson JSON Serializer for org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamMessage
 * @see DemoExchangeMarketDataTickerStreamMessage
 */
@Generated("org.jxapi.generator.java.exchange.api.pojo.JsonPojoSerializerGenerator")
public class DemoExchangeMarketDataTickerStreamMessageSerializer extends StdSerializer<DemoExchangeMarketDataTickerStreamMessage> {
  /**
   * Constructor
   */
  public DemoExchangeMarketDataTickerStreamMessageSerializer() {
    super(DemoExchangeMarketDataTickerStreamMessage.class);
  }
  
  @Override
  public void serialize(DemoExchangeMarketDataTickerStreamMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getTopic() != null){
      gen.writeStringField("t", String.valueOf(value.getTopic()));
    }
    if (value.getSymbol() != null){
      gen.writeStringField("s", String.valueOf(value.getSymbol()));
    }
    if (value.getLast() != null){
      gen.writeStringField("p", EncodingUtil.bigDecimalToString(value.getLast()));
    }
    if (value.getHigh() != null){
      gen.writeStringField("h", EncodingUtil.bigDecimalToString(value.getHigh()));
    }
    if (value.getLow() != null){
      gen.writeStringField("l", EncodingUtil.bigDecimalToString(value.getLow()));
    }
    if (value.getVolume() != null){
      gen.writeStringField("v", EncodingUtil.bigDecimalToString(value.getVolume()));
    }
    if (value.getTime() != null){
      gen.writeNumberField("d", value.getTime());
    }
    gen.writeEndObject();
  }
}
