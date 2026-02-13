package org.jxapi.netutils.serialization.json;

import java.io.IOException;

import org.jxapi.netutils.serialization.MessageSerializer;
import org.jxapi.util.JsonUtil;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * {@link AbstractJsonValueSerializer} for {@link Long} values in JSON
 * messages.
 * <p>
 * This class is a singleton, use {@link #getInstance()} to get the instance.
 * 
 * @see MessageSerializer
 */
public class LongJsonValueSerializer extends AbstractJsonValueSerializer<Long> {

  private static final long serialVersionUID = -7905977870957492988L;
  private static final LongJsonValueSerializer INSTANCE = new LongJsonValueSerializer();

  /**
   * @return the singleton instance of this class
   */
  public static LongJsonValueSerializer getInstance() {
    return INSTANCE;
  }

  private LongJsonValueSerializer() {
    super(Long.class);
  }

  @Override
  public void serialize(Long value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    JsonUtil.writeLongValue(gen, value);
  }

}
