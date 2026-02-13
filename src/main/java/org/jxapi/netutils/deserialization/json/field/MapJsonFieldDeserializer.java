package org.jxapi.netutils.deserialization.json.field;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import org.jxapi.netutils.deserialization.json.JsonDeserializer;
import org.jxapi.util.CollectionUtil;

/**
 * {@link AbstractJsonMessageDeserializer} for {@link Map} fields in JSON messages.
 * 
 * @see MessageDeserializer
 * @see JsonDeserializer
 */
public class MapJsonFieldDeserializer<T> extends AbstractJsonMessageDeserializer<Map<String, T>> {
  
  private final JsonDeserializer<T> itemDeserializer;
  
  /**
   * @param structDeserializer the deserializer for the values of the map
   */
  public MapJsonFieldDeserializer(JsonDeserializer<T> structDeserializer) {
    this.itemDeserializer = structDeserializer;
  }

  @Override
  public Map<String, T> deserialize(JsonParser parser) throws IOException {
    if (parser.currentToken() == JsonToken.VALUE_NULL) {
      return null;
    }
    if (parser.currentToken() != JsonToken.START_OBJECT) {
      throw new IllegalStateException("Expecting start object of map of string keys, and values objects to be deserialized using " 
                      + this.itemDeserializer 
                      + " but found:" 
                      + parser.currentToken());
    }
    
    Map<String, T> res = CollectionUtil.createMap();
        while (parser.nextToken() != JsonToken.END_OBJECT) {
            String key = parser.currentName();
            parser.nextToken();
            res.put(key, itemDeserializer.deserialize(parser));
        }
        
    return res;
  }

}
