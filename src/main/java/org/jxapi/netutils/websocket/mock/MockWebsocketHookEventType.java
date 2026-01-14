package org.jxapi.netutils.websocket.mock;

/**
 * Enum for the different types of {@link MockWebsocketHookEvent}
 * 
 * @see MockWebsocketHookEvent
 */
public enum MockWebsocketHookEventType {

  /**
   * Event that is sent when the
   * {@link MockWebsocketHook#init(org.jxapi.netutils.websocket.WebsocketClient)}
   * method has been called.
   */
  INIT,

  /**
   * Event that is sent when the {@link MockWebsocketHook#beforeConnect()} method
   * is called.
   */
  BEFORE_CONNECT,

  /**
   * Event that is sent when the {@link MockWebsocketHook#afterConnect()} method
   * is called.
   */
  AFTER_CONNECT,

  /**
   * Event that is sent when the {@link MockWebsocketHook#beforeDisconnect()}
   * method is called.
   */
  BEFORE_DISCONNECT,

  /**
   * Event that is sent when the {@link MockWebsocketHook#afterDisconnect()}
   * method is called.
   */
  AFTER_DISCONNECT,

  /**
   * Event that is sent when the {@link MockWebsocketHook#getSubscribeRequestMessage(String)}
   * method is called.
   */
  GET_SUBSCRIBE_REQUEST_MESSAGE,

  /**
   * Event that is sent when the
   * {@link MockWebsocketHook#getUnSubscribeRequestMessage(String)} method is
   * called.
   */
  GET_UNSUBSCRIBE_REQUEST_MESSAGE,

  /**
   * Event that is sent when the {@link MockWebsocketHook#getHeartBeatMessage()}
   * method is called.
   */
  GET_HEARTBEAT_MESSAGE;
}
