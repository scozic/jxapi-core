package com.scz.jcex.binance.spotmarketdata.pojo;

import com.scz.jcex.util.EncodingUtil;
import java.util.List;

/**
 * List of market information for each market symbol
 */
public class BinanceExchangeInformationResponseSymbols {
  private List<String> allowedSelfTradePreventionModes;
  private String baseAsset;
  private String defaultSelfTradePreventionMode;
  private List<BinanceExchangeInformationResponseSymbolsFilters> filters;
  private List<String> orderTypes;
  private List<String> permissions;
  private String quoteAsset;
  private int quoteAssetPrecision;
  private int quotePrecision;
  private String status;
  private String symbol;
  
  /**
   * @return 
   */
  public List<String> getAllowedSelfTradePreventionModes(){
    return allowedSelfTradePreventionModes;
  }
  
  /**
   * @param allowedSelfTradePreventionModes 
   */
  public void setAllowedSelfTradePreventionModes(List<String> allowedSelfTradePreventionModes) {
    this.allowedSelfTradePreventionModes = allowedSelfTradePreventionModes;
  }
  
  /**
   * @return Market base asset that is bought with buy order
   */
  public String getBaseAsset(){
    return baseAsset;
  }
  
  /**
   * @param baseAsset Market base asset that is bought with buy order
   */
  public void setBaseAsset(String baseAsset) {
    this.baseAsset = baseAsset;
  }
  
  /**
   * @return 
   */
  public String getDefaultSelfTradePreventionMode(){
    return defaultSelfTradePreventionMode;
  }
  
  /**
   * @param defaultSelfTradePreventionMode 
   */
  public void setDefaultSelfTradePreventionMode(String defaultSelfTradePreventionMode) {
    this.defaultSelfTradePreventionMode = defaultSelfTradePreventionMode;
  }
  
  /**
   * @return 
   */
  public List<BinanceExchangeInformationResponseSymbolsFilters> getFilters(){
    return filters;
  }
  
  /**
   * @param filters 
   */
  public void setFilters(List<BinanceExchangeInformationResponseSymbolsFilters> filters) {
    this.filters = filters;
  }
  
  /**
   * @return Quote asset precision
   */
  public List<String> getOrderTypes(){
    return orderTypes;
  }
  
  /**
   * @param orderTypes Quote asset precision
   */
  public void setOrderTypes(List<String> orderTypes) {
    this.orderTypes = orderTypes;
  }
  
  /**
   * @return 
   */
  public List<String> getPermissions(){
    return permissions;
  }
  
  /**
   * @param permissions 
   */
  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }
  
  /**
   * @return Market base asset that is bought with sell order
   */
  public String getQuoteAsset(){
    return quoteAsset;
  }
  
  /**
   * @param quoteAsset Market base asset that is bought with sell order
   */
  public void setQuoteAsset(String quoteAsset) {
    this.quoteAsset = quoteAsset;
  }
  
  /**
   * @return Quote asset precision
   */
  public int getQuoteAssetPrecision(){
    return quoteAssetPrecision;
  }
  
  /**
   * @param quoteAssetPrecision Quote asset precision
   */
  public void setQuoteAssetPrecision(int quoteAssetPrecision) {
    this.quoteAssetPrecision = quoteAssetPrecision;
  }
  
  /**
   * @return Quote asset precision
   */
  public int getQuotePrecision(){
    return quotePrecision;
  }
  
  /**
   * @param quotePrecision Quote asset precision
   */
  public void setQuotePrecision(int quotePrecision) {
    this.quotePrecision = quotePrecision;
  }
  
  /**
   * @return Current trading status
   */
  public String getStatus(){
    return status;
  }
  
  /**
   * @param status Current trading status
   */
  public void setStatus(String status) {
    this.status = status;
  }
  
  /**
   * @return Market symbol
   */
  public String getSymbol(){
    return symbol;
  }
  
  /**
   * @param symbol Market symbol
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
  
  @Override
  public String toString(){
    return EncodingUtil.pojoToString(this);
  }
  
}
