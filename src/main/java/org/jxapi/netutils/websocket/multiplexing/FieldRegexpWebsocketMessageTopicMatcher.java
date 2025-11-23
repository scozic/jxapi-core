package org.jxapi.netutils.websocket.multiplexing;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link WebsocketMessageTopicMatcher} that matches a field's value against a
 * regular expression.
 */
public class FieldRegexpWebsocketMessageTopicMatcher extends AbstractWebsocketMessageTopicMatcher {
  
  private static final Logger log = LoggerFactory.getLogger(FieldRegexpWebsocketMessageTopicMatcher.class);
  private static final boolean DEBUG = log.isDebugEnabled();

  private final String fieldName;
  private final Pattern valuePattern;
  
  /**
   * Constructor
   * @param fieldName the field name
   * @param valueRegexp the regular expression to match the field value against
   */
  public FieldRegexpWebsocketMessageTopicMatcher(String fieldName, String valueRegexp) {
    this.fieldName = fieldName;
    this.valuePattern = Pattern.compile(valueRegexp);
  }

  @Override
  public WebsocketMessageTopicMatchStatus matches(String fieldName, String value) {
    if (this.status != WebsocketMessageTopicMatchStatus.NO_MATCH) {
      // Other statuses are terminal statuses
      if (DEBUG) {
        log.debug("{}:Already in terminal status {}, skipping matching for field '{}' with value '{}'",
          this, this.status, fieldName, value);
      }
      return this.status;
    }
    if (this.fieldName.equals(fieldName)) {
      if (value != null && this.valuePattern.matcher(value).matches()) {
        if (DEBUG) {
          log.debug("{}:Field '{}' with value '{}' matched pattern '{}'", 
            this, fieldName, value, this.valuePattern.pattern());
        }
        this.status = WebsocketMessageTopicMatchStatus.MATCHED;
      } else {
        if (DEBUG) {
          log.debug("{}:Field '{}' with value '{}' did not match pattern '{}'", 
              this, fieldName, value, this.valuePattern.pattern());
        }
        this.status = WebsocketMessageTopicMatchStatus.CANT_MATCH;
      }
    } 
    return this.status;
  }

}
