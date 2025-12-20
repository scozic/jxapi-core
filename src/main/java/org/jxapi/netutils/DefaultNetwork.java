package org.jxapi.netutils;

import java.util.List;
import java.util.Map;

import org.jxapi.netutils.rest.HttpClient;
import org.jxapi.netutils.websocket.WebsocketClient;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.DefaultDisposable;

public class DefaultNetwork extends DefaultDisposable implements Network {
  
  private final Map<String, HttpClient> httpClients = CollectionUtil.createMap();
  private final Map<String, WebsocketClient> websockets = CollectionUtil.createMap();

  public void registerHttpClient(String clientId, HttpClient httpClient) {
    this.httpClients.put(clientId, httpClient);
  }
  
  public void registerWebsocket(String wsId, WebsocketClient websocketClient) {
    this.websockets.put(wsId, websocketClient);
  }

  @Override
  public HttpClient getHttpClient(String clientId) {
    return this.httpClients.get(clientId);
  }

  @Override
  public WebsocketClient getWebsocket(String wsId) {
    return this.websockets.get(wsId);
  }
  
  public final List<String> getRegisteredHttpClientIds() {
    return this.httpClients.keySet().stream().toList();
  }
  
  public final List<String> getRegisteredWebsocketIds() {
    return this.websockets.keySet().stream().toList();
  }

  @Override
  public void dispose() {
    super.dispose();
    for (HttpClient httpClient : this.httpClients.values()) {
      httpClient.dispose();
    }
    for (WebsocketClient websocketClient : this.websockets.values()) {
      websocketClient.dispose();
    }
  }

}
