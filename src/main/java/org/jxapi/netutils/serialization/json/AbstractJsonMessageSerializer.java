package org.jxapi.netutils.serialization.json;

import org.jxapi.netutils.serialization.MessageSerializer;
import org.jxapi.util.JsonUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
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
public abstract class AbstractJsonMessageSerializer<T> extends StdSerializer<T> implements MessageSerializer<T> {

  /**
   * The Jackson ObjectMapper used for serialization.
   * </p>
   * Initialized lazily in the {@link #serialize(Object)} method, to avoid 
   * creating it if not needed and this instance is used only for {@link StdSerializer} functionality.
   */
  protected ObjectMapper objectMapper;
  
  /**
   * Constructor
   * @param serializedClass the class of the serialized object
   */
  protected AbstractJsonMessageSerializer(Class<T> serializedClass) {
    super(serializedClass);
  }

  @Override
  public String serialize(T obj) {
    if (objectMapper == null) {
      objectMapper = JsonUtil.createDefaultObjectMapper();
      SimpleModule m = new SimpleModule();
      m.addSerializer(this);
      objectMapper.registerModule(m);
    }
    return JsonUtil.pojoToJsonString(obj, objectMapper);
  }

}
