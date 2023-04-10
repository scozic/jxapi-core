package com.scz.jcex.exchanges.kucoin.gen.futurestrading;

import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetAccountOverviewRequest;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetAccountOverviewResponse;
import java.io.IOException;

/**
 * FuturesTrading CEX FuturesTrading API</br>
 * Kucoin Futures trading API
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public interface  KucoinFuturesTradingApi {
  /**
   * Get account overview.<br/>See <a href="https://docs.kucoin.com/futures/#get-account-overview">API</a>
   */
  KucoinGetAccountOverviewResponse getAccountOverview(KucoinGetAccountOverviewRequest request) throws IOException;
}
