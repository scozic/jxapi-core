package com.scz.jcex.binance.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.binance.gen.spotmarketdata.serializers.BinanceExchangeInformationAllResponseSymbolsSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.util.List;

/**
 * List of market information for each market symbol
 */
@JsonSerialize(using = BinanceExchangeInformationAllResponseSymbolsSerializer.class)
public class BinanceExchangeInformationAllResponseSymbols {
  private boolean allowTrailingStop;
  private List<String> allowedSelfTradePreventionModes;
  private String baseAsset;
  private boolean cancelReplaceAllowed;
  private String defaultSelfTradePreventionMode;
  private List<BinanceExchangeInformationAllResponseSymbolsFilters> filters;
  private boolean icebergAllowed;
  private boolean isMarginTradingAllowed;
  private boolean isSpotTradingAllowed;
  private boolean ocoAllowed;
  private List<String> orderTypes;
  private List<String> permissions;
  private String quoteAsset;
  private int quoteAssetPrecision;
  private boolean quoteOrderQtyMarketAllowed;
  private int quotePrecision;
  private String status;
  private String symbol;
  
  /**
   * @return True if trailing stop orders are supported
   */
  public boolean isAllowTrailingStop(){
    return allowTrailingStop;
  }
  
  /**
   * @param allowTrailingStop True if trailing stop orders are supported
   */
  public void setAllowTrailingStop(boolean allowTrailingStop) {
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
  public boolean isCancelReplaceAllowed(){
    return cancelReplaceAllowed;
  }
  
  /**
   * @param cancelReplaceAllowed 
   */
  public void setCancelReplaceAllowed(boolean cancelReplaceAllowed) {
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
  public List<BinanceExchangeInformationAllResponseSymbolsFilters> getFilters(){
    return filters;
  }
  
  /**
   * @param filters 
   */
  public void setFilters(List<BinanceExchangeInformationAllResponseSymbolsFilters> filters) {
    this.filters = filters;
  }
  
  /**
   * @return True if iceberg orders are allowed
   */
  public boolean isIcebergAllowed(){
    return icebergAllowed;
  }
  
  /**
   * @param icebergAllowed True if iceberg orders are allowed
   */
  public void setIcebergAllowed(boolean icebergAllowed) {
    this.icebergAllowed = icebergAllowed;
  }
  
  /**
   * @return 
   */
  public boolean isIsMarginTradingAllowed(){
    return isMarginTradingAllowed;
  }
  
  /**
   * @param isMarginTradingAllowed 
   */
  public void setIsMarginTradingAllowed(boolean isMarginTradingAllowed) {
    this.isMarginTradingAllowed = isMarginTradingAllowed;
  }
  
  /**
   * @return 
   */
  public boolean isIsSpotTradingAllowed(){
    return isSpotTradingAllowed;
  }
  
  /**
   * @param isSpotTradingAllowed 
   */
  public void setIsSpotTradingAllowed(boolean isSpotTradingAllowed) {
    this.isSpotTradingAllowed = isSpotTradingAllowed;
  }
  
  /**
   * @return True if OCO orders are allowed
   */
  public boolean isOcoAllowed(){
    return ocoAllowed;
  }
  
  /**
   * @param ocoAllowed True if OCO orders are allowed
   */
  public void setOcoAllowed(boolean ocoAllowed) {
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
   * @return 
   */
  public boolean isQuoteOrderQtyMarketAllowed(){
    return quoteOrderQtyMarketAllowed;
  }
  
  /**
   * @param quoteOrderQtyMarketAllowed 
   */
  public void setQuoteOrderQtyMarketAllowed(boolean quoteOrderQtyMarketAllowed) {
    this.quoteOrderQtyMarketAllowed = quoteOrderQtyMarketAllowed;
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
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
  
}
