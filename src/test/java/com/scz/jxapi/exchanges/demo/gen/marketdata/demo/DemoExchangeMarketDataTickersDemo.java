package com.scz.jxapi.exchanges.demo.gen.marketdata.demo;

import com.scz.jxapi.exchanges.demo.gen.DemoExchangeExchangeImpl;
import com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi;
import com.scz.jxapi.util.DemoUtil;
import com.scz.jxapi.util.TestJXApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi#tickers()}<br>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class DemoExchangeMarketDataTickersDemo {
  private static final Logger log = LoggerFactory.getLogger(DemoExchangeMarketDataTickersDemo.class);
  
  public static void main(String[] args) {
    try {
      DemoExchangeMarketDataApi api = new DemoExchangeExchangeImpl("test-demoExchange", TestJXApiProperties.filterProperties("demoExchange", true)).getDemoExchangeMarketDataApi();
      log.info("Calling com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi.tickers() API");
      DemoUtil.checkResponse(api.tickers());
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
