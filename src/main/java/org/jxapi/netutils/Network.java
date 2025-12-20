package org.jxapi.netutils;

import org.jxapi.netutils.rest.HttpClient;
import org.jxapi.netutils.websocket.WebsocketClient;
import org.jxapi.util.Disposable;

/**
 * Network interface providing HTTP clients and WebSocket managers.
 */
public interface Network extends Disposable {

  HttpClient getHttpClient(String clientId);
  
  WebsocketClient getWebsocket(String wsId);
}
