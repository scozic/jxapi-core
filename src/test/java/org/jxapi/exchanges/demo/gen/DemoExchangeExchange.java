package org.jxapi.exchanges.demo.gen;

import javax.annotation.processing.Generated;
import org.jxapi.exchange.Exchange;
import org.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi;

/**
 * DemoExchange API<br>
 * Demo Exchange. This exchange uses connects to mock HTTP server and websocket server. It is used to test and validate a full Java wrapper generated using JXAPI.
 * @see <a href="https://docs.myexchange.com/api">Reference documentation</a>
 */
@Generated("org.jxapi.generator.java.exchange.ExchangeInterfaceGenerator")
public interface DemoExchangeExchange extends Exchange {
  
  /**
   * ID of the 'DemoExchange' exchange
   */
  String ID = "DemoExchange";
  
  /**
   * Version of the 'DemoExchange' exchange
   */
  String VERSION = "1.0.0";
  /**
   * Name of <code>httpDefault</code> HttpClient.
   */
   String HTTP_DEFAULT_HTTP_CLIENT = "httpDefault";
  /**
   * Name of <code>wsDefault</code> WebsocketClient.
   */
   String WS_DEFAULT_WEBSOCKET_CLIENT = "wsDefault";
  
  // API groups
  
  /**
   * @return Demo exchange market data API
   */
  DemoExchangeMarketDataApi getMarketDataApi();
}
