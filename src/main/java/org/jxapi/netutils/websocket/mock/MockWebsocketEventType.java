package org.jxapi.netutils.websocket.mock;

import org.jxapi.netutils.websocket.RawWebsocketMessageHandler;
import org.jxapi.netutils.websocket.Websocket;
import org.jxapi.netutils.websocket.WebsocketErrorHandler;

/**
 * Enum for the different types of events that can be sent to a {@link MockWebsocketEventListener}.
 */
public enum MockWebsocketEventType {
  
  /**
   * Event type for a connection event.
   */
  CONNECT,
  
  /**
   * Event type for a disconnection event.
   */
  DISCONNECT,
  
  /**
   * Event type for events triggered when a message is sent, see {@link Websocket#send(String)}.
   */
  SEND,
  
  /**
   * Event type for events triggered when a message handler is subscribed, see {@link Websocket#addMessageHandler(RawWebsocketMessageHandler)}.
   */
  ADD_MESSAGE_HANDLER,
  
  /**
   * Event type for events triggered when a message handler is unsubscribed, see {@link Websocket#removeMessageHandler(RawWebsocketMessageHandler)}.
   */
  REMOVE_MESSAGE_HANDLER,
  
  /**
   * Event type for events triggered when an error handler is subscribed, see
   * {@link Websocket#addErrorHandler(WebsocketErrorHandler)}.
   */
  ADD_ERROR_HANDLER,
  
  /**
   * Event type for events triggered when an error handler is unsubscribed, see
   * {@link Websocket#removeErrorHandler(WebsocketErrorHandler)}.
   */
  REMOVE_ERROR_HANDLER
}
