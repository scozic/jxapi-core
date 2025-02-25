package com.scz.jxapi.exchanges.demo.gen.marketdata.pojo;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.demo.gen.marketdata.serializers.GenericResponseSerializer;
import com.scz.jxapi.util.CompareUtil;
import com.scz.jxapi.util.EncodingUtil;
import com.scz.jxapi.util.Pojo;

/**
 * Response to DemoExchange MarketData API <br>
 * postRestRequestDataTypeInt REST endpoint request<br>
 * A sample REST endpoint using INT response data type
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = GenericResponseSerializer.class)
public class GenericResponse implements Pojo<GenericResponse> {
  
  private static final long serialVersionUID = -8491361540664955098L;
  
  /**
   * @return A new builder to build {@link GenericResponse} objects
   */
  public static Builder builder() {
    return new Builder();
  }
  
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
  public int compareTo(GenericResponse other) {
    if (other == null) {
      return 1;
    }
    int res = 0;
    res = CompareUtil.compare(this.responseCode, other.responseCode);
    return res;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(responseCode);
  }
  
  @Override
  public GenericResponse deepClone() {
    GenericResponse clone = new GenericResponse();
    clone.responseCode = this.responseCode;
    return clone;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  /**
   * Builder for {@link GenericResponse}
   */
  public static class Builder {
    
    private Integer responseCode;
    
    /**
     * Will set the value of <code>responseCode</code> field in the builder
     * @param responseCode Request response code
     * @return Builder instance
     * @see #setResponseCode(Integer)
     */
    public Builder responseCode(Integer responseCode)  {
      this.responseCode = responseCode;
      return this;
    }
    
    /**
     * @return a new instance of GenericResponse using the values set in this builder
     */
    public GenericResponse build() {
      GenericResponse res = new GenericResponse();
      res.responseCode = this.responseCode;
      return res;
    }
  }
}
