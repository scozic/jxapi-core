package org.jxapi.exchanges.demo.gen.marketdata.pojo.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponsePayload;
import org.jxapi.netutils.serialization.json.AbstractJsonValueSerializer;
import static org.jxapi.util.JsonUtil.writeBigDecimalField;
import static org.jxapi.util.JsonUtil.writeObjectField;
import static org.jxapi.util.JsonUtil.writeStringField;

/**
 * Jackson JSON Serializer for org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponsePayload
 * @see DemoExchangeMarketDataExchangeInfoResponsePayload
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class DemoExchangeMarketDataExchangeInfoResponsePayloadSerializer extends AbstractJsonValueSerializer<DemoExchangeMarketDataExchangeInfoResponsePayload> {
  
  private static final long serialVersionUID = -5123265136479426530L;
  
  /**
   * Constructor
   */
  public DemoExchangeMarketDataExchangeInfoResponsePayloadSerializer() {
    super(DemoExchangeMarketDataExchangeInfoResponsePayload.class);
  }
  
  
  @Override
  public void serialize(DemoExchangeMarketDataExchangeInfoResponsePayload value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    writeStringField(gen, "symbol", value.getSymbol());
    writeBigDecimalField(gen, "minOrderSize", value.getMinOrderSize());
    writeBigDecimalField(gen, "orderTickSize", value.getOrderTickSize());
    writeObjectField(gen, "blob", value.getBlob());
    gen.writeEndObject();
  }
}
