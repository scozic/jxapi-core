package com.scz.jxapi.exchanges.binance.gen.spotmarketdata.demo;

import com.scz.jxapi.exchanges.binance.gen.spotmarketdata.BinanceSpotMarketDataApi;
import com.scz.jxapi.exchanges.binance.gen.spotmarketdata.BinanceSpotMarketDataApiImpl;
import com.scz.jxapi.exchanges.binance.gen.spotmarketdata.pojo.BinanceIndividualSymbolTickerStreamsRequest;
import com.scz.jxapi.util.TestJXApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.binance.gen.spotmarketdata.BinanceSpotMarketDataApi#subscribeIndividualSymbolTickerStreams(com.scz.jxapi.exchanges.binance.gen.spotmarketdata.pojo.BinanceIndividualSymbolTickerStreamsRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class BinanceSpotMarketDataIndividualSymbolTickerStreamsDemo {
  private static final Logger log = LoggerFactory.getLogger(BinanceSpotMarketDataIndividualSymbolTickerStreamsDemo.class);
  
  /**
   * Sample value for <i>symbol</i> parameter of <i>IndividualSymbolTickerStreams</i> API
   */
  public static final String SYMBOL = "BTCUSDT";
  
  public static void main(String[] args) {
    try {
      BinanceSpotMarketDataApi api = new BinanceSpotMarketDataApiImpl(TestJXApiProperties.filterProperties("binance", true));
      BinanceIndividualSymbolTickerStreamsRequest request = new BinanceIndividualSymbolTickerStreamsRequest();
      request.setSymbol(SYMBOL);
      log.info("Subscribing to stream com.scz.jxapi.exchanges.binance.gen.spotmarketdata.BinanceSpotMarketDataApi.subscribeIndividualSymbolTickerStreams() websocket stream with request:" + request);
      api.subscribeIndividualSymbolTickerStreams(request, m -> log.info("Received message:" + m));
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
    }
  }
}
