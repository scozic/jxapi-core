package com.scz.jxapi.exchanges.kucoin.gen.spottrading.demo;

import com.scz.jxapi.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApi;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApiImpl;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinAccountBalanceNoticeRequest;
import com.scz.jxapi.util.TestApiProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApi#subscribeAccountBalanceNotice(com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinAccountBalanceNoticeRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinSpotTradingAccountBalanceNoticeDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinSpotTradingAccountBalanceNoticeDemo.class);
  
  public static void main(String[] args) {
    try {
      KucoinSpotTradingApi api = new KucoinSpotTradingApiImpl(TestApiProperties.filterProperties("kucoin", true));
      KucoinAccountBalanceNoticeRequest request = new KucoinAccountBalanceNoticeRequest();
      log.info("Subscribing to stream com.scz.jcex.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApi.subscribeAccountBalanceNotice() websocket stream with request:" + request);
      api.subscribeAccountBalanceNotice(request, m -> log.info("Received message:" + m));
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
    }
  }
}
