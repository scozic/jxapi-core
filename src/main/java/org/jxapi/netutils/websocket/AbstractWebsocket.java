package org.jxapi.netutils.websocket;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jxapi.observability.DefaultObservable;
import org.jxapi.observability.Observable;

/**
 * Abstract class for Websocket implementations. All websocket implementations
 * should extend this class, implementing the abstract methods.
 * <ul>
 * <li>Implementing {@link #doConnect()} method to establish actual websocket
 * which will be called from {@link #connect()} method only if websocket is
 * currenty in 'disconnected' state.</li>
 * <li>Implementing {@link #doDisconnect()} method to shut down websocket
 * connection and dispose all resources associated to it. This method will be
 * called from {@link #disconnect()} only if websocket is currently in
 * 'connected' state.</li>
 * <li>Implementing {@link #doSend(String)} method to send a message to the
 * websocket server.</li>
 * <li>Calling {@link #dispatchMessage(String)} method to dispatch incoming
 * messages to all message handlers.</li>
 * <li>Calling {@link #dispatchError(WebsocketException)} method to dispatch
 * errors to all error handlers when a connection failure occurs.</li>
 * </ul>
 * 
 * 
 * @see Websocket
 */
public abstract class AbstractWebsocket implements Websocket {

  private static final Logger log = LoggerFactory.getLogger(AbstractWebsocket.class);

  /**
   * Flag to track connection status.
   */
  protected final AtomicBoolean connected = new AtomicBoolean(false);

  private final Observable<RawWebsocketMessageHandler, String> messageObservable = new DefaultObservable<>(RawWebsocketMessageHandler::handleWebsocketMessage);
  private final Observable<WebsocketErrorHandler, WebsocketException> errorObservable = new DefaultObservable<>(WebsocketErrorHandler::handleWebsocketError);

  /**
   * The URL of the websocket server
   */
  protected String url;

  @Override
  public final void connect() throws WebsocketException {
    log.debug("Connecting WS {}", this);
    if (!connected.getAndSet(true)) {
      try {
        doConnect();
      } catch (WebsocketException ex) {
        connected.set(false);
        throw ex;
      }
    }
    log.debug("Connected WS {}", this);
  }

  @Override
  public final void disconnect() {
    log.debug("Disonnecting {}", this);
    if (connected.getAndSet(false)) {
      doDisconnect();
    }
    log.debug("Disonnected WS {}", this);
  }

  @Override
  public final void send(String message) throws WebsocketException {
    if (!isConnected()) {
      throw new WebsocketException("Not connected:" + this);
    }
    log.debug("{} > {}", this, message);
    doSend(message);
  }

  @Override
  public final boolean isConnected() {
    return connected.get();
  }

  @Override
  public void addMessageHandler(RawWebsocketMessageHandler messageHandler) {
    messageObservable.subscribe(messageHandler);
  }

  @Override
  public boolean removeMessageHandler(RawWebsocketMessageHandler messageHandler) {
    return messageObservable.unsubscribe(messageHandler);
  }

  @Override
  public void addErrorHandler(WebsocketErrorHandler errorHandler) {
    this.errorObservable.subscribe(errorHandler);
  }

  @Override
  public boolean removeErrorHandler(WebsocketErrorHandler errorHandler) {
    return this.errorObservable.unsubscribe(errorHandler);
  }

  @Override
  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public String getUrl() {
    return this.url;
  }

  /**
   * Dispatch a message to all message handlers.
   * 
   * @param message the message to dispatch
   */
  protected void dispatchMessage(String message) {
    messageObservable.dispatch(message);
  }

  /**
   * Dispatch an error to all error handlers.
   * 
   * @param msg the error message
   * @param t   the exception that caused the error
   */
  protected void dispatchError(String msg, Throwable t) {
    dispatchError(new WebsocketException(msg, t));
  }

  /**
   * Dispatch an error to all error handlers.
   * 
   * @param error the error to dispatch
   */
  protected void dispatchError(WebsocketException error) {
    errorObservable.dispatch(error);
  }

  /**
   * Overrides default toString method to return a string representation of this
   * instance. For example: <code>MyWebsocket[ws://example.com:8080]</code>
   */
  @Override
  public String toString() {
    return getClass().getSimpleName() + "[" + url + "]";
  }

  /**
   * Actual implementations must implement this method to establish actual
   * websocket connection. This method will be called from connect only when this
   * instance is in 'disconnected' state. {@link #connected} flag will be switched
   * to true just before calling it. When this method returns, it is assumed that
   * websocket is ready to send outgoing messages using {@link #send(String)} and
   * dispatch incoming messages. <br>
   * If an error occurs trying to establish websocket link, a
   * {@link WebsocketException} must be thrown. In this case, {@link #connected}
   * flag will be set back to <code>false</code> and it is assumed that all
   * resources needed for connection are disposed before throwing the exception
   * and {@link #connect()} can be retried.
   * 
   * @throws WebsocketException if failed to establish socket link.
   */
  protected abstract void doConnect() throws WebsocketException;

  /**
   * Actual implementations must implement this method to disconnect socket link
   * and dispose all resources associated to it. This method will be called from
   * {@link #disconnect()} method right after switching {@link #connected} flag to
   * <code>false</code> Unlike {@link #doConnect()} method, no exception is
   * expected to be thrown. This means disconnection action should fail.
   */
  protected abstract void doDisconnect();

  /**
   * Actual implementations must implement this method to write synchronously a
   * message to socket output stream.
   * 
   * @param message Bytes of this string will be sent on websocket output stream.
   * @throws WebsocketException if I/O error occurs when writing to socket or this
   *                            websocket is not
   *                            connected.
   */
  protected abstract void doSend(String message) throws WebsocketException;

}
