package org.jxapi.netutils.websocket.multiplexing;

/**
 * A stub implementation of {@link AbstractWebsocketMessageTopicMatcher} for
 * testing purposes.
 */
public class StubWebsocketMessageTopicMatcher extends AbstractWebsocketMessageTopicMatcher {
  
  public StubWebsocketMessageTopicMatcher() {
    super();
  }

  public StubWebsocketMessageTopicMatcher(WebsocketMessageTopicMatchStatus status) {
    super();
    this.status = status;
  }

  public void setStatus(WebsocketMessageTopicMatchStatus status) {
    this.status = status;
  }

  @Override
  public WebsocketMessageTopicMatchStatus matches(String fieldName, String value) {
    return status;
  }

}
