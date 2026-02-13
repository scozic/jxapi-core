package org.jxapi.netutils.websocket.multiplexing;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link WebsocketMessageTopicMatcher} that matches a field's value against a
 * specific value.
 */
public class FieldValueWebsocketMessageTopicMatcher extends AbstractWebsocketMessageTopicMatcher {
  
  private static final Logger log = LoggerFactory.getLogger(FieldValueWebsocketMessageTopicMatcher.class);
  private static final boolean DEBUG = log.isDebugEnabled();
  
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
      if (DEBUG) {
        log.debug("{}:Already in terminal status {}, skipping match for field '{}' and value '{}'", 
            this, this.status, fieldName, value);
      }
      // Other statuses are terminal statuses
      return this.status;
    }
    if (this.fieldName.equals(fieldName)) {
      if (Objects.equals(this.value, value)) {
        if (DEBUG) {
          log.debug("{}:Matched field '{}' with value '{}'", this, fieldName, value);
        }
        this.status = WebsocketMessageTopicMatchStatus.MATCHED;
      } else {
        if (DEBUG) {
          log.debug("{}:Field '{}' did not match value '{}'", this, fieldName, value);
        }
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
