package org.jxapi.exchanges.demo.gen.marketdata.pojo;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.deserializers.DemoExchangeMarketDataTickerStreamRequestDeserializer;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.serializers.DemoExchangeMarketDataTickerStreamRequestSerializer;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

/**
 * Subscription request object for DemoExchange MarketData API tickerStream Websocket endpoint<br>
 * Subscribe to ticker stream
 */
@Generated("org.jxapi.generator.java.pojo.PojoGenerator")
@JsonSerialize(using = DemoExchangeMarketDataTickerStreamRequestSerializer.class)
@JsonDeserialize(using = DemoExchangeMarketDataTickerStreamRequestDeserializer.class)
public class DemoExchangeMarketDataTickerStreamRequest implements Pojo<DemoExchangeMarketDataTickerStreamRequest> {
  
  private static final long serialVersionUID = -1523648742655057287L;
  
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
    if (other == null) {
      return false;
    }
    if (this == other) {
      return true;
    }
    if (!getClass().equals(other.getClass()))
      return false;
    DemoExchangeMarketDataTickerStreamRequest o = (DemoExchangeMarketDataTickerStreamRequest) other;
    return Objects.equals(this.symbol, o.symbol);
  }
  
  @Override
  public int compareTo(DemoExchangeMarketDataTickerStreamRequest other) {
    if (other == null) {
      return 1;
    }
    if (this == other) {
      return 0;
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
  @Generated("org.jxapi.generator.java.JavaTypeGenerator")
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
