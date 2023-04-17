package com.scz.jcex.exchanges.binance.gen.spottrading.demo;

import com.scz.jcex.exchanges.binance.gen.spottrading.BinanceSpotTradingApi;
import com.scz.jcex.exchanges.binance.gen.spottrading.BinanceSpotTradingApiImpl;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceBalanceUpdateUserDataStreamRequest;
import com.scz.jcex.util.TestApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jcex.exchanges.binance.gen.spottrading.BinanceSpotTradingApi#subscribeBalanceUpdateUserDataStream(com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceBalanceUpdateUserDataStreamRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class BinanceSpotTradingBalanceUpdateUserDataStreamDemo {
  private static final Logger log = LoggerFactory.getLogger(BinanceSpotTradingBalanceUpdateUserDataStreamDemo.class);
  
  public static void main(String[] args) {
    try {
      BinanceSpotTradingApi api = new BinanceSpotTradingApiImpl(TestApiProperties.filterProperties("binance", true));
      BinanceBalanceUpdateUserDataStreamRequest request = new BinanceBalanceUpdateUserDataStreamRequest();
      log.info("Subscribing to stream com.scz.jcex.exchanges.binance.gen.spottrading.BinanceSpotTradingApi.subscribeBalanceUpdateUserDataStream() websocket stream with request:" + request);
      api.subscribeBalanceUpdateUserDataStream(request, m -> log.info("Received message:" + m));
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
    }
  }
}
