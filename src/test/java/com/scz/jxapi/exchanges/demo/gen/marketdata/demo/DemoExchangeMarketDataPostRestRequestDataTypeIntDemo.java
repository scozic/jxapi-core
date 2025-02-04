package com.scz.jxapi.exchanges.demo.gen.marketdata.demo;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import com.scz.jxapi.exchange.ExchangeApiObserver;
import com.scz.jxapi.exchanges.demo.gen.DemoExchangeExchange;
import com.scz.jxapi.exchanges.demo.gen.DemoExchangeExchangeImpl;
import com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.GenericResponse;
import com.scz.jxapi.netutils.rest.RestResponse;
import com.scz.jxapi.util.DemoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link DemoExchangeMarketDataApi#postRestRequestDataTypeInt(java.lang.Integer)})}<br>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class DemoExchangeMarketDataPostRestRequestDataTypeIntDemo {
  private static final Logger log = LoggerFactory.getLogger(DemoExchangeMarketDataPostRestRequestDataTypeIntDemo.class);
  
  public static Integer createRequest() {
    return Integer.valueOf(12345);
  }
  
  /**
   * Submits a call to {@link DemoExchangeMarketDataApi#postRestRequestDataTypeInt(java.lang.Integer)}and waits for response.
   * @param request     The request to submit
   * @param configProperties  The configuration properties to instantiate exchange with
   * @param apiObserver API observer that will notified of events. Is subscribed before REST API call and unsubscribed right after. Ignored if <code>null</code>
   * @return Response data resulting from this API call
   * @throws InterruptedException eventually thrown waiting for response
   * @throws ExecutionException raised if response is not OK, see {@link RestResponse#isOk()}
   */
  public static RestResponse<GenericResponse> execute(Integer request, Properties configProperties, ExchangeApiObserver apiObserver) throws InterruptedException, ExecutionException {
    DemoExchangeExchange exchange = new DemoExchangeExchangeImpl("test-" + DemoExchangeExchange.ID, configProperties);
    DemoExchangeMarketDataApi api = exchange.getDemoExchangeMarketDataApi();
    log.info("Calling com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi.postRestRequestDataTypeInt() API with request:{}", request);
    if (apiObserver != null) {
      api.subscribeObserver(apiObserver);
    }
    try {
      return DemoUtil.checkResponse(api.postRestRequestDataTypeInt(request));
    }
    finally {
      if (apiObserver != null) {
        api.unsubscribeObserver(apiObserver);
      }
      exchange.dispose();
    }
  }
  
  /**
   * Runs REST endpoint demo snippet calling {@link DemoExchangeMarketDataApi#postRestRequestDataTypeInt(java.lang.Integer)}
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
