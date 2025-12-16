package org.jxapi.netutils.serialization.json;

import java.io.IOException;

import org.jxapi.util.JsonUtil;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * {@link AbstractJsonMessageSerializer} for {@link Boolean} values in JSON
 * messages.
 * <p>
 * This class is a singleton, use {@link #getInstance()} to get the instance.
 * 
 * @see MessageSerializer
 */
public class BooleanJsonValueSerializer extends AbstractJsonMessageSerializer<Boolean> {

  private static final BooleanJsonValueSerializer INSTANCE = new BooleanJsonValueSerializer();

  /**
   * @return the singleton instance of this class
   */
  public static BooleanJsonValueSerializer getInstance() {
    return INSTANCE;
  }

  private BooleanJsonValueSerializer() {
    super(Boolean.class);
  }

  @Override
  public void serialize(Boolean value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    JsonUtil.writeBooleanValue(gen, value); 
  }

}
