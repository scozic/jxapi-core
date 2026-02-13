package org.jxapi.netutils.websocket.mock.server;

import java.io.IOException;

import org.jxapi.netutils.websocket.RawWebsocketMessageHandler;
import org.jxapi.observability.Observable;

/**
 * Mock implementation of a websocket server session. It can be used to test
 * websocket clients.
 * <p>
 * The session can send messages to the client.
 * <p>
 * The session has an id and a URL.
 * <p>
 * Extends {@link Observable} to handle events from
 * {@link RawWebsocketMessageHandler}. Events received from the message handler
 * are stored if the session is connected.
 * 
 * @see Observable
 * @see RawWebsocketMessageHandler
 */
public interface MockWebsocketServerSession {

  /**
   * @return Unique identifier of the session
   */
  String getId();
  
  /**
   * @return URI of the session e.g. /myapp/ws when server has appName 'myapp'.
   */
  String getUri();

  /**
   * Sends a message to the client.
   * 
   * @param message The message to send
   * @throws IOException If an error occurs while sending the message
   */
  void sendSync(String message) throws IOException;

}