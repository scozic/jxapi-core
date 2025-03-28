package org.jxapi.netutils.deserialization.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;

/**
 * Deserializer for JSON data using a Jackson {@link JsonParser}.
 * 
 * @param <T> the type of the deserialized object
 */
public interface JsonDeserializer<T> {

  /**
   * Deserializes the JSON data from the given {@link JsonParser}.
   * 
   * @param parser the parser to read the JSON data from
   * @return the deserialized object
   * @throws IOException if an error occurs during deserialization
   */
  T deserialize(JsonParser parser) throws IOException;
}
