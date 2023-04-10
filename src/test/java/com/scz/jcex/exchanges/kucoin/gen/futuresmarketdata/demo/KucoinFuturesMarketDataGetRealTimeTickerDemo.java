package com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.demo;

import com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.KucoinFuturesMarketDataApi;
import com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.KucoinFuturesMarketDataApiImpl;
import com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetRealTimeTickerRequest;
import com.scz.jcex.util.TestApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.KucoinFuturesMarketDataApi#getRealTimeTicker(com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetRealTimeTickerRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class KucoinFuturesMarketDataGetRealTimeTickerDemo {
  private static final Logger log = LoggerFactory.getLogger(KucoinFuturesMarketDataGetRealTimeTickerDemo.class);
  
  /**
   * Sample value for <i>symbol</i> parameter of <i>GetRealTimeTicker</i> API
   */
  public static final String SYMBOL = "XBTUSDM";
  
  public static void main(String[] args) {
    try {
      KucoinFuturesMarketDataApi api = new KucoinFuturesMarketDataApiImpl(TestApiProperties.filterProperties("kucoin", true));
      KucoinGetRealTimeTickerRequest request = new KucoinGetRealTimeTickerRequest();
      request.setSymbol(SYMBOL);
      log.info("Calling 'com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.KucoinFuturesMarketDataApi.getRealTimeTicker() API with request:" + request);
      log.info("Response:" + api.getRealTimeTicker(request));
      System.exit(0);
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
      System.exit(-1);
    }
  }
}
