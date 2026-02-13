package org.jxapi.netutils.websocket;

/**
 * Interface for handling websocket errors. {@link Websocket} client
 * implementations should implement this interface and subscribe using
 * {@link Websocket#addErrorHandler(WebsocketErrorHandler)} to receive error
 * notifications.
 * <p>
 * The error handling method should be non-blocking and should not throw
 * exceptions. When an error occurs, the websocket should be in 'Disconnected'
 * state.
 * 
 * @see Websocket
 */
public interface WebsocketErrorHandler {

  /**
   * Method to handle websocket errors. This method should be non-blocking and should not throw exceptions.
   * @param error the error that occurred
   */
  void handleWebsocketError(WebsocketException error);
}
