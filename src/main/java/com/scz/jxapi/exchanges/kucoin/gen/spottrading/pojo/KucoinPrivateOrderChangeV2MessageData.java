package com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.serializers.KucoinPrivateOrderChangeV2MessageDataSerializer;
import com.scz.jxapi.util.EncodingUtil;

import java.math.BigDecimal;

/**
 * 
 */
@JsonSerialize(using = KucoinPrivateOrderChangeV2MessageDataSerializer.class)
public class KucoinPrivateOrderChangeV2MessageData {
  private BigDecimal canceledFunds;
  private BigDecimal canceledSize;
  private String clientOid;
  private BigDecimal filledSize;
  private String liquidity;
  private BigDecimal matchPrice;
  private BigDecimal matchSize;
  private String orderId;
  private Long orderTime;
  private String orderType;
  private BigDecimal originFunds;
  private BigDecimal originSize;
  private BigDecimal price;
  private BigDecimal remainSize;
  private String side;
  private BigDecimal size;
  private String status;
  private String symbol;
  private String tradeId;
  private Long ts;
  private String type;
  
  /**
   * @return Market order accumulative cancellation funds. Provided only wheny message type is <i>new</i>, <i>match</i>, <i>filled/i> or <i>canceled</i>
   */
  public BigDecimal getCanceledFunds(){
    return canceledFunds;
  }
  
  /**
   * @param canceledFunds Market order accumulative cancellation funds. Provided only wheny message type is <i>new</i>, <i>match</i>, <i>filled/i> or <i>canceled</i>
   */
  public void setCanceledFunds(BigDecimal canceledFunds) {
    this.canceledFunds = canceledFunds;
  }
  
  /**
   * @return Cumulative number of cancellations. Provided only wheny message type is <i>new</i>, <i>match</i>, <i>filled/i> or <i>canceled</i>
   */
  public BigDecimal getCanceledSize(){
    return canceledSize;
  }
  
  /**
   * @param canceledSize Cumulative number of cancellations. Provided only wheny message type is <i>new</i>, <i>match</i>, <i>filled/i> or <i>canceled</i>
   */
  public void setCanceledSize(BigDecimal canceledSize) {
    this.canceledSize = canceledSize;
  }
  
  /**
   * @return 
   */
  public String getClientOid(){
    return clientOid;
  }
  
  /**
   * @param clientOid 
   */
  public void setClientOid(String clientOid) {
    this.clientOid = clientOid;
  }
  
  /**
   * @return Provided only wheny message type is <i>new</i>, <i>match</i>, <i>filled/i> or <i>canceled</i>
   */
  public BigDecimal getFilledSize(){
    return filledSize;
  }
  
  /**
   * @param filledSize Provided only wheny message type is <i>new</i>, <i>match</i>, <i>filled/i> or <i>canceled</i>
   */
  public void setFilledSize(BigDecimal filledSize) {
    this.filledSize = filledSize;
  }
  
  /**
   * @return Provided only wheny message type is <i>match</i>
   */
  public String getLiquidity(){
    return liquidity;
  }
  
  /**
   * @param liquidity Provided only wheny message type is <i>match</i>
   */
  public void setLiquidity(String liquidity) {
    this.liquidity = liquidity;
  }
  
  /**
   * @return Provided only when message type is <i>match</i>
   */
  public BigDecimal getMatchPrice(){
    return matchPrice;
  }
  
  /**
   * @param matchPrice Provided only when message type is <i>match</i>
   */
  public void setMatchPrice(BigDecimal matchPrice) {
    this.matchPrice = matchPrice;
  }
  
  /**
   * @return Provided only when message type is <i>match</i>
   */
  public BigDecimal getMatchSize(){
    return matchSize;
  }
  
  /**
   * @param matchSize Provided only when message type is <i>match</i>
   */
  public void setMatchSize(BigDecimal matchSize) {
    this.matchSize = matchSize;
  }
  
  /**
   * @return 
   */
  public String getOrderId(){
    return orderId;
  }
  
  /**
   * @param orderId 
   */
  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }
  
  /**
   * @return 
   */
  public Long getOrderTime(){
    return orderTime;
  }
  
  /**
   * @param orderTime 
   */
  public void setOrderTime(Long orderTime) {
    this.orderTime = orderTime;
  }
  
  /**
   * @return 
   */
  public String getOrderType(){
    return orderType;
  }
  
  /**
   * @param orderType 
   */
  public void setOrderType(String orderType) {
    this.orderType = orderType;
  }
  
  /**
   * @return The original funds of the market order. Provided only wheny message type is <i>new</i>, <i>match</i>, <i>filled/i> or <i>canceled</i>
   */
  public BigDecimal getOriginFunds(){
    return originFunds;
  }
  
  /**
   * @param originFunds The original funds of the market order. Provided only wheny message type is <i>new</i>, <i>match</i>, <i>filled/i> or <i>canceled</i>
   */
  public void setOriginFunds(BigDecimal originFunds) {
    this.originFunds = originFunds;
  }
  
  /**
   * @return original quantity. Provided only wheny message type is <i>new</i>, <i>match</i>, <i>filled/i> or <i>canceled</i>
   */
  public BigDecimal getOriginSize(){
    return originSize;
  }
  
  /**
   * @param originSize original quantity. Provided only wheny message type is <i>new</i>, <i>match</i>, <i>filled/i> or <i>canceled</i>
   */
  public void setOriginSize(BigDecimal originSize) {
    this.originSize = originSize;
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
   * @return Provided only wheny message type is <i>new</i>, <i>match</i>, <i>filled/i> or <i>canceled</i>
   */
  public BigDecimal getRemainSize(){
    return remainSize;
  }
  
  /**
   * @param remainSize Provided only wheny message type is <i>new</i>, <i>match</i>, <i>filled/i> or <i>canceled</i>
   */
  public void setRemainSize(BigDecimal remainSize) {
    this.remainSize = remainSize;
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
   * @return Provided only wheny message type is <i>new</i>
   */
  public BigDecimal getSize(){
    return size;
  }
  
  /**
   * @param size Provided only wheny message type is <i>new</i>
   */
  public void setSize(BigDecimal size) {
    this.size = size;
  }
  
  /**
   * @return 
   */
  public String getStatus(){
    return status;
  }
  
  /**
   * @param status 
   */
  public void setStatus(String status) {
    this.status = status;
  }
  
  /**
   * @return 
   */
  public String getSymbol(){
    return symbol;
  }
  
  /**
   * @param symbol 
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
  
  /**
   * @return Provided only wheny message type is <i>match</i>
   */
  public String getTradeId(){
    return tradeId;
  }
  
  /**
   * @param tradeId Provided only wheny message type is <i>match</i>
   */
  public void setTradeId(String tradeId) {
    this.tradeId = tradeId;
  }
  
  /**
   * @return 
   */
  public Long getTs(){
    return ts;
  }
  
  /**
   * @param ts 
   */
  public void setTs(Long ts) {
    this.ts = ts;
  }
  
  /**
   * @return received, open, match, filled or canceled.
   */
  public String getType(){
    return type;
  }
  
  /**
   * @param type received, open, match, filled or canceled.
   */
  public void setType(String type) {
    this.type = type;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
