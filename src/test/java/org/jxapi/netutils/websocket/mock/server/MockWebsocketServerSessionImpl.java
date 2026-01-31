package org.jxapi.netutils.websocket.mock.server;

import java.io.IOException;
import java.util.Objects;

import jakarta.websocket.Session;

/**
 * Default implementation of the {@link MockWebsocketServerSession} interface
 * encapsulating a {@link Session}.
 * 
 * @see MockWebsocketServerSession
 * @see Session
 */
public class MockWebsocketServerSessionImpl implements MockWebsocketServerSession {

  private final Session session;

  /**
   * Creates a new instance of the {@link MockWebsocketServerSessionImpl} class.
   * 
   * @param session The {@link Session} to encapsulate
   */
  public MockWebsocketServerSessionImpl(Session session) {
    this.session = session;
  }

  /**
   * @return The encapsulated {@link Session}
   */
  public Session getSession() {
    return session;
  }

  @Override
  public void sendSync(String message) throws IOException {
    session.getBasicRemote().sendText(message);
  }

  @Override
  public String getId() {
    return session.getId();
  }

  @Override
  public String getUri() {
    return session.getRequestURI().toString();
  }

  /**
   * Compares this session with another object. Two sessions are considered equal
   * if their IDs and URIs are equal.
   */
  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    if (!(other instanceof MockWebsocketServerSessionImpl)) {
      return false;
    }
    MockWebsocketServerSession o = (MockWebsocketServerSession) other;
    return Objects.equals(getId(), o.getId()) && Objects.equals(getUri(), o.getUri());
  }

  /**
   * @return The hash code of the session. The hash code is calculated based on
   *         the URI and the ID.
   */
  @Override
  public int hashCode() {
    return Objects.hash(getUri(), getId());
  }

  /**
   * @return A string representation of the session including the URI and the ID.
   */
  @Override
  public String toString() {
    return getClass().getSimpleName() + "[" + getUri() + "][" + getId() + "]";
  }
}
