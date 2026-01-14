package org.jxapi.netutils.websocket;

/**
 * Interface representing a Websocket.
 * A Websocket is a communication protocol that provides full-duplex
 * communication channels over a single TCP connection.<br>
 * A websocket can be used to send and receive text messages between a client
 * and a server.<br>
 * Once established, a websocket connection remains open until the client or
 * server decides to close this connection, or a network error occurs.<br>
 * It is common in API specifications to specificy a multiplexing protocol: a
 * protocol that allows multiple logical channels to be multiplexed over a
 * single connection.
 * This is achieved by requesting client to issue a subscription message for a
 * given topic. Multiple topics can be subscribed to, and messages can be
 * received on any of the subscribed topics.<br>
 * The multiplexing is not managed by this interface that only provides the
 * basic methods to connect, disconnect, send messages, and add/remove message
 * and error handlers.<br>
 * Rather, a {@link WebsocketClient} should be used to manage the multiplexing
 * of the websocket connection.<br>
 * 
 * @see WebsocketClient
 */
public interface Websocket {

  /**
   * Connect to the websocket server. This is a synchronous call, that will block
   * until the connection is established or an error occurs.
   * 
   * @throws WebsocketException if an error occurs during the connection process.
   *                            Websocket will stay in disconnected state if such
   *                            exception is thrown.
   */
  void connect() throws WebsocketException;

  /**
   * Disconnect from the websocket server. This is a synchronous call, that will
   * block until the connection is closed or an error occurs.
   * Unlike {@link #connect()}, this method will not throw any exception if an
   * error occurs during the disconnection process. Implmentations must manage the
   * disconnection process and ensure that the connection is closed and every
   * resource relased before returning.
   */
  void disconnect();

  /**
   * Send a message to the websocket server. This is a synchronous call, that will
   * block until the message is sent or an error occurs.
   * 
   * @param message the message to send. Must not be <code>null</code>.
   * @throws WebsocketException if an error occurs during the sending process, or
   *                            if the websocket is not connected.
   */
  void send(String message) throws WebsocketException;

  /**
   * Add a message handler to this websocket. The handler will be called when a
   * message is received from the server.
   * 
   * @param messageHandler the message handler to add. Must not be
   *                       <code>null</code>.
   */
  void addMessageHandler(RawWebsocketMessageHandler messageHandler);

  /**
   * Remove a message handler from this websocket.
   * 
   * @param messageHandler the message handler to remove. Must not be
   *                       <code>null</code>.
   * @return <code>true</code> if the handler was removed, <code>false</code> if
   *         there was no such handler.
   */
  boolean removeMessageHandler(RawWebsocketMessageHandler messageHandler);

  /**
   * Add an error handler to this websocket. The handler will be called when an
   * error causes an active connection to be closed unexpecteadly.<br>
   * It may not be called if a connection error occurs during the connection, or
   * sending of a message in which case the {@link #connect()} or
   * {@link #send(String)} method will throw a {@link WebsocketException}.
   * <p>
   * When an error handler is called, the websocket may remain in a connected
   * state though the link is broken. It is up to the error handler to disconnect
   * and reconnect the link.
   * 
   * @param errorHandler the error handler to add. Must not be <code>null</code>.
   */
  void addErrorHandler(WebsocketErrorHandler errorHandler);

  /**
   * Remove an error handler from this websocket.
   * 
   * @param errorHandler the error handler to remove. Must not be
   *                     <code>null</code>.
   * @return <code>true</code> if the handler was removed, <code>false</code> if
   *         there was no such handler.
   */
  boolean removeErrorHandler(WebsocketErrorHandler errorHandler);

  /**
   * Check if this websocket is connected.
   * 
   * @return <code>true</code> if the websocket is connected, <code>false</code>
   *         otherwise.
   */
  boolean isConnected();

  /**
   * Set the URL of the websocket server to connect to, for instance
   * "wss://example.com:8080/websocket".
   * 
   * @param url the URL of the websocket server. Must not be <code>null</code>.
   */
  void setUrl(String url);

  /**
   * Get the URL of the websocket server to connect to.
   * 
   * @return the URL of the websocket server.
   */
  String getUrl();

}
