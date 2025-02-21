package com.scz.jxapi.exchanges.demo.gen.marketdata.pojo;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.demo.gen.marketdata.serializers.DemoExchangeMarketDataTickerStreamMessageSerializer;
import com.scz.jxapi.util.DeepCloneable;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Message disseminated upon subscription to DemoExchange MarketData API tickerStream websocket endpoint request<br>
 * Subscribe to ticker stream
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = DemoExchangeMarketDataTickerStreamMessageSerializer.class)
public class DemoExchangeMarketDataTickerStreamMessage implements DeepCloneable<DemoExchangeMarketDataTickerStreamMessage> {
  private BigDecimal high;
  private BigDecimal last;
  private BigDecimal low;
  private String symbol;
  private Long time;
  private String topic;
  private BigDecimal volume;
  
  /**
   * @return Last traded price Message field <strong>h</strong>
   */
  public BigDecimal getHigh() {
    return high;
  }
  
  /**
   * @param high Last traded price Message field <strong>h</strong>
   */
  public void setHigh(BigDecimal high) {
    this.high = high;
  }
  
  /**
   * @return Last traded price Message field <strong>p</strong>
   */
  public BigDecimal getLast() {
    return last;
  }
  
  /**
   * @param last Last traded price Message field <strong>p</strong>
   */
  public void setLast(BigDecimal last) {
    this.last = last;
  }
  
  /**
   * @return Last traded price Message field <strong>l</strong>
   */
  public BigDecimal getLow() {
    return low;
  }
  
  /**
   * @param low Last traded price Message field <strong>l</strong>
   */
  public void setLow(BigDecimal low) {
    this.low = low;
  }
  
  /**
   * @return Symbol name Message field <strong>s</strong>
   */
  public String getSymbol() {
    return symbol;
  }
  
  /**
   * @param symbol Symbol name Message field <strong>s</strong>
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
  
  /**
   * @return Current time Message field <strong>d</strong>
   */
  public Long getTime() {
    return time;
  }
  
  /**
   * @param time Current time Message field <strong>d</strong>
   */
  public void setTime(Long time) {
    this.time = time;
  }
  
  /**
   * @return Topic Message field <strong>t</strong>
   */
  public String getTopic() {
    return topic;
  }
  
  /**
   * @param topic Topic Message field <strong>t</strong>
   */
  public void setTopic(String topic) {
    this.topic = topic;
  }
  
  /**
   * @return Total traded amount in base asset during the last 24h from now Message field <strong>v</strong>
   */
  public BigDecimal getVolume() {
    return volume;
  }
  
  /**
   * @param volume Total traded amount in base asset during the last 24h from now Message field <strong>v</strong>
   */
  public void setVolume(BigDecimal volume) {
    this.volume = volume;
  }
  
  @Override
  public boolean equals(Object other) {
    if (other == null)
      return false;
    if (!getClass().equals(other.getClass()))
      return false;
    DemoExchangeMarketDataTickerStreamMessage o = (DemoExchangeMarketDataTickerStreamMessage) other;
    return Objects.equals(high, o.high)
            && Objects.equals(last, o.last)
            && Objects.equals(low, o.low)
            && Objects.equals(symbol, o.symbol)
            && Objects.equals(time, o.time)
            && Objects.equals(topic, o.topic)
            && Objects.equals(volume, o.volume);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(high, last, low, symbol, time, topic, volume);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
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
}
