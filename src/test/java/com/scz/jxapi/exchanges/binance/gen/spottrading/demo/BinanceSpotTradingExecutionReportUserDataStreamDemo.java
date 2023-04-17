package com.scz.jxapi.exchanges.binance.gen.spottrading.demo;

import com.scz.jxapi.exchanges.binance.gen.spottrading.BinanceSpotTradingApi;
import com.scz.jxapi.exchanges.binance.gen.spottrading.BinanceSpotTradingApiImpl;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceExecutionReportUserDataStreamRequest;
import com.scz.jxapi.util.TestApiProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.binance.gen.spottrading.BinanceSpotTradingApi#subscribeExecutionReportUserDataStream(com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceExecutionReportUserDataStreamRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class BinanceSpotTradingExecutionReportUserDataStreamDemo {
  private static final Logger log = LoggerFactory.getLogger(BinanceSpotTradingExecutionReportUserDataStreamDemo.class);
  
  public static void main(String[] args) {
    try {
      BinanceSpotTradingApi api = new BinanceSpotTradingApiImpl(TestApiProperties.filterProperties("binance", true));
      BinanceExecutionReportUserDataStreamRequest request = new BinanceExecutionReportUserDataStreamRequest();
      log.info("Subscribing to stream com.scz.jcex.exchanges.binance.gen.spottrading.BinanceSpotTradingApi.subscribeExecutionReportUserDataStream() websocket stream with request:" + request);
      api.subscribeExecutionReportUserDataStream(request, m -> log.info("Received message:" + m));
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
    }
  }
}
