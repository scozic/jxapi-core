package com.scz.jcex.binance.spotmarketdata.pojo;

import com.scz.jcex.util.EncodingUtil;

/**
 * Request for Binance SpotMarketData API exchangeInformation REST endpoint
 */
public class BinanceExchangeInformationRequest {
  
  @Override
  public String toString(){
    return EncodingUtil.pojoToString(this);
  }
  
}
