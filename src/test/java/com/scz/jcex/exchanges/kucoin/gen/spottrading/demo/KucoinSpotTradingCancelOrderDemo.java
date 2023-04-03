package com.scz.jcex.exchanges.kucoin.gen.spottrading.demo;

import com.scz.jcex.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApi;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApiImpl;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelOrderRequest;
import com.scz.jcex.util.TestApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jcex.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApi#cancelOrder(com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelOrderRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinSpotTradingCancelOrderDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinSpotTradingCancelOrderDemo.class);
  
  /**
   * Sample value for <i>orderId</i> parameter of <i>CancelOrder</i> API
   */
  public static final String ORDERID = "5bd6e9286d99522a52e458de";
  
  public static void main(String[] args) {
    try {
      KucoinSpotTradingApi api = new KucoinSpotTradingApiImpl(TestApiProperties.filterProperties("kucoin", true));
      KucoinCancelOrderRequest request = new KucoinCancelOrderRequest();
      request.setOrderId(ORDERID);
      log.info("Calling 'com.scz.jcex.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApi.cancelOrder() API with request:" + request);
      log.info("Response:" + api.cancelOrder(request));
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
    }
  }
}
