package org.jxapi.exchanges.demo.gen.marketdata.pojo.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponse;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponsePayload;
import org.jxapi.netutils.serialization.json.AbstractJsonValueSerializer;
import org.jxapi.netutils.serialization.json.MapJsonValueSerializer;
import static org.jxapi.util.JsonUtil.writeCustomSerializerField;
import static org.jxapi.util.JsonUtil.writeIntField;

/**
 * Jackson JSON Serializer for org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponse
 * @see DemoExchangeMarketDataTickersResponse
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class DemoExchangeMarketDataTickersResponseSerializer extends AbstractJsonValueSerializer<DemoExchangeMarketDataTickersResponse> {
  
  private static final long serialVersionUID = -717914394642214189L;
  
  /**
   * Constructor
   */
  public DemoExchangeMarketDataTickersResponseSerializer() {
    super(DemoExchangeMarketDataTickersResponse.class);
  }
  
  private MapJsonValueSerializer<DemoExchangeMarketDataTickersResponsePayload> payloadSerializer;
  
  @Override
  public void serialize(DemoExchangeMarketDataTickersResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    writeIntField(gen, "responseCode", value.getResponseCode());
    if(payloadSerializer == null) {
      payloadSerializer = new MapJsonValueSerializer<>(new DemoExchangeMarketDataTickersResponsePayloadSerializer());
    }
    writeCustomSerializerField(gen, "payload", value.getPayload(), payloadSerializer, provider);
    gen.writeEndObject();
  }
}
