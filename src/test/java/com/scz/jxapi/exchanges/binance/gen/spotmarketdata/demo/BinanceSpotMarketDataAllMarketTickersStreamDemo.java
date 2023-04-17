package com.scz.jxapi.exchanges.binance.gen.spotmarketdata.demo;

import com.scz.jxapi.exchanges.binance.gen.spotmarketdata.BinanceSpotMarketDataApi;
import com.scz.jxapi.exchanges.binance.gen.spotmarketdata.BinanceSpotMarketDataApiImpl;
import com.scz.jxapi.exchanges.binance.gen.spotmarketdata.pojo.BinanceAllMarketTickersStreamRequest;
import com.scz.jxapi.util.TestJXApiProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.binance.gen.spotmarketdata.BinanceSpotMarketDataApi#subscribeAllMarketTickersStream(com.scz.jxapi.exchanges.binance.gen.spotmarketdata.pojo.BinanceAllMarketTickersStreamRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class BinanceSpotMarketDataAllMarketTickersStreamDemo {
  private static final Logger log = LoggerFactory.getLogger(BinanceSpotMarketDataAllMarketTickersStreamDemo.class);
  
  public static void main(String[] args) {
    try {
      BinanceSpotMarketDataApi api = new BinanceSpotMarketDataApiImpl(TestJXApiProperties.filterProperties("binance", true));
      BinanceAllMarketTickersStreamRequest request = new BinanceAllMarketTickersStreamRequest();
      log.info("Subscribing to stream com.scz.jcex.exchanges.binance.gen.spotmarketdata.BinanceSpotMarketDataApi.subscribeAllMarketTickersStream() websocket stream with request:" + request);
      api.subscribeAllMarketTickersStream(request, m -> log.info("Received message:" + m));
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
    }
  }
}
