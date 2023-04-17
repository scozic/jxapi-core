package com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.demo;

import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApiImpl;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetTickerRequest;
import com.scz.jxapi.util.TestJXApiProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi#getTicker(com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetTickerRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinSpotMarketDataGetTickerDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinSpotMarketDataGetTickerDemo.class);
  
  /**
   * Sample value for <i>symbol</i> parameter of <i>getTicker</i> API
   */
  public static final String SYMBOL = "BTC-USDT";
  
  public static void main(String[] args) {
    try {
      KucoinSpotMarketDataApi api = new KucoinSpotMarketDataApiImpl(TestJXApiProperties.filterProperties("kucoin", true));
      KucoinGetTickerRequest request = new KucoinGetTickerRequest();
      request.setSymbol(SYMBOL);
      log.info("Calling 'com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi.getTicker() API with request:" + request);
      log.info("Response:" + api.getTicker(request));
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
