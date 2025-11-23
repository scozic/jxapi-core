package org.jxapi.netutils.websocket.multiplexing;

import org.jxapi.util.EncodingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class for {@link WebsocketMessageTopicMatcher} implementations.
 */
public abstract class AbstractWebsocketMessageTopicMatcher implements WebsocketMessageTopicMatcher {
  
  private static final Logger log = LoggerFactory.getLogger(AbstractWebsocketMessageTopicMatcher.class);
  private static final boolean DEBUG = log.isDebugEnabled();
  
  /**
   * Current status of matcher
   */
  protected WebsocketMessageTopicMatchStatus status = WebsocketMessageTopicMatchStatus.NO_MATCH;

  @Override
  public WebsocketMessageTopicMatchStatus getStatus() {
    return status;
  }

  @Override
  public void reset() {
    if (DEBUG) {
      log.debug("{}:Resetting matcher", this);
    }
    this.status = WebsocketMessageTopicMatchStatus.NO_MATCH;
  }

  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
