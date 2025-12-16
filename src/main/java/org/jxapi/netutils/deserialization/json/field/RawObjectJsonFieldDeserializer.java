package org.jxapi.netutils.deserialization.json.field;

import java.io.IOException;

import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import org.jxapi.util.JsonUtil;

import com.fasterxml.jackson.core.JsonParser;

/**
 * Deserializer that reads raw JSON objects into raw <code>java.lang.Object</code> instances.
 * <p>
 * The actual type of the returned object depends on the found JSON structure
 * from parser stream: see {@link JsonUtil#readCurrentObject(JsonParser)} for details.
 * <p>
 * This class is a singleton, use {@link #getInstance()} to get the instance.
 * @see JsonUtil#readCurrentObject(JsonParser)
 */
public class RawObjectJsonFieldDeserializer extends AbstractJsonMessageDeserializer<Object> {
  
  private static final RawObjectJsonFieldDeserializer INSTANCE = new RawObjectJsonFieldDeserializer();
  
  /**
   * @return the singleton instance of this deserializer
   */
  public static RawObjectJsonFieldDeserializer getInstance() {
    return INSTANCE;
  }
  
  private RawObjectJsonFieldDeserializer() {
  }

  @Override
  public Object deserialize(JsonParser parser) throws IOException {
    return JsonUtil.readCurrentObject(parser);
  }

}
