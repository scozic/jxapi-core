package com.scz.jxapi.exchanges.demo.gen.marketdata.pojo;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.demo.gen.marketdata.serializers.DemoExchangeMarketDataTickerStreamRequestSerializer;
import com.scz.jxapi.util.CompareUtil;
import com.scz.jxapi.util.EncodingUtil;
import com.scz.jxapi.util.Pojo;
import javax.annotation.processing.Generated;

/**
 * Subscription request toDemoExchange MarketData API tickerStream websocket endpoint<br>
 * Subscribe to ticker stream
 */
@Generated("com.scz.jxapi.generator.java.exchange.api.pojo.PojoGenerator")
@JsonSerialize(using = DemoExchangeMarketDataTickerStreamRequestSerializer.class)
public class DemoExchangeMarketDataTickerStreamRequest implements Pojo<DemoExchangeMarketDataTickerStreamRequest> {
  
  private static final long serialVersionUID = 2267005182100274315L;
  
  /**
   * @return A new builder to build {@link DemoExchangeMarketDataTickerStreamRequest} objects
   */
  public static Builder builder() {
    return new Builder();
  }
  
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
  public int compareTo(DemoExchangeMarketDataTickerStreamRequest other) {
    if (other == null) {
      return 1;
    }
    int res = 0;
    res = CompareUtil.compare(this.symbol, other.symbol);
    return res;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(symbol);
  }
  
  @Override
  public DemoExchangeMarketDataTickerStreamRequest deepClone() {
    DemoExchangeMarketDataTickerStreamRequest clone = new DemoExchangeMarketDataTickerStreamRequest();
    clone.symbol = this.symbol;
    return clone;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  /**
   * Builder for {@link DemoExchangeMarketDataTickerStreamRequest}
   */
  @Generated("com.scz.jxapi.generator.java.JavaTypeGenerator")
  public static class Builder {
    
    private String symbol;
    
    /**
     * Will set the value of <code>symbol</code> field in the builder
     * @param symbol Symbol to subscribe to ticker stream of
     * @return Builder instance
     * @see #setSymbol(String)
     */
    public Builder symbol(String symbol)  {
      this.symbol = symbol;
      return this;
    }
    
    /**
     * @return a new instance of DemoExchangeMarketDataTickerStreamRequest using the values set in this builder
     */
    public DemoExchangeMarketDataTickerStreamRequest build() {
      DemoExchangeMarketDataTickerStreamRequest res = new DemoExchangeMarketDataTickerStreamRequest();
      res.symbol = this.symbol;
      return res;
    }
  }
}
