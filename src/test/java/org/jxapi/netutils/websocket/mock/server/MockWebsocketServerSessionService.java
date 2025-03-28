package org.jxapi.netutils.websocket.mock.server;

import java.util.HashMap;
import java.util.Map;

import org.jxapi.observability.SynchronizedObservable;

/**
 * Service class for the mock websocket server sessions.
 * <p>
 * The service manages the sessions and dispatches events to the listeners.
 * <p>
 * The service is a singleton.
 * <p>
 * The service is thread-safe and singleton. It can be accessed by any
 * {@link MockWebsocketServerEndpoint} to register and unregister sessions and
 * to dispatch events, and by any MockWebsocketServer that will subcribe as
 * listener to receive events they will filter for sessions mapped to their URI.
 * 
 * @see MockWebsocketServerSession
 * @see MockWebsocketServerListener
 * @see MockWebsocketServerEvent
 */
public class MockWebsocketServerSessionService extends SynchronizedObservable<MockWebsocketServerListener, MockWebsocketServerEvent> {

  private static final MockWebsocketServerSessionService INSTANCE = new MockWebsocketServerSessionService();
  
  private static final MockWebsocketServerSessionService get() {
    return INSTANCE;
  }
  
  private final Map<String, Map<String, MockWebsocketServerSession>> sessions = new HashMap<>();
  
  private MockWebsocketServerSessionService() {
    super((l, e) -> l.handleEvent(e));
  }
  
  private synchronized void register(MockWebsocketServerSession session) {
    Map<String, MockWebsocketServerSession> sessionsForUrl = sessions.get(session.getUri());
    if (sessionsForUrl == null) {
      sessionsForUrl = new HashMap<>();
      sessions.put(session.getUri(), sessionsForUrl);
    }
    sessionsForUrl.put(session.getId(), session);
    dispatch(MockWebsocketServerEvent.createClientConnectEvent(session));
  }
  
  private synchronized boolean unregister(MockWebsocketServerSession session) {
    boolean res = false;
    Map<String, MockWebsocketServerSession> sessionsForUrl = sessions.get(session.getUri());
    if (sessionsForUrl != null) {
      res = sessionsForUrl.remove(session.getId()) != null;
      if (sessionsForUrl.isEmpty()) {
        sessions.remove(session.getUri());
      }
    }
    if (res) {
      dispatch(MockWebsocketServerEvent.createClientDisconnectEvent(session));
    }
    return res;
  }
  
  private void dispatchMessage(String sessionUrl, String sessionId, String message) {
    MockWebsocketServerSession session = null;
    synchronized (this) {
      Map<String, MockWebsocketServerSession> sessionsForUrl = sessions.get(sessionUrl);
      if (sessionsForUrl != null) {
        session = sessionsForUrl.get(sessionId);
      }
    }
    if (session != null) {
      dispatch(MockWebsocketServerEvent.createMessageReeivedEvent(session, message));
    }
  }
   
  /**
   * Registers a new session.
   * @param websocketServerSession The session to register
   */
  public static void registerNewSession(MockWebsocketServerSession websocketServerSession) {
    get().register(websocketServerSession);
  }
  
  /**
   * Unregisters a closed session.
   * @param websocketServerSession The session to unregister
   * @return true if the session was unregistered, false otherwise
   */
  public static boolean unregisterClosedSession(MockWebsocketServerSession websocketServerSession) {
    return get().unregister(websocketServerSession);
  }
  
  /**
   * Subscribes a listener to receive events.
   * @param listener The listener to subscribe
   */
  public static void subscribeListener(MockWebsocketServerListener listener) {
    get().subscribe(listener);
  }
  
  /**
   * Unsubscribes a listener.
   * @param listener The listener to unsubscribe
   * @return true if the listener was unsubscribed, false otherwise
   */
  public static boolean unsubscribeListener(MockWebsocketServerListener listener) {
    return get().unsubscribe(listener);
  }
  
  /**
   * Dispatches a message event received from a client connection.
   * @param sessionUrl The URI of the session
   * @param sessionId The ID of the session
   * @param message The message received
   */
  public static void dispatchWebsocketClientMessageEvent(String sessionUrl, String sessionId, String message) {
    get().dispatchMessage(sessionUrl, sessionId, message);
  }

}
