package com.scz.jxapi.exchanges.demo.gen.marketdata.pojo;

import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.demo.gen.marketdata.serializers.DemoExchangeMarketDataTickersResponseSerializer;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Response to DemoExchange MarketData API <br>
 * tickers REST endpoint request<br>
 * Fetch current tickers for all markets
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = DemoExchangeMarketDataTickersResponseSerializer.class)
public class DemoExchangeMarketDataTickersResponse {
  private Map<String, DemoExchangeMarketDataTickersResponsePayload> payload;
  private Integer responseCode;
  
  /**
   * @return Tickers for each symbol
   */
  public Map<String, DemoExchangeMarketDataTickersResponsePayload> getPayload() {
    return payload;
  }
  
  /**
   * @param payload Tickers for each symbol
   */
  public void setPayload(Map<String, DemoExchangeMarketDataTickersResponsePayload> payload) {
    this.payload = payload;
  }
  
  /**
   * @return Request response code
   */
  public Integer getResponseCode() {
    return responseCode;
  }
  
  /**
   * @param responseCode Request response code
   */
  public void setResponseCode(Integer responseCode) {
    this.responseCode = responseCode;
  }
  
  @Override
  public boolean equals(Object other) {
    if (other == null)
      return false;
    if (!getClass().equals(other.getClass()))
      return false;
    DemoExchangeMarketDataTickersResponse o = (DemoExchangeMarketDataTickersResponse) other;
    return Objects.equals(payload, o.payload)
            && Objects.equals(responseCode, o.responseCode);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(payload, responseCode);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
