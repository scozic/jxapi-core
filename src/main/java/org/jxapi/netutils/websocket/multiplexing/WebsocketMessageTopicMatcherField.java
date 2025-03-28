package org.jxapi.netutils.websocket.multiplexing;

import java.util.ArrayList;
import java.util.List;

import org.jxapi.util.EncodingUtil;

/**
 * A field to match in a {@link WebsocketMessageTopicMatcher}.
 * Represents a key (field name) and a value for that field to match against.
 */
public class WebsocketMessageTopicMatcherField {
  
  /**
   * Create a list of {@link WebsocketMessageTopicMatcherField} instances from a list of names and values.
   * @param namesAndValues A list of names and values like <code>key1, value1, key2, value2...</code> The list must have an even number of elements.
   * @return A list of {@link WebsocketMessageTopicMatcherField} instances.
   */
  public static List<WebsocketMessageTopicMatcherField> createList(String... namesAndValues) {
    List<WebsocketMessageTopicMatcherField> l = new ArrayList<>(namesAndValues.length / 2);
    for (int i = 0; i < namesAndValues.length;i++) {
      WebsocketMessageTopicMatcherField f = new WebsocketMessageTopicMatcherField();
      f.setName(namesAndValues[i++]);
      f.setValue(namesAndValues[i]);
      l.add(f);
    }
    return l;
  }

  private String fieldName;
  
  private String value;

  /**
   * @return The field name
   */
  public String getName() {
    return fieldName;
  }

  /**
   * Set the field name
   * @param fieldName The field name
   */
  public void setName(String fieldName) {
    this.fieldName = fieldName;
  }

  /**
   * @return The value to match against
   */
  public String getValue() {
    return value;
  }

  /**
   * Set the value to match against
   * @param value The value to match against
   */
  public void setValue(String value) {
    this.value = value;
  }
  
  /**
   * @return A string representation of this object as provided by {@link EncodingUtil#pojoToString(Object)}.
   */
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
