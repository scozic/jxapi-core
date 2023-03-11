package com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.binance.gen.spotmarketdata.serializers.BinanceExchangeInformationResponseSymbolsFiltersSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.math.BigDecimal;

/**
 * 
 */
@JsonSerialize(using = BinanceExchangeInformationResponseSymbolsFiltersSerializer.class)
public class BinanceExchangeInformationResponseSymbolsFilters {
  private String filterType;
  private BigDecimal maxPrice;
  private BigDecimal minPrice;
  private BigDecimal tickSize;
  
  /**
   * @return Price filter, see <a href="https://binance-docs.github.io/apidocs/spot/en/#filters">API</a> documentation.
   */
  public String getFilterType(){
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
  public BigDecimal getMaxPrice(){
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
  public BigDecimal getMinPrice(){
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
  public BigDecimal getTickSize(){
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
