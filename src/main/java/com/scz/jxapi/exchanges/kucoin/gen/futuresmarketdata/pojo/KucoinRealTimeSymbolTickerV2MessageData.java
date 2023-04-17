package com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.serializers.KucoinRealTimeSymbolTickerV2MessageDataSerializer;
import com.scz.jxapi.util.EncodingUtil;
import java.math.BigDecimal;

/**
 * 
 */
@JsonSerialize(using = KucoinRealTimeSymbolTickerV2MessageDataSerializer.class)
public class KucoinRealTimeSymbolTickerV2MessageData {
  private BigDecimal bestAskPrice;
  private BigDecimal bestAskSize;
  private BigDecimal bestBidPrice;
  private BigDecimal bestBidSize;
  private String symbol;
  
  /**
   * @return Best ask price
   */
  public BigDecimal getBestAskPrice(){
    return bestAskPrice;
  }
  
  /**
   * @param bestAskPrice Best ask price
   */
  public void setBestAskPrice(BigDecimal bestAskPrice) {
    this.bestAskPrice = bestAskPrice;
  }
  
  /**
   * @return Best ask size
   */
  public BigDecimal getBestAskSize(){
    return bestAskSize;
  }
  
  /**
   * @param bestAskSize Best ask size
   */
  public void setBestAskSize(BigDecimal bestAskSize) {
    this.bestAskSize = bestAskSize;
  }
  
  /**
   * @return Best bid price
   */
  public BigDecimal getBestBidPrice(){
    return bestBidPrice;
  }
  
  /**
   * @param bestBidPrice Best bid price
   */
  public void setBestBidPrice(BigDecimal bestBidPrice) {
    this.bestBidPrice = bestBidPrice;
  }
  
  /**
   * @return Best bid size
   */
  public BigDecimal getBestBidSize(){
    return bestBidSize;
  }
  
  /**
   * @param bestBidSize Best bid size
   */
  public void setBestBidSize(BigDecimal bestBidSize) {
    this.bestBidSize = bestBidSize;
  }
  
  /**
   * @return Symbol of the contract
   */
  public String getSymbol(){
    return symbol;
  }
  
  /**
   * @param symbol Symbol of the contract
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
