package org.jxapi.exchanges.demo.gen;

import java.util.Properties;

import javax.annotation.processing.Generated;
import org.jxapi.exchange.AbstractExchange;
import org.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi;
import org.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApiImpl;

/**
 * Actual implementation of {@link DemoExchangeExchange}<br>
 */
@Generated("org.jxapi.generator.java.exchange.ExchangeInterfaceImplementationGenerator")
public class DemoExchangeExchangeImpl extends AbstractExchange implements DemoExchangeExchange {
  
  private final DemoExchangeMarketDataApi demoExchangeMarketDataApi;
  
  public DemoExchangeExchangeImpl(String exchangeName, Properties properties) {
    super(ID, VERSION, exchangeName, properties, "BASEURL", "BASEURL");
    this.demoExchangeMarketDataApi = addApi(new DemoExchangeMarketDataApiImpl(this));
  }
  
  @Override
  public DemoExchangeMarketDataApi getDemoExchangeMarketDataApi() {
    return this.demoExchangeMarketDataApi;
  }
  
}
