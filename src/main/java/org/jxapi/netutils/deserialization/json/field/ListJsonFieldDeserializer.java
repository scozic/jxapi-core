package org.jxapi.netutils.deserialization.json.field;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import org.jxapi.netutils.deserialization.json.JsonDeserializer;
import org.jxapi.util.CollectionUtil;

/**
 * {@link AbstractJsonMessageDeserializer} for {@link List} fields in JSON messages.
 * 
 * @param <T> the type of the list items
 * 
 * @see MessageDeserializer
 * @see JsonDeserializer
 */
public class ListJsonFieldDeserializer<T> extends AbstractJsonMessageDeserializer<List<T>> {
  
  /**
   * The deserializer for the list items
   */
  protected final JsonDeserializer<T> itemDeserializer;
  
  /**
   * @param itemDeserializer the deserializer for the list items
   */
  public ListJsonFieldDeserializer(JsonDeserializer<T> itemDeserializer) {
    this.itemDeserializer = itemDeserializer;
  }

  @Override
  public List<T> deserialize(JsonParser parser) throws IOException {
    if (parser.currentToken() == JsonToken.VALUE_NULL) {
      return null;
    }
    if (parser.currentToken() != JsonToken.START_ARRAY) {
      throw new IllegalStateException("Expecting start array of String, got:" + parser.currentToken() + " with name:" + parser.currentName());
    }
    
    List<T> res = CollectionUtil.createList();
        for (parser.nextToken(); parser.currentToken() != JsonToken.END_ARRAY; parser.nextToken()) {
            res.add(itemDeserializer.deserialize(parser));
        }
    return res;
  }

}
