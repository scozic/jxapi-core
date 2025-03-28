package org.jxapi.netutils.websocket;

/**
 * Websocket endpoint callback
 * @param <M> the message object disseminated by this websocket stream.
 */
public interface WebsocketListener<M> {
  
  /**
   * Handle a message received from the websocket stream.
   * @param message the message received
   */
  void handleMessage(M message);

}
