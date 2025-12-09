package org.jxapi.netutils.serialization.json;

import java.io.IOException;

import org.jxapi.util.JsonUtil;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

public class IntegerValueJsonSerializer extends AbstractJsonMessageSerializer<Integer> {

  private static final IntegerValueJsonSerializer INSTANCE = new IntegerValueJsonSerializer();

  /**
   * @return the singleton instance of this class
   */
  public static IntegerValueJsonSerializer getInstance() {
    return INSTANCE;
  }

  private IntegerValueJsonSerializer() {
    super(Integer.class);
  }

  @Override
  public void serialize(Integer value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    JsonUtil.writeIntegerValue(gen, value);
  }

}
