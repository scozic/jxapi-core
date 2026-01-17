package org.jxapi.exchanges.demo.gen.marketdata.pojo.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamMessage;
import org.jxapi.netutils.serialization.json.AbstractJsonValueSerializer;
import static org.jxapi.util.JsonUtil.writeBigDecimalField;
import static org.jxapi.util.JsonUtil.writeLongField;
import static org.jxapi.util.JsonUtil.writeStringField;

/**
 * Jackson JSON Serializer for org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamMessage
 * @see DemoExchangeMarketDataTickerStreamMessage
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class DemoExchangeMarketDataTickerStreamMessageSerializer extends AbstractJsonValueSerializer<DemoExchangeMarketDataTickerStreamMessage> {
  
  private static final long serialVersionUID = -2506891314597185572L;
  
  /**
   * Constructor
   */
  public DemoExchangeMarketDataTickerStreamMessageSerializer() {
    super(DemoExchangeMarketDataTickerStreamMessage.class);
  }
  
  
  @Override
  public void serialize(DemoExchangeMarketDataTickerStreamMessage value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    writeStringField(gen, "t", value.getTopic());
    writeStringField(gen, "s", value.getSymbol());
    writeBigDecimalField(gen, "p", value.getLast());
    writeBigDecimalField(gen, "h", value.getHigh());
    writeBigDecimalField(gen, "l", value.getLow());
    writeBigDecimalField(gen, "v", value.getVolume());
    writeLongField(gen, "d", value.getTime());
    gen.writeEndObject();
  }
}
