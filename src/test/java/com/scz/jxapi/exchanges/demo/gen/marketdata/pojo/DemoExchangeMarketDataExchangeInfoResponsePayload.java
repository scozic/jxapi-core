package com.scz.jxapi.exchanges.demo.gen.marketdata.pojo;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.demo.gen.marketdata.serializers.DemoExchangeMarketDataExchangeInfoResponsePayloadSerializer;
import com.scz.jxapi.util.DeepCloneable;
import com.scz.jxapi.util.EncodingUtil;

/**
 * List of market information for each requested symbol
 */
@JsonSerialize(using = DemoExchangeMarketDataExchangeInfoResponsePayloadSerializer.class)
public class DemoExchangeMarketDataExchangeInfoResponsePayload implements DeepCloneable<DemoExchangeMarketDataExchangeInfoResponsePayload> {
  private BigDecimal minOrderSize;
  private BigDecimal orderTickSize;
  private String symbol;
  
  /**
   * @return Minimum order amount
   */
  public BigDecimal getMinOrderSize() {
    return minOrderSize;
  }
  
  /**
   * @param minOrderSize Minimum order amount
   */
  public void setMinOrderSize(BigDecimal minOrderSize) {
    this.minOrderSize = minOrderSize;
  }
  
  /**
   * @return Price precision. Prce of an order should be a multiple of this value
   */
  public BigDecimal getOrderTickSize() {
    return orderTickSize;
  }
  
  /**
   * @param orderTickSize Price precision. Prce of an order should be a multiple of this value
   */
  public void setOrderTickSize(BigDecimal orderTickSize) {
    this.orderTickSize = orderTickSize;
  }
  
  /**
   * @return Market symbol
   */
  public String getSymbol() {
    return symbol;
  }
  
  /**
   * @param symbol Market symbol
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
    DemoExchangeMarketDataExchangeInfoResponsePayload o = (DemoExchangeMarketDataExchangeInfoResponsePayload) other;
    return Objects.equals(minOrderSize, o.minOrderSize)
            && Objects.equals(orderTickSize, o.orderTickSize)
            && Objects.equals(symbol, o.symbol);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(minOrderSize, orderTickSize, symbol);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  @Override
  public DemoExchangeMarketDataExchangeInfoResponsePayload deepClone() {
    DemoExchangeMarketDataExchangeInfoResponsePayload clone = new DemoExchangeMarketDataExchangeInfoResponsePayload();
    clone.symbol = this.symbol;
    clone.minOrderSize = this.minOrderSize;
    clone.orderTickSize = this.orderTickSize;
    return clone;
  }
}
