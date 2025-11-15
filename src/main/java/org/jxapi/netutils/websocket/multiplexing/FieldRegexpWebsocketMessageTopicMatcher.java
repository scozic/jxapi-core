package org.jxapi.netutils.websocket.multiplexing;

import java.util.regex.Pattern;

/**
 * A {@link WebsocketMessageTopicMatcher} that matches a field's value against a
 * regular expression.
 */
public class FieldRegexpWebsocketMessageTopicMatcher extends AbstractWebsocketMessageTopicMatcher {

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
      return this.status;
    }
    if (this.fieldName.equals(fieldName)) {
      if (value != null && this.valuePattern.matcher(value).matches()) {
        this.status = WebsocketMessageTopicMatchStatus.MATCHED;
      } else {
        this.status = WebsocketMessageTopicMatchStatus.CANT_MATCH;
      }
    } 
    return this.status;
  }

}
