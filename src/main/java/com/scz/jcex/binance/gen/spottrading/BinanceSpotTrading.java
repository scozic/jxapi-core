package com.scz.jcex.binance.gen.spottrading;

import com.scz.jcex.binance.gen.spottrading.pojo.BinanceAccountRequest;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceAccountResponse;
import java.io.IOException;

/**
 * SpotTrading CEX SpotTrading API</br>
 * Binance spot trading API
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public interface  BinanceSpotTrading {
  /**
   * Get current account information, see <a href="https://binance-docs.github.io/apidocs/spot/en/#account-information-user_data">API</a>
   */
  BinanceAccountResponse account(BinanceAccountRequest request) throws IOException;
  
}
