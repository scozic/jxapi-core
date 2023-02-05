package com.scz.jcex.binance.spotmarketdata.pojo;

import com.scz.jcex.netutils.websocket.WebsocketSubscribeParameters;
import com.scz.jcex.util.EncodingUtil;

/**
 * Subscription request toBinance SpotMarketData API AllMarketTickersStream websocket endpoint<br/>All Market Tickers Stream, see <a href="https://binance-docs.github.io/apidocs/spot/en/#all-market-tickers-stream">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class BinanceAllMarketTickersStreamRequest implements WebsocketSubscribeParameters {
  
  
  @Override
  public String getTopic() {
    return com.scz.jcex.util.EncodingUtil.substituteArguments("!ticker@arr");
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
}
