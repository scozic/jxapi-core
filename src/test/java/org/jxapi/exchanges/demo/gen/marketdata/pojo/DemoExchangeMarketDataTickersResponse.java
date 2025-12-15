package org.jxapi.exchanges.demo.gen.marketdata.pojo;

import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.deserializers.DemoExchangeMarketDataTickersResponseDeserializer;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.serializers.DemoExchangeMarketDataTickersResponseSerializer;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.DeepCloneable;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

/**
 * Response object for DemoExchange MarketData API tickers REST endpoint<br>
 * Fetch current tickers for all markets
 */
@Generated("org.jxapi.generator.java.pojo.PojoGenerator")
@JsonSerialize(using = DemoExchangeMarketDataTickersResponseSerializer.class)
@JsonDeserialize(using = DemoExchangeMarketDataTickersResponseDeserializer.class)
public class DemoExchangeMarketDataTickersResponse implements Pojo<DemoExchangeMarketDataTickersResponse> {
  
  private static final long serialVersionUID = 927904055578287526L;
  
  /**
   * @return A new builder to build {@link DemoExchangeMarketDataTickersResponse} objects
   */
  public static Builder builder() {
    return new Builder();
  }
  
  private Integer responseCode;
  private Map<String, DemoExchangeMarketDataTickersResponsePayload> payload;
  
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
    DemoExchangeMarketDataTickersResponse o = (DemoExchangeMarketDataTickersResponse) other;
    return Objects.equals(this.responseCode, o.responseCode)
        && Objects.equals(this.payload, o.payload);
  }
  
  @Override
  public int compareTo(DemoExchangeMarketDataTickersResponse other) {
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
    res = CompareUtil.compareMaps(this.payload, other.payload, CompareUtil::compare);
    return res;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(responseCode, payload);
  }
  
  @Override
  public DemoExchangeMarketDataTickersResponse deepClone() {
    DemoExchangeMarketDataTickersResponse clone = new DemoExchangeMarketDataTickersResponse();
    clone.responseCode = this.responseCode;
    clone.payload = CollectionUtil.deepCloneMap(this.payload, DeepCloneable::deepClone);
    return clone;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  /**
   * Builder for {@link DemoExchangeMarketDataTickersResponse}
   */
  @Generated("org.jxapi.generator.java.JavaTypeGenerator")
  public static class Builder {
    
    private Integer responseCode;
    private Map<String, DemoExchangeMarketDataTickersResponsePayload> payload;
    
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
     * @param payload Tickers for each symbol
     * @return Builder instance
     * @see #setPayload(Map<String, DemoExchangeMarketDataTickersResponsePayload>)
     */
    public Builder payload(Map<String, DemoExchangeMarketDataTickersResponsePayload> payload)  {
      this.payload = payload;
      return this;
    }
    
    
    /**
     * Will add or update a key/value pair to the <code>payload</code> map.
     * @param item Item to add to current <code>payload</code> list
     * @return Builder instance
     * @see DemoExchangeMarketDataTickersResponse#setPayload(Map)
     */
    public Builder addToPayload(String key, DemoExchangeMarketDataTickersResponsePayload item) {
      if (this.payload == null) {
        this.payload = CollectionUtil.createMap();
      }
      this.payload.put(key, item);
      return this;
    }
    
    /**
     * @return a new instance of DemoExchangeMarketDataTickersResponse using the values set in this builder
     */
    public DemoExchangeMarketDataTickersResponse build() {
      DemoExchangeMarketDataTickersResponse res = new DemoExchangeMarketDataTickersResponse();
      res.responseCode = this.responseCode;
      res.payload = CollectionUtil.deepCloneMap(this.payload, DeepCloneable::deepClone);
      return res;
    }
  }
}
