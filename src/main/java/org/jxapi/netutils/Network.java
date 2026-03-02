package org.jxapi.netutils;

import org.jxapi.netutils.rest.HttpClient;
import org.jxapi.netutils.websocket.WebsocketClient;
import org.jxapi.util.Disposable;

/**
 * Network interface providing HTTP clients and WebSocket managers.
 */
public interface Network extends Disposable {

  /**
   * Retrieves the {@link HttpClient} associated with the given client ID.
   * 
   * @param clientId the ID of the HTTP client to retrieve
   * @return the {@link HttpClient} associated with the given client ID, or
   *         <code>null</code> if no such client is registered
   */
  HttpClient getHttpClient(String clientId);
  
  /**
   * Retrieves the {@link WebsocketClient} associated with the given websocket ID.
   * 
   * @param wsId the ID of the WebSocket client to retrieve
   * @return the {@link WebsocketClient} associated with the given websocket ID, or
   *         <code>null</code> if no such client is registered
   */
  WebsocketClient getWebsocket(String wsId);
}
