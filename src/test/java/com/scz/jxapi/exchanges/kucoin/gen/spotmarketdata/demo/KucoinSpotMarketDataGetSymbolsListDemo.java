package com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.demo;

import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApiImpl;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetSymbolsListRequest;
import com.scz.jxapi.util.TestJXApiProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi#getSymbolsList(com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetSymbolsListRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinSpotMarketDataGetSymbolsListDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinSpotMarketDataGetSymbolsListDemo.class);
  
  public static void main(String[] args) {
    try {
      KucoinSpotMarketDataApi api = new KucoinSpotMarketDataApiImpl(TestJXApiProperties.filterProperties("kucoin", true));
      KucoinGetSymbolsListRequest request = new KucoinGetSymbolsListRequest();
      log.info("Calling 'com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi.getSymbolsList() API with request:" + request);
      log.info("Response:" + api.getSymbolsList(request));
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
