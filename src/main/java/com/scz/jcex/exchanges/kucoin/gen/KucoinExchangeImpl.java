package com.scz.jcex.exchanges.kucoin.gen;

import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApi;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.KucoinSpotMarketDataApiImpl;
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
  
  public KucoinExchangeImpl(Properties properties) {
    this.kucoinSpotMarketDataApi = new KucoinSpotMarketDataApiImpl(properties);
  }
}
