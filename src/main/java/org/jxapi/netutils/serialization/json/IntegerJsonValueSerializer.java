package org.jxapi.netutils.serialization.json;

import java.io.IOException;

import org.jxapi.netutils.serialization.MessageSerializer;
import org.jxapi.util.JsonUtil;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * {@link AbstractJsonValueSerializer} for {@link Integer} values in JSON
 * messages.
 * <p>
 * This class is a singleton, use {@link #getInstance()} to get the instance.
 * 
 * @see MessageSerializer
 */
public class IntegerJsonValueSerializer extends AbstractJsonValueSerializer<Integer> {

  private static final long serialVersionUID = -4335112677327187416L;
  private static final IntegerJsonValueSerializer INSTANCE = new IntegerJsonValueSerializer();

  /**
   * @return the singleton instance of this class
   */
  public static IntegerJsonValueSerializer getInstance() {
    return INSTANCE;
  }

  private IntegerJsonValueSerializer() {
    super(Integer.class);
  }

  @Override
  public void serialize(Integer value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    JsonUtil.writeIntegerValue(gen, value);
  }

}
