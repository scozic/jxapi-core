package com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.demo;

import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApiImpl;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinAllSymbolsTickerStreamRequest;
import com.scz.jxapi.util.TestJXApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi#subscribeAllSymbolsTickerStream(com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinAllSymbolsTickerStreamRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinSpotMarketDataAllSymbolsTickerStreamDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinSpotMarketDataAllSymbolsTickerStreamDemo.class);
  
  public static void main(String[] args) {
    try {
      KucoinSpotMarketDataApi api = new KucoinSpotMarketDataApiImpl(TestJXApiProperties.filterProperties("kucoin", true));
      KucoinAllSymbolsTickerStreamRequest request = new KucoinAllSymbolsTickerStreamRequest();
      log.info("Subscribing to stream com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi.subscribeAllSymbolsTickerStream() websocket stream with request:" + request);
      api.subscribeAllSymbolsTickerStream(request, m -> log.info("Received message:" + m));
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
    }
  }
}
