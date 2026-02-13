package org.jxapi.netutils.deserialization.json.field;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import org.jxapi.netutils.deserialization.json.JsonDeserializer;
import org.jxapi.util.JsonUtil;

/**
 * {@link AbstractJsonMessageDeserializer} for {@link Boolean} fields in JSON messages.
 * <p>
 * This class is a singleton, use {@link #getInstance()} to get the instance.
 * 
 * @see MessageDeserializer
 * @see JsonDeserializer
 */
public class BooleanJsonFieldDeserializer extends AbstractJsonMessageDeserializer<Boolean> {
  
  private static final BooleanJsonFieldDeserializer INSTANCE = new BooleanJsonFieldDeserializer();
  
  /**
   * @return the singleton instance of this class
   */
  public static BooleanJsonFieldDeserializer getInstance() {
    return INSTANCE;
  }
  
  private BooleanJsonFieldDeserializer() {}

  @Override
  public Boolean deserialize(JsonParser parser) throws IOException {
    return JsonUtil.readCurrentBoolean(parser);
  }

}
