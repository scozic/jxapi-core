package org.jxapi.exchanges.demo.gen.marketdata.pojo;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.annotation.processing.Generated;
import org.jxapi.exchanges.demo.gen.marketdata.serializers.DemoExchangeMarketDataExchangeInfoRequestSerializer;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.CompareUtil;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.Pojo;

/**
 * Request for DemoExchange MarketData API exchangeInfo REST endpoint<br>
 * Fetch market information of symbols that can be traded
 */
@Generated("org.jxapi.generator.java.exchange.api.pojo.PojoGenerator")
@JsonSerialize(using = DemoExchangeMarketDataExchangeInfoRequestSerializer.class)
public class DemoExchangeMarketDataExchangeInfoRequest implements Pojo<DemoExchangeMarketDataExchangeInfoRequest> {
  
  private static final long serialVersionUID = 6651377746235151905L;
  
  /**
   * @return A new builder to build {@link DemoExchangeMarketDataExchangeInfoRequest} objects
   */
  public static Builder builder() {
    return new Builder();
  }
  
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
    if (other == null) {
      return false;
    }
    if (this == other) {
      return true;
    }
    if (!getClass().equals(other.getClass()))
      return false;
    DemoExchangeMarketDataExchangeInfoRequest o = (DemoExchangeMarketDataExchangeInfoRequest) other;
    return Objects.equals(this.symbols, o.symbols);
  }
  
  @Override
  public int compareTo(DemoExchangeMarketDataExchangeInfoRequest other) {
    if (other == null) {
      return 1;
    }
    if (this == other) {
      return 0;
    }
    int res = 0;
    res = CompareUtil.compareLists(this.symbols, other.symbols, CompareUtil::compare);
    return res;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(symbols);
  }
  
  @Override
  public DemoExchangeMarketDataExchangeInfoRequest deepClone() {
    DemoExchangeMarketDataExchangeInfoRequest clone = new DemoExchangeMarketDataExchangeInfoRequest();
    clone.symbols = CollectionUtil.cloneList(this.symbols);
    return clone;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
  /**
   * Builder for {@link DemoExchangeMarketDataExchangeInfoRequest}
   */
  @Generated("org.jxapi.generator.java.JavaTypeGenerator")
  public static class Builder {
    
    private List<String> symbols;
    
    /**
     * Will set the value of <code>symbols</code> field in the builder
     * @param symbols The list of symbol to fetch market information for. Leave empty to fetch all markets
     * @return Builder instance
     * @see #setSymbols(List<String>)
     */
    public Builder symbols(List<String> symbols)  {
      this.symbols = symbols;
      return this;
    }
    
    
    /**
     * Will add an item to the <code>symbols</code> list.
     * @param item Item to add to current <code>symbols</code> list
     * @return Builder instance
     * @see DemoExchangeMarketDataExchangeInfoRequest#setSymbols(String)
     */
    public Builder addToSymbols(String item) {
      if (this.symbols == null) {
        this.symbols = CollectionUtil.createList();
      }
      this.symbols.add(item);
      return this;
    }
    
    /**
     * @return a new instance of DemoExchangeMarketDataExchangeInfoRequest using the values set in this builder
     */
    public DemoExchangeMarketDataExchangeInfoRequest build() {
      DemoExchangeMarketDataExchangeInfoRequest res = new DemoExchangeMarketDataExchangeInfoRequest();
      res.symbols = CollectionUtil.cloneList(this.symbols);
      return res;
    }
  }
}
