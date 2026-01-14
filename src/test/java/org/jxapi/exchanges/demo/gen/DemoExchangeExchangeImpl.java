package org.jxapi.exchanges.demo.gen;

import java.util.Properties;

import javax.annotation.processing.Generated;
import org.jxapi.exchange.AbstractExchange;
import org.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi;
import org.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApiImpl;
import org.jxapi.util.EncodingUtil;

/**
 * Actual implementation of {@link DemoExchangeExchange}<br>
 */
@Generated("org.jxapi.generator.java.exchange.ExchangeInterfaceImplementationGenerator")
public class DemoExchangeExchangeImpl extends AbstractExchange implements DemoExchangeExchange {
  
  private final DemoExchangeMarketDataApi marketDataApi;
  
  public DemoExchangeExchangeImpl(String exchangeName, Properties properties) {
    super(ID,
          VERSION,
          exchangeName,
          properties,
          EncodingUtil.substituteArguments("${config.baseHttpUrl}", "config.baseHttpUrl", DemoExchangeProperties.getBaseHttpUrl(properties)),
          false);
    // Network
    createHttpClient("httpDefault",
      "org.jxapi.exchanges.demo.net.DemoExchangeHttpRequestInterceptorFactory",
      null,
      null);
    createWebsocketClient("wsDefault",
      EncodingUtil.substituteArguments("${config.baseWebsocketUrl}", "config.baseWebsocketUrl", DemoExchangeProperties.getBaseWebsocketUrl(properties)),
      null,
      "org.jxapi.exchanges.demo.net.DemoExchangeWebsocketHookFactory");
    
     // APIs
    this.marketDataApi = addApi(DemoExchangeMarketDataApi.ID, new DemoExchangeMarketDataApiImpl(this, exchangeObserver));
    afterInit("org.jxapi.exchange.MockExchangeHookFactory");
  }
  
  @Override
  public DemoExchangeMarketDataApi getMarketDataApi() {
    return this.marketDataApi;
  }
  
}
