package org.jxapi.netutils.websocket.multiplexing;

import org.jxapi.util.EncodingUtil;

/**
 * Abstract base class for {@link WebsocketMessageTopicMatcher} implementations.
 */
public abstract class AbstractWebsocketMessageTopicMatcher implements WebsocketMessageTopicMatcher {
  
  protected WebsocketMessageTopicMatchStatus status = WebsocketMessageTopicMatchStatus.NO_MATCH;

  @Override
  public WebsocketMessageTopicMatchStatus getStatus() {
    return status;
  }

  @Override
  public void reset() {
    this.status = WebsocketMessageTopicMatchStatus.NO_MATCH;
  }

  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
