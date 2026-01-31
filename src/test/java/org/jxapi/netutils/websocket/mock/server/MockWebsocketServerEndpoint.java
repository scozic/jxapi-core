package org.jxapi.netutils.websocket.mock.server;

import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link ServerEndpoint} implementation for the mock websocket server.
 * <p>
 * This class is used to handle websocket connections and messages.
 * Incomiming connections, disconnections and messages are dispatched to the
 * {@link MockWebsocketServerSessionService}.
 * 
 * @see MockWebsocketServerSessionService
 * @see ServerEndpoint
 */
@ServerEndpoint("/ws")
public class MockWebsocketServerEndpoint {

  private static final Logger log = LoggerFactory.getLogger(MockWebsocketServerEndpoint.class);

  @OnOpen
  public void onOpen(Session session) {
    MockWebsocketServerSession mockSession = new MockWebsocketServerSessionImpl(session);
    log.debug("New session opened:{}", mockSession);
    MockWebsocketServerSessionService.registerNewSession(mockSession);
  }

  @OnMessage
  public String onMessage(String message, Session session) {
    String uri = session.getRequestURI().toString();
    String sessionId = session.getId();
    log.debug("Received message on WS:{}, sessionId:{}:[{}]", uri, sessionId, message);
    MockWebsocketServerSessionService.dispatchWebsocketClientMessageEvent(uri, session.getId(), message);

    return null;
  }

  @OnClose
  public void onClose(Session session, CloseReason closeReason) {
    MockWebsocketServerSession mockSession = new MockWebsocketServerSessionImpl(session);
    log.debug("Session closed:{}", mockSession);
    MockWebsocketServerSessionService.unregisterClosedSession(mockSession);
  }

}
