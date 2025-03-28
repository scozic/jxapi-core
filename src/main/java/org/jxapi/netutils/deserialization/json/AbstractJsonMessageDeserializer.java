package org.jxapi.netutils.deserialization.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import org.jxapi.netutils.deserialization.MessageDeserializer;

/**
 * Abstract base class for JSON data message deserializers.
 * Implements the {@link MessageDeserializer#deserialize(String)} method using a
 * Jackson {@link JsonParser} to parse incoming messages.
 * Actual implementations must provide a
 * {@link JsonDeserializer#deserialize(JsonParser)} method to deserialize the
 * parsed JSON data.
 * 
 * @param <T> the type of the deserialized object
 * 
 * @see MessageDeserializer
 * @see JsonDeserializer
 */
public abstract class AbstractJsonMessageDeserializer<T> implements MessageDeserializer<T>, JsonDeserializer<T> {
  
  private final JsonFactory jsonFactory = new JsonFactory();
  
  @Override
  public T deserialize(String msg) {
    try {
      if (msg == null) {
        return null;
      }
      JsonParser parser = jsonFactory.createParser(msg.getBytes());
      parser.nextToken();
      return deserialize(parser);
    } catch (IOException e) {
      throw new IllegalArgumentException("Error parsing JSON:[" + msg + "]", e);
    } 
  }
  
}
