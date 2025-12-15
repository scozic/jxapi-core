package org.jxapi.exchanges.demo.gen.marketdata.pojo.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponsePayload;
import org.jxapi.netutils.serialization.json.AbstractJsonMessageSerializer;
import static org.jxapi.util.JsonUtil.writeBigDecimalField;
import static org.jxapi.util.JsonUtil.writeLongField;
import static org.jxapi.util.JsonUtil.writeObjectField;

/**
 * Jackson JSON Serializer for org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponsePayload
 * @see DemoExchangeMarketDataTickersResponsePayload
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class DemoExchangeMarketDataTickersResponsePayloadSerializer extends AbstractJsonMessageSerializer<DemoExchangeMarketDataTickersResponsePayload> {
  
  /**
   * Constructor
   */
  public DemoExchangeMarketDataTickersResponsePayloadSerializer() {
    super(DemoExchangeMarketDataTickersResponsePayload.class);
  }
  
  
  @Override
  public void serialize(DemoExchangeMarketDataTickersResponsePayload value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    writeBigDecimalField(gen, "last", value.getLast());
    writeBigDecimalField(gen, "high", value.getHigh());
    writeBigDecimalField(gen, "low", value.getLow());
    writeBigDecimalField(gen, "volume", value.getVolume());
    writeLongField(gen, "time", value.getTime());
    writeObjectField(gen, "meta", value.getMeta());
    gen.writeEndObject();
  }
}
