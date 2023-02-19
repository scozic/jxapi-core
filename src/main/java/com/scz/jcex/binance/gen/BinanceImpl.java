package com.scz.jcex.binance.gen;

import com.scz.jcex.binance.gen.spotmarketdata.BinanceSpotMarketData;
import com.scz.jcex.binance.gen.spotmarketdata.BinanceSpotMarketDataImpl;
import com.scz.jcex.binance.gen.spottrading.BinanceSpotTrading;
import com.scz.jcex.binance.gen.spottrading.BinanceSpotTradingImpl;
import java.util.Properties;

/**
 * Actual implementation of {@link Binance}<br/>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class  BinanceImpl implements Binance {
  private final BinanceSpotMarketData binanceSpotMarketData;
  
  @Override
  public BinanceSpotMarketData getBinanceSpotMarketData() {
    return this.binanceSpotMarketData;
  }
  
  private final BinanceSpotTrading binanceSpotTrading;
  
  @Override
  public BinanceSpotTrading getBinanceSpotTrading() {
    return this.binanceSpotTrading;
  }
  
  public BinanceImpl(Properties properties) {
    this.binanceSpotMarketData = new BinanceSpotMarketDataImpl(properties);
    this.binanceSpotTrading = new BinanceSpotTradingImpl(properties);
  }
}
