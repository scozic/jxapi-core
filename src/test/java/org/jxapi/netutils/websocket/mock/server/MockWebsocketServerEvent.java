package org.jxapi.netutils.websocket.mock.server;

/**
 * Event class for the mock websocket server.
 * <p>
 * The event can be of type CLIENT_CONNECT, CLIENT_DISCONNECT or MESSAGE_RECEIVED.
 * <p>
 * The event is related to a specific session.
 * 
 * @see MockWebsocketServerEventType
 * @see MockWebsocketServerSession
 */
public class MockWebsocketServerEvent {
  
  /**
   * Creates a new event of type CLIENT_CONNECT.
   * @param session The session which is related to the event
   * @return a new event instance
   */
  public static MockWebsocketServerEvent createClientConnectEvent(MockWebsocketServerSession session) {
    MockWebsocketServerEvent e = new MockWebsocketServerEvent();
    e.setSession(session);
    e.setType(MockWebsocketServerEventType.CLIENT_CONNECT);
    return e;
  }
  
  /**
   * Creates a new event of type CLIENT_DISCONNECT.
   * @param session The session which is related to the event
   * @return a new event instance
   */
  public static MockWebsocketServerEvent createClientDisconnectEvent(MockWebsocketServerSession session) {
    MockWebsocketServerEvent e = new MockWebsocketServerEvent();
    e.setSession(session);
    e.setType(MockWebsocketServerEventType.CLIENT_DISCONNECT);
    return e;
  }
  
  /**
   * Creates a new event of type MESSAGE_RECEIVED.
   * @param session The session which is related to the event
   * @param message The message which was received
   * @return a new event instance
   */
  public static MockWebsocketServerEvent createMessageReeivedEvent(MockWebsocketServerSession session, String message) {
    MockWebsocketServerEvent e = new MockWebsocketServerEvent();
    e.setSession(session);
    e.setType(MockWebsocketServerEventType.MESSAGE_RECEIVED);
    e.setMessage(message);
    return e;
  }

  private MockWebsocketServerEventType type;
  
  private String message;
  
  private MockWebsocketServerSession session;

  public MockWebsocketServerEventType getType() {
    return type;
  }

  public void setType(MockWebsocketServerEventType type) {
    this.type = type;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public MockWebsocketServerSession getSession() {
    return session;
  }

  public void setSession(MockWebsocketServerSession session) {
    this.session = session;
  }
  
  @Override
  public String toString() {
    return getClass().getSimpleName() + "[" + type + "], " + session + (message == null? "": " message:" + message);
  }

}
