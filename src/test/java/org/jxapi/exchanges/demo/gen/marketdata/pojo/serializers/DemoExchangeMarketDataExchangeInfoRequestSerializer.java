package org.jxapi.exchanges.demo.gen.marketdata.pojo.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoRequest;
import org.jxapi.netutils.serialization.json.AbstractJsonValueSerializer;
import org.jxapi.netutils.serialization.json.ListJsonValueSerializer;
import org.jxapi.netutils.serialization.json.StringJsonValueSerializer;
import static org.jxapi.util.JsonUtil.writeCustomSerializerField;

/**
 * Jackson JSON Serializer for org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoRequest
 * @see DemoExchangeMarketDataExchangeInfoRequest
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class DemoExchangeMarketDataExchangeInfoRequestSerializer extends AbstractJsonValueSerializer<DemoExchangeMarketDataExchangeInfoRequest> {
  
  private static final long serialVersionUID = -3027126271179631247L;
  
  /**
   * Constructor
   */
  public DemoExchangeMarketDataExchangeInfoRequestSerializer() {
    super(DemoExchangeMarketDataExchangeInfoRequest.class);
  }
  
  private final ListJsonValueSerializer<String> symbolsSerializer = new ListJsonValueSerializer<>(StringJsonValueSerializer.getInstance());
  
  @Override
  public void serialize(DemoExchangeMarketDataExchangeInfoRequest value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    writeCustomSerializerField(gen, "symbols", value.getSymbols(), symbolsSerializer, provider);
    gen.writeEndObject();
  }
}
