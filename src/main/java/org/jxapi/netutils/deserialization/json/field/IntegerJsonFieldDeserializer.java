package org.jxapi.netutils.deserialization.json.field;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import org.jxapi.util.JsonUtil;
import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.netutils.deserialization.json.JsonDeserializer;

/**
 * {@link AbstractJsonMessageDeserializer} for {@link Integer} fields in JSON messages.
 * <p>
 * This class is a singleton, use {@link #getInstance()} to get the instance.
 * 
 * @see MessageDeserializer
 * @see JsonDeserializer
 */
public class IntegerJsonFieldDeserializer extends AbstractJsonMessageDeserializer<Integer> {
  
  private static final IntegerJsonFieldDeserializer INSTANCE = new IntegerJsonFieldDeserializer();
  
  /**
   * @return the singleton instance of this class
   */
  public static IntegerJsonFieldDeserializer getInstance() {
    return INSTANCE;
  }
  
  private IntegerJsonFieldDeserializer() {}

  @Override
  public Integer deserialize(JsonParser parser) throws IOException {
    return JsonUtil.readCurrentInteger(parser);
  } 

}
