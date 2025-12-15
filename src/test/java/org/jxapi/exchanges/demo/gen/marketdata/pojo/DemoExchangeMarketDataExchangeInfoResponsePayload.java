package org.jxapi.exchanges.demo.gen.marketdata.pojo;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.deserializers.DemoExchangeMarketDataExchangeInfoResponsePayloadDeserializer;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.serializers.DemoExchangeMarketDataExchangeInfoResponsePayloadSerializer;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

/**
 * List of market information for each requested symbol
 */
@Generated("org.jxapi.generator.java.pojo.PojoGenerator")
@JsonSerialize(using = DemoExchangeMarketDataExchangeInfoResponsePayloadSerializer.class)
@JsonDeserialize(using = DemoExchangeMarketDataExchangeInfoResponsePayloadDeserializer.class)
public class DemoExchangeMarketDataExchangeInfoResponsePayload implements Pojo<DemoExchangeMarketDataExchangeInfoResponsePayload> {
  
  private static final long serialVersionUID = 8233098697383898261L;
  
  /**
   * @return A new builder to build {@link DemoExchangeMarketDataExchangeInfoResponsePayload} objects
   */
  public static Builder builder() {
    return new Builder();
  }
  
  private String symbol;
  private BigDecimal minOrderSize;
  private BigDecimal orderTickSize;
  private Object blob;
  
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
   * @return Additional metadata for the symbol
   */
  public Object getBlob() {
    return blob;
  }
  
  /**
   * @param blob Additional metadata for the symbol
   */
  public void setBlob(Object blob) {
    this.blob = blob;
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
    DemoExchangeMarketDataExchangeInfoResponsePayload o = (DemoExchangeMarketDataExchangeInfoResponsePayload) other;
    return Objects.equals(this.symbol, o.symbol)
        && Objects.equals(this.minOrderSize, o.minOrderSize)
        && Objects.equals(this.orderTickSize, o.orderTickSize)
        && Objects.equals(this.blob, o.blob);
  }
  
  @Override
  public int compareTo(DemoExchangeMarketDataExchangeInfoResponsePayload other) {
    if (other == null) {
      return 1;
    }
    if (this == other) {
      return 0;
    }
    int res = 0;
    res = CompareUtil.compare(this.symbol, other.symbol);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.minOrderSize, other.minOrderSize);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compare(this.orderTickSize, other.orderTickSize);
    if (res != 0) {
      return res;
    }
    res = CompareUtil.compareObjects(this.blob, other.blob);
    return res;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(symbol, minOrderSize, orderTickSize, blob);
  }
  
  @Override
  public DemoExchangeMarketDataExchangeInfoResponsePayload deepClone() {
    DemoExchangeMarketDataExchangeInfoResponsePayload clone = new DemoExchangeMarketDataExchangeInfoResponsePayload();
    clone.symbol = this.symbol;
    clone.minOrderSize = this.minOrderSize;
    clone.orderTickSize = this.orderTickSize;
    clone.blob = this.blob;
    return clone;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  /**
   * Builder for {@link DemoExchangeMarketDataExchangeInfoResponsePayload}
   */
  @Generated("org.jxapi.generator.java.JavaTypeGenerator")
  public static class Builder {
    
    private String symbol;
    private BigDecimal minOrderSize;
    private BigDecimal orderTickSize;
    private Object blob;
    
    /**
     * Will set the value of <code>symbol</code> field in the builder
     * @param symbol Market symbol
     * @return Builder instance
     * @see #setSymbol(String)
     */
    public Builder symbol(String symbol)  {
      this.symbol = symbol;
      return this;
    }
    
    /**
     * Will set the value of <code>minOrderSize</code> field in the builder
     * @param minOrderSize Minimum order amount
     * @return Builder instance
     * @see #setMinOrderSize(BigDecimal)
     */
    public Builder minOrderSize(BigDecimal minOrderSize)  {
      this.minOrderSize = minOrderSize;
      return this;
    }
    
    /**
     * Will set the value of <code>orderTickSize</code> field in the builder
     * @param orderTickSize Price precision. Prce of an order should be a multiple of this value
     * @return Builder instance
     * @see #setOrderTickSize(BigDecimal)
     */
    public Builder orderTickSize(BigDecimal orderTickSize)  {
      this.orderTickSize = orderTickSize;
      return this;
    }
    
    /**
     * Will set the value of <code>blob</code> field in the builder
     * @param blob Additional metadata for the symbol
     * @return Builder instance
     * @see #setBlob(Object)
     */
    public Builder blob(Object blob)  {
      this.blob = blob;
      return this;
    }
    
    /**
     * @return a new instance of DemoExchangeMarketDataExchangeInfoResponsePayload using the values set in this builder
     */
    public DemoExchangeMarketDataExchangeInfoResponsePayload build() {
      DemoExchangeMarketDataExchangeInfoResponsePayload res = new DemoExchangeMarketDataExchangeInfoResponsePayload();
      res.symbol = this.symbol;
      res.minOrderSize = this.minOrderSize;
      res.orderTickSize = this.orderTickSize;
      res.blob = this.blob;
      return res;
    }
  }
}
