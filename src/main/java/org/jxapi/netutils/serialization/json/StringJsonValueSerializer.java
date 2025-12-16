package org.jxapi.netutils.serialization.json;

import java.io.IOException;

import org.jxapi.util.JsonUtil;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * {@link AbstractJsonMessageSerializer} for {@link String} values in JSON
 * messages.
 * <p>
 * This class is a singleton, use {@link #getInstance()} to get the instance.
 * 
 * @see MessageSerializer
 */
public class StringJsonValueSerializer extends AbstractJsonMessageSerializer<String> {

  private static final StringJsonValueSerializer INSTANCE = new StringJsonValueSerializer();

  /**
   * @return the singleton instance of this class
   */
  public static StringJsonValueSerializer getInstance() {
    return INSTANCE;
  }

  private StringJsonValueSerializer() {
    super(String.class);
  }

  @Override
  public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    JsonUtil.writeStringValue(gen, value);
  }

}
