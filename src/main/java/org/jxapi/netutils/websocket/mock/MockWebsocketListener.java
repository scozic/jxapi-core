package org.jxapi.netutils.websocket.mock;

import org.jxapi.netutils.websocket.WebsocketListener;
import org.jxapi.observability.GenericObserver;

/**
 * Mock implementation of a {@link WebsocketListener}.
 *
 * @param <M> The type of messages to listen to
 */
public class MockWebsocketListener<M> extends GenericObserver<M> implements WebsocketListener<M> {

  @Override
  public void handleMessage(M message) {
    handleEvent(message);
  }

}
