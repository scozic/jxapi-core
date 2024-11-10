package com.scz.jxapi.exchanges.demo.gen.marketdata.pojo;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.demo.gen.marketdata.serializers.DemoExchangeMarketDataTickersResponsePayloadSerializer;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Tickers for each symbol
 */
@JsonSerialize(using = DemoExchangeMarketDataTickersResponsePayloadSerializer.class)
public class DemoExchangeMarketDataTickersResponsePayload {
  private BigDecimal high;
  private BigDecimal last;
  private BigDecimal low;
  private Long time;
  private BigDecimal volume;
  
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
  
  @Override
  public boolean equals(Object other) {
    if (other == null)
      return false;
    if (!getClass().equals(other.getClass()))
      return false;
    DemoExchangeMarketDataTickersResponsePayload o = (DemoExchangeMarketDataTickersResponsePayload) other;
    return Objects.equals(high, o.high)
            && Objects.equals(last, o.last)
            && Objects.equals(low, o.low)
            && Objects.equals(time, o.time)
            && Objects.equals(volume, o.volume);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(high, last, low, time, volume);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
