package com.scz.jxapi.exchanges.demo.gen.marketdata.demo;

import com.scz.jxapi.exchanges.demo.gen.DemoExchangeExchangeImpl;
import com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi;
import com.scz.jxapi.util.DemoUtil;
import com.scz.jxapi.util.TestJXApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi#getRestRequestDataTypePrimitiveWithMsgField(Integer)}<br>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class DemoExchangeMarketDataGetRestRequestDataTypePrimitiveWithMsgFieldDemo {
  private static final Logger log = LoggerFactory.getLogger(DemoExchangeMarketDataGetRestRequestDataTypePrimitiveWithMsgFieldDemo.class);
  
  public static Integer createAge() {
    return null;
  }
  public static void main(String[] args) {
    try {
      DemoExchangeMarketDataApi api = new DemoExchangeExchangeImpl("test-demoExchange", TestJXApiProperties.filterProperties("demoExchange", true)).getDemoExchangeMarketDataApi();
      Integer request = createAge();
      log.info("Calling com.scz.jxapi.exchanges.demo.gen.marketdata.DemoExchangeMarketDataApi.getRestRequestDataTypePrimitiveWithMsgField() API with request:" + request);
      DemoUtil.checkResponse(api.getRestRequestDataTypePrimitiveWithMsgField(request));
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
