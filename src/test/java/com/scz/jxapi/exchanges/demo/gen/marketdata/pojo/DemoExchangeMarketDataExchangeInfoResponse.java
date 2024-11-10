package com.scz.jxapi.exchanges.demo.gen.marketdata.pojo;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.demo.gen.marketdata.serializers.DemoExchangeMarketDataExchangeInfoResponseSerializer;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Response to DemoExchange MarketData API <br>
 * exchangeInfo REST endpoint request<br>
 * Fetch market information of symbols that can be traded
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = DemoExchangeMarketDataExchangeInfoResponseSerializer.class)
public class DemoExchangeMarketDataExchangeInfoResponse {
  private List<DemoExchangeMarketDataExchangeInfoResponsePayload> payload;
  private Integer responseCode;
  
  /**
   * @return List of market information for each requested symbol
   */
  public List<DemoExchangeMarketDataExchangeInfoResponsePayload> getPayload() {
    return payload;
  }
  
  /**
   * @param payload List of market information for each requested symbol
   */
  public void setPayload(List<DemoExchangeMarketDataExchangeInfoResponsePayload> payload) {
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
    DemoExchangeMarketDataExchangeInfoResponse o = (DemoExchangeMarketDataExchangeInfoResponse) other;
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
