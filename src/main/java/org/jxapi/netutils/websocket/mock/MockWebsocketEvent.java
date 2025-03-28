package org.jxapi.netutils.websocket.mock;

import org.jxapi.netutils.websocket.RawWebsocketMessageHandler;
import org.jxapi.netutils.websocket.WebsocketErrorHandler;

/**
 * The MockWebsocketEvent class represents an event that can occur in a mock
 * websocket.<br>
 * It provides static factory methods to create different types of events and
 * access their properties.
 */
public class MockWebsocketEvent {
  
  /**
   * Creates a {@link MockWebsocketEventType#CONNECT} event.
   * 
   * @return The created MockWebsocketEvent object.
   */
  public static MockWebsocketEvent createConnectEvent() {
    return new MockWebsocketEvent(MockWebsocketEventType.CONNECT);
  }
  
  /**
   * Creates a {@link MockWebsocketEventType#DISCONNECT} event.
   * @return The created MockWebsocketEvent object.
   */
  public static MockWebsocketEvent createDisconnectEvent() {
    return new MockWebsocketEvent(MockWebsocketEventType.DISCONNECT);
  }
  
  /**
   * Creates a {@link MockWebsocketEventType#SEND} event for the with the
   * specified message.
   * 
   * @param message The message to be sent.
   * @return The created MockWebsocketEvent object.
   */
  public static MockWebsocketEvent createSendEvent(String message) {
    MockWebsocketEvent event = new MockWebsocketEvent(MockWebsocketEventType.SEND);
    event.setMessage(message);
    return event;
  }
  
  /**
   * Creates an {@link MockWebsocketEventType#ADD_MESSAGE_HANDLER} event with the
   * specified message handler.
   * 
   * @param messageHandler The message handler to be added.
   * @return The created MockWebsocketEvent object.
   */
  public static MockWebsocketEvent createAddMessageHandlerEvent(RawWebsocketMessageHandler messageHandler) {
    MockWebsocketEvent event = new MockWebsocketEvent(MockWebsocketEventType.ADD_MESSAGE_HANDLER);
    event.setMessageHandler(messageHandler);
    return event;
  }
  
  /**
   * Creates a {@link MockWebsocketEventType#REMOVE_MESSAGE_HANDLER} event with
   * the specified message handler.
   * 
   * @param messageHandler The message handler to be removed.
   * @return The created MockWebsocketEvent object.
   */
  public static MockWebsocketEvent createRemoveMessageHandlerEvent(RawWebsocketMessageHandler messageHandler) {
    MockWebsocketEvent event = new MockWebsocketEvent(MockWebsocketEventType.REMOVE_MESSAGE_HANDLER);
    event.setMessageHandler(messageHandler);
    return event;
  }
  
  /**
   * Creates an {@link MockWebsocketEventType#ADD_ERROR_HANDLER} event with the
   * specified error handler.
   * 
   * @param errorHandler The error handler to be added.
   * @return The created MockWebsocketEvent object.
   */
  public static MockWebsocketEvent createAddErrorHandlerEvent(WebsocketErrorHandler errorHandler) {
    MockWebsocketEvent event = new MockWebsocketEvent(MockWebsocketEventType.ADD_ERROR_HANDLER);
    event.setErrorHandler(errorHandler);
    return event;
  }
  
  /**
   * Creates a {@link MockWebsocketEventType#REMOVE_ERROR_HANDLER} event with the
   * specified error handler.
   * 
   * @param errorHandler The error handler to be removed.
   * @return The created MockWebsocketEvent object.
   */
  public static MockWebsocketEvent createRemoveErrorHandlerEvent(WebsocketErrorHandler errorHandler) {
    MockWebsocketEvent event = new MockWebsocketEvent(MockWebsocketEventType.REMOVE_ERROR_HANDLER);
    event.setErrorHandler(errorHandler);
    return event;
  }
  
  private final MockWebsocketEventType type;
  private RawWebsocketMessageHandler messageHandler;
  private WebsocketErrorHandler errorHandler;
  private String message;

  /**
   * Constructs a MockWebsocketEvent object with the specified type.
   * 
   * @param type The type of the event.
   */
  public MockWebsocketEvent(MockWebsocketEventType type) {
    this.type = type;
  }

  /**
   * Returns the type of the event.
   * 
   * @return The type of the event.
   */
  public MockWebsocketEventType getType() {
    return type;
  }

  /**
   * Returns the message handler associated with the event.
   * 
   * @return The message handler associated with the event.
   */
  public RawWebsocketMessageHandler getMessageHandler() {
    return messageHandler;
  }

  /**
   * Sets the message handler for the event.
   * 
   * @param messageHandler The message handler to be set.
   */
  public void setMessageHandler(RawWebsocketMessageHandler messageHandler) {
    this.messageHandler = messageHandler;
  }

  /**
   * Returns the error handler associated with the event.
   * 
   * @return The error handler associated with the event.
   */
  public WebsocketErrorHandler getErrorHandler() {
    return errorHandler;
  }

  /**
   * Sets the error handler for the event.
   * 
   * @param errorHandler The error handler to be set.
   */
  public void setErrorHandler(WebsocketErrorHandler errorHandler) {
    this.errorHandler = errorHandler;
  }
  
  /**
   * Returns the message associated with the event.
   * 
   * @return The message associated with the event.
   */
  public String getMessage() {
    return message;
  }

  /**
   * Sets the message for the event.
   * 
   * @param message The message to be set.
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Returns a string representation of the MockWebsocketEvent object.
   * 
   * @return A string representation of the MockWebsocketEvent object.
   */
  public String toString() {
    return getClass().getSimpleName() + "[" + type + "]";
  }
}
