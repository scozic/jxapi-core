package com.scz.jcex.exchanges.binance.gen.spottrading.demo;

import com.scz.jcex.exchanges.binance.gen.spottrading.BinanceSpotTradingApi;
import com.scz.jcex.exchanges.binance.gen.spottrading.BinanceSpotTradingApiImpl;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceAccountRequest;
import com.scz.jcex.util.TestApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jcex.exchanges.binance.gen.spottrading.BinanceSpotTradingApi#account(com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceAccountRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class BinanceSpotTradingAccountDemo {
  private static final Logger log = LoggerFactory.getLogger(BinanceSpotTradingAccountDemo.class);
  
  /**
   * Sample value for <i>recvWindow</i> parameter of <i>account</i> API
   */
  public static final Long RECVWINDOW = 60000L;
  
  /**
   * Sample value for <i>timestamp</i> parameter of <i>account</i> API
   */
  public static final Long TIMESTAMP = System.currentTimeMillis();
  
  public static void main(String[] args) {
    try {
      BinanceSpotTradingApi api = new BinanceSpotTradingApiImpl(TestApiProperties.filterProperties("binance", true));
      BinanceAccountRequest request = new BinanceAccountRequest();
      request.setRecvWindow(RECVWINDOW);
      request.setTimestamp(TIMESTAMP);
      log.info("Calling 'com.scz.jcex.exchanges.binance.gen.spottrading.BinanceSpotTradingApi.account() API with request:" + request);
      log.info("Response:" + api.account(request));
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
