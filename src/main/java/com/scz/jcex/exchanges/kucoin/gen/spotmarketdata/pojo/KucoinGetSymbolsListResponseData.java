package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.serializers.KucoinGetSymbolsListResponseDataSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.math.BigDecimal;

/**
 * List of market information for each market symbol
 */
@JsonSerialize(using = KucoinGetSymbolsListResponseDataSerializer.class)
public class KucoinGetSymbolsListResponseData {
  private String baseCurrency;
  private BigDecimal baseIncrement;
  private BigDecimal baseMaxSize;
  private BigDecimal baseMinSize;
  private boolean enableTrading;
  private String feeCurrency;
  private boolean isMarginEnabled;
  private String market;
  private BigDecimal minFunds;
  private String name;
  private BigDecimal priceIncrement;
  private BigDecimal priceLLimitRate;
  private String quoteCurrency;
  private BigDecimal quoteIncrement;
  private BigDecimal quoteMaxSize;
  private BigDecimal quoteMinSize;
  private String symbol;
  
  /**
   * @return Base currency,e.g. BTC.
   */
  public String getBaseCurrency(){
    return baseCurrency;
  }
  
  /**
   * @param baseCurrency Base currency,e.g. BTC.
   */
  public void setBaseCurrency(String baseCurrency) {
    this.baseCurrency = baseCurrency;
  }
  
  /**
   * @return The increment of the order size. The value shall be a positive multiple of the baseIncrement.
   */
  public BigDecimal getBaseIncrement(){
    return baseIncrement;
  }
  
  /**
   * @param baseIncrement The increment of the order size. The value shall be a positive multiple of the baseIncrement.
   */
  public void setBaseIncrement(BigDecimal baseIncrement) {
    this.baseIncrement = baseIncrement;
  }
  
  /**
   * @return The maximum order size required to place an order.
   */
  public BigDecimal getBaseMaxSize(){
    return baseMaxSize;
  }
  
  /**
   * @param baseMaxSize The maximum order size required to place an order.
   */
  public void setBaseMaxSize(BigDecimal baseMaxSize) {
    this.baseMaxSize = baseMaxSize;
  }
  
  /**
   * @return The minimum order quantity requried to place an order.
   */
  public BigDecimal getBaseMinSize(){
    return baseMinSize;
  }
  
  /**
   * @param baseMinSize The minimum order quantity requried to place an order.
   */
  public void setBaseMinSize(BigDecimal baseMinSize) {
    this.baseMinSize = baseMinSize;
  }
  
  /**
   * @return Available for transaction or not.
   */
  public boolean isEnableTrading(){
    return enableTrading;
  }
  
  /**
   * @param enableTrading Available for transaction or not.
   */
  public void setEnableTrading(boolean enableTrading) {
    this.enableTrading = enableTrading;
  }
  
  /**
   * @return The currency of charged fees.
   */
  public String getFeeCurrency(){
    return feeCurrency;
  }
  
  /**
   * @param feeCurrency The currency of charged fees.
   */
  public void setFeeCurrency(String feeCurrency) {
    this.feeCurrency = feeCurrency;
  }
  
  /**
   * @return Available for margin or not.
   */
  public boolean isIsMarginEnabled(){
    return isMarginEnabled;
  }
  
  /**
   * @param isMarginEnabled Available for margin or not.
   */
  public void setIsMarginEnabled(boolean isMarginEnabled) {
    this.isMarginEnabled = isMarginEnabled;
  }
  
  /**
   * @return The trading  <a href="https://docs.kucoin.com/#get-market-list">market</a>.
   */
  public String getMarket(){
    return market;
  }
  
  /**
   * @param market The trading  <a href="https://docs.kucoin.com/#get-market-list">market</a>.
   */
  public void setMarket(String market) {
    this.market = market;
  }
  
  /**
   * @return The minimum spot and margin trading amounts
   */
  public BigDecimal getMinFunds(){
    return minFunds;
  }
  
  /**
   * @param minFunds The minimum spot and margin trading amounts
   */
  public void setMinFunds(BigDecimal minFunds) {
    this.minFunds = minFunds;
  }
  
  /**
   * @return Name of trading pairs, it would change after renaming
   */
  public String getName(){
    return name;
  }
  
  /**
   * @param name Name of trading pairs, it would change after renaming
   */
  public void setName(String name) {
    this.name = name;
  }
  
  /**
   * @return The increment of the price required to place a limit order. The value shall be a positive multiple of the priceIncrement.
   */
  public BigDecimal getPriceIncrement(){
    return priceIncrement;
  }
  
  /**
   * @param priceIncrement The increment of the price required to place a limit order. The value shall be a positive multiple of the priceIncrement.
   */
  public void setPriceIncrement(BigDecimal priceIncrement) {
    this.priceIncrement = priceIncrement;
  }
  
  /**
   * @return Threshold for price portection
   */
  public BigDecimal getPriceLLimitRate(){
    return priceLLimitRate;
  }
  
  /**
   * @param priceLLimitRate Threshold for price portection
   */
  public void setPriceLLimitRate(BigDecimal priceLLimitRate) {
    this.priceLLimitRate = priceLLimitRate;
  }
  
  /**
   * @return Quote currency,e.g. USDT.
   */
  public String getQuoteCurrency(){
    return quoteCurrency;
  }
  
  /**
   * @param quoteCurrency Quote currency,e.g. USDT.
   */
  public void setQuoteCurrency(String quoteCurrency) {
    this.quoteCurrency = quoteCurrency;
  }
  
  /**
   * @return The increment of the funds required to place a market order. The value shall be a positive multiple of the quoteIncrement.
   */
  public BigDecimal getQuoteIncrement(){
    return quoteIncrement;
  }
  
  /**
   * @param quoteIncrement The increment of the funds required to place a market order. The value shall be a positive multiple of the quoteIncrement.
   */
  public void setQuoteIncrement(BigDecimal quoteIncrement) {
    this.quoteIncrement = quoteIncrement;
  }
  
  /**
   * @return The maximum order funds required to place a market order.
   */
  public BigDecimal getQuoteMaxSize(){
    return quoteMaxSize;
  }
  
  /**
   * @param quoteMaxSize The maximum order funds required to place a market order.
   */
  public void setQuoteMaxSize(BigDecimal quoteMaxSize) {
    this.quoteMaxSize = quoteMaxSize;
  }
  
  /**
   * @return The minimum order funds required to place a market order.
   */
  public BigDecimal getQuoteMinSize(){
    return quoteMinSize;
  }
  
  /**
   * @param quoteMinSize The minimum order funds required to place a market order.
   */
  public void setQuoteMinSize(BigDecimal quoteMinSize) {
    this.quoteMinSize = quoteMinSize;
  }
  
  /**
   * @return unique code of a symbol, it would not change after renaming
   */
  public String getSymbol(){
    return symbol;
  }
  
  /**
   * @param symbol unique code of a symbol, it would not change after renaming
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
