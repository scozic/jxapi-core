package org.jxapi.netutils.serialization.json;

import java.io.IOException;
import java.io.StringWriter;

import org.jxapi.netutils.serialization.MessageSerializer;
import org.jxapi.util.JsonUtil;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Abstract base class for JSON data message serializers. Implements the
 * {@link MessageSerializer#serialize(Object)} method using a Jackson
 * {@link ObjectMapper} to serialize objects into JSON strings. Actual
 * implementations can extend this class to provide custom serialization logic
 * if needed.
 * 
 * @param <T> the type of the serialized object
 * 
 * @see MessageSerializer
 */
public abstract class AbstractJsonValueSerializer<T> extends StdSerializer<T> implements MessageSerializer<T> {
  
  private static final long serialVersionUID = -781509171850696587L;

  /**
   * Constructor
   * @param serializedClass the class of the serialized object
   */
  protected AbstractJsonValueSerializer(Class<T> serializedClass) {
    super(serializedClass);
  }

  @Override
  public String serialize(T obj) {
    if (obj == null) {
      return null;
    }
    StringWriter writer = new StringWriter();
    try (JsonGenerator gen = JsonUtil.DEFAULT_JSON_FACTORY.createGenerator(writer)) {
      serialize(obj, gen, JsonUtil.DEFAULT_OBJECT_MAPPER.getSerializerProvider());
    } catch (IOException e) {
      throw new IllegalStateException("Error serializing object to JSON", e);
    }
    return writer.toString();
  }

}
