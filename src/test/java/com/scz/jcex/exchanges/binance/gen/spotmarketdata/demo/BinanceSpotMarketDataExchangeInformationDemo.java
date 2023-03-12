package com.scz.jcex.exchanges.binance.gen.spotmarketdata.demo;

import com.scz.jcex.exchanges.binance.gen.spotmarketdata.BinanceSpotMarketDataApi;
import com.scz.jcex.exchanges.binance.gen.spotmarketdata.BinanceSpotMarketDataApiImpl;
import com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationRequest;
import com.scz.jcex.util.TestApiProperties;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snippet to test call to {@link com.scz.jcex.exchanges.binance.gen.spotmarketdata.BinanceSpotMarketDataApi#exchangeInformation(com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationRequest)}
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class BinanceSpotMarketDataExchangeInformationDemo {
  private static final Logger log = LoggerFactory.getLogger(BinanceSpotMarketDataExchangeInformationDemo.class);
  
  /**
   * Sample value for <i>symbols</i> parameter of <i>exchangeInformation</i> API
   */
  public static final List<String> SYMBOLS = List.of("ETHBTC", "BNBUSDT");
  
  public static void main(String[] args) {
    try {
      BinanceSpotMarketDataApi api = new BinanceSpotMarketDataApiImpl(TestApiProperties.filterProperties("binance", true));
      BinanceExchangeInformationRequest request = new BinanceExchangeInformationRequest();
      request.setSymbols(SYMBOLS);
      log.info("Calling 'com.scz.jcex.exchanges.binance.gen.spotmarketdata.BinanceSpotMarketDataApi.exchangeInformation() API with request:" + request);
      log.info("Response:" + api.exchangeInformation(request));
    } catch (Throwable t) {
      log.error("Exception raised from main()", t);
    }
  }
}
