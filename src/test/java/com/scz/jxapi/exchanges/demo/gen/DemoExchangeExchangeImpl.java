package com.scz.jxapi.exchanges.demo.gen;

import java.util.Properties;

import com.scz.jxapi.exchange.AbstractExchange;
import com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi;
import com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApiImpl;
import javax.annotation.processing.Generated;

/**
 * Actual implementation of {@link DemoExchangeExchange}<br>
 */
@Generated("com.scz.jxapi.generator.java.exchange.ExchangeInterfaceImplementationGenerator")
public class DemoExchangeExchangeImpl extends AbstractExchange implements DemoExchangeExchange {
  
  /**
   * Base REST API URL
   */
  public static final String HTTP_URL = "BASEURL";
  
  /**
   * Base websocket endpoint URL
   */
  public static final String WEBSOCKET_URL = "BASEURL";
  
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
