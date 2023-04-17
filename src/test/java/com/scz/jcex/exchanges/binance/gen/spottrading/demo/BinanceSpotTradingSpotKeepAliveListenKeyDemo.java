package com.scz.jcex.exchanges.binance.gen.spottrading.demo;

import com.scz.jcex.exchanges.binance.gen.spottrading.BinanceSpotTradingApi;
import com.scz.jcex.exchanges.binance.gen.spottrading.BinanceSpotTradingApiImpl;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceSpotKeepAliveListenKeyRequest;
import com.scz.jcex.util.TestApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jcex.exchanges.binance.gen.spottrading.BinanceSpotTradingApi#spotKeepAliveListenKey(com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceSpotKeepAliveListenKeyRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class BinanceSpotTradingSpotKeepAliveListenKeyDemo {
  private static final Logger log = LoggerFactory.getLogger(BinanceSpotTradingSpotKeepAliveListenKeyDemo.class);
  
  /**
   * Sample value for <i>listenKey</i> parameter of <i>spotKeepAliveListenKey</i> API
   */
  public static final String LISTENKEY = "pqia91ma19a5s61cv6a81va65sdf19v8a65a1a5s61cv6a81va65sdf19v8a65a1";
  
  public static void main(String[] args) {
    try {
      BinanceSpotTradingApi api = new BinanceSpotTradingApiImpl(TestApiProperties.filterProperties("binance", true));
      BinanceSpotKeepAliveListenKeyRequest request = new BinanceSpotKeepAliveListenKeyRequest();
      request.setListenKey(LISTENKEY);
      log.info("Calling 'com.scz.jcex.exchanges.binance.gen.spottrading.BinanceSpotTradingApi.spotKeepAliveListenKey() API with request:" + request);
      log.info("Response:" + api.spotKeepAliveListenKey(request));
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
