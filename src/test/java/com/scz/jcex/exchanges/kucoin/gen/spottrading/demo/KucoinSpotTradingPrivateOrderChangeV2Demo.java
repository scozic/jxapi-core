package com.scz.jcex.exchanges.kucoin.gen.spottrading.demo;

import com.scz.jcex.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApi;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApiImpl;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinPrivateOrderChangeV2Request;
import com.scz.jcex.util.TestApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jcex.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApi#subscribePrivateOrderChangeV2(com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinPrivateOrderChangeV2Request)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinSpotTradingPrivateOrderChangeV2Demo {
  private static final Logger log = LoggerFactory.getLogger(KucoinSpotTradingPrivateOrderChangeV2Demo.class);
  
  public static void main(String[] args) {
    try {
      KucoinSpotTradingApi api = new KucoinSpotTradingApiImpl(TestApiProperties.filterProperties("kucoin", true));
      KucoinPrivateOrderChangeV2Request request = new KucoinPrivateOrderChangeV2Request();
      log.info("Subscribing to stream com.scz.jcex.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApi.subscribePrivateOrderChangeV2() websocket stream with request:" + request);
      api.subscribePrivateOrderChangeV2(request, m -> log.info("Received message:" + m));
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
    }
  }
}
