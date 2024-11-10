package com.scz.jxapi.exchanges.demo.gen.marketdata.demo;

import com.scz.jxapi.exchanges.demo.gen.DemoExchangeExchangeImpl;
import com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest;
import com.scz.jxapi.util.DemoUtil;
import com.scz.jxapi.util.TestJXApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi#tickerStream(DemoExchangeMarketDataTickerStreamRequest)}<br>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class DemoExchangeMarketDataTickerStreamDemo {
  private static final Logger log = LoggerFactory.getLogger(DemoExchangeMarketDataTickerStreamDemo.class);
  
  private static final long SUBSCRIPTION_DURATION = TestJXApiProperties.DEMO_WS_SUBSCRIPTION_DURATION;
  private static final long DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION = TestJXApiProperties.DEMO_WS_DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION;
  
  public static DemoExchangeMarketDataTickerStreamRequest createRequest() {
    DemoExchangeMarketDataTickerStreamRequest request = new DemoExchangeMarketDataTickerStreamRequest();
    request.setSymbol("BTC_USDT");
    return request;
  }
  
  public static void main(String[] args) {
    try {
      DemoExchangeMarketDataApi api = new DemoExchangeExchangeImpl("test-demoExchange", TestJXApiProperties.filterProperties("demoExchange", true)).getDemoExchangeMarketDataApi();
      DemoExchangeMarketDataTickerStreamRequest request = createRequest();
      log.info("Subscribing to websocket API 'DemoExchange MarketData tickerStream' for " + SUBSCRIPTION_DURATION + "ms with request:" + request);
      String subId = api.subscribeTickerStream(request, m -> DemoUtil.logWsMessage(m));
      Thread.sleep(SUBSCRIPTION_DURATION);
      log.info("Unubscribing from 'DemoExchange MarketData tickerStream' stream");
      api.unsubscribeTickerStream(subId);
      Thread.sleep(DELAY_BEFORE_EXIT_AFTER_UNSUBSCRIPTION);
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
