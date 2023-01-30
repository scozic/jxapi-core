package com.scz.jcex.binance.spotmarketdata.pojo;

import com.scz.jcex.util.EncodingUtil;

/**
 * Subscription request toBinance SpotMarketData API AllMarketTickersStream REST endpoint
 */
public class BinanceAllMarketTickersStreamRequest {
  
  @Override
  public String toString(){
    return EncodingUtil.pojoToString(this);
  }
  
}
