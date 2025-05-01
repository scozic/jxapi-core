package org.jxapi.exchange;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jxapi.netutils.rest.ratelimits.RequestThrottlingMode;
import org.jxapi.util.DefaultDisposable;

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
   * @see #getWebSocketUrl()
   */
  protected final String wsUrl;
  
  /**
   * Constructor
   * @param id the exchange id
   * @param version the exchange version
   * @param name the exchange name
   * @param properties the exchange configuration properties
   * @param httpUrl the base HTTP URL of the exchange
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
  
  @Override
  protected void doDispose() {
    apis.values().forEach(ExchangeApi::dispose);
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
    apis.values().forEach(api -> api.setHttpRequestTimeout(httpRequestTimeout));
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
 
}
