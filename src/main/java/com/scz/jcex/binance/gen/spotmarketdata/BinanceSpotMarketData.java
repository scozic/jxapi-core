package com.scz.jcex.binance.gen.spotmarketdata;

import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationAllRequest;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationAllResponse;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationRequest;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponse;
import java.io.IOException;

/**
 * SpotMarketData CEX SpotMarketData API</br>
 * Binance spot market data API
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public interface  BinanceSpotMarketData {
  /**
   * Current exchange trading rules and symbol information for all spot trading pairs
   */
  BinanceExchangeInformationAllResponse exchangeInformationAll(BinanceExchangeInformationAllRequest request) throws IOException;
  /**
   * Current exchange trading rules and symbol information for a list of spot trading pairs
   */
  BinanceExchangeInformationResponse exchangeInformation(BinanceExchangeInformationRequest request) throws IOException;
  
}
