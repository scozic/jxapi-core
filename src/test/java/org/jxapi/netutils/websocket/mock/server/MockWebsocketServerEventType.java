package org.jxapi.netutils.websocket.mock.server;

/**
 * Type of events for {@link MockWebsocketServerEvent}.
 * 
 * @see MockWebsocketServerEvent
 * @see MockWebsocketServerSession
 */
public enum MockWebsocketServerEventType {

  /**
   * Event type for a client connect event, issued when a client connects to the websocket server.
   */
  CLIENT_CONNECT,

  /**
   * Event type for a client disconnect event, issued when a client disconnects from the websocket server.
   */
  CLIENT_DISCONNECT,

  /**
   * Event type for a message received event, issued when a message is received from a client.
   */
  MESSAGE_RECEIVED
}
