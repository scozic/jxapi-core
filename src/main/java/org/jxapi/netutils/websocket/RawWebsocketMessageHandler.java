package org.jxapi.netutils.websocket;

/**
 * Interface for handling websocket messages dispacthed by a {@link Websocket}. The message is a raw string.
 */
public interface RawWebsocketMessageHandler {

  /**
   * Handle a websocket message.
   * @param message The message.
   */
  void handleWebsocketMessage(String message);
}
