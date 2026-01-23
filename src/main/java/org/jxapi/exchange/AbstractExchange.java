package org.jxapi.exchange;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.jxapi.netutils.DefaultNetwork;
import org.jxapi.netutils.Network;
import org.jxapi.netutils.rest.HttpClient;
import org.jxapi.netutils.rest.HttpRequestExecutor;
import org.jxapi.netutils.rest.HttpRequestExecutorFactory;
import org.jxapi.netutils.rest.HttpRequestInterceptor;
import org.jxapi.netutils.rest.HttpRequestInterceptorFactory;
import org.jxapi.netutils.rest.javanet.JavaNetHttpRequestExecutor;
import org.jxapi.netutils.rest.ratelimits.RequestThrottler;
import org.jxapi.netutils.websocket.DefaultWebsocketClient;
import org.jxapi.netutils.websocket.DefaultWebsocketFactory;
import org.jxapi.netutils.websocket.Websocket;
import org.jxapi.netutils.websocket.WebsocketClient;
import org.jxapi.netutils.websocket.WebsocketErrorHandler;
import org.jxapi.netutils.websocket.WebsocketFactory;
import org.jxapi.netutils.websocket.WebsocketHook;
import org.jxapi.netutils.websocket.WebsocketHookFactory;
import org.jxapi.observability.Observable;
import org.jxapi.observability.SynchronizedObservable;
import org.jxapi.util.DefaultDisposable;
import org.jxapi.util.FactoryUtil;
import org.jxapi.util.PropertiesUtil;

/**
 * Abstract {@link Exchange} implementation to be used as super class of actual
 * implementations.
 */
public abstract class AbstractExchange extends DefaultDisposable implements Exchange {

  /**
   * The exchange name
   */
  protected final String name;
  
  /**
   * The exchange id
   */
  protected final String id;
  
  /**
   * The exchange version
   */
  protected final String version;
  
  /**
   * The exchange configuration properties
   */
  protected final Properties properties;
  
  /**
   * The {@link ExchangeApi}s exposed by this exchange
   */
  protected final Map<String, ExchangeApi> apis = new HashMap<>();
  
  protected final DefaultNetwork network;
  
  /**
   * The observable used to handle exchange API events.
   */
  private final Observable<ExchangeObserver, ExchangeEvent> observable 
            = new SynchronizedObservable<>(ExchangeObserver::handleEvent);
  
  /**
   * The exchange API observer that dispatches events to subscribed observers.
   */
  protected final ExchangeObserver exchangeObserver = this::dispatchApiEvent;
  
  private final WebsocketErrorHandler wsErrorHandler = error -> dispatchApiEvent(ExchangeEvent.createWebsocketErrorEvent(error));
  
  protected final RequestThrottler requestThrottler;
  
  private HttpRequestExecutor defaultHttpRequestExecutor;

  private String httpUrl;
  
  /**
   * Constructor
   * 
   * @param id              the exchange id
   * @param version         the exchange version
   * @param name            the exchange name
   * @param properties      the exchange configuration properties
   * @param httpUrl         the base HTTP URL of the exchange
   * @param hasRateLimiting whether the exchange has rate limiting
   */
  protected AbstractExchange(String id, 
                             String version, 
                             String name, 
                             Properties properties, 
                             String httpUrl, 
                             boolean hasRateLimiting) {
    this.id = id;
    this.version = version;
    this.name = name;
    this.properties = properties;
    this.httpUrl = httpUrl;
    this.network = new DefaultNetwork();
    if (hasRateLimiting) {
      this.requestThrottler = new RequestThrottler(name);
    } else {
      this.requestThrottler = null;
    }
  }

  @Override
  public Properties getProperties() {
    return properties;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getId() {
    return id;
  }
  
  @Override
  public List<ExchangeApi> getApis() {
    return List.copyOf(apis.values());
  }

  @Override
  public void subscribeObserver(ExchangeObserver exchangeApiObserver) {
    observable.subscribe(exchangeApiObserver);
  }

  @Override
  public boolean unsubscribeObserver(ExchangeObserver exchangeApiObserver) {
    return observable.unsubscribe(exchangeApiObserver);
  }
  
  /**
   * Subclasses should call this method to register every exposed {@link ExchangeApi}. 
   * @param <T> the type of the {@link ExchangeApi}
   * @param api the {@link ExchangeApi} to add
   * @return <code>api</code>
   */
  protected <T extends ExchangeApi> T addApi(String name, T api) {
    apis.put(name, api);
    return api;
  }
  
  /**
   * Dispatches the specified {@link ExchangeEvent} to all subscribed
   * observers, after setting the exchange id and name on the event.
   * 
   * @param event the event to dispatch
   */
  protected void dispatchApiEvent(ExchangeEvent event) {
    event.setExchangeId(id);
    event.setExchangeName(name);
    observable.dispatch(event);
  }
  
  @Override
  protected void doDispose() {
    if (requestThrottler != null) {
      requestThrottler.dispose();
    }
    if (defaultHttpRequestExecutor != null) {
    	defaultHttpRequestExecutor.dispose();
    }
    network.dispose();
  }
  
  /**
   * This method can be called by subclasses at end of constructor after the exchange has been initialzed, when an after-init hook should be executed.
   * The factory class will be instantiated and the {@link ExchangeHook#afterInit(Exchange)} method will be called using this exchange instance.
   * @param exchangeHookFactoryClass the class name of the {@link ExchangeHookFactory} to use
   */
  protected void afterInit(String exchangeHookFactoryClass) {
    ExchangeHookFactory factory = ExchangeHookFactory.NO_OP;
    if (!StringUtils.isBlank(exchangeHookFactoryClass)) {
      factory = (ExchangeHookFactory) FactoryUtil.fromClassName(exchangeHookFactoryClass);
    }
    factory.createExchangeHook().afterInit(this);
  }
  
  /**
   * Creates an HTTP client using the specified client ID, HTTP request
   * interceptor factory class name, HTTP request executor factory class name, and
   * default request timeout. Should be called by subclasses to create HTTP
   * clients.
   * 
   * @param clientId                           The ID of the HTTP client.
   * @param httpRequestInterceptorFactoryClass The fully qualified class name of
   *                                           the HTTP request interceptor
   *                                           factory.
   * @param httpRequestExecutorFactoryClass    The fully qualified class name of
   *                                           the HTTP request executor factory.
   * @param defaultRequestTimeout              The default request timeout in
   *                                           milliseconds.
   */
  protected void createHttpClient(
      String clientId, 
      String httpRequestInterceptorFactoryClass,
      String httpRequestExecutorFactoryClass,
      Long defaultRequestTimeout) {
    HttpRequestExecutor httpRequestExecutor = null;
    if  (httpRequestExecutorFactoryClass != null) {
      httpRequestExecutor = (HttpRequestExecutorFactory.fromClassName(httpRequestExecutorFactoryClass)).createExecutor(this);
    } else {
      if (defaultHttpRequestExecutor == null) {
    	defaultHttpRequestExecutor = new JavaNetHttpRequestExecutor(id + "-" + name + "-http-");
      }
      httpRequestExecutor = defaultHttpRequestExecutor;
    }
    defaultRequestTimeout = Optional
        .ofNullable(defaultRequestTimeout)
        .orElse(Long.valueOf(HttpRequestExecutor.DEFAULT_REQUEST_TIMEOUT));
    long requestTimeout = PropertiesUtil.getLong(
        getProperties(), 
        CommonConfigProperties.HTTP_REQUEST_TIMEOUT_PROPERTY.getName(), 
        defaultRequestTimeout);
    if (requestTimeout >= 0) {
      httpRequestExecutor.setRequestTimeout(requestTimeout);
    }
    createHttpClient(clientId, httpRequestInterceptorFactoryClass, httpRequestExecutor);
  }
  
  private void createHttpClient(
      String name, 
      String httpRequestInterceptorFactoryClass,
      HttpRequestExecutor httpRequestExecutor) {
    HttpRequestInterceptor httpRequestInterceptor = null;
    if (httpRequestInterceptorFactoryClass != null) {
      httpRequestInterceptor = ((HttpRequestInterceptorFactory) FactoryUtil.fromClassName(httpRequestInterceptorFactoryClass)).createInterceptor(this);
    }
    network.registerHttpClient(name, new HttpClient(httpRequestInterceptor, httpRequestExecutor, requestThrottler));
  }
  
  /**
   * Creates a websocket manager using the specified URL, websocket factory class name, and websocket hook factory class name.
   * Should be called by subclasses to create the websocket manager if there is at least one websocket endpoint.
   * 
   * @param url The URL of the websocket server. Can be <code>null</code> to set it later, or use the default URL defined by the websocket implementation.
   * @param websocketFactoryClassName The fully qualified class name of the websocket factory class that creates the websocket.
   * @param websocketHookFactoryClassName The fully qualified class name of the websocket hook factory class that creates the websocket hook.
   */
  protected void createWebsocketClient(
      String name,
      String url, 
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
    WebsocketClient websocketClient = new DefaultWebsocketClient(websocket, websocketHook);
    websocketClient.subscribeErrorHandler(wsErrorHandler);
    network.registerWebsocket(name, websocketClient);
  }
  
  /**
   * Gets the HTTP client with the specified client ID.
   * 
   * @param clientId The ID of the HTTP client.
   * @return The HTTP client with the specified ID.
   * @throws IllegalArgumentException if no HTTP client is registered with the
   *                                  specified ID.
   */
  protected HttpClient getHttpClient(String clientId) {
    HttpClient httpClient = network.getHttpClient(clientId);
    if (httpClient == null) {
      throw new IllegalArgumentException("No HttpClient registered with id: " + clientId);
    }
    return httpClient;
  }
  
  @Override
  public String getVersion() {
   return version;
  }
  
  @Override
  public String getHttpUrl() {
    return this.httpUrl;
  }
 
  @Override
  public Network getNetwork() {
    return network;
  }
}
