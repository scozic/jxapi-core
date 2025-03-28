package org.jxapi.netutils.deserialization.json.field;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import org.jxapi.util.JsonUtil;
import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.netutils.deserialization.json.JsonDeserializer;

/**
 * {@link AbstractJsonMessageDeserializer} for {@link Long} fields in JSON messages.
 * <p>
 * This class is a singleton, use {@link #getInstance()} to get the instance.
 * 
 * @see MessageDeserializer
 * @see JsonDeserializer
 */
public class LongJsonFieldDeserializer extends AbstractJsonMessageDeserializer<Long> {
  
  private static final LongJsonFieldDeserializer INSTANCE = new LongJsonFieldDeserializer();
  
  /**
   * @return the singleton instance of this class
   */
  public static LongJsonFieldDeserializer getInstance() {
    return INSTANCE;
  }
  
  private LongJsonFieldDeserializer() {}

  @Override
  public Long deserialize(JsonParser parser) throws IOException {
    return JsonUtil.readCurrentLong(parser);
  }

}
