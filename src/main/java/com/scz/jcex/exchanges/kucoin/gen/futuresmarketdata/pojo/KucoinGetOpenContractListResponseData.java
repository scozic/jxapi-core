package com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.futuresmarketdata.serializers.KucoinGetOpenContractListResponseDataSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.math.BigDecimal;
import java.util.List;

/**
 * List of market information for each market symbol
 */
@JsonSerialize(using = KucoinGetOpenContractListResponseDataSerializer.class)
public class KucoinGetOpenContractListResponseData {
  private String baseCurrency;
  private Long expireDate;
  private String fairMethod;
  private Long firstOpenDate;
  private String fundingBaseSymbol;
  private String fundingBaseSymbol1M;
  private BigDecimal fundingFeeRate;
  private String fundingQuoteSymbol;
  private String fundingQuoteSymbol1M;
  private String fundingRateSymbol;
  private BigDecimal highPrice;
  private BigDecimal indexPrice;
  private BigDecimal indexPriceTickSize;
  private String indexSymbol;
  private BigDecimal initialMargin;
  private Boolean isDeleverage;
  private Boolean isInverse;
  private Boolean isQuanto;
  private BigDecimal lastTradePrice;
  private Integer lotSize;
  private BigDecimal lowPrice;
  private BigDecimal maintainMargin;
  private BigDecimal makerFeeRate;
  private BigDecimal makerFixFee;
  private String markMethod;
  private BigDecimal markPrice;
  private BigDecimal maxLeverage;
  private Integer maxOrderQty;
  private BigDecimal maxPrice;
  private Integer maxRiskLimit;
  private Integer minRiskLimit;
  private BigDecimal multiplier;
  private Long nextFundingRateTime;
  private String openInterest;
  private BigDecimal predictedFundingFeeRate;
  private String premiumsSymbol1M;
  private String premiumsSymbol8H;
  private BigDecimal priceChg;
  private BigDecimal priceChgPct;
  private String quoteCurrency;
  private Integer riskStep;
  private String rootSymbol;
  private String settleCurrency;
  private Long settleDate;
  private BigDecimal settlementFee;
  private String settlementSymbol;
  private List<String> sourceExchanges;
  private String status;
  private String symbol;
  private BigDecimal takerFeeRate;
  private BigDecimal takerFixFee;
  private BigDecimal tickSize;
  private BigDecimal turnoverOf24h;
  private String type;
  private BigDecimal volumeOf24h;
  
  /**
   * @return Base currency
   */
  public String getBaseCurrency(){
    return baseCurrency;
  }
  
  /**
   * @param baseCurrency Base currency
   */
  public void setBaseCurrency(String baseCurrency) {
    this.baseCurrency = baseCurrency;
  }
  
  /**
   * @return Expiration date. Null means it will never expire
   */
  public Long getExpireDate(){
    return expireDate;
  }
  
  /**
   * @param expireDate Expiration date. Null means it will never expire
   */
  public void setExpireDate(Long expireDate) {
    this.expireDate = expireDate;
  }
  
  /**
   * @return Fair price marking method
   */
  public String getFairMethod(){
    return fairMethod;
  }
  
  /**
   * @param fairMethod Fair price marking method
   */
  public void setFairMethod(String fairMethod) {
    this.fairMethod = fairMethod;
  }
  
  /**
   * @return First Open Date
   */
  public Long getFirstOpenDate(){
    return firstOpenDate;
  }
  
  /**
   * @param firstOpenDate First Open Date
   */
  public void setFirstOpenDate(Long firstOpenDate) {
    this.firstOpenDate = firstOpenDate;
  }
  
  /**
   * @return Ticker symbol of the base currency
   */
  public String getFundingBaseSymbol(){
    return fundingBaseSymbol;
  }
  
  /**
   * @param fundingBaseSymbol Ticker symbol of the base currency
   */
  public void setFundingBaseSymbol(String fundingBaseSymbol) {
    this.fundingBaseSymbol = fundingBaseSymbol;
  }
  
  /**
   * @return Base currency interest rate symbol (1 minute)
   */
  public String getFundingBaseSymbol1M(){
    return fundingBaseSymbol1M;
  }
  
  /**
   * @param fundingBaseSymbol1M Base currency interest rate symbol (1 minute)
   */
  public void setFundingBaseSymbol1M(String fundingBaseSymbol1M) {
    this.fundingBaseSymbol1M = fundingBaseSymbol1M;
  }
  
  /**
   * @return Funding fee rate
   */
  public BigDecimal getFundingFeeRate(){
    return fundingFeeRate;
  }
  
  /**
   * @param fundingFeeRate Funding fee rate
   */
  public void setFundingFeeRate(BigDecimal fundingFeeRate) {
    this.fundingFeeRate = fundingFeeRate;
  }
  
  /**
   * @return Ticker symbol of the quote currency
   */
  public String getFundingQuoteSymbol(){
    return fundingQuoteSymbol;
  }
  
  /**
   * @param fundingQuoteSymbol Ticker symbol of the quote currency
   */
  public void setFundingQuoteSymbol(String fundingQuoteSymbol) {
    this.fundingQuoteSymbol = fundingQuoteSymbol;
  }
  
  /**
   * @return Quote currency interest rate symbol (1 minute)
   */
  public String getFundingQuoteSymbol1M(){
    return fundingQuoteSymbol1M;
  }
  
  /**
   * @param fundingQuoteSymbol1M Quote currency interest rate symbol (1 minute)
   */
  public void setFundingQuoteSymbol1M(String fundingQuoteSymbol1M) {
    this.fundingQuoteSymbol1M = fundingQuoteSymbol1M;
  }
  
  /**
   * @return Funding rate symbol
   */
  public String getFundingRateSymbol(){
    return fundingRateSymbol;
  }
  
  /**
   * @param fundingRateSymbol Funding rate symbol
   */
  public void setFundingRateSymbol(String fundingRateSymbol) {
    this.fundingRateSymbol = fundingRateSymbol;
  }
  
  /**
   * @return 24H High
   */
  public BigDecimal getHighPrice(){
    return highPrice;
  }
  
  /**
   * @param highPrice 24H High
   */
  public void setHighPrice(BigDecimal highPrice) {
    this.highPrice = highPrice;
  }
  
  /**
   * @return Index price
   */
  public BigDecimal getIndexPrice(){
    return indexPrice;
  }
  
  /**
   * @param indexPrice Index price
   */
  public void setIndexPrice(BigDecimal indexPrice) {
    this.indexPrice = indexPrice;
  }
  
  /**
   * @return Index price of tick size
   */
  public BigDecimal getIndexPriceTickSize(){
    return indexPriceTickSize;
  }
  
  /**
   * @param indexPriceTickSize Index price of tick size
   */
  public void setIndexPriceTickSize(BigDecimal indexPriceTickSize) {
    this.indexPriceTickSize = indexPriceTickSize;
  }
  
  /**
   * @return Index symbol
   */
  public String getIndexSymbol(){
    return indexSymbol;
  }
  
  /**
   * @param indexSymbol Index symbol
   */
  public void setIndexSymbol(String indexSymbol) {
    this.indexSymbol = indexSymbol;
  }
  
  /**
   * @return Initial margin requirement
   */
  public BigDecimal getInitialMargin(){
    return initialMargin;
  }
  
  /**
   * @param initialMargin Initial margin requirement
   */
  public void setInitialMargin(BigDecimal initialMargin) {
    this.initialMargin = initialMargin;
  }
  
  /**
   * @return Enabled ADL or not
   */
  public Boolean isIsDeleverage(){
    return isDeleverage;
  }
  
  /**
   * @param isDeleverage Enabled ADL or not
   */
  public void setIsDeleverage(Boolean isDeleverage) {
    this.isDeleverage = isDeleverage;
  }
  
  /**
   * @return Reverse contract or not
   */
  public Boolean isIsInverse(){
    return isInverse;
  }
  
  /**
   * @param isInverse Reverse contract or not
   */
  public void setIsInverse(Boolean isInverse) {
    this.isInverse = isInverse;
  }
  
  /**
   * @return Whether quanto or not(Deprecated field, no actual use of the value field)
   */
  public Boolean isIsQuanto(){
    return isQuanto;
  }
  
  /**
   * @param isQuanto Whether quanto or not(Deprecated field, no actual use of the value field)
   */
  public void setIsQuanto(Boolean isQuanto) {
    this.isQuanto = isQuanto;
  }
  
  /**
   * @return Last trade price
   */
  public BigDecimal getLastTradePrice(){
    return lastTradePrice;
  }
  
  /**
   * @param lastTradePrice Last trade price
   */
  public void setLastTradePrice(BigDecimal lastTradePrice) {
    this.lastTradePrice = lastTradePrice;
  }
  
  /**
   * @return  Minimum lot size
   */
  public Integer getLotSize(){
    return lotSize;
  }
  
  /**
   * @param lotSize  Minimum lot size
   */
  public void setLotSize(Integer lotSize) {
    this.lotSize = lotSize;
  }
  
  /**
   * @return 24H Low
   */
  public BigDecimal getLowPrice(){
    return lowPrice;
  }
  
  /**
   * @param lowPrice 24H Low
   */
  public void setLowPrice(BigDecimal lowPrice) {
    this.lowPrice = lowPrice;
  }
  
  /**
   * @return Maintenance margin requirement
   */
  public BigDecimal getMaintainMargin(){
    return maintainMargin;
  }
  
  /**
   * @param maintainMargin Maintenance margin requirement
   */
  public void setMaintainMargin(BigDecimal maintainMargin) {
    this.maintainMargin = maintainMargin;
  }
  
  /**
   * @return Maker fees
   */
  public BigDecimal getMakerFeeRate(){
    return makerFeeRate;
  }
  
  /**
   * @param makerFeeRate Maker fees
   */
  public void setMakerFeeRate(BigDecimal makerFeeRate) {
    this.makerFeeRate = makerFeeRate;
  }
  
  /**
   * @return Fixed maker fees(Deprecated field, no actual use of the value field)
   */
  public BigDecimal getMakerFixFee(){
    return makerFixFee;
  }
  
  /**
   * @param makerFixFee Fixed maker fees(Deprecated field, no actual use of the value field)
   */
  public void setMakerFixFee(BigDecimal makerFixFee) {
    this.makerFixFee = makerFixFee;
  }
  
  /**
   * @return Marking method
   */
  public String getMarkMethod(){
    return markMethod;
  }
  
  /**
   * @param markMethod Marking method
   */
  public void setMarkMethod(String markMethod) {
    this.markMethod = markMethod;
  }
  
  /**
   * @return Mark price
   */
  public BigDecimal getMarkPrice(){
    return markPrice;
  }
  
  /**
   * @param markPrice Mark price
   */
  public void setMarkPrice(BigDecimal markPrice) {
    this.markPrice = markPrice;
  }
  
  /**
   * @return Maximum leverage
   */
  public BigDecimal getMaxLeverage(){
    return maxLeverage;
  }
  
  /**
   * @param maxLeverage Maximum leverage
   */
  public void setMaxLeverage(BigDecimal maxLeverage) {
    this.maxLeverage = maxLeverage;
  }
  
  /**
   * @return Maximum order quantity
   */
  public Integer getMaxOrderQty(){
    return maxOrderQty;
  }
  
  /**
   * @param maxOrderQty Maximum order quantity
   */
  public void setMaxOrderQty(Integer maxOrderQty) {
    this.maxOrderQty = maxOrderQty;
  }
  
  /**
   * @return Maximum order price
   */
  public BigDecimal getMaxPrice(){
    return maxPrice;
  }
  
  /**
   * @param maxPrice Maximum order price
   */
  public void setMaxPrice(BigDecimal maxPrice) {
    this.maxPrice = maxPrice;
  }
  
  /**
   * @return Maximum risk limit (unit: XBT)
   */
  public Integer getMaxRiskLimit(){
    return maxRiskLimit;
  }
  
  /**
   * @param maxRiskLimit Maximum risk limit (unit: XBT)
   */
  public void setMaxRiskLimit(Integer maxRiskLimit) {
    this.maxRiskLimit = maxRiskLimit;
  }
  
  /**
   * @return Minimum risk limit (unit: XBT)
   */
  public Integer getMinRiskLimit(){
    return minRiskLimit;
  }
  
  /**
   * @param minRiskLimit Minimum risk limit (unit: XBT)
   */
  public void setMinRiskLimit(Integer minRiskLimit) {
    this.minRiskLimit = minRiskLimit;
  }
  
  /**
   * @return Contract multiplier
   */
  public BigDecimal getMultiplier(){
    return multiplier;
  }
  
  /**
   * @param multiplier Contract multiplier
   */
  public void setMultiplier(BigDecimal multiplier) {
    this.multiplier = multiplier;
  }
  
  /**
   * @return Next funding rate time
   */
  public Long getNextFundingRateTime(){
    return nextFundingRateTime;
  }
  
  /**
   * @param nextFundingRateTime Next funding rate time
   */
  public void setNextFundingRateTime(Long nextFundingRateTime) {
    this.nextFundingRateTime = nextFundingRateTime;
  }
  
  /**
   * @return Open interest
   */
  public String getOpenInterest(){
    return openInterest;
  }
  
  /**
   * @param openInterest Open interest
   */
  public void setOpenInterest(String openInterest) {
    this.openInterest = openInterest;
  }
  
  /**
   * @return Predicted funding fee rate
   */
  public BigDecimal getPredictedFundingFeeRate(){
    return predictedFundingFeeRate;
  }
  
  /**
   * @param predictedFundingFeeRate Predicted funding fee rate
   */
  public void setPredictedFundingFeeRate(BigDecimal predictedFundingFeeRate) {
    this.predictedFundingFeeRate = predictedFundingFeeRate;
  }
  
  /**
   * @return Premium index symbol (1 minute)
   */
  public String getPremiumsSymbol1M(){
    return premiumsSymbol1M;
  }
  
  /**
   * @param premiumsSymbol1M Premium index symbol (1 minute)
   */
  public void setPremiumsSymbol1M(String premiumsSymbol1M) {
    this.premiumsSymbol1M = premiumsSymbol1M;
  }
  
  /**
   * @return Premium index symbol (8 hours)
   */
  public String getPremiumsSymbol8H(){
    return premiumsSymbol8H;
  }
  
  /**
   * @param premiumsSymbol8H Premium index symbol (8 hours)
   */
  public void setPremiumsSymbol8H(String premiumsSymbol8H) {
    this.premiumsSymbol8H = premiumsSymbol8H;
  }
  
  /**
   * @return 24H Change
   */
  public BigDecimal getPriceChg(){
    return priceChg;
  }
  
  /**
   * @param priceChg 24H Change
   */
  public void setPriceChg(BigDecimal priceChg) {
    this.priceChg = priceChg;
  }
  
  /**
   * @return 24H Change%
   */
  public BigDecimal getPriceChgPct(){
    return priceChgPct;
  }
  
  /**
   * @param priceChgPct 24H Change%
   */
  public void setPriceChgPct(BigDecimal priceChgPct) {
    this.priceChgPct = priceChgPct;
  }
  
  /**
   * @return Quote currency
   */
  public String getQuoteCurrency(){
    return quoteCurrency;
  }
  
  /**
   * @param quoteCurrency Quote currency
   */
  public void setQuoteCurrency(String quoteCurrency) {
    this.quoteCurrency = quoteCurrency;
  }
  
  /**
   * @return Risk limit increment value (unit: XBT)
   */
  public Integer getRiskStep(){
    return riskStep;
  }
  
  /**
   * @param riskStep Risk limit increment value (unit: XBT)
   */
  public void setRiskStep(Integer riskStep) {
    this.riskStep = riskStep;
  }
  
  /**
   * @return Contract group
   */
  public String getRootSymbol(){
    return rootSymbol;
  }
  
  /**
   * @param rootSymbol Contract group
   */
  public void setRootSymbol(String rootSymbol) {
    this.rootSymbol = rootSymbol;
  }
  
  /**
   * @return Currency used to clear and settle the trades
   */
  public String getSettleCurrency(){
    return settleCurrency;
  }
  
  /**
   * @param settleCurrency Currency used to clear and settle the trades
   */
  public void setSettleCurrency(String settleCurrency) {
    this.settleCurrency = settleCurrency;
  }
  
  /**
   * @return Settlement date. Null indicates that automatic settlement is not supported
   */
  public Long getSettleDate(){
    return settleDate;
  }
  
  /**
   * @param settleDate Settlement date. Null indicates that automatic settlement is not supported
   */
  public void setSettleDate(Long settleDate) {
    this.settleDate = settleDate;
  }
  
  /**
   * @return Settlement fee
   */
  public BigDecimal getSettlementFee(){
    return settlementFee;
  }
  
  /**
   * @param settlementFee Settlement fee
   */
  public void setSettlementFee(BigDecimal settlementFee) {
    this.settlementFee = settlementFee;
  }
  
  /**
   * @return Settlement Symbol
   */
  public String getSettlementSymbol(){
    return settlementSymbol;
  }
  
  /**
   * @param settlementSymbol Settlement Symbol
   */
  public void setSettlementSymbol(String settlementSymbol) {
    this.settlementSymbol = settlementSymbol;
  }
  
  /**
   * @return The contract index source exchange
   */
  public List<String> getSourceExchanges(){
    return sourceExchanges;
  }
  
  /**
   * @param sourceExchanges The contract index source exchange
   */
  public void setSourceExchanges(List<String> sourceExchanges) {
    this.sourceExchanges = sourceExchanges;
  }
  
  /**
   * @return Contract status
   */
  public String getStatus(){
    return status;
  }
  
  /**
   * @param status Contract status
   */
  public void setStatus(String status) {
    this.status = status;
  }
  
  /**
   * @return Contract status
   */
  public String getSymbol(){
    return symbol;
  }
  
  /**
   * @param symbol Contract status
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
  
  /**
   * @return Taker fees
   */
  public BigDecimal getTakerFeeRate(){
    return takerFeeRate;
  }
  
  /**
   * @param takerFeeRate Taker fees
   */
  public void setTakerFeeRate(BigDecimal takerFeeRate) {
    this.takerFeeRate = takerFeeRate;
  }
  
  /**
   * @return Fixed taker fees(Deprecated field, no actual use of the value field)
   */
  public BigDecimal getTakerFixFee(){
    return takerFixFee;
  }
  
  /**
   * @param takerFixFee Fixed taker fees(Deprecated field, no actual use of the value field)
   */
  public void setTakerFixFee(BigDecimal takerFixFee) {
    this.takerFixFee = takerFixFee;
  }
  
  /**
   * @return  Minimum price changes
   */
  public BigDecimal getTickSize(){
    return tickSize;
  }
  
  /**
   * @param tickSize  Minimum price changes
   */
  public void setTickSize(BigDecimal tickSize) {
    this.tickSize = tickSize;
  }
  
  /**
   * @return Turnover of 24 hours
   */
  public BigDecimal getTurnoverOf24h(){
    return turnoverOf24h;
  }
  
  /**
   * @param turnoverOf24h Turnover of 24 hours
   */
  public void setTurnoverOf24h(BigDecimal turnoverOf24h) {
    this.turnoverOf24h = turnoverOf24h;
  }
  
  /**
   * @return Type of the contract
   */
  public String getType(){
    return type;
  }
  
  /**
   * @param type Type of the contract
   */
  public void setType(String type) {
    this.type = type;
  }
  
  /**
   * @return Volume of 24 hours
   */
  public BigDecimal getVolumeOf24h(){
    return volumeOf24h;
  }
  
  /**
   * @param volumeOf24h Volume of 24 hours
   */
  public void setVolumeOf24h(BigDecimal volumeOf24h) {
    this.volumeOf24h = volumeOf24h;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
