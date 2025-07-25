package org.jxapi.netutils.deserialization.json.field;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.netutils.deserialization.json.JsonDeserializer;
import org.jxapi.util.JsonUtil;

/**
 * {@link AbstractJsonMessageDeserializer} for {@link String} fields in JSON messages.
 * <p>
 * This class is a singleton, use {@link #getInstance()} to get the instance.
 * 
 * @see MessageDeserializer
 * @see JsonDeserializer
 */
public class StringJsonFieldDeserializer extends AbstractJsonMessageDeserializer<String> {
  
  private static final StringJsonFieldDeserializer INSTANCE = new StringJsonFieldDeserializer();
  
  /**
   * @return the singleton instance of this class
   */
  public static StringJsonFieldDeserializer getInstance() {
    return INSTANCE;
  }
  
  private StringJsonFieldDeserializer() {}

  @Override
  public String deserialize(JsonParser parser) throws IOException {
    return JsonUtil.readCurrentString(parser);
  }

}
