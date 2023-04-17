package com.scz.jxapi.exchanges.kucoin.gen.spottrading.demo;

import com.scz.jxapi.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApi;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApiImpl;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateRequest;
import com.scz.jxapi.util.TestApiProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApi#applyConnectTokenPrivate(com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinSpotTradingApplyConnectTokenPrivateDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinSpotTradingApplyConnectTokenPrivateDemo.class);
  
  public static void main(String[] args) {
    try {
      KucoinSpotTradingApi api = new KucoinSpotTradingApiImpl(TestApiProperties.filterProperties("kucoin", true));
      KucoinApplyConnectTokenPrivateRequest request = new KucoinApplyConnectTokenPrivateRequest();
      log.info("Calling 'com.scz.jcex.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApi.applyConnectTokenPrivate() API with request:" + request);
      log.info("Response:" + api.applyConnectTokenPrivate(request));
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
