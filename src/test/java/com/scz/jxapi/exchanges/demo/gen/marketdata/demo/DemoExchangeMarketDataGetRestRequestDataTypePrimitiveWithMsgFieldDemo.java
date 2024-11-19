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
import com.scz.jxapi.util.TestJXApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi{@link DemoExchangeMarketDataApi#getRestRequestDataTypePrimitiveWithMsgField(Integer)})}<br>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class DemoExchangeMarketDataGetRestRequestDataTypePrimitiveWithMsgFieldDemo {
  private static final Logger log = LoggerFactory.getLogger(DemoExchangeMarketDataGetRestRequestDataTypePrimitiveWithMsgFieldDemo.class);
  
  public static Integer createAge() {
    return Integer.valueOf(18);
  }
  
  /**
   * Submits a call to {@link DemoExchangeMarketDataApi#getRestRequestDataTypePrimitiveWithMsgField(Integer)}and waits for response.
   * @param request     The request to submit
   * @param properties  The configuration properties to instantiate exchange with
   * @param apiObserver API observer that will notified of events. Is subscribed before REST API call and unsubscribed right after. Ignored if <code>null</code>
   * @return Response data resulting from this API call
   * @throws InterruptedException eventually thrown waiting for response@throws ExecutionException raised if response is not OK, see {@link RestResponse#isOk()}
   */
  public static RestResponse<GenericResponse> execute(Integer request, Properties configProperties, ExchangeApiObserver apiObserver) throws InterruptedException, ExecutionException {
    DemoExchangeMarketDataApi api = new DemoExchangeExchangeImpl("test-" + DemoExchangeExchange.ID, configProperties).getDemoExchangeMarketDataApi();
    log.info("Calling com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi.getRestRequestDataTypePrimitiveWithMsgField() API with request:{}", request);
    if (apiObserver != null) {
      api.subscribeObserver(apiObserver);
    }
    try {
      return DemoUtil.checkResponse(api.getRestRequestDataTypePrimitiveWithMsgField(request));
    } finally {
      if (apiObserver != null) {
        api.unsubscribeObserver(apiObserver);
      }
    }
  }
  
  /**
   * Runs REST endpoint demo snippet calling {@link DemoExchangeMarketDataApi#getRestRequestDataTypePrimitiveWithMsgField(Integer)}
   */
  public static void main(String[] args) {
    try {
      execute(createAge(), TestJXApiProperties.filterProperties(DemoExchangeExchange.ID, true), DemoUtil::logRestApiEvent);
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
  
}
