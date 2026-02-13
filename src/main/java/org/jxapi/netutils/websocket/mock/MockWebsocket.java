package org.jxapi.netutils.websocket.mock;

import java.util.concurrent.LinkedBlockingQueue;

import org.jxapi.netutils.websocket.AbstractWebsocket;
import org.jxapi.netutils.websocket.RawWebsocketMessageHandler;
import org.jxapi.netutils.websocket.Websocket;
import org.jxapi.netutils.websocket.WebsocketErrorHandler;
import org.jxapi.netutils.websocket.WebsocketException;
import org.jxapi.observability.GenericObserver;

/**
 * A mock implementation of the {@link Websocket} interface for testing purposes.
 * This class extends the {@link GenericObserver} class and delegates the actual
 * websocket functionality to an internal instance of {@link AbstractWebsocket}.
 */
public class MockWebsocket extends GenericObserver<MockWebsocketEvent> implements Websocket {

  private final DelegateWebsocket delegate = new DelegateWebsocket();

  /**
   * Internal class that extends {@link AbstractWebsocket} and provides the
   * implementation for the websocket methods.
   */
  private class DelegateWebsocket extends AbstractWebsocket {

    @Override
    protected void doConnect() throws WebsocketException {
      handleEvent(MockWebsocketEvent.createConnectEvent());
      WebsocketException ex = exceptionsToThrowOnConnect.poll();
      if (ex != null) {
        throw ex;
      }
    }

    @Override
    protected void doDisconnect() {
      handleEvent(MockWebsocketEvent.createDisconnectEvent());
    }

    @Override
    protected void doSend(String message) throws WebsocketException {
      handleEvent(MockWebsocketEvent.createSendEvent(message));
      WebsocketException ex = exceptionsToThrowOnSend.poll();
      if (ex != null) {
        throw ex;
      }
    }

    @Override
    public void dispatchMessage(String message) {
      super.dispatchMessage(message);
    }

    @Override
    public void dispatchError(WebsocketException error) {
      super.dispatchError(error);
    }

  }
  
  private final LinkedBlockingQueue<WebsocketException> exceptionsToThrowOnConnect = new LinkedBlockingQueue<>();
  private final LinkedBlockingQueue<WebsocketException> exceptionsToThrowOnSend = new LinkedBlockingQueue<>();

  @Override
  public void connect() throws WebsocketException {
    delegate.connect();
  }

  @Override
  public void disconnect() {
    delegate.disconnect();
  }

  /**
   * Sends a message to the websocket server.
   *
   * @param message the message to send.
   * @throws WebsocketException if an error occurs while sending the message.
   */
  @Override
  public void send(String message) throws WebsocketException {    
    delegate.send(message);
  }

  /**
   * Adds a message handler to receive incoming messages from the websocket server.
   *
   * @param messageHandler the message handler to add.
   */
  @Override
  public void addMessageHandler(RawWebsocketMessageHandler messageHandler) {
    handleEvent(MockWebsocketEvent.createAddMessageHandlerEvent(messageHandler));
    delegate.addMessageHandler(messageHandler);
  }

  /**
   * Removes a message handler from the websocket.
   *
   * @param messageHandler the message handler to remove.
   * @return true if the message handler was successfully removed, false otherwise.
   */
  @Override
  public boolean removeMessageHandler(RawWebsocketMessageHandler messageHandler) {
    handleEvent(MockWebsocketEvent.createRemoveMessageHandlerEvent(messageHandler));
    return delegate.removeMessageHandler(messageHandler);
  }

  /**
   * Adds an error handler to handle websocket errors.
   *
   * @param errorHandler the error handler to add.
   */
  @Override
  public void addErrorHandler(WebsocketErrorHandler errorHandler) {
    handleEvent(MockWebsocketEvent.createAddErrorHandlerEvent(errorHandler));
    delegate.addErrorHandler(errorHandler);
  }

  /**
   * Removes an error handler from the websocket.
   *
   * @param errorHandler the error handler to remove.
   * @return true if the error handler was successfully removed, false otherwise.
   */
  @Override
  public boolean removeErrorHandler(WebsocketErrorHandler errorHandler) {
    handleEvent(MockWebsocketEvent.createRemoveErrorHandlerEvent(errorHandler));
    return delegate.removeErrorHandler(errorHandler);
  }

  /**
   * Checks if the websocket is currently connected to the server.
   *
   * @return true if the websocket is connected, false otherwise.
   */
  @Override
  public boolean isConnected() {
    return delegate.isConnected();
  }

  /**
   * Sets the URL of the websocket server.
   *
   * @param url the URL of the server.
   */
  @Override
  public void setUrl(String url) {
    delegate.setUrl(url);
  }

  /**
   * Gets the URL of the websocket server.
   *
   * @return the URL of the server.
   */
  @Override
  public String getUrl() {
    return delegate.getUrl();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "[" + getUrl() + "]";
  }

  /**
   * Dispatches a message to the websocket.
   *
   * @param message the message to dispatch.
   */
  public void dispatchMessage(String message) {
    this.delegate.dispatchMessage(message);
  }

  /**
   * Dispatches an error to the websocket.
   *
   * @param error the error to dispatch.
   */
  public void dispatchError(WebsocketException error) {
    this.delegate.dispatchError(error);
  }
  
  /**
   * Adds an exception to throw when the websocket is connected.
   * 
   * @param errorMessage the error message of the exception to throw
   * @see #addExceptionToThrowOnConnect(WebsocketException)
   */
  public void addExceptionToThrowOnConnect(String errorMessage) {
    addExceptionToThrowOnConnect(new WebsocketException(errorMessage));
  }
  
  /**
   * Adds an exception to throw when the websocket is connected.
   * 
   * @param ex the exception to throw
   */
  public void addExceptionToThrowOnConnect(WebsocketException ex) {
    exceptionsToThrowOnConnect.add(ex);
  }
  
  /**
   * Adds an exception to throw when a message is sent through the websocket, see
   * {@link #send(String)}.
   * 
   * @param errorMessage the error message of the exception to throw
   * @see #addExceptionToThrowOnSend(WebsocketException)
   */
  public void addExceptionToThrowOnSend(String errorMessage) {
    addExceptionToThrowOnSend(new WebsocketException(errorMessage));
  }
  
  /**
   * Adds an exception to throw when the websocket is sending a message, see
   * {@link #send(String)}.
   * 
   * @param ex the exception to throw
   */
  public void addExceptionToThrowOnSend(WebsocketException ex) {
    exceptionsToThrowOnSend.add(ex);
  }

}
