package com.scz.jxapi.exchanges.kucoin.gen.spottrading.demo;

import com.scz.jxapi.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApi;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApiImpl;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelSingleOrderByClientOidRequest;
import com.scz.jxapi.util.TestJXApiProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApi#cancelSingleOrderByClientOid(com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelSingleOrderByClientOidRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinSpotTradingCancelSingleOrderByClientOidDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinSpotTradingCancelSingleOrderByClientOidDemo.class);
  
  /**
   * Sample value for <i>clientOid</i> parameter of <i>CancelSingleOrderByClientOid</i> API
   */
  public static final String CLIENTOID = "myClientOid";
  
  public static void main(String[] args) {
    try {
      KucoinSpotTradingApi api = new KucoinSpotTradingApiImpl(TestJXApiProperties.filterProperties("kucoin", true));
      KucoinCancelSingleOrderByClientOidRequest request = new KucoinCancelSingleOrderByClientOidRequest();
      request.setClientOid(CLIENTOID);
      log.info("Calling 'com.scz.jcex.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApi.cancelSingleOrderByClientOid() API with request:" + request);
      log.info("Response:" + api.cancelSingleOrderByClientOid(request));
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
