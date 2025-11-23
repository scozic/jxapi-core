package org.jxapi.netutils.websocket.multiplexing;

import java.util.List;

import org.jxapi.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A websocket message topic matcher that matches when all its operand matchers
 * match. Notice that once a matcher matches, it won't be re-checked for subsequent calls
 * to {@link #matches(String, String)}.<br>
 * When no operand matchers are given, this matcher is considered to be matched.
 */
public class AndWebsocketMessageTopicMatcher extends AbstractWebsocketMessageTopicMatcher {
  
  private static final Logger log = LoggerFactory.getLogger(AndWebsocketMessageTopicMatcher.class);
  private static final boolean DEBUG = log.isDebugEnabled();
  
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
      if (DEBUG) {
        log.debug("{}:Skipping matching attempt for AndWebsocketMessageTopicMatcher since status is {}", this, this.status);
      }
      
      // Other statuses are terminal statuses
      return this.status;
    }
    
    for (int i = firstUnmatchedOffset; i < matchers.size(); i++) {
      WebsocketMessageTopicMatchStatus s = matchers.get(i).matches(fieldName, value);
      if (s == WebsocketMessageTopicMatchStatus.CANT_MATCH) {
        this.status = WebsocketMessageTopicMatchStatus.CANT_MATCH;
        if (DEBUG) {
          log.debug("{}:Matcher {} returned CANT_MATCH, setting AndWebsocketMessageTopicMatcher status to CANT_MATCH", this, matchers.get(i));
        }
        return this.status;
      } else if (s == WebsocketMessageTopicMatchStatus.MATCHED) {
        unmatchedCount--;
        if (unmatchedCount <= 0) {
          this.status = WebsocketMessageTopicMatchStatus.MATCHED;
          if (DEBUG) {
            log.debug("{}:All operand matchers matched, setting AndWebsocketMessageTopicMatcher status to MATCHED", this);
          }
          return this.status;
        } else if (i == firstUnmatchedOffset) {
          // Move the first unmatched offset forward
          firstUnmatchedOffset++;
        }
      } 
    }
    
    if (DEBUG) {
      log.debug("{}:Finished matching attempt, still have {} unmatched operand matchers, status remains NO_MATCH", this, unmatchedCount);
    }
    return this.status;
  }
  
  @Override
  public void reset() {
    if (DEBUG) {
      log.debug("{}: AND matcher resetting", this);
    }
    if (!matchers.isEmpty()) {
      for (int i = 0; i < matchers.size(); i++) {
        matchers.get(i).reset();
      }
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
