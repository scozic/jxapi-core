package com.scz.jxapi.exchanges.demo.gen.marketdata.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.SingleSymbol;
import javax.annotation.processing.Generated;

/**
 * Jackson JSON Serializer for com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.SingleSymbol
 * @see SingleSymbol
 */
@Generated("com.scz.jxapi.generator.java.exchange.api.pojo.JsonPojoSerializerGenerator")
public class SingleSymbolSerializer extends StdSerializer<SingleSymbol> {
  public SingleSymbolSerializer() {
    super(SingleSymbol.class);
  }
  
  @Override
  public void serialize(SingleSymbol value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getSymbol() != null){
      gen.writeStringField("s", String.valueOf(value.getSymbol()));
    }
    gen.writeEndObject();
  }
}
