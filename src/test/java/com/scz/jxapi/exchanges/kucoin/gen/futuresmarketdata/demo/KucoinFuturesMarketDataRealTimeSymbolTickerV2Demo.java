package com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.demo;

import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.KucoinFuturesMarketDataApi;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.KucoinFuturesMarketDataApiImpl;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinRealTimeSymbolTickerV2Request;
import com.scz.jxapi.util.TestJXApiProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.KucoinFuturesMarketDataApi#subscribeRealTimeSymbolTickerV2(com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinRealTimeSymbolTickerV2Request)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinFuturesMarketDataRealTimeSymbolTickerV2Demo {
  private static final Logger log = LoggerFactory.getLogger(KucoinFuturesMarketDataRealTimeSymbolTickerV2Demo.class);
  
  /**
   * Sample value for <i>symbol</i> parameter of <i>RealTimeSymbolTickerV2</i> API
   */
  public static final String SYMBOL = "XBTUSDM";
  
  public static void main(String[] args) {
    try {
      KucoinFuturesMarketDataApi api = new KucoinFuturesMarketDataApiImpl(TestJXApiProperties.filterProperties("kucoin", true));
      KucoinRealTimeSymbolTickerV2Request request = new KucoinRealTimeSymbolTickerV2Request();
      request.setSymbol(SYMBOL);
      log.info("Subscribing to stream com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.KucoinFuturesMarketDataApi.subscribeRealTimeSymbolTickerV2() websocket stream with request:" + request);
      api.subscribeRealTimeSymbolTickerV2(request, m -> log.info("Received message:" + m));
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
    }
  }
}
