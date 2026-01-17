package org.jxapi.netutils.serialization.json;

import java.io.IOException;

import org.jxapi.netutils.serialization.MessageSerializer;
import org.jxapi.util.JsonUtil;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * {@link AbstractJsonValueSerializer} for {@link Object} values in JSON
 * messages.
 * <p>
 * This class is a singleton, use {@link #getInstance()} to get the instance.
 * 
 * @see MessageSerializer
 */
public class ObjectJsonValueSerializer extends AbstractJsonValueSerializer<Object> {

  private static final long serialVersionUID = 1556157038182265772L;
  private static final ObjectJsonValueSerializer INSTANCE = new ObjectJsonValueSerializer();

  /**
   * @return the singleton instance of this class
   */
  public static ObjectJsonValueSerializer getInstance() {
    return INSTANCE;
  }

  private ObjectJsonValueSerializer() {
    super(Object.class);
  }

  @Override
  public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    JsonUtil.writeObjectValue(gen, value);
  }

}
