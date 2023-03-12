package com.scz.jcex.exchanges.binance.gen.spotmarketdata.demo;

import com.scz.jcex.exchanges.binance.gen.spotmarketdata.BinanceSpotMarketDataApi;
import com.scz.jcex.exchanges.binance.gen.spotmarketdata.BinanceSpotMarketDataApiImpl;
import com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationAllRequest;
import com.scz.jcex.util.TestApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jcex.exchanges.binance.gen.spotmarketdata.BinanceSpotMarketDataApi#exchangeInformationAll(com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationAllRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class BinanceSpotMarketDataExchangeInformationAllDemo {
  private static final Logger log = LoggerFactory.getLogger(BinanceSpotMarketDataExchangeInformationAllDemo.class);
  
  public static void main(String[] args) {
    try {
      BinanceSpotMarketDataApi api = new BinanceSpotMarketDataApiImpl(TestApiProperties.filterProperties("binance", true));
      BinanceExchangeInformationAllRequest request = new BinanceExchangeInformationAllRequest();
      log.info("Calling 'com.scz.jcex.exchanges.binance.gen.spotmarketdata.BinanceSpotMarketDataApi.exchangeInformationAll() API with request:" + request);
      log.info("Response:" + api.exchangeInformationAll(request));
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
    }
  }
}
