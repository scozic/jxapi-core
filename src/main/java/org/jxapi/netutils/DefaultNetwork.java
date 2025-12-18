package org.jxapi.netutils;

import java.util.List;
import java.util.Map;

import org.jxapi.netutils.rest.HttpClient;
import org.jxapi.netutils.websocket.WebsocketManager;
import org.jxapi.util.CollectionUtil;

public class DefaultNetwork implements Network {
  
  private final Map<String, HttpClient> httpClients = CollectionUtil.createMap();
  private final Map<String, WebsocketManager> websockets = CollectionUtil.createMap();

  public void registerHttpClient(String clientId, HttpClient httpClient) {
    this.httpClients.put(clientId, httpClient);
  }
  
  public void registerWebsocket(String wsId, WebsocketManager websocketManager) {
    this.websockets.put(wsId, websocketManager);
  }

  @Override
  public HttpClient getHttpClient(String clientId) {
    return this.httpClients.get(clientId);
  }

  @Override
  public WebsocketManager getWebsocket(String wsId) {
    return this.websockets.get(wsId);
  }
  
  public final List<String> getRegisteredHttpClientIds() {
    return this.httpClients.keySet().stream().toList();
  }
  
  public final List<String> getRegisteredWebsocketIds() {
    return this.websockets.keySet().stream().toList();
  }

}
