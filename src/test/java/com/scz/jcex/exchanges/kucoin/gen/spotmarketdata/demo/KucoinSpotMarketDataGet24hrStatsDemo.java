package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.demo;

import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApiImpl;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGet24hrStatsRequest;
import com.scz.jcex.util.TestApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi#get24hrStats(com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGet24hrStatsRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinSpotMarketDataGet24hrStatsDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinSpotMarketDataGet24hrStatsDemo.class);
  
  /**
   * Sample value for <i>symbol</i> parameter of <i>get24hrStats</i> API
   */
  public static final String SYMBOL = "BTC-USDT";
  
  public static void main(String[] args) {
    try {
      KucoinSpotMarketDataApi api = new KucoinSpotMarketDataApiImpl(TestApiProperties.filterProperties("kucoin", true));
      KucoinGet24hrStatsRequest request = new KucoinGet24hrStatsRequest();
      request.setSymbol(SYMBOL);
      log.info("Calling 'com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi.get24hrStats() API with request:" + request);
      log.info("Response:" + api.get24hrStats(request));
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
