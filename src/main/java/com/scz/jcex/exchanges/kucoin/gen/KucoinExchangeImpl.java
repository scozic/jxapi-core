package com.scz.jcex.exchanges.kucoin.gen;

import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApiImpl;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApi;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.KucoinSpotTradingApiImpl;
import java.util.Properties;

/**
 * Actual implementation of {@link KucoinExchange}<br/>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class  KucoinExchangeImpl implements KucoinExchange {
  private final KucoinSpotMarketDataApi kucoinSpotMarketDataApi;
  
  @Override
  public KucoinSpotMarketDataApi getKucoinSpotMarketDataApi() {
    return this.kucoinSpotMarketDataApi;
  }
  
  private final KucoinSpotTradingApi kucoinSpotTradingApi;
  
  @Override
  public KucoinSpotTradingApi getKucoinSpotTradingApi() {
    return this.kucoinSpotTradingApi;
  }
  
  public KucoinExchangeImpl(Properties properties) {
    this.kucoinSpotMarketDataApi = new KucoinSpotMarketDataApiImpl(properties);
    this.kucoinSpotTradingApi = new KucoinSpotTradingApiImpl(properties);
  }
}
