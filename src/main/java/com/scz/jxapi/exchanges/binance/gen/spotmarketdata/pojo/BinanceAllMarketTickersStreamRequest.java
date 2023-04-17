package com.scz.jxapi.exchanges.binance.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.binance.gen.spotmarketdata.serializers.BinanceAllMarketTickersStreamRequestSerializer;
import com.scz.jxapi.netutils.websocket.WebsocketSubscribeParameters;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Subscription request toBinance SpotMarketData API AllMarketTickersStream websocket endpoint<br/>All Market Tickers Stream.<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#all-market-tickers-stream">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = BinanceAllMarketTickersStreamRequestSerializer.class)
public class BinanceAllMarketTickersStreamRequest implements WebsocketSubscribeParameters {
  
  
  @Override
  public String getTopic() {
    return "!ticker@arr";
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
