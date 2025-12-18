package org.jxapi.exchange;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.jxapi.netutils.DefaultNetwork;
import org.jxapi.netutils.Network;
import org.jxapi.netutils.rest.HttpClient;
import org.jxapi.netutils.rest.HttpRequestExecutor;
import org.jxapi.netutils.rest.HttpRequestExecutorFactory;
import org.jxapi.netutils.rest.HttpRequestInterceptor;
import org.jxapi.netutils.rest.javanet.JavaNetHttpRequestExecutor;
import org.jxapi.netutils.rest.ratelimits.RequestThrottlingMode;
import org.jxapi.netutils.websocket.DefaultWebsocketFactory;
import org.jxapi.netutils.websocket.DefaultWebsocketManager;
import org.jxapi.netutils.websocket.Websocket;
import org.jxapi.netutils.websocket.WebsocketErrorHandler;
import org.jxapi.netutils.websocket.WebsocketFactory;
import org.jxapi.netutils.websocket.WebsocketHook;
import org.jxapi.netutils.websocket.WebsocketHookFactory;
import org.jxapi.netutils.websocket.WebsocketManager;
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
  
  /**
   * The base HTTP URL of the exchange
   * @see #getHttpUrl()
   */
  protected final String httpUrl;
  
  /**
   * The base WebSocket URL of the exchange
   * 
   * @see #getWsUrl()
   */
  protected final String wsUrl;
  
  protected final DefaultNetwork network;
  
  /**
   * The observable used to handle exchange API events.
   */
  protected final Observable<ExchangeApiObserver, ExchangeApiEvent> observable 
            = new SynchronizedObservable<>(ExchangeApiObserver::handleEvent);
  
  private final WebsocketErrorHandler wsErrorHandler = error -> dispatchApiEvent(ExchangeApiEvent.createWebsocketErrorEvent(error));
  
  /**
   * Constructor
   * @param id the exchange id
   * @param version the exchange version
   * @param name the exchange name
   * @param properties the exchange configuration properties
   * @param httpUrl the base HTTP URL of the exchange
   * @param wsUrl the base WebSocket URL of the exchange
   */
  protected AbstractExchange(String id, 
                             String version, 
                             String name, 
                             Properties properties, 
                             String httpUrl, 
                             String wsUrl) {
    this.id = id;
    this.version = version;
    this.name = name;
    this.properties = properties;
    this.httpUrl = httpUrl;
    this.wsUrl = wsUrl;
    this.network = new DefaultNetwork();
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
  public void subscribeObserver(ExchangeApiObserver exchangeApiObserver) {
    apis.values().forEach(api -> api.subscribeObserver(exchangeApiObserver));
  }

  @Override
  public boolean unsubscribeObserver(ExchangeApiObserver exchangeApiObserver) {
    boolean res = false;
    for (ExchangeApi api: getApis()) {
      res |= api.unsubscribeObserver(exchangeApiObserver);
    }
    return res;
  }
  
  /**
   * Subclasses should call this method to register every exposed {@link ExchangeApi}. 
   * @param <T> the type of the {@link ExchangeApi}
   * @param api the {@link ExchangeApi} to add
   * @return <code>api</code>
   */
  protected <T extends ExchangeApi> T addApi(T api) {
    apis.put(api.getName(), api);
    return api;
  }
  
  protected void dispatchApiEvent(ExchangeApiEvent event) {
    event.setExchangeId(id);
    event.setExchangeName(name);
    observable.dispatch(event);
  }
  
  @Override
  protected void doDispose() {
    apis.values().forEach(ExchangeApi::dispose);
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
  
  protected void createHttpClient(
      String id, 
      String httpRequestInterceptorFactoryClass,
      String httpRequestExecutorFactoryClass,
      long defaultRequestTimeout) {
    HttpRequestExecutor httpRequestExecutor = null;
    if  (httpRequestExecutorFactoryClass != null) {
      httpRequestExecutor = (HttpRequestExecutorFactory.fromClassName(httpRequestExecutorFactoryClass)).createExecutor(this);
    } else {
      httpRequestExecutor = new JavaNetHttpRequestExecutor();
    }
    long requestTimeout = PropertiesUtil.getLong(
        getProperties(), 
        CommonConfigProperties.HTTP_REQUEST_TIMEOUT_PROPERTY.getName(), 
        defaultRequestTimeout);
    if (requestTimeout >= 0) {
      httpRequestExecutor.setRequestTimeout(requestTimeout);
    }
    createHttpClient(id, httpRequestInterceptorFactoryClass, httpRequestExecutor);
  }
  
  protected void createHttpClient(
      String id, 
      String httpRequestInterceptorFactoryClass,
      HttpRequestExecutor httpRequestExecutor) {
    HttpRequestInterceptor httpRequestInterceptor = null;
    if (httpRequestInterceptorFactoryClass != null) {
      httpRequestInterceptor = (HttpRequestInterceptor) FactoryUtil.fromClassName(httpRequestInterceptorFactoryClass);
    }
    network.registerHttpClient(id, new HttpClient(httpRequestInterceptor, httpRequestExecutor));
  }
  
  /**
   * Creates a websocket manager using the specified URL, websocket factory class name, and websocket hook factory class name.
   * Should be called by subclasses to create the websocket manager if there is at least one websocket endpoint.
   * 
   * @param url The URL of the websocket server.
   * @param websocketFactoryClassName The fully qualified class name of the websocket factory class that creates the websocket.
   * @param websocketHookFactoryClassName The fully qualified class name of the websocket hook factory class that creates the websocket hook.
   */
  protected void createWebsocketManager(
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
    WebsocketManager websocketManager = new DefaultWebsocketManager(this, websocket, websocketHook);
    websocketManager.subscribeErrorHandler(wsErrorHandler);
    network.registerWebsocket(name, websocketManager);
  }
  
  protected HttpClient getHttpClient(String clientId) {
    HttpClient httpClient = network.getHttpClient(clientId);
    if (httpClient == null) {
      throw new IllegalArgumentException("No HttpClient registered with id: " + clientId);
    }
    return httpClient;
  }
  
  @Override
  public void setRequestThrottlingMode(RequestThrottlingMode requestThrottlingMode) {
    apis.values().forEach(api -> api.setRequestThrottlingMode(requestThrottlingMode));
    
  }

  @Override
  public void setMaxRequestThrottleDelay(long maxRequestThrottleDelay) {
    apis.values().forEach(api -> api.setMaxRequestThrottleDelay(maxRequestThrottleDelay));
  }
  
  @Override
  public void setHttpRequesTimeout(long httpRequestTimeout) {
    network.getRegisteredHttpClientIds().forEach(clientId -> {
      HttpRequestExecutor executor = getHttpClient(clientId);
      executor.setRequestTimeout(httpRequestTimeout);
    });
  }
  
  @Override
  public String getVersion() {
   return version;
  }
  
  @Override
  public String getHttpUrl() {
    return httpUrl;
  }
  
  @Override
  public String getWsUrl() {
    return wsUrl;
  }
 
  @Override
  public Network getNetwork() {
    return network;
  }
}
