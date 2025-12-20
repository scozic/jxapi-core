package org.jxapi.exchange;

/**
 * Enumeration of event types that can be dispatched by the exchange observability API.
 * 
 * @see ExchangeEvent
 */
public enum ExchangeEventType {

  /**
   * A new HTTP request has been submitted.
   */
  HTTP_REQUEST,

  /**
   * A HTTP response to a submitted request has been received.
   */
  HTTP_RESPONSE,

  /**
   * A new websocket subscription has been requested.
   */
  WEBSOCKET_SUBSCRIBE,

  /**
   * A websocket subscription has been cancelled.
   */
  WEBSOCKET_UNSUBSCRIBE,

  /**
   * A message has been received on a websocket subscription.
   */
  WEBSOCKET_MESSAGE,

  /**
   * An error has been detected on a websocket subscription.
   */
  WEBSOCKET_ERROR
}
