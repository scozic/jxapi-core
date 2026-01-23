package org.jxapi.netutils.websocket.mock.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.websocket.DeploymentException;


import org.glassfish.tyrus.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jxapi.observability.GenericObserver;

/**
 * Mock implementation of a websocket server. It can be used to test websocket
 * clients.
 * Its implementation is based on the Tyrus websocket server.
 * <p>
 * The server can be started and stopped. It can send messages to all connected
 * clients.
 * <p>
 * The server listens on a specific port and a specific application name, which
 * is used in the URI. The actual URI is ws://localhost:port/appName/ws where
 * appName is the application name and port is the port, for example
 * ws://localhost:8080/myapp/ws.
 * <p>
 * Extends {@link GenericObserver} to handle events from
 * {@link MockWebsocketServerSessionService}. Events received from the session
 * service are stored if the server is started and the event is related to the
 * server (matches /appName/ws URI).
 * 
 * @see GenericObserver
 * @see MockWebsocketServerEvent
 * @see Server
 */
public class MockWebsocketServer extends GenericObserver<MockWebsocketServerEvent> {
  
  private static final Logger log = LoggerFactory.getLogger(MockWebsocketServer.class);

  private final int port;
  private final String appName;
  private Server server;
  private final MockWebsocketServerListener mockWebsocketServerListener = this::handle;
  private final String uri;
  private final String url;
  private final List<MockWebsocketServerSession> sessions = new ArrayList<>();
  private boolean started = false;
  
  /**
   * Creates a new MockWebsocketServer instance.
   * 
   * @param port The port on which the server listens
   * @param appName The application name which is used in the URI, for example 'myapp'
   */
  public MockWebsocketServer(int port, String appName) {
    this.port = port;
    this.appName = appName;
    uri = "/" + appName + "/ws";
    url = "ws://localhost:" + port + uri;
    
  }
  
  private void handle(MockWebsocketServerEvent e) {
    if (!isStarted() || !e.getSession().getUri().equals(url)) {
      return;
    }
    log.debug("handle:{}", e);
    if (e.getType() == MockWebsocketServerEventType.CLIENT_CONNECT) {
      addSession(e.getSession());
    }
    if (e.getType() == MockWebsocketServerEventType.CLIENT_DISCONNECT) {
      removeSession(e.getSession());
    }
    handleEvent(e);
  }
  
  private synchronized void addSession(MockWebsocketServerSession session) {
    sessions.add(session);
  }
  
  private synchronized boolean removeSession(MockWebsocketServerSession session) {
    return sessions.remove(session);
  }
  
  /**
   * Starts the server. If the server is already started, this method does
   * nothing.
   * 
   * @throws Exception If the server cannot be started
   */
  public synchronized void start() throws IOException {
    if (isStarted()) {
      return;
    }
      server = new Server("localhost", port, "/" + appName, null, MockWebsocketServerEndpoint.class);
      MockWebsocketServerSessionService.subscribeListener(mockWebsocketServerListener);
      started = true;
      try {
      server.start();
    } catch (DeploymentException e) {
      throw new IOException("Failed to start websocket server " + uri, e);
    }
      log.info("Started WS server on port {}", port);
  }
  
  /**
   * @return The local URL of the server
   */
  public String getUrl() {
    return url;
  }
  
  /**
   * Stops the server. If the server is not started, this method does nothing.
   */
  public synchronized void stop() {
    started = false;
    if (server != null) {
      sessions.clear();
      server.stop();
      server = null;
      MockWebsocketServerSessionService.unsubscribeListener(mockWebsocketServerListener);
        log.info("Stopped WS server on port {}", port);
    }
  }

  /**
   * Returns whether the server is started.
   * 
   * @return true if the server is started, false otherwise
   */
  public synchronized boolean isStarted() {
    return started;
  }
  
  /**
   * Sends a message to all connected clients.
   * 
   * @param message The message to send
   * @throws IOException If the message cannot be sent
   */
  public synchronized void sendMessageToClients(String message) throws IOException {
    for (MockWebsocketServerSession s : sessions) {
      s.sendSync(message);
    }
  }
  
  /**
   * @return String representation carrying the server's URI.
   */
  public String toString() {
    return getClass().getSimpleName() + ":" + getUrl();
  }
}
