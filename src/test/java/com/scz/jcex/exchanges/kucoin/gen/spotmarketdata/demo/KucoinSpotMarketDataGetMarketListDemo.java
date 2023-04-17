package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.demo;

import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApiImpl;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetMarketListRequest;
import com.scz.jcex.util.TestApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi#getMarketList(com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetMarketListRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinSpotMarketDataGetMarketListDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinSpotMarketDataGetMarketListDemo.class);
  
  public static void main(String[] args) {
    try {
      KucoinSpotMarketDataApi api = new KucoinSpotMarketDataApiImpl(TestApiProperties.filterProperties("kucoin", true));
      KucoinGetMarketListRequest request = new KucoinGetMarketListRequest();
      log.info("Calling 'com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi.getMarketList() API with request:" + request);
      log.info("Response:" + api.getMarketList(request));
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
