package com.scz.jxapi.exchanges.demo.gen.marketdata.demo;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import com.scz.jxapi.exchange.ExchangeApiObserver;
import com.scz.jxapi.exchanges.demo.gen.DemoExchangeExchange;
import com.scz.jxapi.exchanges.demo.gen.DemoExchangeExchangeImpl;
import com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.GenericResponse;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.SingleSymbol;
import com.scz.jxapi.netutils.rest.RestResponse;
import com.scz.jxapi.util.DemoUtil;
import com.scz.jxapi.util.TestJXApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link DemoExchangeMarketDataApi#postRestRequestDataTypeObjectListMap(Map<String, List<SingleSymbol>>)})}<br>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class DemoExchangeMarketDataPostRestRequestDataTypeObjectListMapDemo {
  private static final Logger log = LoggerFactory.getLogger(DemoExchangeMarketDataPostRestRequestDataTypeObjectListMapDemo.class);
  
  public static Map<String, List<SingleSymbol>> createRequest() {
    SingleSymbol requestItem = new SingleSymbol();
    requestItem.setSymbol("BTC_USDT");
    return Map.of("spot", List.of(requestItem));
  }
  
  /**
   * Submits a call to {@link DemoExchangeMarketDataApi#postRestRequestDataTypeObjectListMap(Map<String, List<SingleSymbol>>)}and waits for response.
   * @param request     The request to submit
   * @param properties  The configuration properties to instantiate exchange with
   * @param apiObserver API observer that will notified of events. Is subscribed before REST API call and unsubscribed right after. Ignored if <code>null</code>
   * @return Response data resulting from this API call
   * @throws InterruptedException eventually thrown waiting for response@throws ExecutionException raised if response is not OK, see {@link RestResponse#isOk()}
   */
  public static RestResponse<GenericResponse> execute(Map<String, List<SingleSymbol>> request, Properties configProperties, ExchangeApiObserver apiObserver) throws InterruptedException, ExecutionException {
    DemoExchangeMarketDataApi api = new DemoExchangeExchangeImpl("test-" + DemoExchangeExchange.ID, configProperties).getDemoExchangeMarketDataApi();
    log.info("Calling com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi.postRestRequestDataTypeObjectListMap() API with request:{}", request);
    if (apiObserver != null) {
      api.subscribeObserver(apiObserver);
    }
    try {
      return DemoUtil.checkResponse(api.postRestRequestDataTypeObjectListMap(request));
    }
    finally {
      if (apiObserver != null) {
        api.unsubscribeObserver(apiObserver);
      }
    }
  }
  
  /**
   * Runs REST endpoint demo snippet calling {@link DemoExchangeMarketDataApi#postRestRequestDataTypeObjectListMap(Map<String, List<SingleSymbol>>)}
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
