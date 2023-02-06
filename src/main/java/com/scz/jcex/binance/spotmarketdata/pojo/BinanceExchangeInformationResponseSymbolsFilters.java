package com.scz.jcex.binance.spotmarketdata.pojo;

import com.scz.jcex.util.EncodingUtil;
import java.math.BigDecimal;

/**
 * 
 */
public class BinanceExchangeInformationResponseSymbolsFilters {
  private String filterType;
  private BigDecimal maxPrice;
  private BigDecimal minPrice;
  private BigDecimal tickSize;
  
  /**
   * @return Price filter, see <a href="https://binance-docs.github.io/apidocs/spot/en/#filters">API</a> documentation.
   */
  public String setFilterType(){
    return filterType;
  }
  
  /**
   * @param filterType Price filter, see <a href="https://binance-docs.github.io/apidocs/spot/en/#filters">API</a> documentation.
   */
  public void setFilterType(String filterType) {
    this.filterType = filterType;
  }
  
  /**
   * @return Provided with PRICE_FILTER filter type
   */
  public BigDecimal setMaxPrice(){
    return maxPrice;
  }
  
  /**
   * @param maxPrice Provided with PRICE_FILTER filter type
   */
  public void setMaxPrice(BigDecimal maxPrice) {
    this.maxPrice = maxPrice;
  }
  
  /**
   * @return Provided with PRICE_FILTER filter type
   */
  public BigDecimal setMinPrice(){
    return minPrice;
  }
  
  /**
   * @param minPrice Provided with PRICE_FILTER filter type
   */
  public void setMinPrice(BigDecimal minPrice) {
    this.minPrice = minPrice;
  }
  
  /**
   * @return Provided with PRICE_FILTER filter type
   */
  public BigDecimal setTickSize(){
    return tickSize;
  }
  
  /**
   * @param tickSize Provided with PRICE_FILTER filter type
   */
  public void setTickSize(BigDecimal tickSize) {
    this.tickSize = tickSize;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
}
