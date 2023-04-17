package com.scz.jxapi.exchanges.binance.gen.spottrading.demo;

import com.scz.jxapi.exchanges.binance.gen.spottrading.BinanceSpotTradingApi;
import com.scz.jxapi.exchanges.binance.gen.spottrading.BinanceSpotTradingApiImpl;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceSpotListenKeyRequest;
import com.scz.jxapi.util.TestApiProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.binance.gen.spottrading.BinanceSpotTradingApi#spotListenKey(com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceSpotListenKeyRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class BinanceSpotTradingSpotListenKeyDemo {
  private static final Logger log = LoggerFactory.getLogger(BinanceSpotTradingSpotListenKeyDemo.class);
  
  public static void main(String[] args) {
    try {
      BinanceSpotTradingApi api = new BinanceSpotTradingApiImpl(TestApiProperties.filterProperties("binance", true));
      BinanceSpotListenKeyRequest request = new BinanceSpotListenKeyRequest();
      log.info("Calling 'com.scz.jcex.exchanges.binance.gen.spottrading.BinanceSpotTradingApi.spotListenKey() API with request:" + request);
      log.info("Response:" + api.spotListenKey(request));
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
