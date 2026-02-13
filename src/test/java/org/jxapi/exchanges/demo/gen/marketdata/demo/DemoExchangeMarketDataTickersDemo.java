package org.jxapi.exchanges.demo.gen.marketdata.demo;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import javax.annotation.processing.Generated;
import org.jxapi.exchange.ExchangeObserver;
import org.jxapi.exchanges.demo.gen.DemoExchangeExchange;
import org.jxapi.exchanges.demo.gen.DemoExchangeExchangeImpl;
import org.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponse;
import org.jxapi.netutils.rest.RestResponse;
import org.jxapi.util.DemoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link DemoExchangeMarketDataApi#tickers()})}<br>
 */
@Generated("org.jxapi.generator.java.exchange.api.demo.RestEndpointDemoGenerator")
public class DemoExchangeMarketDataTickersDemo {
  private static final Logger log = LoggerFactory.getLogger(DemoExchangeMarketDataTickersDemo.class);
  
  /**
   * Submits a call to {@link DemoExchangeMarketDataApi#tickers()}and waits for response.
   * @param configProperties  The configuration properties to instantiate exchange with
   * @param observer API observer that will notified of events. Is subscribed before REST API call and unsubscribed right after. Ignored if <code>null</code>
   * @return Response data resulting from this API call
   * @throws InterruptedException eventually thrown waiting for response
   * @throws ExecutionException raised if response is not OK, see {@link RestResponse#isOk()}
   */
  public static RestResponse<DemoExchangeMarketDataTickersResponse> execute(Properties configProperties, ExchangeObserver observer) throws InterruptedException, ExecutionException {
    DemoExchangeExchange exchange = new DemoExchangeExchangeImpl("test-" + DemoExchangeExchange.ID, configProperties);
    DemoExchangeMarketDataApi api = exchange.getMarketDataApi();
    log.info("Calling org.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi.tickers() API");
    if (observer != null) {
      exchange.subscribeObserver(observer);
    }
    try {
      return DemoUtil.checkResponse(api.tickers());
    }
    finally {
      if (observer != null) {
        exchange.unsubscribeObserver(observer);
      }
      exchange.dispose();
    }
  }
  
  /**
   * Runs REST endpoint demo snippet calling {@link DemoExchangeMarketDataApi#tickers()}
   * @param args no argument expected
   */
  public static void main(String[] args) {
    try {
      Properties properties = DemoUtil.loadDemoExchangeProperties(DemoExchangeExchange.ID);
      execute(properties,
              DemoUtil::logRestApiEvent);
      System.exit(0);
    }
    catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
