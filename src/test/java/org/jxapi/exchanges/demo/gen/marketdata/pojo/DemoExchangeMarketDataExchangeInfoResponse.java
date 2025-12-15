package org.jxapi.exchanges.demo.gen.marketdata.pojo;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.deserializers.DemoExchangeMarketDataExchangeInfoResponseDeserializer;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.serializers.DemoExchangeMarketDataExchangeInfoResponseSerializer;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.DeepCloneable;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

/**
 * Response object for DemoExchange MarketData API exchangeInfo REST endpoint<br>
 * Fetch market information of symbols that can be traded
 */
@Generated("org.jxapi.generator.java.pojo.PojoGenerator")
@JsonSerialize(using = DemoExchangeMarketDataExchangeInfoResponseSerializer.class)
@JsonDeserialize(using = DemoExchangeMarketDataExchangeInfoResponseDeserializer.class)
public class DemoExchangeMarketDataExchangeInfoResponse implements Pojo<DemoExchangeMarketDataExchangeInfoResponse> {
  
  private static final long serialVersionUID = 2532768580321491677L;
  
  /**
   * @return A new builder to build {@link DemoExchangeMarketDataExchangeInfoResponse} objects
   */
  public static Builder builder() {
    return new Builder();
  }
  
  private Integer responseCode;
  private List<DemoExchangeMarketDataExchangeInfoResponsePayload> payload;
  
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
    DemoExchangeMarketDataExchangeInfoResponse o = (DemoExchangeMarketDataExchangeInfoResponse) other;
    return Objects.equals(this.responseCode, o.responseCode)
        && Objects.equals(this.payload, o.payload);
  }
  
  @Override
  public int compareTo(DemoExchangeMarketDataExchangeInfoResponse other) {
    if (other == null) {
      return 1;
    }
    if (this == other) {
      return 0;
    }
    int res = 0;
    res = CompareUtil.compare(this.responseCode, other.responseCode);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compareLists(this.payload, other.payload, CompareUtil::compare);
    return res;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(responseCode, payload);
  }
  
  @Override
  public DemoExchangeMarketDataExchangeInfoResponse deepClone() {
    DemoExchangeMarketDataExchangeInfoResponse clone = new DemoExchangeMarketDataExchangeInfoResponse();
    clone.responseCode = this.responseCode;
    clone.payload = CollectionUtil.deepCloneList(this.payload, DeepCloneable::deepClone);
    return clone;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  /**
   * Builder for {@link DemoExchangeMarketDataExchangeInfoResponse}
   */
  @Generated("org.jxapi.generator.java.JavaTypeGenerator")
  public static class Builder {
    
    private Integer responseCode;
    private List<DemoExchangeMarketDataExchangeInfoResponsePayload> payload;
    
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
     * Will set the value of <code>payload</code> field in the builder
     * @param payload List of market information for each requested symbol
     * @return Builder instance
     * @see #setPayload(List<DemoExchangeMarketDataExchangeInfoResponsePayload>)
     */
    public Builder payload(List<DemoExchangeMarketDataExchangeInfoResponsePayload> payload)  {
      this.payload = payload;
      return this;
    }
    
    
    /**
     * Will add an item to the <code>payload</code> list.
     * @param item Item to add to current <code>payload</code> list
     * @return Builder instance
     * @see DemoExchangeMarketDataExchangeInfoResponse#setPayload(List)
     */
    public Builder addToPayload(DemoExchangeMarketDataExchangeInfoResponsePayload item) {
      if (this.payload == null) {
        this.payload = CollectionUtil.createList();
      }
      this.payload.add(item);
      return this;
    }
    
    /**
     * @return a new instance of DemoExchangeMarketDataExchangeInfoResponse using the values set in this builder
     */
    public DemoExchangeMarketDataExchangeInfoResponse build() {
      DemoExchangeMarketDataExchangeInfoResponse res = new DemoExchangeMarketDataExchangeInfoResponse();
      res.responseCode = this.responseCode;
      res.payload = CollectionUtil.deepCloneList(this.payload, DeepCloneable::deepClone);
      return res;
    }
  }
}
