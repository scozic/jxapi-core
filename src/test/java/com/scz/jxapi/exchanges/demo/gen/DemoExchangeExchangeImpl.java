package com.scz.jxapi.exchanges.demo.gen;

import java.util.Properties;

import com.scz.jxapi.exchange.AbstractExchange;
import com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi;
import com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApiImpl;

/**
 * Actual implementation of {@link DemoExchangeExchange}<br>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class DemoExchangeExchangeImpl extends AbstractExchange implements DemoExchangeExchange {
  
  /**
   * Base REST API URL
   */
  public static final String HTTP_URL = "http://HTTPSERVERHOST:8080";
  
  /**
   * Base websocket endpoint URL
   */
  public static final String WEBSOCKET_URL = "ws://MOCKWEBSOCKETSERVERHOST:8090/ws";
  
  private final DemoExchangeMarketDataApi demoExchangeMarketDataApi;
  
  public DemoExchangeExchangeImpl(String exchangeName, Properties properties) {
    super(ID, exchangeName, properties);
    this.demoExchangeMarketDataApi = addApi(new DemoExchangeMarketDataApiImpl(getName(), properties));
  }
  
  @Override
  public DemoExchangeMarketDataApi getDemoExchangeMarketDataApi() {
    return this.demoExchangeMarketDataApi;
  }
  
}
