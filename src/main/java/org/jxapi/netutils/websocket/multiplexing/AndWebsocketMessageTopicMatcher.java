package org.jxapi.netutils.websocket.multiplexing;

import java.util.List;

import org.jxapi.util.CollectionUtil;

/**
 * A websocket message topic matcher that matches when all its operand matchers
 * match. Notice that once a matcher matches, it won't be re-checked for subsequent calls
 * to {@link #matches(String, String)}.<br>
 * When no operand matchers are given, this matcher is considered to be matched.
 */
public class AndWebsocketMessageTopicMatcher extends AbstractWebsocketMessageTopicMatcher {
  
  private final List<WebsocketMessageTopicMatcher> matchers;
  
  private int unmatchedCount = 0;
  private int firstUnmatchedOffset = 0;
  
  /**
   * Constructor
   * @param matchers the operand matchers
   */
  public AndWebsocketMessageTopicMatcher(List<WebsocketMessageTopicMatcher> matchers) {
    this.matchers = CollectionUtil.emptyIfNull(matchers);
    if (matchers.isEmpty()) {
      this.status = WebsocketMessageTopicMatchStatus.MATCHED;
    } else {
      this.status = WebsocketMessageTopicMatchStatus.NO_MATCH;
    }
    reset();
  }
  
  @Override
  public WebsocketMessageTopicMatchStatus matches(String fieldName, String value) {
    if (this.status != WebsocketMessageTopicMatchStatus.NO_MATCH) {
      // Other statuses are terminal statuses
      return this.status;
    }
    
    for (int i = firstUnmatchedOffset; i < matchers.size(); i++) {
      WebsocketMessageTopicMatchStatus s = matchers.get(i).matches(fieldName, value);
      if (s == WebsocketMessageTopicMatchStatus.CANT_MATCH) {
        this.status = WebsocketMessageTopicMatchStatus.CANT_MATCH;
        return this.status;
      } else if (s == WebsocketMessageTopicMatchStatus.MATCHED) {
        unmatchedCount--;
        if (unmatchedCount <= 0) {
          this.status = WebsocketMessageTopicMatchStatus.MATCHED;
          return this.status;
        } else if (i == firstUnmatchedOffset) {
          // Move the first unmatched offset forward
          firstUnmatchedOffset++;
        }
      } 
    }
    
    return this.status;
  }
  
  @Override
  public void reset() {
    if (!matchers.isEmpty()) {
      this.matchers.forEach(WebsocketMessageTopicMatcher::reset);
      this.status = WebsocketMessageTopicMatchStatus.NO_MATCH;
      this.unmatchedCount = matchers.size();
      this.firstUnmatchedOffset = 0;
    }
  }

  /**
   * Gets the operand matchers list.
   * @return the operand matchers
   */
  public List<WebsocketMessageTopicMatcher> getMatchers() {
    return matchers;
  }
}
