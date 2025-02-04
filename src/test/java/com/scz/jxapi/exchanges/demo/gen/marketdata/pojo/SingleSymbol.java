package com.scz.jxapi.exchanges.demo.gen.marketdata.pojo;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.demo.gen.marketdata.serializers.SingleSymbolSerializer;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Request for DemoExchange MarketData API postRestRequestDataTypeObjectListMap REST endpoint<br>
 * A sample REST endpoint using OBJECT_LIST_MAP request data type
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = SingleSymbolSerializer.class)
public class SingleSymbol {
  private String symbol;
  
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
  
  @Override
  public boolean equals(Object other) {
    if (other == null)
      return false;
    if (!getClass().equals(other.getClass()))
      return false;
    SingleSymbol o = (SingleSymbol) other;
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
}
