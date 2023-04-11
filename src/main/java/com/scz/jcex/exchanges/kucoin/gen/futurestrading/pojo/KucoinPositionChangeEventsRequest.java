package com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.serializers.KucoinPositionChangeEventsRequestSerializer;
import com.scz.jcex.netutils.websocket.WebsocketSubscribeParameters;
import com.scz.jcex.util.EncodingUtil;

/**
 * Subscription request toKucoin FuturesTrading API PositionChangeEvents websocket endpoint<br/>Stop Order Lifecycle Event websocket stream.<br/>See <a href="https://docs.kucoin.com/futures/#account-balance-events">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinPositionChangeEventsRequestSerializer.class)
public class KucoinPositionChangeEventsRequest implements WebsocketSubscribeParameters {
  private String symbol;
  
  /**
   * @return Symbol of the contract, ex:'XBTUSDTM'. Empty string to subscribe for all symbols.
   */
  public String getSymbol(){
    return symbol;
  }
  
  /**
   * @param symbol Symbol of the contract, ex:'XBTUSDTM'. Empty string to subscribe for all symbols.
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
  
  
  @Override
  public String getTopic() {
    return EncodingUtil.substituteArguments("/contract/position:${symbol}", "symbol", symbol);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
