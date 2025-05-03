package org.jxapi.exchanges.demo.gen;

import java.util.Properties;

import javax.annotation.processing.Generated;
import org.jxapi.exchange.AbstractExchange;
import org.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi;
import org.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApiImpl;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.PropertiesUtil;

/**
 * Actual implementation of {@link DemoExchangeExchange}<br>
 */
@Generated("org.jxapi.generator.java.exchange.ExchangeInterfaceImplementationGenerator")
public class DemoExchangeExchangeImpl extends AbstractExchange implements DemoExchangeExchange {
  
  private final DemoExchangeMarketDataApi demoExchangeMarketDataApi;
  
  public DemoExchangeExchangeImpl(String exchangeName, Properties properties) {
    super(ID,
          VERSION,
          exchangeName,
          properties,
          EncodingUtil.substituteArguments("${config.baseHttpUrl}", "config.baseHttpUrl", PropertiesUtil.getString(properties, DemoExchangeProperties.BASE_HTTP_URL)),
          EncodingUtil.substituteArguments("${config.baseWebsocketUrl}", "config.baseWebsocketUrl", PropertiesUtil.getString(properties, DemoExchangeProperties.BASE_WEBSOCKET_URL)));
    this.demoExchangeMarketDataApi = addApi(new DemoExchangeMarketDataApiImpl(this));
  }
  
  @Override
  public DemoExchangeMarketDataApi getDemoExchangeMarketDataApi() {
    return this.demoExchangeMarketDataApi;
  }
  
}
