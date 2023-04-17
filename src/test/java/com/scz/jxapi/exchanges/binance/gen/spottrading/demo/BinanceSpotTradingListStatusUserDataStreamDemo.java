package com.scz.jxapi.exchanges.binance.gen.spottrading.demo;

import com.scz.jxapi.exchanges.binance.gen.spottrading.BinanceSpotTradingApi;
import com.scz.jxapi.exchanges.binance.gen.spottrading.BinanceSpotTradingApiImpl;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceListStatusUserDataStreamRequest;
import com.scz.jxapi.util.TestApiProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.binance.gen.spottrading.BinanceSpotTradingApi#subscribeListStatusUserDataStream(com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceListStatusUserDataStreamRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class BinanceSpotTradingListStatusUserDataStreamDemo {
  private static final Logger log = LoggerFactory.getLogger(BinanceSpotTradingListStatusUserDataStreamDemo.class);
  
  public static void main(String[] args) {
    try {
      BinanceSpotTradingApi api = new BinanceSpotTradingApiImpl(TestApiProperties.filterProperties("binance", true));
      BinanceListStatusUserDataStreamRequest request = new BinanceListStatusUserDataStreamRequest();
      log.info("Subscribing to stream com.scz.jcex.exchanges.binance.gen.spottrading.BinanceSpotTradingApi.subscribeListStatusUserDataStream() websocket stream with request:" + request);
      api.subscribeListStatusUserDataStream(request, m -> log.info("Received message:" + m));
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
    }
  }
}
