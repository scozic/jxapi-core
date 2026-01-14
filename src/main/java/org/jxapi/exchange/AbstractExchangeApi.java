
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
import org.jxapi.util.DefaultDisposable;
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
public abstract class AbstractExchangeApi extends DefaultDisposable implements ExchangeApi {

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
  
  protected <R, A> RestEndpoint<R, A> createRestEndpoint(String name, String httpClientId) {
    return initRestEndpoint(new RestEndpoint<>(), name, httpClientId);
  }
  
  protected <R extends PaginatedRestRequest, A extends PaginatedRestResponse> RestEndpoint<R, A> createPaginatedRestEndpoint(String name, String httpClientId) {
    return initRestEndpoint(new PaginatedRestEndpoint<>(), name, httpClientId);
  }
  
  private <R, A> RestEndpoint<R, A> initRestEndpoint(RestEndpoint<R, A> restEndpoint, String name, String httpClientId) {
    HttpClient httpClient = exchange.getNetwork().getHttpClient(httpClientId);
    if (httpClient == null) {
      throw new IllegalStateException(String.format("[%s]:Cannot create REST endpoint [%s] as no HTTP client exists with ID [%s]", this.name, name, httpClientId));
    }
    restEndpoint.setName(name);
    restEndpoint.setHttpRequestExecutor(httpClient);
    restEndpoint.setObserver(apiObserver);
    return restEndpoint;
  }
}
