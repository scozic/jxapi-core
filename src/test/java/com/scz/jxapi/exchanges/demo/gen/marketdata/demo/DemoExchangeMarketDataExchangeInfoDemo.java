package com.scz.jxapi.exchanges.demo.gen.marketdata.demo;

import com.scz.jxapi.exchanges.demo.gen.DemoExchangeExchangeImpl;
import com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoRequest;
import com.scz.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import com.scz.jxapi.netutils.deserialization.json.field.StringJsonFieldDeserializer;
import com.scz.jxapi.util.DemoUtil;
import com.scz.jxapi.util.TestJXApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi#exchangeInfo(DemoExchangeMarketDataExchangeInfoRequest)}<br>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class DemoExchangeMarketDataExchangeInfoDemo {
  private static final Logger log = LoggerFactory.getLogger(DemoExchangeMarketDataExchangeInfoDemo.class);
  
  public static DemoExchangeMarketDataExchangeInfoRequest createRequest() {
    DemoExchangeMarketDataExchangeInfoRequest request = new DemoExchangeMarketDataExchangeInfoRequest();
    request.setSymbols(new ListJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance()).deserialize("[\"BTC\", \"ETH\"]"));
    return request;
  }
  public static void main(String[] args) {
    try {
      DemoExchangeMarketDataApi api = new DemoExchangeExchangeImpl("test-demoExchange", TestJXApiProperties.filterProperties("demoExchange", true)).getDemoExchangeMarketDataApi();
      DemoExchangeMarketDataExchangeInfoRequest request = createRequest();
      log.info("Calling com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi.exchangeInfo() API with request:" + request);
      DemoUtil.checkResponse(api.exchangeInfo(request));
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
