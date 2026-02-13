package org.jxapi.exchanges.demo.gen.marketdata.pojo.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.GenericResponse;
import org.jxapi.netutils.serialization.json.AbstractJsonValueSerializer;
import static org.jxapi.util.JsonUtil.writeIntField;

/**
 * Jackson JSON Serializer for org.jxapi.exchanges.demo.gen.marketdata.pojo.GenericResponse
 * @see GenericResponse
 */
@Generated("org.jxapi.generator.java.pojo.JsonPojoSerializerGenerator")
public class GenericResponseSerializer extends AbstractJsonValueSerializer<GenericResponse> {
  
  private static final long serialVersionUID = 7520270830479836114L;
  
  /**
   * Constructor
   */
  public GenericResponseSerializer() {
    super(GenericResponse.class);
  }
  
  
  @Override
  public void serialize(GenericResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeStartObject();
    writeIntField(gen, "responseCode", value.getResponseCode());
    gen.writeEndObject();
  }
}
