package org.jxapi.exchanges.demo.gen.marketdata.pojo.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponse;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponsePayload;
import org.jxapi.netutils.serialization.json.AbstractJsonValueSerializer;
import org.jxapi.netutils.serialization.json.ListJsonValueSerializer;
import static org.jxapi.util.JsonUtil.writeCustomSerializerField;
import static org.jxapi.util.JsonUtil.writeIntField;

/**
 * Jackson JSON Serializer for org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponse
 * @see DemoExchangeMarketDataExchangeInfoResponse
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class DemoExchangeMarketDataExchangeInfoResponseSerializer extends AbstractJsonValueSerializer<DemoExchangeMarketDataExchangeInfoResponse> {
  
  private static final long serialVersionUID = 6349529947775148169L;
  
  /**
   * Constructor
   */
  public DemoExchangeMarketDataExchangeInfoResponseSerializer() {
    super(DemoExchangeMarketDataExchangeInfoResponse.class);
  }
  
  private ListJsonValueSerializer<DemoExchangeMarketDataExchangeInfoResponsePayload> payloadSerializer;
  
  @Override
  public void serialize(DemoExchangeMarketDataExchangeInfoResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    writeIntField(gen, "responseCode", value.getResponseCode());
    if(payloadSerializer == null) {
      payloadSerializer = new ListJsonValueSerializer<>(new DemoExchangeMarketDataExchangeInfoResponsePayloadSerializer());
    }
    writeCustomSerializerField(gen, "payload", value.getPayload(), payloadSerializer, provider);
    gen.writeEndObject();
  }
}
