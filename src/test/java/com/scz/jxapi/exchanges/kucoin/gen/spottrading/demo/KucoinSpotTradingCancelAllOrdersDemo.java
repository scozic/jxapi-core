package com.scz.jxapi.exchanges.kucoin.gen.spottrading.demo;

import com.scz.jxapi.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApi;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApiImpl;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelAllOrdersRequest;
import com.scz.jxapi.util.TestJXApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApi#cancelAllOrders(com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelAllOrdersRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinSpotTradingCancelAllOrdersDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinSpotTradingCancelAllOrdersDemo.class);
  
  /**
   * Sample value for <i>symbol</i> parameter of <i>CancelAllOrders</i> API
   */
  public static final String SYMBOL = null;
  
  /**
   * Sample value for <i>tradeType</i> parameter of <i>CancelAllOrders</i> API
   */
  public static final String TRADETYPE = null;
  
  public static void main(String[] args) {
    try {
      KucoinSpotTradingApi api = new KucoinSpotTradingApiImpl(TestJXApiProperties.filterProperties("kucoin", true));
      KucoinCancelAllOrdersRequest request = new KucoinCancelAllOrdersRequest();
      request.setSymbol(SYMBOL);
      request.setTradeType(TRADETYPE);
      log.info("Calling 'com.scz.jxapi.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApi.cancelAllOrders() API with request:" + request);
      log.info("Response:" + api.cancelAllOrders(request));
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
