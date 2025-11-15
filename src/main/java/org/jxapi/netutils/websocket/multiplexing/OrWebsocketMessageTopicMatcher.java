package org.jxapi.netutils.websocket.multiplexing;

import java.util.List;

import org.jxapi.util.CollectionUtil;

/**
 * A {@link WebsocketMessageTopicMatcher} that matches if any of its operand matchers matches.
 * Notice all consecutive matchers in {@link WebsocketMessageTopicMatchStatus#CANT_MATCH} from offset 0 are not re-checked.
 * <br>
 * When no operand matchers are given, this matcher is considered to be in {@link WebsocketMessageTopicMatchStatus#CANT_MATCH} status.
 */
public class OrWebsocketMessageTopicMatcher extends AbstractWebsocketMessageTopicMatcher {

private final List<WebsocketMessageTopicMatcher> matchers;
  
  private int unmatchedCount = 0;
  private int firstUnmatchedOffset = 0;
  
  public OrWebsocketMessageTopicMatcher(List<WebsocketMessageTopicMatcher> matchers) {
    this.matchers = CollectionUtil.emptyIfNull(matchers);
    if (matchers.isEmpty()) {
      this.status = WebsocketMessageTopicMatchStatus.CANT_MATCH;
    } else {
      this.status = WebsocketMessageTopicMatchStatus.NO_MATCH;
    }
    reset();
  }

  public List<WebsocketMessageTopicMatcher> getMatchers() {
    return matchers;
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
        return this.status;
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

}
