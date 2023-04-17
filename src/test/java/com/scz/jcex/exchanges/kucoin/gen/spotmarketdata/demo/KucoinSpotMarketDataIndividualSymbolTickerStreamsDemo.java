package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.demo;

import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApiImpl;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinIndividualSymbolTickerStreamsRequest;
import com.scz.jcex.util.TestApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi#subscribeIndividualSymbolTickerStreams(com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinIndividualSymbolTickerStreamsRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinSpotMarketDataIndividualSymbolTickerStreamsDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinSpotMarketDataIndividualSymbolTickerStreamsDemo.class);
  
  /**
   * Sample value for <i>symbol</i> parameter of <i>IndividualSymbolTickerStreams</i> API
   */
  public static final String SYMBOL = "BTC-USDT";
  
  public static void main(String[] args) {
    try {
      KucoinSpotMarketDataApi api = new KucoinSpotMarketDataApiImpl(TestApiProperties.filterProperties("kucoin", true));
      KucoinIndividualSymbolTickerStreamsRequest request = new KucoinIndividualSymbolTickerStreamsRequest();
      request.setSymbol(SYMBOL);
      log.info("Subscribing to stream com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi.subscribeIndividualSymbolTickerStreams() websocket stream with request:" + request);
      api.subscribeIndividualSymbolTickerStreams(request, m -> log.info("Received message:" + m));
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
    }
  }
}
