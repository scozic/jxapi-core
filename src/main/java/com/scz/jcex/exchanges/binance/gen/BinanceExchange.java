package com.scz.jcex.exchanges.binance.gen;

import com.scz.jcex.exchanges.binance.gen.spotmarketdata.BinanceSpotMarketDataApi;
import com.scz.jcex.exchanges.binance.gen.spottrading.BinanceSpotTradingApi;

/**
 * Binance CEX API</br>
 * Binance Spot and futures API
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public interface  BinanceExchange {
  
  BinanceSpotMarketDataApi getBinanceSpotMarketDataApi();
  
  BinanceSpotTradingApi getBinanceSpotTradingApi();
}
