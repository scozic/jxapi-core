package org.jxapi.exchanges.demo.gen.marketdata.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.SingleSymbol;
import org.jxapi.netutils.serialization.json.AbstractJsonMessageSerializer;
import static org.jxapi.util.JsonUtil.writeStringField;

/**
 * Jackson JSON Serializer for org.jxapi.exchanges.demo.gen.marketdata.pojo.SingleSymbol
 * @see SingleSymbol
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class SingleSymbolSerializer extends AbstractJsonMessageSerializer<SingleSymbol> {
  
  /**
   * Constructor
   */
  public SingleSymbolSerializer() {
    super(SingleSymbol.class);
  }
  
  
  @Override
  public void serialize(SingleSymbol value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    writeStringField(gen, "s", value.getSymbol());
    gen.writeEndObject();
  }
}
