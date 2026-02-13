package org.jxapi.netutils.serialization.json;

import java.io.IOException;
import java.util.List;

import org.jxapi.netutils.serialization.MessageSerializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * {@link AbstractJsonValueSerializer} for {@link List} values in JSON
 * messages.
 * <p>
 * This class is parameterized with the type of the list elements and requires an
 * element serializer to serialize each element in the list.
 * 
 * @see MessageSerializer
 */
public class ListJsonValueSerializer<T> extends AbstractJsonValueSerializer<List<T>> {

  private static final long serialVersionUID = -3149117333168465950L;
  protected final StdSerializer<T> elementSerializer;
  
  /**
   * Constructor.
   * @param elementSerializer the serializer for the list elements
   */
  @SuppressWarnings("unchecked")
  public ListJsonValueSerializer(StdSerializer<T> elementSerializer) {
    super((Class<List<T>>) (Class<?>) List.class);
    this.elementSerializer = elementSerializer;
  }

  @Override
  public void serialize(List<T> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    if (value == null) {
      gen.writeNull();
      return;
    }
    gen.writeStartArray();
    for (T element : value) {
      elementSerializer.serialize(element, gen, provider);
    }
    gen.writeEndArray();
  }

}
