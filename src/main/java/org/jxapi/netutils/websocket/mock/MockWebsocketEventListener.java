package org.jxapi.netutils.websocket.mock;

/**
 * Interface for listening to events from a {@link MockWebsocket}.
 */
public interface MockWebsocketEventListener {

  /**
   * Handle a {@link MockWebsocketEvent}.
   *
   * @param event The event to handle
   */
  void handleEvent(MockWebsocketEvent event);
}
