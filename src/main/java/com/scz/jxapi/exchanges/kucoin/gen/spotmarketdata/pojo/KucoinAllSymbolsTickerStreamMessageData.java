package com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.serializers.KucoinAllSymbolsTickerStreamMessageDataSerializer;
import com.scz.jxapi.util.EncodingUtil;

import java.math.BigDecimal;

/**
 * 
 */
@JsonSerialize(using = KucoinAllSymbolsTickerStreamMessageDataSerializer.class)
public class KucoinAllSymbolsTickerStreamMessageData {
  private BigDecimal bestAsk;
  private BigDecimal bestAskSize;
  private BigDecimal bestBid;
  private BigDecimal bestBidSize;
  private BigDecimal price;
  private String sequence;
  private BigDecimal size;
  
  /**
   * @return Best ask price
   */
  public BigDecimal getBestAsk(){
    return bestAsk;
  }
  
  /**
   * @param bestAsk Best ask price
   */
  public void setBestAsk(BigDecimal bestAsk) {
    this.bestAsk = bestAsk;
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
  public BigDecimal getBestBid(){
    return bestBid;
  }
  
  /**
   * @param bestBid Best bid price
   */
  public void setBestBid(BigDecimal bestBid) {
    this.bestBid = bestBid;
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
   * @return Last traded price
   */
  public BigDecimal getPrice(){
    return price;
  }
  
  /**
   * @param price Last traded price
   */
  public void setPrice(BigDecimal price) {
    this.price = price;
  }
  
  /**
   * @return Sequence
   */
  public String getSequence(){
    return sequence;
  }
  
  /**
   * @param sequence Sequence
   */
  public void setSequence(String sequence) {
    this.sequence = sequence;
  }
  
  /**
   * @return Last traded size
   */
  public BigDecimal getSize(){
    return size;
  }
  
  /**
   * @param size Last traded size
   */
  public void setSize(BigDecimal size) {
    this.size = size;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
