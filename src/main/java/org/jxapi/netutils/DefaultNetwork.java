package org.jxapi.netutils;

import java.util.List;
import java.util.Map;

import org.jxapi.netutils.rest.HttpClient;
import org.jxapi.netutils.websocket.WebsocketClient;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.DefaultDisposable;
import org.jxapi.util.Disposable;

/**
 * Default implementation of the {@link Network} interface. This class allows
 * registering and retrieving {@link HttpClient} and {@link WebsocketClient}
 * instances by their IDs. It also implements the {@link Disposable} interface to
 * dispose of all registered clients when the network is disposed.
 */
public class DefaultNetwork extends DefaultDisposable implements Network {
  
  private final Map<String, HttpClient> httpClients = CollectionUtil.createMap();
  private final Map<String, WebsocketClient> websockets = CollectionUtil.createMap();

  /**
   * Registers an {@link HttpClient} with the given client ID. The client ID can
   * be used to retrieve the registered {@link HttpClient} using the
   * {@link #getHttpClient(String)} method.
   * 
   * @param clientId   the ID to register the {@link HttpClient} with
   * @param httpClient the {@link HttpClient} to register
   */
  public void registerHttpClient(String clientId, HttpClient httpClient) {
    this.httpClients.put(clientId, httpClient);
  }

  /**
   * Registers a {@link WebsocketClient} with the given websocket ID. The websocket
   * @param wsId the ID to register the {@link WebsocketClient} with
   * @param websocketClient the {@link WebsocketClient} to register
   */
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
  
  /**
   * Returns a list of all registered HTTP client IDs.
   * @return a list of all registered HTTP client IDs
   */
  public final List<String> getRegisteredHttpClientIds() {
    return this.httpClients.keySet().stream().toList();
  }
  
  
  /**
   * Returns a list of all registered websocket IDs.
   * @return a list of all registered websocket IDs
   */
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
