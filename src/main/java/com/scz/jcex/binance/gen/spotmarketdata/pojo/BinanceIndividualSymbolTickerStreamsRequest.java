package com.scz.jcex.binance.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.binance.gen.spotmarketdata.serializers.BinanceIndividualSymbolTickerStreamsRequestSerializer;
import com.scz.jcex.netutils.websocket.WebsocketSubscribeParameters;
import com.scz.jcex.util.EncodingUtil;

/**
 * Subscription request toBinance SpotMarketData API IndividualSymbolTickerStreams websocket endpoint<br/>24hr rolling window ticker statistics for a single symbol. These are NOT the statistics of the UTC day, but a 24hr rolling window for the previous 24hrs.<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#individual-symbol-ticker-streams">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = BinanceIndividualSymbolTickerStreamsRequestSerializer.class)
public class BinanceIndividualSymbolTickerStreamsRequest implements WebsocketSubscribeParameters {
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
    return com.scz.jcex.util.EncodingUtil.substituteArguments("${symbol}@arr", "symbol", symbol);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
