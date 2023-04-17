package com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.demo;

import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.KucoinFuturesMarketDataApi;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.KucoinFuturesMarketDataApiImpl;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetOpenContractListRequest;
import com.scz.jxapi.util.TestApiProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.KucoinFuturesMarketDataApi#getOpenContractList(com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetOpenContractListRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinFuturesMarketDataGetOpenContractListDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinFuturesMarketDataGetOpenContractListDemo.class);
  
  public static void main(String[] args) {
    try {
      KucoinFuturesMarketDataApi api = new KucoinFuturesMarketDataApiImpl(TestApiProperties.filterProperties("kucoin", true));
      KucoinGetOpenContractListRequest request = new KucoinGetOpenContractListRequest();
      log.info("Calling 'com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.KucoinFuturesMarketDataApi.getOpenContractList() API with request:" + request);
      log.info("Response:" + api.getOpenContractList(request));
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
