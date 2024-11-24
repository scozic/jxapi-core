package com.scz.jxapi.exchanges.demo.gen.marketdata.demo;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import com.scz.jxapi.exchange.ExchangeApiObserver;
import com.scz.jxapi.exchanges.demo.gen.DemoExchangeExchange;
import com.scz.jxapi.exchanges.demo.gen.DemoExchangeExchangeImpl;
import com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoRequest;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponse;
import com.scz.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.StringJsonFieldDeserializer;
import com.scz.jxapi.netutils.rest.RestResponse;
import com.scz.jxapi.util.DemoUtil;
import com.scz.jxapi.util.TestJXApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link DemoExchangeMarketDataApi#exchangeInfo(DemoExchangeMarketDataExchangeInfoRequest)})}<br>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class DemoExchangeMarketDataExchangeInfoDemo {
  private static final Logger log = LoggerFactory.getLogger(DemoExchangeMarketDataExchangeInfoDemo.class);
  
  public static DemoExchangeMarketDataExchangeInfoRequest createRequest() {
    DemoExchangeMarketDataExchangeInfoRequest request = new DemoExchangeMarketDataExchangeInfoRequest();
    request.setSymbols(new ListJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance()).deserialize("[\"BTC\", \"ETH\"]"));
    return request;
  }
  
  /**
   * Submits a call to {@link DemoExchangeMarketDataApi#exchangeInfo(DemoExchangeMarketDataExchangeInfoRequest)}and waits for response.
   * @param request     The request to submit
   * @param properties  The configuration properties to instantiate exchange with
   * @param apiObserver API observer that will notified of events. Is subscribed before REST API call and unsubscribed right after. Ignored if <code>null</code>
   * @return Response data resulting from this API call
   * @throws InterruptedException eventually thrown waiting for response@throws ExecutionException raised if response is not OK, see {@link RestResponse#isOk()}
   */
  public static RestResponse<DemoExchangeMarketDataExchangeInfoResponse> execute(DemoExchangeMarketDataExchangeInfoRequest request, Properties configProperties, ExchangeApiObserver apiObserver) throws InterruptedException, ExecutionException {
    DemoExchangeMarketDataApi api = new DemoExchangeExchangeImpl("test-" + DemoExchangeExchange.ID, configProperties).getDemoExchangeMarketDataApi();
    log.info("Calling com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi.exchangeInfo() API with request:{}", request);
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
    }
  }
  
  /**
   * Runs REST endpoint demo snippet calling {@link DemoExchangeMarketDataApi#exchangeInfo(DemoExchangeMarketDataExchangeInfoRequest)}
   * @param args no argument expected
   */
  public static void main(String[] args) {
    try {
      execute(createRequest(), TestJXApiProperties.filterProperties(DemoExchangeExchange.ID, true), DemoUtil::logRestApiEvent);
      System.exit(0);
    }
    catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
