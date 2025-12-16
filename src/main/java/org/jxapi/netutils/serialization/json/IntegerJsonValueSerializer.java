package org.jxapi.netutils.serialization.json;

import java.io.IOException;

import org.jxapi.util.JsonUtil;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

public class IntegerJsonValueSerializer extends AbstractJsonMessageSerializer<Integer> {

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
