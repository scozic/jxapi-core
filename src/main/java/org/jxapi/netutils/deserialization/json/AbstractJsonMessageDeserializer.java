package org.jxapi.netutils.deserialization.json;

import java.io.IOException;

import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.util.JsonUtil;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;

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
public abstract class AbstractJsonMessageDeserializer<T> extends com.fasterxml.jackson.databind.JsonDeserializer<T> implements MessageDeserializer<T>, JsonDeserializer<T> {
  
  @Override
  public T deserialize(String msg) {
    try {
      if (msg == null) {
        return null;
      }
      JsonParser parser = JsonUtil.DEFAULT_JSON_FACTORY.createParser(msg.getBytes());
      parser.nextToken();
      return deserialize(parser);
    } catch (IOException e) {
      throw new IllegalArgumentException("Error parsing JSON:[" + msg + "]", e);
    } 
  }
  
  @Override
  public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    return deserialize(p);
  }
  
}
