package org.jxapi.exchanges.demo.gen.marketdata.pojo;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.deserializers.GenericResponseDeserializer;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.serializers.GenericResponseSerializer;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

/**
 * Response object for DemoExchange MarketData API postRestRequestDataTypeInt REST endpoint<br>
 * A sample REST endpoint using INT response data type
 */
@Generated("org.jxapi.generator.java.pojo.PojoGenerator")
@JsonSerialize(using = GenericResponseSerializer.class)
@JsonDeserialize(using = GenericResponseDeserializer.class)
public class GenericResponse implements Pojo<GenericResponse> {
  
  private static final long serialVersionUID = 6491747203800805580L;
  
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
    if (other == null) {
      return false;
    }
    if (this == other) {
      return true;
    }
    if (!getClass().equals(other.getClass()))
      return false;
    GenericResponse o = (GenericResponse) other;
    return Objects.equals(this.responseCode, o.responseCode);
  }
  
  @Override
  public int compareTo(GenericResponse other) {
    if (other == null) {
      return 1;
    }
    if (this == other) {
      return 0;
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
  @Generated("org.jxapi.generator.java.JavaTypeGenerator")
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
