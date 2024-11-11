package com.scz.jxapi.exchanges.demo.gen.marketdata.pojo;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.demo.gen.marketdata.serializers.GenericResponseSerializer;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Response to DemoExchange MarketData API <br>
 * postRestRequestDataTypeInt REST endpoint request<br>
 * A sample REST endpoint using INT response data type
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = GenericResponseSerializer.class)
public class GenericResponse {
  private Integer responseCode;
  
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
    GenericResponse o = (GenericResponse) other;
    return Objects.equals(responseCode, o.responseCode);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(responseCode);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
