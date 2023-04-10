package com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata;

import com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetOpenContractListRequest;
import com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetOpenContractListResponse;
import java.io.IOException;

/**
 * FuturesMarketData CEX FuturesMarketData API</br>
 * Kucoin Futures market data API
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public interface  KucoinFuturesMarketDataApi {
  /**
   * Submit request to get the info of all open contracts.<br/>See <a href="https://docs.kucoin.com/futures/#get-open-contract-list">API</a>
   */
  KucoinGetOpenContractListResponse getOpenContractList(KucoinGetOpenContractListRequest request) throws IOException;
}
