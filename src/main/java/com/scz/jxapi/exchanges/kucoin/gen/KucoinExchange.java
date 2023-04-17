package com.scz.jxapi.exchanges.kucoin.gen;

import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.KucoinFuturesMarketDataApi;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.KucoinFuturesTradingApi;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApi;

/**
 * Kucoin CEX API</br>
 * Kucoin Spot and futures API
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public interface  KucoinExchange {
  
  KucoinSpotMarketDataApi getKucoinSpotMarketDataApi();
  
  KucoinSpotTradingApi getKucoinSpotTradingApi();
  
  KucoinFuturesMarketDataApi getKucoinFuturesMarketDataApi();
  
  KucoinFuturesTradingApi getKucoinFuturesTradingApi();
}
