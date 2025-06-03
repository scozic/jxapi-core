
package org.jxapi.exchange;

import java.net.http.HttpClient;
import java.util.Properties;

import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.netutils.rest.FutureRestResponse;
import org.jxapi.netutils.rest.HttpMethod;
import org.jxapi.netutils.rest.HttpRequest;
import org.jxapi.netutils.rest.HttpRequestExecutor;
import org.jxapi.netutils.rest.HttpRequestExecutorFactory;
import org.jxapi.netutils.rest.HttpRequestInterceptor;
import org.jxapi.netutils.rest.HttpRequestInterceptorFactory;
import org.jxapi.netutils.rest.RestResponse;
import org.jxapi.netutils.rest.javanet.JavaNetHttpRequestExecutor;
import org.jxapi.netutils.rest.ratelimits.RequestThrottler;
import org.jxapi.netutils.rest.ratelimits.RequestThrottlingMode;
import org.jxapi.netutils.websocket.DefaultWebsocketEndpoint;
import org.jxapi.netutils.websocket.DefaultWebsocketFactory;
import org.jxapi.netutils.websocket.DefaultWebsocketManager;
import org.jxapi.netutils.websocket.Websocket;
import org.jxapi.netutils.websocket.WebsocketEndpoint;
import org.jxapi.netutils.websocket.WebsocketFactory;
import org.jxapi.netutils.websocket.WebsocketHook;
import org.jxapi.netutils.websocket.WebsocketHookFactory;
import org.jxapi.netutils.websocket.WebsocketManager;
import org.jxapi.observability.Observable;
import org.jxapi.observability.SynchronizedObservable;
import org.jxapi.util.DefaultDisposable;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.FactoryUtil;
import org.jxapi.util.JsonUtil;
import org.jxapi.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * @see WebsocketManager
 * @see ExchangeApiObserver
 * @see ExchangeApiEvent
 * @see Observable
 */
public abstract class AbstractExchangeApi extends DefaultDisposable implements ExchangeApi {
  
  private static final Logger log = LoggerFactory.getLogger(AbstractExchangeApi.class);

  /**
   * The name of this exchange API group.
   */
  protected final String name;
  
  /**
   * The the exchange instance associated with this API.
   */
  protected final Exchange exchange;
  
  /**
   * The request throttler used for REST request rate limiting.
   */
  protected final RequestThrottler requestThrottler;
  
  /**
   * The base HTTP URL for all REST endpoints of this API group.
   * @see #getHttpUrl()
   */
  protected final String httpUrl;
  
  /**
   * The base WebSocket URL used for websocket connections.
   * 
   * @see #getWsUrl()
   */
  protected final String wsUrl;
  
  /**
   * The HTTP request executor used to submit REST APIrequests.
   */
  protected HttpRequestExecutor httpRequestExecutor = null;
  
  /**
   * The HTTP request interceptor used to intercept REST API requests.
   */
  protected HttpRequestInterceptor httpRequestInterceptor = null;
  
  /**
   * The websocket manager used to manage websocket connections
   */
  protected WebsocketManager websocketManager = null;
  
  /**
   * The observable used to handle exchange API events.
   */
  protected final Observable<ExchangeApiObserver, ExchangeApiEvent> observable 
            = new SynchronizedObservable<>(ExchangeApiObserver::handleEvent);
  
  /**
   * Creates a new AbstractExchangeApi instance with the specified API name,
   * exchange name, exchange ID, and properties.<p>
   * Request throttler, httop URL and websocket URL are set to <code>null</code>.<br>
   * 
   * @param apiName      The name of the API.
   * @param exchange     The exchange instance associated with this API.
   */
  protected AbstractExchangeApi(String apiName, Exchange exchange) {
    this(apiName, exchange, null, null, null);
  }  

  /**
   * Creates a new AbstractExchangeApi instance with the specified API name,
   * exchange name, exchange ID, properties, and request throttler.
   * 
   * @param apiName          The name of the API.
   * @param exchange         The exchange instance associated with this API.
   * @param requestThrottler The request throttler to use for rate limiting.
   * @param httpUrl          The base HTTP URL for all REST endpoints of this API group.
   * @param wsUrl            The base WebSocket URL used for websocket connections.
   */
  protected AbstractExchangeApi(String apiName, 
                                Exchange exchange, 
                                RequestThrottler requestThrottler,
                                String httpUrl,
                                String wsUrl) {
    this.name = apiName;
    this.exchange = exchange;
    this.httpUrl = EncodingUtil.buildUrl(exchange.getHttpUrl(), httpUrl);
    this.wsUrl = EncodingUtil.buildUrl(exchange.getWsUrl(), wsUrl);
    this.requestThrottler = requestThrottler;
    if (requestThrottler != null) {
      applyRequestThrottlerProperties();
    }
  }
  
  private void applyRequestThrottlerProperties() {
    Properties properties = exchange.getProperties();
    String requestThrottlingModeValue = properties.getProperty(CommonConfigProperties.REQUEST_THROTTLING_MODE_PROPERTY.getName());
    if (requestThrottlingModeValue != null) {
      requestThrottler.setThrottlingMode(RequestThrottlingMode.valueOf(requestThrottlingModeValue));
    }
    Long maxThrottleDelay = PropertiesUtil.getLong(properties, CommonConfigProperties.MAX_REQUEST_THROTTLE_DELAY_PROPERTY.getName(), null);
    if (maxThrottleDelay != null) {
      requestThrottler.setMaxThrottleDelay(maxThrottleDelay);
    }
  }

  /**
   * Returns the name of the exchange instance associated with this API.
   * @return The name of the exchange instance.
   */
  @Override
  public Exchange getExchange() {
    return exchange;
  }
  
  @Override
  public String getName() {
    return name;
  }
  
  /**
   * @return the request executor used by this exchange API group.
   */
  public HttpRequestExecutor getHttpRequestExecutor() {
    return httpRequestExecutor;
  }

  /**
   * @param httpRequestExecutor the request executor used by this exchange API group.
   */
  public void setHttpRequestExecutor(HttpRequestExecutor httpRequestExecutor) {
    this.httpRequestExecutor = httpRequestExecutor;
  }
  
  @Override
  public void subscribeObserver(ExchangeApiObserver exchangeApiObserver) {
    this.observable.subscribe(exchangeApiObserver);
  }

  @Override
  public boolean unsubscribeObserver(ExchangeApiObserver exchangeApiObserver) {
    return this.observable.unsubscribe(exchangeApiObserver);
  }
  
  /**
   * Instantiates HTTP request interceptor using the specified factory class name.
   * Should be called by subclasses to create the HTTP request interceptor if
   * there is at least one REST API and a {@link HttpRequestInterceptorFactory}
   * class name is specified.
   * 
   * @param factoryClassName The fully qualified class name of the factory class
   *                         that creates the HTTP request interceptor.
   */
  protected void createHttpRequestInterceptor(String factoryClassName) {
    httpRequestInterceptor = ((HttpRequestInterceptorFactory) FactoryUtil.fromClassName(factoryClassName)).createInterceptor(this);
  }
  
  /**
   * Instantiates HTTP request executor using the specified factory class name, or
   * the default executor factory which is usually sufficient. Should be called by
   * subclasses to create the HTTP request executor if there is at least one REST
   * API.
   * 
   * @param factoryClassName The fully qualified class name of the factory class
   *                         that creates the HTTP request executor, or null if
   *                         the default executor factory
   *                         {@link JavaNetHttpRequestExecutor} should be used.
   * @param defaultRequestTimeout   the request timeout used on executor, see
   *                         {@link HttpRequestExecutor#setRequestTimeout(long)}.
   *                         If value is &lt; 0, the default
   *                         {@link HttpRequestExecutor#DEFAULT_REQUEST_TIMEOUT}
   *                         is used.
   */
  protected void createHttpRequestExecutor(String factoryClassName, long defaultRequestTimeout) {
    if  (factoryClassName != null) {
      httpRequestExecutor = ((HttpRequestExecutorFactory)  FactoryUtil.fromClassName(factoryClassName)).createExecutor(this);
    } else {
      httpRequestExecutor = new JavaNetHttpRequestExecutor(HttpClient.newHttpClient());
    }
    long requestTimeout = PropertiesUtil.getLong(
        getProperties(), 
        CommonConfigProperties.HTTP_REQUEST_TIMEOUT_PROPERTY.getName(), 
        defaultRequestTimeout);
    if (requestTimeout >= 0) {
      httpRequestExecutor.setRequestTimeout(requestTimeout);
    }
  }
  
  /**
   * Submits a REST request asynchronously using the specified request and message deserializer to deserialize response.
   * If a request throttler is set, the request is submitted through the throttler.
   * <br>
   * This method should used by subclasses to submit REST requests.
   * @param <A> The type of the response to the request
   * @param request The request to submit
   * @param deserializer The deserializer to use to deserialize the response
   * @return The response to the request, as a {@link FutureRestResponse}
   */
  protected <A> FutureRestResponse<A> submit(HttpRequest request, MessageDeserializer<A> deserializer) {
    log.debug("{} {} > {}", request.getHttpMethod(), request.getEndpoint(), request);
    dispatchApiEvent(ExchangeApiEvent.createHttpRequestEvent(request));
    if (request.getHttpMethod().requestHasBody && request.getRequest() != null) {
      serializeRequestBody(request);
    }
    if (requestThrottler != null) {
      return requestThrottler.submit(request, r -> { 
        try {
          return submitNow(r, deserializer);
        } catch (Exception ex) {
          log.error("Error submitting request " + request, ex);
          FutureRestResponse<A> callback = new FutureRestResponse<>();
          RestResponse<A> response = new RestResponse<>();
          response.setException(ex);
          callback.complete(response);
          return callback;
        }
      });
    } else {
      return submitNow(request, deserializer);
    }
  }

  private <A> FutureRestResponse<A> submitNow(HttpRequest request, MessageDeserializer<A> deserializer) {
    if (httpRequestExecutor == null) {
      throw new IllegalStateException("No " + HttpRequestExecutor.class.getSimpleName() + " set");
    }
    if (httpRequestInterceptor != null) {
      httpRequestInterceptor.intercept(request);
    }
    
    FutureRestResponse<A> callback = new FutureRestResponse<>();
    httpRequestExecutor.execute(request).thenAccept(httpResponse -> {
      RestResponse<A> response = new RestResponse<>(httpResponse);
      response.setHttpStatus(httpResponse.getResponseCode());
      if(response.isOk()) {
        try {
          response.setResponse(deserializer.deserialize(httpResponse.getBody()));
        } catch (Exception ex) {
          response.setException(ex);
        }
      }
      log.debug("{} {} < {}", request.getHttpMethod(), request.getEndpoint(), response);
      dispatchApiEvent(ExchangeApiEvent.createRestResponseEvent(response));
      try {
        callback.complete(response);
      } catch (Exception ex) {
        log.error("Error completing request " + request, ex);
      }
    });
    return callback;
  }
  
  /**
   * Creates a websocket manager using the specified URL, websocket factory class name, and websocket hook factory class name.
   * Should be called by subclasses to create the websocket manager if there is at least one websocket endpoint.
   * 
   * @param url The URL of the websocket server.
   * @param websocketFactoryClassName The fully qualified class name of the websocket factory class that creates the websocket.
   * @param websocketHookFactoryClassName The fully qualified class name of the websocket hook factory class that creates the websocket hook.
   */
  protected void createWebsocketManager(String url, 
                      String websocketFactoryClassName, 
                      String websocketHookFactoryClassName) {
    WebsocketFactory websocketFactory = websocketFactoryClassName == null? 
                        new DefaultWebsocketFactory(): 
                        WebsocketFactory.fromClassName(websocketFactoryClassName);
    Websocket websocket = websocketFactory.createWebsocket(this);
    if (url != null) {
      websocket.setUrl(url);
    }
    WebsocketHook websocketHook = websocketHookFactoryClassName == null? 
                    null: 
                    WebsocketHookFactory.fromClassName(websocketHookFactoryClassName).createWebsocketHook(this);
    websocketManager = new DefaultWebsocketManager(this, websocket, websocketHook);
    websocketManager.subscribeErrorHandler(error -> dispatchApiEvent(ExchangeApiEvent.createWebsocketErrorEvent(error)));
  }
  
  /**
   * Creates a websocket endpoint with the specified name and message deserializer.
   * Should be called by subclasses to create websocket endpoints.
   * 
   * @param <M> The type of the message deserialized for this endpoint.
   * @param endpointName The name of the websocket endpoint.
   * @param messageDeserializer The message deserializer to use for deserializing messages received by the endpoint.
   * @return The created websocket endpoint.
   */
  protected <M> WebsocketEndpoint<M> createWebsocketEndpoint(String endpointName, 
                                 MessageDeserializer<M> messageDeserializer) {
    if (websocketManager == null) {
      throw new IllegalStateException("Cannot create websocket endpoint as no websocket manager is set");
    }
    return new DefaultWebsocketEndpoint<>(endpointName, 
                           websocketManager, 
                        messageDeserializer,
                        this::dispatchApiEvent);
  }
  
  /**
   * Dispatches the specified exchange API event to all observers.
   * <br>
   * Needs usually not be called by subclasses, as it is called for every call to
   * {@link #submit(HttpRequest, MessageDeserializer)} and
   * {@link #createWebsocketEndpoint(String, MessageDeserializer)}.
   * 
   * @param event The exchange API event to dispatch.
   */
  protected void dispatchApiEvent(ExchangeApiEvent event) {
    event.setExchangeName(exchange.getName());
    event.setExchangeApiName(name);
    event.setExchangeId(exchange.getId());
    observable.dispatch(event);
  }
  
  /**
   * When submitting a REST request call, outgoing HTTP request may carry a body,
   * this is usually the case for {@link HttpMethod#POST} method, see
   * {@link HttpMethod#requestHasBody}.<br>
   * This method is called when submitting such request and is responsible for
   * setting the body of incoming request serialized as String. Default
   * implementation will serialize the nested request POJO (see
   * {@link HttpRequest#getRequest()}) as JSON.
   * 
   * @param request HTTP Request to set the body for.
   * @see JsonUtil#pojoToJsonString(Object)
   * @see HttpRequest#getRequest()
   * @see HttpRequest#setBody(String)
   */
  protected void serializeRequestBody(HttpRequest request) {
    request.setBody(JsonUtil.pojoToJsonString(request.getRequest()));
  }
  
  @Override
  protected void doDispose() {
    if (requestThrottler != null) {
      requestThrottler.dispose();
    }
    if (httpRequestExecutor != null) {
      httpRequestExecutor.dispose();
    }
    if (websocketManager != null) {
      websocketManager.dispose();
    }
  }
  
  @Override
  public void setRequestThrottlingMode(RequestThrottlingMode requestThrottlingMode) {
    if (requestThrottler != null) {
      requestThrottler.setThrottlingMode(requestThrottlingMode);
    }
  }

  @Override
  public RequestThrottlingMode getRequestThrottlingMode() {
    if (requestThrottler != null) {
      return requestThrottler.getThrottlingMode();
    }
    return RequestThrottlingMode.NONE;
  }

  @Override
  public void setMaxRequestThrottleDelay(long maxRequestThrottleDelay) {
    if (requestThrottler != null) {
      requestThrottler.setMaxThrottleDelay(maxRequestThrottleDelay);
    }
  }

  @Override
  public long getMaxRequestThrottleDelay() {
    if (requestThrottler != null) {
      return requestThrottler.getMaxThrottleDelay();
    }
    return -1L;
  }
  
  @Override
  public void setHttpRequestTimeout(long httpRequesTimeout) {
    if (httpRequestExecutor != null) {
      httpRequestExecutor.setRequestTimeout(httpRequesTimeout);
    }
    
  }

  @Override
  public long getHttpRequestTimeout() {
    if (httpRequestExecutor != null) {
      return httpRequestExecutor.getRequestTimeout();
    }
    return -1L;
  }
  
  @Override
  public String getHttpUrl() {
    return httpUrl;
  }
  
  @Override
  public String getWsUrl() {
    return wsUrl;
  }
}
