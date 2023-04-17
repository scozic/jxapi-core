package com.scz.jcex.exchanges.kucoin.gen.futurestrading.demo;

import com.scz.jcex.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApiImpl;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinCancelAnOrderRequest;
import com.scz.jcex.util.TestApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jcex.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi#cancelAnOrder(com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinCancelAnOrderRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinFuturesTradingCancelAnOrderDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinFuturesTradingCancelAnOrderDemo.class);
  
  /**
   * Sample value for <i>orderId</i> parameter of <i>CancelAnOrder</i> API
   */
  public static final String ORDERID = "5bd6e9286d99522a52e458de";
  
  public static void main(String[] args) {
    try {
      KucoinFuturesTradingApi api = new KucoinFuturesTradingApiImpl(TestApiProperties.filterProperties("kucoin", true));
      KucoinCancelAnOrderRequest request = new KucoinCancelAnOrderRequest();
      request.setOrderId(ORDERID);
      log.info("Calling 'com.scz.jcex.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi.cancelAnOrder() API with request:" + request);
      log.info("Response:" + api.cancelAnOrder(request));
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
