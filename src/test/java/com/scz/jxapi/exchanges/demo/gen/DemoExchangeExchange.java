package com.scz.jxapi.exchanges.demo.gen;

import com.scz.jxapi.exchange.Exchange;
import com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi;

/**
 * DemoExchange API</br>
 * Demo Exchange. This exchange uses connects to mock HTTP server and websocket server. It is used to test and validate a full Java wrapper generated using JXAPI.
 * @see <a href="https://docs.myexchange.com/api">Reference documentation</a>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public interface DemoExchangeExchange extends Exchange {
  
  String ID = "DemoExchange";
  
  DemoExchangeMarketDataApi getDemoExchangeMarketDataApi();
}
