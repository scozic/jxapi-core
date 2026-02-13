package org.jxapi.netutils.websocket.multiplexing;

import java.util.List;

import org.jxapi.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link WebsocketMessageTopicMatcher} that matches if any of its operand matchers matches.
 * Notice all consecutive matchers in {@link WebsocketMessageTopicMatchStatus#CANT_MATCH} from offset 0 are not re-checked.
 * <br>
 * When no operand matchers are given, this matcher is considered to be in {@link WebsocketMessageTopicMatchStatus#CANT_MATCH} status.
 */
public class OrWebsocketMessageTopicMatcher extends AbstractWebsocketMessageTopicMatcher {
  
  private static final Logger log = LoggerFactory.getLogger(OrWebsocketMessageTopicMatcher.class);
  private static final boolean DEBUG = log.isDebugEnabled();

  private final List<WebsocketMessageTopicMatcher> matchers;
  
  private int unmatchedCount = 0;
  private int firstUnmatchedOffset = 0;
  
  /**
   * Constructor
   * 
   * @param matchers the operand matchers
   */
  public OrWebsocketMessageTopicMatcher(List<WebsocketMessageTopicMatcher> matchers) {
    this.matchers = CollectionUtil.emptyIfNull(matchers);
    if (matchers.isEmpty()) {
      this.status = WebsocketMessageTopicMatchStatus.CANT_MATCH;
    } else {
      this.status = WebsocketMessageTopicMatchStatus.NO_MATCH;
    }
    reset();
  }

  /**
   * @return the operand matchers
   */
  public List<WebsocketMessageTopicMatcher> getMatchers() {
    return matchers;
  }
  
  @Override
  public WebsocketMessageTopicMatchStatus matches(String fieldName, String value) {
    if (this.status != WebsocketMessageTopicMatchStatus.NO_MATCH) {
      if (DEBUG) {
        log.debug("{}: Or matcher short-circuiting with status {}", this, this.status);
      }
      // Other statuses are terminal statuses
      return this.status;
    }
    
    for (int i = firstUnmatchedOffset; i < matchers.size(); i++) {
      WebsocketMessageTopicMatchStatus s = matchers.get(i).matches(fieldName, value);
      if (s == WebsocketMessageTopicMatchStatus.CANT_MATCH) {
        unmatchedCount--;
        if (unmatchedCount <= 0) {
          this.status = WebsocketMessageTopicMatchStatus.CANT_MATCH;
          return this.status;
        } else if (i == firstUnmatchedOffset) {
          // Move the first unmatched offset forward
          firstUnmatchedOffset++;
        }
        
      } else if (s == WebsocketMessageTopicMatchStatus.MATCHED) {
        this.status = WebsocketMessageTopicMatchStatus.MATCHED;
        if (DEBUG) {
          log.debug("{}: Or matcher matched with matcher {}", this, matchers.get(i));
        }
        return this.status;
      } 
    }
    if (DEBUG) {
      log.debug("{}: Or matcher no match yet, unmatchedCount={}, firstUnmatchedOffset={}", 
          this, 
          unmatchedCount, 
          firstUnmatchedOffset);
    }
    return this.status;
  }
  
  @Override
  public void reset() {
    if (DEBUG) {
      log.debug("{}: OR matcher resetting", this);
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

}
