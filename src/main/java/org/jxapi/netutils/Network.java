package org.jxapi.netutils;

import org.jxapi.netutils.rest.HttpClient;
import org.jxapi.netutils.websocket.WebsocketManager;

/**
 * Network interface providing HTTP clients and WebSocket managers.
 */
public interface Network {

  HttpClient getHttpClient(String clientId);
  
  WebsocketManager getWebsocket(String wsId);
}
