package com.scz.jxapi.exchanges.demo.gen.marketdata.pojo;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.demo.gen.marketdata.serializers.DemoExchangeMarketDataExchangeInfoRequestSerializer;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Request for DemoExchange MarketData API exchangeInfo REST endpoint<br>
 * Fetch market information of symbols that can be traded
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = DemoExchangeMarketDataExchangeInfoRequestSerializer.class)
public class DemoExchangeMarketDataExchangeInfoRequest {
  private List<String> symbols;
  
  /**
   * @return The list of symbol to fetch market information for. Leave empty to fetch all markets
   */
  public List<String> getSymbols() {
    return symbols;
  }
  
  /**
   * @param symbols The list of symbol to fetch market information for. Leave empty to fetch all markets
   */
  public void setSymbols(List<String> symbols) {
    this.symbols = symbols;
  }
  
  @Override
  public boolean equals(Object other) {
    if (other == null)
      return false;
    if (!getClass().equals(other.getClass()))
      return false;
    DemoExchangeMarketDataExchangeInfoRequest o = (DemoExchangeMarketDataExchangeInfoRequest) other;
    return Objects.equals(symbols, o.symbols);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(symbols);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
