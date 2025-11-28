package org.jxapi.exchanges.demo.gen.marketdata.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.GenericResponse;

/**
 * Jackson JSON Serializer for org.jxapi.exchanges.demo.gen.marketdata.pojo.GenericResponse
 * @see GenericResponse
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class GenericResponseSerializer extends StdSerializer<GenericResponse> {
  /**
   * Constructor
   */
  public GenericResponseSerializer() {
    super(GenericResponse.class);
  }
  
  @Override
  public void serialize(GenericResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    if (value.getResponseCode() != null){
      gen.writeNumberField("responseCode", value.getResponseCode());
    }
    gen.writeEndObject();
  }
}
