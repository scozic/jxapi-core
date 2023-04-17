package com.scz.jxapi.exchanges.binance.gen.spottrading.demo;

import com.scz.jxapi.exchanges.binance.gen.spottrading.BinanceSpotTradingApi;
import com.scz.jxapi.exchanges.binance.gen.spottrading.BinanceSpotTradingApiImpl;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamRequest;
import com.scz.jxapi.util.TestJXApiProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.binance.gen.spottrading.BinanceSpotTradingApi#subscribeOutboundAccountPositionUserDataStream(com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class BinanceSpotTradingOutboundAccountPositionUserDataStreamDemo {
  private static final Logger log = LoggerFactory.getLogger(BinanceSpotTradingOutboundAccountPositionUserDataStreamDemo.class);
  
  public static void main(String[] args) {
    try {
      BinanceSpotTradingApi api = new BinanceSpotTradingApiImpl(TestJXApiProperties.filterProperties("binance", true));
      BinanceOutboundAccountPositionUserDataStreamRequest request = new BinanceOutboundAccountPositionUserDataStreamRequest();
      log.info("Subscribing to stream com.scz.jcex.exchanges.binance.gen.spottrading.BinanceSpotTradingApi.subscribeOutboundAccountPositionUserDataStream() websocket stream with request:" + request);
      api.subscribeOutboundAccountPositionUserDataStream(request, m -> log.info("Received message:" + m));
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
    }
  }
}
