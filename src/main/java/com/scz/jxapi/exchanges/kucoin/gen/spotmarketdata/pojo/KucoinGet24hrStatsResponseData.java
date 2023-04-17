package com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.serializers.KucoinGet24hrStatsResponseDataSerializer;
import com.scz.jxapi.util.EncodingUtil;

import java.math.BigDecimal;

/**
 * List of market information for each market symbol
 */
@JsonSerialize(using = KucoinGet24hrStatsResponseDataSerializer.class)
public class KucoinGet24hrStatsResponseData {
  private BigDecimal averagePrice;
  private BigDecimal buy;
  private BigDecimal changePrice;
  private BigDecimal changeRate;
  private BigDecimal high;
  private BigDecimal last;
  private BigDecimal low;
  private BigDecimal makerCoefficient;
  private BigDecimal makerFeeRate;
  private BigDecimal sell;
  private String symbol;
  private BigDecimal takerCoefficient;
  private BigDecimal takerFeeRate;
  private Long time;
  private BigDecimal vol;
  private BigDecimal volValue;
  
  /**
   * @return Average trading price in the last 24 hours
   */
  public BigDecimal getAveragePrice(){
    return averagePrice;
  }
  
  /**
   * @param averagePrice Average trading price in the last 24 hours
   */
  public void setAveragePrice(BigDecimal averagePrice) {
    this.averagePrice = averagePrice;
  }
  
  /**
   * @return Best bid price
   */
  public BigDecimal getBuy(){
    return buy;
  }
  
  /**
   * @param buy Best bid price
   */
  public void setBuy(BigDecimal buy) {
    this.buy = buy;
  }
  
  /**
   * @return 24h change price
   */
  public BigDecimal getChangePrice(){
    return changePrice;
  }
  
  /**
   * @param changePrice 24h change price
   */
  public void setChangePrice(BigDecimal changePrice) {
    this.changePrice = changePrice;
  }
  
  /**
   * @return 24h change rate
   */
  public BigDecimal getChangeRate(){
    return changeRate;
  }
  
  /**
   * @param changeRate 24h change rate
   */
  public void setChangeRate(BigDecimal changeRate) {
    this.changeRate = changeRate;
  }
  
  /**
   * @return 24h highest price
   */
  public BigDecimal getHigh(){
    return high;
  }
  
  /**
   * @param high 24h highest price
   */
  public void setHigh(BigDecimal high) {
    this.high = high;
  }
  
  /**
   * @return Last price
   */
  public BigDecimal getLast(){
    return last;
  }
  
  /**
   * @param last Last price
   */
  public void setLast(BigDecimal last) {
    this.last = last;
  }
  
  /**
   * @return 24h lowest price
   */
  public BigDecimal getLow(){
    return low;
  }
  
  /**
   * @param low 24h lowest price
   */
  public void setLow(BigDecimal low) {
    this.low = low;
  }
  
  /**
   * @return Maker fee coefficient
   */
  public BigDecimal getMakerCoefficient(){
    return makerCoefficient;
  }
  
  /**
   * @param makerCoefficient Maker fee coefficient
   */
  public void setMakerCoefficient(BigDecimal makerCoefficient) {
    this.makerCoefficient = makerCoefficient;
  }
  
  /**
   * @return Basic Maker Fee
   */
  public BigDecimal getMakerFeeRate(){
    return makerFeeRate;
  }
  
  /**
   * @param makerFeeRate Basic Maker Fee
   */
  public void setMakerFeeRate(BigDecimal makerFeeRate) {
    this.makerFeeRate = makerFeeRate;
  }
  
  /**
   * @return Best ask price
   */
  public BigDecimal getSell(){
    return sell;
  }
  
  /**
   * @param sell Best ask price
   */
  public void setSell(BigDecimal sell) {
    this.sell = sell;
  }
  
  /**
   * @return <a href="https://docs.kucoin.com/#get-symbols-list">symbol</a>
   */
  public String getSymbol(){
    return symbol;
  }
  
  /**
   * @param symbol <a href="https://docs.kucoin.com/#get-symbols-list">symbol</a>
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
  
  /**
   * @return Taker fee coefficient
   */
  public BigDecimal getTakerCoefficient(){
    return takerCoefficient;
  }
  
  /**
   * @param takerCoefficient Taker fee coefficient
   */
  public void setTakerCoefficient(BigDecimal takerCoefficient) {
    this.takerCoefficient = takerCoefficient;
  }
  
  /**
   * @return Basic Taker Fee
   */
  public BigDecimal getTakerFeeRate(){
    return takerFeeRate;
  }
  
  /**
   * @param takerFeeRate Basic Taker Fee
   */
  public void setTakerFeeRate(BigDecimal takerFeeRate) {
    this.takerFeeRate = takerFeeRate;
  }
  
  /**
   * @return Timestamp
   */
  public Long getTime(){
    return time;
  }
  
  /**
   * @param time Timestamp
   */
  public void setTime(Long time) {
    this.time = time;
  }
  
  /**
   * @return 24h volume, executed based on base currency
   */
  public BigDecimal getVol(){
    return vol;
  }
  
  /**
   * @param vol 24h volume, executed based on base currency
   */
  public void setVol(BigDecimal vol) {
    this.vol = vol;
  }
  
  /**
   * @return 24h traded amount in quote currency
   */
  public BigDecimal getVolValue(){
    return volValue;
  }
  
  /**
   * @param volValue 24h traded amount in quote currency
   */
  public void setVolValue(BigDecimal volValue) {
    this.volValue = volValue;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
