package com.scz.jxapi.exchanges.binance.gen.spottrading.demo;

import com.scz.jxapi.exchanges.binance.gen.spottrading.BinanceSpotTradingApi;
import com.scz.jxapi.exchanges.binance.gen.spottrading.BinanceSpotTradingApiImpl;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceSpotDeleteListenKeyRequest;
import com.scz.jxapi.util.TestJXApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.binance.gen.spottrading.BinanceSpotTradingApi#spotDeleteListenKey(com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceSpotDeleteListenKeyRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class BinanceSpotTradingSpotDeleteListenKeyDemo {
  private static final Logger log = LoggerFactory.getLogger(BinanceSpotTradingSpotDeleteListenKeyDemo.class);
  
  /**
   * Sample value for <i>listenKey</i> parameter of <i>spotDeleteListenKey</i> API
   */
  public static final String LISTENKEY = "pqia91ma19a5s61cv6a81va65sdf19v8a65a1a5s61cv6a81va65sdf19v8a65a1";
  
  public static void main(String[] args) {
    try {
      BinanceSpotTradingApi api = new BinanceSpotTradingApiImpl(TestJXApiProperties.filterProperties("binance", true));
      BinanceSpotDeleteListenKeyRequest request = new BinanceSpotDeleteListenKeyRequest();
      request.setListenKey(LISTENKEY);
      log.info("Calling 'com.scz.jxapi.exchanges.binance.gen.spottrading.BinanceSpotTradingApi.spotDeleteListenKey() API with request:" + request);
      log.info("Response:" + api.spotDeleteListenKey(request));
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
