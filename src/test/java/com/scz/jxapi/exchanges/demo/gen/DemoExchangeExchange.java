package com.scz.jxapi.exchanges.demo.gen;

import com.scz.jxapi.exchange.Exchange;
import com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi;
import javax.annotation.processing.Generated;

/**
 * DemoExchange API<br>
 * Demo Exchange. This exchange uses connects to mock HTTP server and websocket server. It is used to test and validate a full Java wrapper generated using JXAPI.
 * @see <a href="https://docs.myexchange.com/api">Reference documentation</a>
 */
@Generated("com.scz.jxapi.generator.java.exchange.ExchangeInterfaceGenerator")
public interface DemoExchangeExchange extends Exchange {
  
  String ID = "DemoExchange";
  
  /**
   * @return Demo exchange market data API
   */
  DemoExchangeMarketDataApi getDemoExchangeMarketDataApi();
}
