package com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.serializers.KucoinRealTimeSymbolTickerV2RequestSerializer;
import com.scz.jcex.netutils.websocket.WebsocketSubscribeParameters;
import com.scz.jcex.util.EncodingUtil;

/**
 * Subscription request toKucoin FuturesMarketData API RealTimeSymbolTickerV2 websocket endpoint<br/>24hr rolling window ticker statistics for a single symbol. These are NOT the statistics of the UTC day, but a 24hr rolling window for the previous 24hrs.<br/>See <a href="https://docs.kucoin.com/#symbol-ticker">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinRealTimeSymbolTickerV2RequestSerializer.class)
public class KucoinRealTimeSymbolTickerV2Request implements WebsocketSubscribeParameters {
  private String symbol;
  
  /**
   * @return Market symbol
   */
  public String getSymbol(){
    return symbol;
  }
  
  /**
   * @param symbol Market symbol
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
  
  
  @Override
  public String getTopic() {
    return EncodingUtil.substituteArguments("/contractMarket/tickerV2:${symbol}", "symbol", symbol);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
