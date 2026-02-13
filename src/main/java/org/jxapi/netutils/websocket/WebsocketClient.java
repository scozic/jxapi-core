package org.jxapi.netutils.websocket;

import java.util.concurrent.CompletableFuture;

import org.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatcher;
import org.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatcherFactory;
import org.jxapi.util.Disposable;

/**
 * A WebsocketClient is a wrapper around a {@link Websocket} that manages most
 * common operations specified in a Websocket API.
 * <br>
 * <h2>Multiplexing</h2>
 * Given that a Websocket can be used to subscribe to multiple topics, a
 * WebsocketClient allows to subscribe a single message handler to a topic.
 * The actual message that must be sent to the server to subscribe to a topic is
 * managed by the {@link WebsocketHook}, see
 * {@link WebsocketHook#getSubscribeRequestMessage(String)}.
 * Filtering incoming messages matching a topic is done by the
 * {@link WebsocketMessageTopicMatcher} that is used to match incoming messages
 * to a topic using a JSON parser to extract properties of incoming JSON
 * messages. The properties will be searched in the JSON message nested objects
 * recursively.
 * A single message handler can be subscribed to a topic. Subscribing multiple
 * message handlers to the same topic is not supported but managed by
 * {@link WebsocketEndpoint}.
 * <br>
 * API specification may not require multiplexing, in which case a single
 * message handler can be subscribed to the WebsocketClient using
 * <code>null</code> as topic.
 * 
 * <h2>Wrapping {@link Websocket} methods</h2>
 * The WebsocketClient is responsible to ensure that the underlying Websocket
 * is connected before sending a message or subscribing to a topic.
 * Calls to {@link Websocket#connect()}, {@link Websocket#disconnect()},
 * {@link Websocket#send(String)} methods are called asynchronously and thread
 * safe from the WebsocketClient dedicated write executor.
 * <br>
 * Errors that occur during the connection process, while sending messages or
 * anytime when connection is alive are dispatched to all error handlers using
 * {@link WebsocketErrorHandler#handleWebsocketError(WebsocketException)}.
 * Before such error is dispatched, the WebsocketClient will disconnect the
 * underlying Websocket and try to reconnect it if reconnection is configured.
 * 
 * <h2>Injection of API specific logic using {@link WebsocketHook}</h2>
 * When provided a {@link WebsocketHook}, the WebsocketClient will call the
 * hook methods at specific points in the Websocket lifecycle, allowing to
 * inject API specific logic.
 * <br>
 * In particular, when multiplexing is required, the hook will be used to
 * provide the message to send to the server to subscribe to or unsubscribe from
 * a topic.<br>
 * When authentication is required, the hook will be used to provide the message
 * to send to the server to authenticate.<br>
 * When heartbeat is required, the hook will be used to provide the message to
 * send to the server to keep the connection alive.
 * 
 * <h2>Heartbeat</h2>
 * The WebsocketClient is responsible to manage the heartbeat mechanism. It
 * will send a heartbeat message to the server at regular interval if configured
 * for it.
 * Usually, the server will respond to the heartbeat message with a heartbeat
 * response message. If no response is received within a timeout, the
 * WebsocketClient will disconnect the underlying Websocket and try to
 * reconnect it if reconnection is configured.<br>
 * It is the responsibility of the WebsocketHook to provide the heartbeat
 * message to send to the server.<br>
 * It is the responsibility of client implementation to register a specific
 * message handler to handle the heartbeat response message and call
 * #{@link #hearbeatReceived()} when a heartbeat response message is received.
 * <br>
 * <ul>
 * <li>When heartbeat interval is set to a positive value means server expects
 * heartbeats for client. A heartbeat message is sent to the server at regular
 * interval.</li>
 * <li>When no heartbeat response timeout is set to a positive value means
 * client expects heartbeats from server (can be initiated by server or in
 * response to client heartbeat). If no heartbeat response is received within
 * this timeout, the underlying Websocket will be disconnected and an error will
 * be dispatched to all error handlers. Recconnection will be attempted if
 * configured.</li>
 * </ul>
 * Using <code>heartbeat interval</code> means server expects heartbeats for
 * client.
 * Using <code>no heartbeat response timeout</code> means client expects
 * heartbeats from server.
 * <h2>Reconnection</h2>
 * The WebsocketClient is responsible to manage the reconnection mechanism. It
 * will try to reconnect the underlying Websocket upon error if reconnection is
 * configured, see {@link #setReconnectDelay(long)}. Upon successful connection,
 * the WebsocketClient will resubscribe all message handlers to the topics that
 * were subscribed to before the disconnection.
 * 
 * <h2>Idle timeout</h2>
 * The WebsocketClient is responsible to manage the idle timeout mechanism. It
 * will disconnect the underlying Websocket if no message is received within a
 * timeout, see {@link #setNoMessageTimeout(long)}
 * 
 * <h2>Additional notes</h2>
 * The WebsocketClient is responsible to manage the connection state of the
 * underlying Websocket. This means client implementation should not call
 * {@link Websocket#connect()}, {@link Websocket#disconnect()},
 * {@link Websocket#send(String)} methods directly on the underlying Websocket.
 * Rather, client implementation should call {@link #sendAsync(String)} to
 * dispatch a message asynchronously and {@link #dispose()} to dispose the
 * WebsocketClient and underlying Websocket definitely.
 * 
 */
public interface WebsocketClient extends Disposable {

  /**
   * Adds a message handler to handle specific/non business messages. This is
   * similar to
   * {@link #subscribe(String, WebsocketMessageTopicMatcherFactory, RawWebsocketMessageHandler)}
   * but for system messages.
   * 
   * @param topic          The topic for the system message to handle. Should be
   *                       unique and not overlap with business messages topics.
   * @param matcherFactory        The factory matcher to use to match incoming messages to the
   *                       topic.
   * @param messageHandler The message handler to call when a message related to
   *                       topic is received.
   */
  void addSystemMessageHandler(String topic, 
                 WebsocketMessageTopicMatcherFactory matcherFactory,
                 RawWebsocketMessageHandler messageHandler);

  /**
   * Subscribes a message handler to a topic. The message handler will be called
   * when a message is received from the server that matches the topic.
   * <br>
   * Client implementations must take care not to subscribe multiple message
   * handlers to the same topic. This is managed by the {@link WebsocketEndpoint}.
   * Subscribing a message handler for a topic that already has a message handler
   * will cause an error to be raised by the WebsocketClient.
   * 
   * @param topic          The topic to subscribe to. Can be <code>null</code> if
   *                       no multiplexing is required. In this case the message
   *                       handler will be called for every message received.
   * @param matcher        The matcher to use to match incoming messages to the
   *                       topic. Can be <code>null</code> if no multiplexing is
   *                       required.
   * @param messageHandler The message handler to call when a message related to
   *                       topic is received.
   */
  void subscribe(String topic, WebsocketMessageTopicMatcherFactory matcher, RawWebsocketMessageHandler messageHandler);

  /**
   * Unsubscribes from a topic.
   * 
   * @param topic The topic to unsubscribe from.
   * @see WebsocketClient#subscribe(String, WebsocketMessageTopicMatcherFactory, RawWebsocketMessageHandler)
   */
  void unsubscribe(String topic);

  /**
   * Adds an error handler to handle errors that occur during the connection
   * process, while sending messages or anytime when connection is alive.
   * Client implementations should not initiate shutdown of the WebsocketClient
   * or underlying Websocket from the error handler. Reconnection is managed by
   * the WebsocketClient.
   * 
   * @param websocketErrorHandler The error handler to add.
   */
  void subscribeErrorHandler(WebsocketErrorHandler websocketErrorHandler);

  /**
   * Removes an error handler from the list of error handlers.
   * 
   * @param websocketErrorHandler The error handler to remove.
   * @return <code>true</code> if the error handler was removed,
   *         <code>false</code> if the error handler was not found.
   */
  boolean unsubscribeErrorHandler(WebsocketErrorHandler websocketErrorHandler);

  /**
   * Sends a message to this manager underlying {@link Websocket}.
   * <br>
   * This method must be called from dedicated write executor thread, which is the
   * case in {@link WebsocketHook#beforeConnect()},
   * {@link WebsocketHook#beforeDisconnect()},
   * {@link WebsocketHook#afterConnect()}, and
   * {@link WebsocketHook#afterDisconnect()} methods.
   * <br>
   * Sending messages from another thread should be done using
   * {@link #sendAsync(String)} which will write on websocket output
   * asynchronously from WebsocketClient dedicated write excutor.
   * <br>
   * A typical use case would be sending a message for authentication handshake
   * from {@link WebsocketHook#afterConnect()}.
   * 
   * @param msg Message to send on underlying Websocket output.
   * @throws WebsocketException If an error occurs while sending the data on the websocket output, 
   *        or if this manager is disposed.
   */
  void send(String msg) throws WebsocketException;

  /**
   * Sends a message to this manager underlying {@link Websocket} asynchronously.
   * <br>
   * This method can be called from any thread. The message will be written on
   * websocket output asynchronously from WebsocketClient dedicated write
   * executor.
   * 
   * @param msg Message to send on underlying Websocket output.
   * @return A CompletableFuture that will complete when the message is sent or an
   *         error occurs. The future will complete with an exception if an error
   *         occurs, or <code>null</code> if the message is sent successfully.
   */
  CompletableFuture<WebsocketException> sendAsync(String msg);

  /**
   * Must be called from specific system message handler to notify that a
   * heartbeat response message has been received. This will reset the no
   * heartbeat response timeout.
   */
  void hearbeatReceived();

  /**
   * Gets the reconnect delay in milliseconds.
   * 
   * @return The reconnect delay in milliseconds. A negative value means no
   *         reconnection is configured and error will trigger disconnection and
   *         no further reconnection attempt of the underlying Websocket.
   */
  long getReconnectDelay();

  /**
   * Sets the reconnect delay in milliseconds. A negative value means no
   * reconnection is configured and error will trigger disconnection and no
   * further reconnection attempt of the underlying Websocket.
   * 
   * @param reconnectDelay The reconnect delay in milliseconds.
   */
  void setReconnectDelay(long reconnectDelay);

  /**
   * Gets the no message timeout in milliseconds. If no message is received within
   * this timeout, the underlying Websocket will be disconnected and an error will
   * be dispatched to all error handlers. Recconnection will be attempted if
   * configured.
   * 
   * @return The no message timeout in milliseconds. A negative value means no
   *         timeout is configured and no disconnection will be ever triggered if
   *         no message is received.
   */
  long getNoMessageTimeout();

  /**
   * Sets the no message timeout in milliseconds. If no message is received within
   * this timeout, the underlying Websocket will be disconnected and an error will
   * be dispatched to all error handlers. Recconnection will be attempted if
   * configured.
   * 
   * @param noMessageTimeout The no message timeout in milliseconds.
   */
  void setNoMessageTimeout(long noMessageTimeout);

  /**
   * Gets the heartbeat interval in milliseconds. If a heartbeat interval is
   * configured, a heartbeat message will be sent to the server at regular
   * interval.
   * 
   * @return The heartbeat interval in milliseconds. A negative value means no
   *         heartbeat is configured and no heartbeat message will be sent.
   */
  long getHeartBeatInterval();

  /**
   * Sets the heartbeat interval in milliseconds. If a heartbeat interval is
   * configured, a heartbeat message will be sent to the server at regular
   * interval.
   * 
   * @param heartBeatInterval The heartbeat interval in milliseconds.
   * @throws IllegalStateException if heartbeat interval is &gt; 0 and no websocket
   *                               hook is provided. When heartbeat interval is  &gt;
   *                               0, a websocket hook must be provided to provide
   *                               the heartbeat message to send to the server.
   */
  void setHeartBeatInterval(long heartBeatInterval);

  /**
   * Gets the no heartbeat response timeout in milliseconds. If no heartbeat is
   * received from server within this timeout, the underlying Websocket will be
   * disconnected and an error will be dispatched to all error handlers.
   * Recconnection will be attempted if configured.
   * 
   * @return The no heartbeat response timeout in milliseconds. A negative value
   *         means no timeout is configured and no disconnection will be ever
   *         triggered if no heartbeat response is received.
   */
  long getNoHeartBeatResponseTimeout();

  /**
   * Sets the no heartbeat response timeout in milliseconds. If no heartbeat is
   * received from server within this timeout, the underlying Websocket will be
   * disconnected and an error will be dispatched to all error handlers.
   * Recconnection will be attempted if configured.
   * 
   * @param noHeartBeatResponseTimeout The no heartbeat response timeout in
   *                                   milliseconds.
   */
  void setNoHeartBeatResponseTimeout(long noHeartBeatResponseTimeout);

  /**
   * Gets the URL of the underlying Websocket. Proxy method for {@link Websocket#getUrl()}.
   * 
   * @return The URL of the underlying Websocket.
   */
  String getUrl();

  /**
   * Sets the URL of the underlying Websocket. Proxy method for
   * {@link Websocket#setUrl(String)}.
   * 
   * @param url The URL of the underlying Websocket.
   */
  void setUrl(String url);

  /**
   * Can be called from {@link WebsocketHook} to notify of an error.
   * 
   * @param exception The exception that caused the error.
   */
  void notifyError(WebsocketException exception);
}
