package org.jxapi.netutils.websocket.multiplexing;

import java.util.Objects;

/**
 * A {@link WebsocketMessageTopicMatcher} that matches a field's value against a
 * specific value.
 */
public class FieldValueWebsocketMessageTopicMatcher extends AbstractWebsocketMessageTopicMatcher {
  
  private WebsocketMessageTopicMatchStatus status = WebsocketMessageTopicMatchStatus.NO_MATCH;
  
  private final String fieldName;
  private final String value;

  /**
   * Constructor
   * 
   * @param fieldName the field name
   * @param value     the value to match the field value against
   */
  public FieldValueWebsocketMessageTopicMatcher(String fieldName, String value) {
    this.fieldName = fieldName;
    this.value = value;
  }

  @Override
  public WebsocketMessageTopicMatchStatus matches(String fieldName, String value) {
    if (this.status != WebsocketMessageTopicMatchStatus.NO_MATCH) {
      // Other statuses are terminal statuses
      return this.status;
    }
    if (this.fieldName.equals(fieldName)) {
      if (Objects.equals(this.value, value)) {
        this.status = WebsocketMessageTopicMatchStatus.MATCHED;
      } else {
        this.status = WebsocketMessageTopicMatchStatus.CANT_MATCH;
      }
    } 
    return this.status;
  }
  
  /**
   * Get the field name
   * @return the field name
   */
  public String getFieldName() {
    return fieldName;
  }
  
  /**
   * Get the value to match against the field value
   * 
   * @return the value
   */
  public String getValue() {
    return value;
  }

}
