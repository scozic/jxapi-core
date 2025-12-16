package org.jxapi.exchanges.demo.gen.marketdata.pojo;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.deserializers.DemoExchangeMarketDataTickerStreamMessageDeserializer;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.serializers.DemoExchangeMarketDataTickerStreamMessageSerializer;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

/**
 * Message object for DemoExchange MarketData API tickerStream Websocket endpoint<br>
 * Subscribe to ticker stream
 */
@Generated("org.jxapi.generator.java.pojo.PojoGenerator")
@JsonSerialize(using = DemoExchangeMarketDataTickerStreamMessageSerializer.class)
@JsonDeserialize(using = DemoExchangeMarketDataTickerStreamMessageDeserializer.class)
public class DemoExchangeMarketDataTickerStreamMessage implements Pojo<DemoExchangeMarketDataTickerStreamMessage> {
  
  private static final long serialVersionUID = 1440987101262228707L;
  
  /**
   * @return A new builder to build {@link DemoExchangeMarketDataTickerStreamMessage} objects
   */
  public static Builder builder() {
    return new Builder();
  }
  
  private String topic;
  private String symbol;
  private BigDecimal last;
  private BigDecimal high;
  private BigDecimal low;
  private BigDecimal volume;
  private Long time;
  
  /**
   * @return Topic <br>Message field <strong>t</strong>
   */
  public String getTopic() {
    return topic;
  }
  
  /**
   * @param topic Topic <br>Message field <strong>t</strong>
   */
  public void setTopic(String topic) {
    this.topic = topic;
  }
  
  /**
   * @return Symbol name <br>Message field <strong>s</strong>
   */
  public String getSymbol() {
    return symbol;
  }
  
  /**
   * @param symbol Symbol name <br>Message field <strong>s</strong>
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
  
  /**
   * @return Last traded price <br>Message field <strong>p</strong>
   */
  public BigDecimal getLast() {
    return last;
  }
  
  /**
   * @param last Last traded price <br>Message field <strong>p</strong>
   */
  public void setLast(BigDecimal last) {
    this.last = last;
  }
  
  /**
   * @return Last traded price <br>Message field <strong>h</strong>
   */
  public BigDecimal getHigh() {
    return high;
  }
  
  /**
   * @param high Last traded price <br>Message field <strong>h</strong>
   */
  public void setHigh(BigDecimal high) {
    this.high = high;
  }
  
  /**
   * @return Last traded price <br>Message field <strong>l</strong>
   */
  public BigDecimal getLow() {
    return low;
  }
  
  /**
   * @param low Last traded price <br>Message field <strong>l</strong>
   */
  public void setLow(BigDecimal low) {
    this.low = low;
  }
  
  /**
   * @return Total traded amount in base asset during the last 24h from now <br>Message field <strong>v</strong>
   */
  public BigDecimal getVolume() {
    return volume;
  }
  
  /**
   * @param volume Total traded amount in base asset during the last 24h from now <br>Message field <strong>v</strong>
   */
  public void setVolume(BigDecimal volume) {
    this.volume = volume;
  }
  
  /**
   * @return Current time <br>Message field <strong>d</strong>
   */
  public Long getTime() {
    return time;
  }
  
  /**
   * @param time Current time <br>Message field <strong>d</strong>
   */
  public void setTime(Long time) {
    this.time = time;
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
    DemoExchangeMarketDataTickerStreamMessage o = (DemoExchangeMarketDataTickerStreamMessage) other;
    return Objects.equals(this.topic, o.topic)
        && Objects.equals(this.symbol, o.symbol)
        && Objects.equals(this.last, o.last)
        && Objects.equals(this.high, o.high)
        && Objects.equals(this.low, o.low)
        && Objects.equals(this.volume, o.volume)
        && Objects.equals(this.time, o.time);
  }
  
  @Override
  public int compareTo(DemoExchangeMarketDataTickerStreamMessage other) {
    if (other == null) {
      return 1;
    }
    if (this == other) {
      return 0;
    }
    int res = 0;
    res = CompareUtil.compare(this.topic, other.topic);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.symbol, other.symbol);
    if (res != 0) {
      return res;
    }
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
    return res;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(topic, symbol, last, high, low, volume, time);
  }
  
  @Override
  public DemoExchangeMarketDataTickerStreamMessage deepClone() {
    DemoExchangeMarketDataTickerStreamMessage clone = new DemoExchangeMarketDataTickerStreamMessage();
    clone.topic = this.topic;
    clone.symbol = this.symbol;
    clone.last = this.last;
    clone.high = this.high;
    clone.low = this.low;
    clone.volume = this.volume;
    clone.time = this.time;
    return clone;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  /**
   * Builder for {@link DemoExchangeMarketDataTickerStreamMessage}
   */
  @Generated("org.jxapi.generator.java.JavaTypeGenerator")
  public static class Builder {
    
    private String topic;
    private String symbol;
    private BigDecimal last;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal volume;
    private Long time;
    
    /**
     * Will set the value of <code>topic</code> field in the builder
     * @param topic Topic Message field <strong>t</strong>
     * @return Builder instance
     * @see #setTopic(String)
     */
    public Builder topic(String topic)  {
      this.topic = topic;
      return this;
    }
    
    /**
     * Will set the value of <code>symbol</code> field in the builder
     * @param symbol Symbol name Message field <strong>s</strong>
     * @return Builder instance
     * @see #setSymbol(String)
     */
    public Builder symbol(String symbol)  {
      this.symbol = symbol;
      return this;
    }
    
    /**
     * Will set the value of <code>last</code> field in the builder
     * @param last Last traded price Message field <strong>p</strong>
     * @return Builder instance
     * @see #setLast(BigDecimal)
     */
    public Builder last(BigDecimal last)  {
      this.last = last;
      return this;
    }
    
    /**
     * Will set the value of <code>high</code> field in the builder
     * @param high Last traded price Message field <strong>h</strong>
     * @return Builder instance
     * @see #setHigh(BigDecimal)
     */
    public Builder high(BigDecimal high)  {
      this.high = high;
      return this;
    }
    
    /**
     * Will set the value of <code>low</code> field in the builder
     * @param low Last traded price Message field <strong>l</strong>
     * @return Builder instance
     * @see #setLow(BigDecimal)
     */
    public Builder low(BigDecimal low)  {
      this.low = low;
      return this;
    }
    
    /**
     * Will set the value of <code>volume</code> field in the builder
     * @param volume Total traded amount in base asset during the last 24h from now Message field <strong>v</strong>
     * @return Builder instance
     * @see #setVolume(BigDecimal)
     */
    public Builder volume(BigDecimal volume)  {
      this.volume = volume;
      return this;
    }
    
    /**
     * Will set the value of <code>time</code> field in the builder
     * @param time Current time Message field <strong>d</strong>
     * @return Builder instance
     * @see #setTime(Long)
     */
    public Builder time(Long time)  {
      this.time = time;
      return this;
    }
    
    /**
     * @return a new instance of DemoExchangeMarketDataTickerStreamMessage using the values set in this builder
     */
    public DemoExchangeMarketDataTickerStreamMessage build() {
      DemoExchangeMarketDataTickerStreamMessage res = new DemoExchangeMarketDataTickerStreamMessage();
      res.topic = this.topic;
      res.symbol = this.symbol;
      res.last = this.last;
      res.high = this.high;
      res.low = this.low;
      res.volume = this.volume;
      res.time = this.time;
      return res;
    }
  }
}
