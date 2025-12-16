package org.jxapi.exchanges.demo.gen.marketdata.pojo;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.TickerMeta;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.deserializers.DemoExchangeMarketDataTickersResponsePayloadDeserializer;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.serializers.DemoExchangeMarketDataTickersResponsePayloadSerializer;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

/**
 * Tickers for each symbol
 */
@Generated("org.jxapi.generator.java.pojo.PojoGenerator")
@JsonSerialize(using = DemoExchangeMarketDataTickersResponsePayloadSerializer.class)
@JsonDeserialize(using = DemoExchangeMarketDataTickersResponsePayloadDeserializer.class)
public class DemoExchangeMarketDataTickersResponsePayload implements Pojo<DemoExchangeMarketDataTickersResponsePayload> {
  
  private static final long serialVersionUID = 2227932091088175817L;
  
  /**
   * @return A new builder to build {@link DemoExchangeMarketDataTickersResponsePayload} objects
   */
  public static Builder builder() {
    return new Builder();
  }
  
  private BigDecimal last;
  private BigDecimal high;
  private BigDecimal low;
  private BigDecimal volume;
  private Long time;
  private TickerMeta meta;
  
  /**
   * @return Last traded price
   */
  public BigDecimal getLast() {
    return last;
  }
  
  /**
   * @param last Last traded price
   */
  public void setLast(BigDecimal last) {
    this.last = last;
  }
  
  /**
   * @return Last traded price
   */
  public BigDecimal getHigh() {
    return high;
  }
  
  /**
   * @param high Last traded price
   */
  public void setHigh(BigDecimal high) {
    this.high = high;
  }
  
  /**
   * @return Last traded price
   */
  public BigDecimal getLow() {
    return low;
  }
  
  /**
   * @param low Last traded price
   */
  public void setLow(BigDecimal low) {
    this.low = low;
  }
  
  /**
   * @return Total traded amount in base asset during the last 24h from now
   */
  public BigDecimal getVolume() {
    return volume;
  }
  
  /**
   * @param volume Total traded amount in base asset during the last 24h from now
   */
  public void setVolume(BigDecimal volume) {
    this.volume = volume;
  }
  
  /**
   * @return Current time
   */
  public Long getTime() {
    return time;
  }
  
  /**
   * @param time Current time
   */
  public void setTime(Long time) {
    this.time = time;
  }
  
  /**
   * @return Additional metadata for the ticker
   */
  public TickerMeta getMeta() {
    return meta;
  }
  
  /**
   * @param meta Additional metadata for the ticker
   */
  public void setMeta(TickerMeta meta) {
    this.meta = meta;
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
    DemoExchangeMarketDataTickersResponsePayload o = (DemoExchangeMarketDataTickersResponsePayload) other;
    return Objects.equals(this.last, o.last)
        && Objects.equals(this.high, o.high)
        && Objects.equals(this.low, o.low)
        && Objects.equals(this.volume, o.volume)
        && Objects.equals(this.time, o.time)
        && Objects.equals(this.meta, o.meta);
  }
  
  @Override
  public int compareTo(DemoExchangeMarketDataTickersResponsePayload other) {
    if (other == null) {
      return 1;
    }
    if (this == other) {
      return 0;
    }
    int res = 0;
    res = CompareUtil.compare(this.last, other.last);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.high, other.high);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.low, other.low);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.volume, other.volume);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.time, other.time);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.meta, other.meta);
    return res;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(last, high, low, volume, time, meta);
  }
  
  @Override
  public DemoExchangeMarketDataTickersResponsePayload deepClone() {
    DemoExchangeMarketDataTickersResponsePayload clone = new DemoExchangeMarketDataTickersResponsePayload();
    clone.last = this.last;
    clone.high = this.high;
    clone.low = this.low;
    clone.volume = this.volume;
    clone.time = this.time;
    clone.meta = this.meta != null ? this.meta.deepClone() : null;
    return clone;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  /**
   * Builder for {@link DemoExchangeMarketDataTickersResponsePayload}
   */
  @Generated("org.jxapi.generator.java.JavaTypeGenerator")
  public static class Builder {
    
    private BigDecimal last;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal volume;
    private Long time;
    private TickerMeta meta;
    
    /**
     * Will set the value of <code>last</code> field in the builder
     * @param last Last traded price
     * @return Builder instance
     * @see #setLast(BigDecimal)
     */
    public Builder last(BigDecimal last)  {
      this.last = last;
      return this;
    }
    
    /**
     * Will set the value of <code>high</code> field in the builder
     * @param high Last traded price
     * @return Builder instance
     * @see #setHigh(BigDecimal)
     */
    public Builder high(BigDecimal high)  {
      this.high = high;
      return this;
    }
    
    /**
     * Will set the value of <code>low</code> field in the builder
     * @param low Last traded price
     * @return Builder instance
     * @see #setLow(BigDecimal)
     */
    public Builder low(BigDecimal low)  {
      this.low = low;
      return this;
    }
    
    /**
     * Will set the value of <code>volume</code> field in the builder
     * @param volume Total traded amount in base asset during the last 24h from now
     * @return Builder instance
     * @see #setVolume(BigDecimal)
     */
    public Builder volume(BigDecimal volume)  {
      this.volume = volume;
      return this;
    }
    
    /**
     * Will set the value of <code>time</code> field in the builder
     * @param time Current time
     * @return Builder instance
     * @see #setTime(Long)
     */
    public Builder time(Long time)  {
      this.time = time;
      return this;
    }
    
    /**
     * Will set the value of <code>meta</code> field in the builder
     * @param meta Additional metadata for the ticker
     * @return Builder instance
     * @see #setMeta(TickerMeta)
     */
    public Builder meta(TickerMeta meta)  {
      this.meta = meta;
      return this;
    }
    
    /**
     * @return a new instance of DemoExchangeMarketDataTickersResponsePayload using the values set in this builder
     */
    public DemoExchangeMarketDataTickersResponsePayload build() {
      DemoExchangeMarketDataTickersResponsePayload res = new DemoExchangeMarketDataTickersResponsePayload();
      res.last = this.last;
      res.high = this.high;
      res.low = this.low;
      res.volume = this.volume;
      res.time = this.time;
      res.meta = this.meta != null ? this.meta.deepClone() : null;
      return res;
    }
  }
}
