package org.jxapi.exchanges.demo.gen.marketdata.pojo;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.deserializers.SingleSymbolDeserializer;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.serializers.SingleSymbolSerializer;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

/**
 * Request object for DemoExchange MarketData API postRestRequestDataTypeObjectListMap REST endpoint<br>
 * A sample REST endpoint using OBJECT_LIST_MAP request data type
 */
@Generated("org.jxapi.generator.java.pojo.PojoGenerator")
@JsonSerialize(using = SingleSymbolSerializer.class)
@JsonDeserialize(using = SingleSymbolDeserializer.class)
public class SingleSymbol implements Pojo<SingleSymbol> {
  
  private static final long serialVersionUID = 3008479971180440278L;
  
  /**
   * @return A new builder to build {@link SingleSymbol} objects
   */
  public static Builder builder() {
    return new Builder();
  }
  
  private String symbol;
  
  /**
   * @return Symbol name <br>Message field <strong>s</strong>
   */
  public String getSymbol() {
    return symbol;
  }
  
  /**
   * @param symbol Symbol name <br>Message field <strong>s</strong>
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
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
    SingleSymbol o = (SingleSymbol) other;
    return Objects.equals(this.symbol, o.symbol);
  }
  
  @Override
  public int compareTo(SingleSymbol other) {
    if (other == null) {
      return 1;
    }
    if (this == other) {
      return 0;
    }
    int res = 0;
    res = CompareUtil.compare(this.symbol, other.symbol);
    return res;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(symbol);
  }
  
  @Override
  public SingleSymbol deepClone() {
    SingleSymbol clone = new SingleSymbol();
    clone.symbol = this.symbol;
    return clone;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  /**
   * Builder for {@link SingleSymbol}
   */
  @Generated("org.jxapi.generator.java.JavaTypeGenerator")
  public static class Builder {
    
    private String symbol;
    
    /**
     * Will set the value of <code>symbol</code> field in the builder
     * @param symbol Symbol name Message field <strong>s</strong>
     * @return Builder instance
     * @see #setSymbol(String)
     */
    public Builder symbol(String symbol)  {
      this.symbol = symbol;
      return this;
    }
    
    /**
     * @return a new instance of SingleSymbol using the values set in this builder
     */
    public SingleSymbol build() {
      SingleSymbol res = new SingleSymbol();
      res.symbol = this.symbol;
      return res;
    }
  }
}
