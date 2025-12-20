package org.jxapi.netutils.websocket;

/**
 * Interface for hooking into the websocket lifecycle.
 * <p>
 * Exchange websocket API specifications usually have specific requirements for:
 * <ul>
 * <li><strong>Intialization</strong>: See {@link #init(WebsocketClient)}
 * method called at end {@link WebsocketClient} constructor. The
 * {@link WebsocketClient} instance passed in parameter to
 * {@link #init(WebsocketClient)} method is bound to this hook instance and all
 * further hook calls will be performed from its write executor thread. A
 * reference to this manager should be kept, subclassing
 * {@link AbstractWebsocketHook} is recommanded for implementations that need
 * resources from the manager. Websocket is not connected or connecting then.
 * This is where configuration that remains unchanged can be performed, for
 * instance subscribing 'technical' message listeners see
 * {@link WebsocketClient#addSystemMessageHandler(String, org.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatcherFactory, RawWebsocketMessageHandler)},
 * or customizing manager's configuration like heartbeat timeout, no message
 * timeout or delay before reconnection.</li>
 * <li><strong>Connection</strong>: See {@link #beforeConnect()} and
 * {@link #afterConnect()} methods called just before and after connecting
 * socket. For instance API specific protocol may require to append base url a
 * token, {@link WebsocketClient#setUrl(String)} must be called before
 * connecting to specialize websocket URL. Or a specific message for
 * authentication should be sent right after connection</li>
 * <li><strong>Disconnection</strong>: See {@link #beforeConnect()} and See
 * {@link #afterConnect()} called just before and after disconnecting
 * websocket.</li>
 * <li><strong>Subscribe/unsubscribe request messages</strong> : When
 * multiplexing (e.g. subscribing / unsubscribing for different independant
 * streams/topics, the hook must provide the message to send for subscribing to
 * a topic, and unsubscribing/</li>
 * <li><strong>Heartbeats</strong>When API protocol requires sending of regular
 * 'heartbeat' messages to keep connection alive, {@link #getHeartBeatMessage()}
 * method must be overridden</li>
 * </ul>
 * Remarks: An instance of {@link WebsocketHook} is bound to a
 * {@link WebsocketClient} using {@link #init(WebsocketClient)} method and all
 * other methods will be called from manager single 'write' thread so
 * implementations do not need to be stateless or thread safe.
 * 
 * @see WebsocketClient
 * @see Websocket
 */
public interface WebsocketHook {

  /**
   * Called after the websocket manager has been initialized, to bind this hook to
   * the manager.
   * <p>
   * This is where configuration that remains unchanged can be performed, for
   * instance subscribing 'technical' message listeners see
   * {@link WebsocketClient#addSystemMessageHandler(String, org.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatcherFactory, RawWebsocketMessageHandler)},
   * or customizing manager's configuration like heartbeat timeout, no message
   * timeout or delay before reconnection.
   * 
   * @param websocketClient the websocket manager this hook is bound to. All
   *                         calls to further websocket lifecycle methods will be
   *                         called from this manager instance dedicated write
   *                         thread.
   */
  default void init(WebsocketClient websocketClient) {
  }

  /**
   * Called just before connecting the websocket.
   * <p>
   * For instance API specific protocol may require to append base url a token,
   * {@link WebsocketClient#setUrl(String)} must be called from this method
   * before connecting to specialize websocket URL. The token may require to be
   * refreshed before each connection using a REST API method. 
   * 
   * @throws WebsocketException if an error occurs during the connection process.
   *                            Websocket connection will process will fail and
   *                            WebsocketClient will eventually retry to connect.
   * 
   * @see WebsocketClient
   * @see WebsocketClient#setUrl(String)
   */
  default void beforeConnect() throws WebsocketException {
  }

  /**
   * Called just after connecting the websocket.
   * <p>
   * For instance a specific message for authentication should be sent right after
   * connection. This method is the right place to send it.
   * 
   * @throws WebsocketException if an error occurs after during post connection
   *                            process. Websocket connection will be closed and
   *                            WebsocketClient will eventually retry to connect.
   */
  default void afterConnect() throws WebsocketException {
  }

  /**
   * Called just before disconnecting the websocket.
   * <p>
   * This is where any cleanup or final message sending should be performed before
   * disconnecting.
   * 
   * @throws WebsocketException if an error occurs during the disconnection
   *                            process. Websocket connection will be closed and
   *                            WebsocketClient will eventually retry to connect.
   */
  default void beforeDisconnect() throws WebsocketException {
  }

  /**
   * Called just after disconnecting the websocket.
   * <p>
   * This is where any cleanup or final message sending should be performed after
   * disconnecting.
   * 
   * @throws WebsocketException if an error occurs after during post disconnection
   *                            process. Websocket connection will be closed and
   *                            WebsocketClient will eventually retry to connect.
   */
  default void afterDisconnect() throws WebsocketException {
  }

  /**
   * Get the message to send to subscribe to a topic.
   * <p>
   * This method must be overridden when the API protocol supports multiplexing
   * and requires sending a specific message to subscribe to a topic.
   * 
   * @param topic the topic to subscribe to
   * @return the message to send to subscribe to the topic, or <code>null</code>
   *         if no message is required, which means no multiplexing is supported.
   */
  default String getSubscribeRequestMessage(String topic) {
    return null;
  }

  /**
   * Get the message to send to unsubscribe from a topic.
   * <p>
   * This method must be overridden when the API protocol supports multiplexing
   * and requires sending a specific message to unsubscribe from a topic.
   * 
   * @param topic the topic to unsubscribe from
   * @return the message to send to unsubscribe from the topic, or
   *         <code>null</code> if no message is required, which means no
   *         multiplexing is supported.
   */
  default String getUnSubscribeRequestMessage(String topic) {
    return null;
  }

  /**
   * Get the message to send to keep the connection alive.
   * <p>
   * This method must be overridden when the API protocol requires sending of
   * regular 'heartbeat' messages to keep connection alive.
   * 
   * @return the message to send to keep the connection alive, or
   *         <code>null</code> if no heartbeat is required.
   */
  default String getHeartBeatMessage() {
    return null;
  }

}
