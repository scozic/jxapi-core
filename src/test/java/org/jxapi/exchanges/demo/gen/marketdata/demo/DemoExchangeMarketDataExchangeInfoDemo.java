package org.jxapi.exchanges.demo.gen.marketdata.demo;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import javax.annotation.processing.Generated;
import org.jxapi.exchange.ExchangeApiObserver;
import org.jxapi.exchanges.demo.gen.DemoExchangeExchange;
import org.jxapi.exchanges.demo.gen.DemoExchangeExchangeImpl;
import org.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoRequest;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponse;
import org.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.StringJsonFieldDeserializer;
import org.jxapi.netutils.rest.RestResponse;
import org.jxapi.util.DemoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link DemoExchangeMarketDataApi#exchangeInfo(org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoRequest)})}<br>
 */
@Generated("org.jxapi.generator.java.exchange.api.demo.RestEndpointDemoGenerator")
public class DemoExchangeMarketDataExchangeInfoDemo {
  private static final Logger log = LoggerFactory.getLogger(DemoExchangeMarketDataExchangeInfoDemo.class);
  
  public static DemoExchangeMarketDataExchangeInfoRequest createRequest() {
    DemoExchangeMarketDataExchangeInfoRequest request = new DemoExchangeMarketDataExchangeInfoRequest();
    request.setSymbols(new ListJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance()).deserialize("[\"BTC_USDT\", \"ETH_USDT\"]"));
    return request;
  }
  
  /**
   * Submits a call to {@link DemoExchangeMarketDataApi#exchangeInfo(org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoRequest)}and waits for response.
   * @param request     The request to submit
   * @param configProperties  The configuration properties to instantiate exchange with
   * @param apiObserver API observer that will notified of events. Is subscribed before REST API call and unsubscribed right after. Ignored if <code>null</code>
   * @return Response data resulting from this API call
   * @throws InterruptedException eventually thrown waiting for response
   * @throws ExecutionException raised if response is not OK, see {@link RestResponse#isOk()}
   */
  public static RestResponse<DemoExchangeMarketDataExchangeInfoResponse> execute(DemoExchangeMarketDataExchangeInfoRequest request, Properties configProperties, ExchangeApiObserver apiObserver) throws InterruptedException, ExecutionException {
    DemoExchangeExchange exchange = new DemoExchangeExchangeImpl("test-" + DemoExchangeExchange.ID, configProperties);
    DemoExchangeMarketDataApi api = exchange.getDemoExchangeMarketDataApi();
    log.info("Calling org.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi.exchangeInfo() API with request:{}", request);
    if (apiObserver != null) {
      api.subscribeObserver(apiObserver);
    }
    try {
      return DemoUtil.checkResponse(api.exchangeInfo(request));
    }
    finally {
      if (apiObserver != null) {
        api.unsubscribeObserver(apiObserver);
      }
      exchange.dispose();
    }
  }
  
  /**
   * Runs REST endpoint demo snippet calling {@link DemoExchangeMarketDataApi#exchangeInfo(org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoRequest)}
   * @param args no argument expected
   */
  public static void main(String[] args) {
    try {
      execute(createRequest(),
              DemoUtil.loadDemoExchangeProperties(DemoExchangeExchange.ID),
              DemoUtil::logRestApiEvent);
      System.exit(0);
    }
    catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
