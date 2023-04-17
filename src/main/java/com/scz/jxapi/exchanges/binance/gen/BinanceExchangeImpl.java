package com.scz.jxapi.exchanges.binance.gen;

import com.scz.jxapi.exchanges.binance.gen.spotmarketdata.BinanceSpotMarketDataApi;
import com.scz.jxapi.exchanges.binance.gen.spotmarketdata.BinanceSpotMarketDataApiImpl;
import com.scz.jxapi.exchanges.binance.gen.spottrading.BinanceSpotTradingApi;
import com.scz.jxapi.exchanges.binance.gen.spottrading.BinanceSpotTradingApiImpl;
import java.util.Properties;

/**
 * Actual implementation of {@link BinanceExchange}<br/>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class  BinanceExchangeImpl implements BinanceExchange {
  private final BinanceSpotMarketDataApi binanceSpotMarketDataApi;
  
  @Override
  public BinanceSpotMarketDataApi getBinanceSpotMarketDataApi() {
    return this.binanceSpotMarketDataApi;
  }
  
  private final BinanceSpotTradingApi binanceSpotTradingApi;
  
  @Override
  public BinanceSpotTradingApi getBinanceSpotTradingApi() {
    return this.binanceSpotTradingApi;
  }
  
  public BinanceExchangeImpl(Properties properties) {
    this.binanceSpotMarketDataApi = new BinanceSpotMarketDataApiImpl(properties);
    this.binanceSpotTradingApi = new BinanceSpotTradingApiImpl(properties);
  }
}
