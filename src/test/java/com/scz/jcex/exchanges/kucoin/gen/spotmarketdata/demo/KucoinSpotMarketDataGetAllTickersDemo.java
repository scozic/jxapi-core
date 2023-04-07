package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.demo;

import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApiImpl;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetAllTickersRequest;
import com.scz.jcex.util.TestApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi#getAllTickers(com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetAllTickersRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinSpotMarketDataGetAllTickersDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinSpotMarketDataGetAllTickersDemo.class);
  
  /**
   * Sample value for <i>symbol</i> parameter of <i>getAllTickers</i> API
   */
  public static final String SYMBOL = "BTC-USDT";
  
  public static void main(String[] args) {
    try {
      KucoinSpotMarketDataApi api = new KucoinSpotMarketDataApiImpl(TestApiProperties.filterProperties("kucoin", true));
      KucoinGetAllTickersRequest request = new KucoinGetAllTickersRequest();
      request.setSymbol(SYMBOL);
      log.info("Calling 'com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi.getAllTickers() API with request:" + request);
      log.info("Response:" + api.getAllTickers(request));
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
