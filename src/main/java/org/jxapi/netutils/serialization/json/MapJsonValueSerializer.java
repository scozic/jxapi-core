package org.jxapi.netutils.serialization.json;

import java.io.IOException;
import java.util.Map;

import org.jxapi.netutils.serialization.MessageSerializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * {@link AbstractJsonValueSerializer} for {@link Map} values in JSON
 * messages.
 * <p>
 * This class is parameterized with the type of the map values and requires an
 * element serializer to serialize each value in the map.
 * 
 * @see MessageSerializer
 */
public class MapJsonValueSerializer<T> extends AbstractJsonValueSerializer<Map<String, T>> {

  private static final long serialVersionUID = -246627299365683049L;
  protected final StdSerializer<T> elementSerializer;
  
  @SuppressWarnings("unchecked")
  public MapJsonValueSerializer(StdSerializer<T> elementSerializer) {
    super((Class<Map<String, T>>)(Class<?>)Map.class);
    this.elementSerializer = elementSerializer;
  }

  @Override
  public void serialize(Map<String, T> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    if (value == null) {
      gen.writeNull();
      return;
    }
    gen.writeStartObject();
    for (Map.Entry<String, T> entry : value.entrySet()) {
      gen.writeFieldName(entry.getKey());
      T element = entry.getValue();
      elementSerializer.serialize(element, gen, provider);
    }
    gen.writeEndObject();
  }

}
