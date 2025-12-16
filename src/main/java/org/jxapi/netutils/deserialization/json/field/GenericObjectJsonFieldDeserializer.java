package org.jxapi.netutils.deserialization.json.field;

import java.io.IOException;

import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import org.jxapi.util.JsonUtil;

import com.fasterxml.jackson.core.JsonParser;

/**
 * {@link AbstractJsonMessageDeserializer} for generic object fields in JSON
 * messages.
 * <p>
 * This class can be used to deserialize any object type from JSON by providing 
 * the corresponding class type.
 * @param <T> the type of the object to be deserialized
 * 
 * @see org.jxapi.netutils.deserialization.MessageDeserializer
 * @see org.jxapi.netutils.deserialization.json.JsonDeserializer
 */
public class GenericObjectJsonFieldDeserializer<T> extends AbstractJsonMessageDeserializer<T> {

  private final Class<T> deserializedClass;

  /**
   * Creates a new {@link GenericObjectJsonFieldDeserializer} for the specified class type.
   * @param deserializedClass the class type to be deserialized
   */
  public GenericObjectJsonFieldDeserializer(Class<T> deserializedClass) {
    this.deserializedClass = deserializedClass;
  }

  @Override
  public T deserialize(JsonParser parser) throws IOException {
    return JsonUtil.readCurrentObject(parser, deserializedClass);
  }

}
