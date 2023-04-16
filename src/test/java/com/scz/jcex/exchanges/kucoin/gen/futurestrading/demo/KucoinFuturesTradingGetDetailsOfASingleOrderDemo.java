package com.scz.jcex.exchanges.kucoin.gen.futurestrading.demo;

import com.scz.jcex.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApiImpl;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetDetailsOfASingleOrderRequest;
import com.scz.jcex.util.TestApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jcex.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi#getDetailsOfASingleOrder(com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetDetailsOfASingleOrderRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinFuturesTradingGetDetailsOfASingleOrderDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinFuturesTradingGetDetailsOfASingleOrderDemo.class);
  
  /**
   * Sample value for <i>orderId</i> parameter of <i>GetDetailsOfASingleOrder</i> API
   */
  public static final String ORDERID = "5cdfc138b21023a909e5ad55";
  
  public static void main(String[] args) {
    try {
      KucoinFuturesTradingApi api = new KucoinFuturesTradingApiImpl(TestApiProperties.filterProperties("kucoin", true));
      KucoinGetDetailsOfASingleOrderRequest request = new KucoinGetDetailsOfASingleOrderRequest();
      request.setOrderId(ORDERID);
      log.info("Calling 'com.scz.jcex.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi.getDetailsOfASingleOrder() API with request:" + request);
      log.info("Response:" + api.getDetailsOfASingleOrder(request));
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
