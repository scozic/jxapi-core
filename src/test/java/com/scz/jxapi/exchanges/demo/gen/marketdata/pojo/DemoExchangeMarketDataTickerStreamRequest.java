package com.scz.jxapi.exchanges.demo.gen.marketdata.pojo;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.demo.gen.marketdata.serializers.DemoExchangeMarketDataTickerStreamRequestSerializer;
import com.scz.jxapi.util.DeepCloneable;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Subscription request toDemoExchange MarketData API tickerStream websocket endpoint<br>
 * Subscribe to ticker stream
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = DemoExchangeMarketDataTickerStreamRequestSerializer.class)
public class DemoExchangeMarketDataTickerStreamRequest implements DeepCloneable<DemoExchangeMarketDataTickerStreamRequest> {
  private String symbol;
  
  /**
   * @return Symbol to subscribe to ticker stream of
   */
  public String getSymbol() {
    return symbol;
  }
  
  /**
   * @param symbol Symbol to subscribe to ticker stream of
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
  
  @Override
  public boolean equals(Object other) {
    if (other == null)
      return false;
    if (!getClass().equals(other.getClass()))
      return false;
    DemoExchangeMarketDataTickerStreamRequest o = (DemoExchangeMarketDataTickerStreamRequest) other;
    return Objects.equals(symbol, o.symbol);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(symbol);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  @Override
  public DemoExchangeMarketDataTickerStreamRequest deepClone() {
    DemoExchangeMarketDataTickerStreamRequest clone = new DemoExchangeMarketDataTickerStreamRequest();
    clone.symbol = this.symbol;
    return clone;
  }
}
