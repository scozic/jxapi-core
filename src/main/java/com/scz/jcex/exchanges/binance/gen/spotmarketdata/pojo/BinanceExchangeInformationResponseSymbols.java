package com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.binance.gen.spotmarketdata.serializers.BinanceExchangeInformationResponseSymbolsSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.util.List;

/**
 * List of market information for each market symbol
 */
@JsonSerialize(using = BinanceExchangeInformationResponseSymbolsSerializer.class)
public class BinanceExchangeInformationResponseSymbols {
  private Boolean allowTrailingStop;
  private List<String> allowedSelfTradePreventionModes;
  private String baseAsset;
  private Boolean cancelReplaceAllowed;
  private String defaultSelfTradePreventionMode;
  private List<BinanceExchangeInformationResponseSymbolsFilters> filters;
  private Boolean icebergAllowed;
  private Boolean isMarginTradingAllowed;
  private Boolean isSpotTradingAllowed;
  private Boolean ocoAllowed;
  private List<String> orderTypes;
  private List<String> permissions;
  private String quoteAsset;
  private Integer quoteAssetPrecision;
  private Boolean quoteOrderQtyMarketAllowed;
  private Integer quotePrecision;
  private String status;
  private String symbol;
  
  /**
   * @return True if trailing stop orders are supported
   */
  public Boolean isAllowTrailingStop(){
    return allowTrailingStop;
  }
  
  /**
   * @param allowTrailingStop True if trailing stop orders are supported
   */
  public void setAllowTrailingStop(Boolean allowTrailingStop) {
    this.allowTrailingStop = allowTrailingStop;
  }
  
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
  public Boolean isCancelReplaceAllowed(){
    return cancelReplaceAllowed;
  }
  
  /**
   * @param cancelReplaceAllowed 
   */
  public void setCancelReplaceAllowed(Boolean cancelReplaceAllowed) {
    this.cancelReplaceAllowed = cancelReplaceAllowed;
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
   * @return True if iceberg orders are allowed
   */
  public Boolean isIcebergAllowed(){
    return icebergAllowed;
  }
  
  /**
   * @param icebergAllowed True if iceberg orders are allowed
   */
  public void setIcebergAllowed(Boolean icebergAllowed) {
    this.icebergAllowed = icebergAllowed;
  }
  
  /**
   * @return 
   */
  public Boolean isIsMarginTradingAllowed(){
    return isMarginTradingAllowed;
  }
  
  /**
   * @param isMarginTradingAllowed 
   */
  public void setIsMarginTradingAllowed(Boolean isMarginTradingAllowed) {
    this.isMarginTradingAllowed = isMarginTradingAllowed;
  }
  
  /**
   * @return 
   */
  public Boolean isIsSpotTradingAllowed(){
    return isSpotTradingAllowed;
  }
  
  /**
   * @param isSpotTradingAllowed 
   */
  public void setIsSpotTradingAllowed(Boolean isSpotTradingAllowed) {
    this.isSpotTradingAllowed = isSpotTradingAllowed;
  }
  
  /**
   * @return True if OCO orders are allowed
   */
  public Boolean isOcoAllowed(){
    return ocoAllowed;
  }
  
  /**
   * @param ocoAllowed True if OCO orders are allowed
   */
  public void setOcoAllowed(Boolean ocoAllowed) {
    this.ocoAllowed = ocoAllowed;
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
  public Integer getQuoteAssetPrecision(){
    return quoteAssetPrecision;
  }
  
  /**
   * @param quoteAssetPrecision Quote asset precision
   */
  public void setQuoteAssetPrecision(Integer quoteAssetPrecision) {
    this.quoteAssetPrecision = quoteAssetPrecision;
  }
  
  /**
   * @return 
   */
  public Boolean isQuoteOrderQtyMarketAllowed(){
    return quoteOrderQtyMarketAllowed;
  }
  
  /**
   * @param quoteOrderQtyMarketAllowed 
   */
  public void setQuoteOrderQtyMarketAllowed(Boolean quoteOrderQtyMarketAllowed) {
    this.quoteOrderQtyMarketAllowed = quoteOrderQtyMarketAllowed;
  }
  
  /**
   * @return Quote asset precision
   */
  public Integer getQuotePrecision(){
    return quotePrecision;
  }
  
  /**
   * @param quotePrecision Quote asset precision
   */
  public void setQuotePrecision(Integer quotePrecision) {
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
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
