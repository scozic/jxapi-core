package com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.serializers.KucoinGetRealTimeTickerResponseDataSerializer;
import com.scz.jxapi.util.EncodingUtil;

import java.math.BigDecimal;

/**
 * List of market information for each market symbol
 */
@JsonSerialize(using = KucoinGetRealTimeTickerResponseDataSerializer.class)
public class KucoinGetRealTimeTickerResponseData {
  private BigDecimal bestAskPrice;
  private BigDecimal bestAskSize;
  private BigDecimal bestBidPrice;
  private BigDecimal bestBidSize;
  private BigDecimal price;
  private Long sequence;
  private String side;
  private BigDecimal size;
  private String symbol;
  private String tradeId;
  private Long ts;
  
  /**
   * @return 
   */
  public BigDecimal getBestAskPrice(){
    return bestAskPrice;
  }
  
  /**
   * @param bestAskPrice 
   */
  public void setBestAskPrice(BigDecimal bestAskPrice) {
    this.bestAskPrice = bestAskPrice;
  }
  
  /**
   * @return 
   */
  public BigDecimal getBestAskSize(){
    return bestAskSize;
  }
  
  /**
   * @param bestAskSize 
   */
  public void setBestAskSize(BigDecimal bestAskSize) {
    this.bestAskSize = bestAskSize;
  }
  
  /**
   * @return 
   */
  public BigDecimal getBestBidPrice(){
    return bestBidPrice;
  }
  
  /**
   * @param bestBidPrice 
   */
  public void setBestBidPrice(BigDecimal bestBidPrice) {
    this.bestBidPrice = bestBidPrice;
  }
  
  /**
   * @return 
   */
  public BigDecimal getBestBidSize(){
    return bestBidSize;
  }
  
  /**
   * @param bestBidSize 
   */
  public void setBestBidSize(BigDecimal bestBidSize) {
    this.bestBidSize = bestBidSize;
  }
  
  /**
   * @return 
   */
  public BigDecimal getPrice(){
    return price;
  }
  
  /**
   * @param price 
   */
  public void setPrice(BigDecimal price) {
    this.price = price;
  }
  
  /**
   * @return Sequence number
   */
  public Long getSequence(){
    return sequence;
  }
  
  /**
   * @param sequence Sequence number
   */
  public void setSequence(Long sequence) {
    this.sequence = sequence;
  }
  
  /**
   * @return 
   */
  public String getSide(){
    return side;
  }
  
  /**
   * @param side 
   */
  public void setSide(String side) {
    this.side = side;
  }
  
  /**
   * @return 
   */
  public BigDecimal getSize(){
    return size;
  }
  
  /**
   * @param size 
   */
  public void setSize(BigDecimal size) {
    this.size = size;
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
  
  /**
   * @return 
   */
  public String getTradeId(){
    return tradeId;
  }
  
  /**
   * @param tradeId 
   */
  public void setTradeId(String tradeId) {
    this.tradeId = tradeId;
  }
  
  /**
   * @return Filled time - nanosecond
   */
  public Long getTs(){
    return ts;
  }
  
  /**
   * @param ts Filled time - nanosecond
   */
  public void setTs(Long ts) {
    this.ts = ts;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
