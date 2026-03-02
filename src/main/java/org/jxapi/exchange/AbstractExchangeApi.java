
package org.jxapi.exchange;

import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.netutils.rest.HttpClient;
import org.jxapi.netutils.rest.HttpRequestExecutor;
import org.jxapi.netutils.rest.HttpRequestInterceptor;
import org.jxapi.netutils.rest.PaginatedRestEndpoint;
import org.jxapi.netutils.rest.RestEndpoint;
import org.jxapi.netutils.rest.pagination.PaginatedRestRequest;
import org.jxapi.netutils.rest.pagination.PaginatedRestResponse;
import org.jxapi.netutils.websocket.DefaultWebsocketEndpoint;
import org.jxapi.netutils.websocket.WebsocketClient;
import org.jxapi.netutils.websocket.WebsocketEndpoint;
import org.jxapi.observability.Observable;
import org.jxapi.util.EncodingUtil;

/**
 * The AbstractExchangeApi class is an abstract base class that provides common
 * functionality and structure for exchange APIs.
 * It implements the ExchangeApi interface and provides default implementations
 * for some of its methods.
 * It is designed to be used as a base class by ExchangeApiGenerator to generate
 * concrete exchange API classes.
 * <br>
 * This class contains properties and methods that are shared by all exchange
 * APIs, such as the exchange name, exchange ID,
 * properties, request throttler, HTTP request executor, and observable for
 * handling exchange API events.
 * <br>
 * Subclasses of AbstractExchangeApi are expected to provide concrete
 * implementations for the remaining methods defined in the
 * ExchangeApi interface.
 * <br>
 * This class also provides helper methods for creating HTTP request
 * interceptors, HTTP request executors, and websocket managers.
 * 
 * @see ExchangeApi
 * @see HttpRequestInterceptor
 * @see HttpRequestExecutor
 * @see WebsocketClient
 * @see ExchangeObserver
 * @see ExchangeEvent
 * @see Observable
 */
public abstract class AbstractExchangeApi implements ExchangeApi {

  /**
   * The name of this exchange API group.
   */
  protected final String name;
  
  /**
   * The the exchange instance associated with this API.
   */
  protected final Exchange exchange;
  
  /**
   * The base HTTP URL for all REST endpoints of this API group.
   */
  protected final String httpUrl;
  
  private final ExchangeObserver exchangeObserver;
  private final ExchangeObserver apiObserver = this::dispatchApiEvent;
  
  /**
   * Creates a new AbstractExchangeApi instance with the specified API name,
   * exchange name, exchange ID, properties, and request throttler.
   * 
   * @param apiName             The name of the API.
   * @param exchange            The exchange instance associated with this API.
   * @param exchangeApiObserver The observer to handle exchange API events.
   * @param httpUrl             The base HTTP URL for all REST endpoints of this
   *                            API group.
   */
  protected AbstractExchangeApi(String apiName, 
                                Exchange exchange, 
                                ExchangeObserver exchangeApiObserver,
                                String httpUrl) {
    this.name = apiName;
    this.exchange = exchange;
    this.httpUrl = EncodingUtil.buildUrl(exchange.getHttpUrl(), httpUrl);
    this.exchangeObserver = exchangeApiObserver;
  }
  
  /**
   * Dispatches the specified exchange API event to all observers.
   * 
   * @param event The exchange API event to dispatch.
   */
  protected void dispatchApiEvent(ExchangeEvent event) {
    event.setExchangeApiName(name);
    this.exchangeObserver.handleEvent(event);
  }
  
  /**
   * Creates a new WebsocketEndpoint instance with the specified endpoint name,
   * websocket client ID, and message deserializer.
   * 
   * @param <M>                 The type of messages handled by the websocket
   *                            endpoint.
   * @param endpointName       The name of the websocket endpoint.
   * @param wsClientId         The ID of the websocket client to use for this
   *                           endpoint.
   * @param messageDeserializer The message deserializer to use for this endpoint.
   * @return A new WebsocketEndpoint instance.
   * @throws IllegalStateException If no websocket client is found with the
   *                               specified ID.
   */
  protected <M> WebsocketEndpoint<M> createWebsocketEndpoint(String endpointName, String wsClientId, MessageDeserializer<M> messageDeserializer) {
    WebsocketClient websocketClient = exchange.getNetwork().getWebsocket(wsClientId);
    if (websocketClient == null) {
      throw new IllegalStateException("Cannot create websocket endpoint as no websocket manager is set");
    }
    DefaultWebsocketEndpoint<M> websocketEndpoint = new DefaultWebsocketEndpoint<>();
    websocketEndpoint.setEndpointName(endpointName);
    websocketEndpoint.setMessageDeserializer(messageDeserializer);
    websocketEndpoint.setWebsocketClient(websocketClient);
    websocketEndpoint.setObserver(apiObserver);
    return websocketEndpoint;
  }
  
  /**
   * Creates a new RestEndpoint instance with the specified endpoint name and
   * HTTP client ID.
   * 
   * @param <R>          The type of REST request handled by the endpoint.
   * @param <A>          The type of REST response handled by the endpoint.
   * @param name        The name of the REST endpoint.
   * @param httpClientId The ID of the HTTP client to use for this endpoint.
   * @return A new RestEndpoint instance.
   * @throws IllegalStateException If no HTTP client is found with the specified
   *                               ID.
   */
  protected <R, A> RestEndpoint<R, A> createRestEndpoint(String name, String httpClientId) {
    return initRestEndpoint(new RestEndpoint<>(), name, httpClientId);
  }
  
  /**
   * Creates a new PaginatedRestEndpoint instance with the specified endpoint
   * name and HTTP client ID.
   * 
   * @param <R>          The type of paginated REST request handled by the
   *                     endpoint.
   * @param <A>          The type of paginated REST response handled by the
   *                     endpoint.
   * @param name        The name of the paginated REST endpoint.
   * @param httpClientId The ID of the HTTP client to use for this endpoint.
   * @return A new PaginatedRestEndpoint instance.
   * @throws IllegalStateException If no HTTP client is found with the specified
   *                               ID.
   */
  protected <R extends PaginatedRestRequest, A extends PaginatedRestResponse> RestEndpoint<R, A> createPaginatedRestEndpoint(String name, String httpClientId) {
    return initRestEndpoint(new PaginatedRestEndpoint<>(), name, httpClientId);
  }
  
  private <R, A> RestEndpoint<R, A> initRestEndpoint(RestEndpoint<R, A> restEndpoint, String name, String httpClientId) {
    HttpClient httpClient = exchange.getNetwork().getHttpClient(httpClientId);
    if (httpClient == null) {
      throw new IllegalStateException(String.format("[%s]:Cannot create REST endpoint [%s] as no HTTP client exists with ID [%s]", this.name, name, httpClientId));
    }
    restEndpoint.setName(name);
    restEndpoint.setHttpClient(httpClient);
    restEndpoint.setObserver(apiObserver);
    return restEndpoint;
  }
}
