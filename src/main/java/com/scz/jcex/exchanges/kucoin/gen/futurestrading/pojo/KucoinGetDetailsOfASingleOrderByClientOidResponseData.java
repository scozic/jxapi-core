package com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.serializers.KucoinGetDetailsOfASingleOrderByClientOidResponseDataSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.math.BigDecimal;

/**
 * Response payload
 */
@JsonSerialize(using = KucoinGetDetailsOfASingleOrderByClientOidResponseDataSerializer.class)
public class KucoinGetDetailsOfASingleOrderByClientOidResponseData {
  private Boolean cancelExist;
  private String clientOid;
  private Boolean closeOrder;
  private Long createdAt;
  private Long endAt;
  private BigDecimal filledSize;
  private BigDecimal filledValue;
  private Boolean forceHold;
  private Boolean hidden;
  private Boolean iceberg;
  private String id;
  private Boolean isActive;
  private BigDecimal leverage;
  private Long orderTime;
  private Boolean postOnly;
  private BigDecimal price;
  private Boolean reduceOnly;
  private String remark;
  private String settleCurrency;
  private String side;
  private BigDecimal size;
  private String status;
  private String stop;
  private BigDecimal stopPrice;
  private Boolean stopTriggered;
  private String stp;
  private String symbol;
  private String tags;
  private String timeInForce;
  private String tradeType;
  private String type;
  private Long updatedAt;
  private BigDecimal visibleSize;
  
  /**
   * @return Order cancellation transaction record
   */
  public Boolean isCancelExist(){
    return cancelExist;
  }
  
  /**
   * @param cancelExist Order cancellation transaction record
   */
  public void setCancelExist(Boolean cancelExist) {
    this.cancelExist = cancelExist;
  }
  
  /**
   * @return User-entered order unique mark
   */
  public String getClientOid(){
    return clientOid;
  }
  
  /**
   * @param clientOid User-entered order unique mark
   */
  public void setClientOid(String clientOid) {
    this.clientOid = clientOid;
  }
  
  /**
   * @return A mark to close the position
   */
  public Boolean isCloseOrder(){
    return closeOrder;
  }
  
  /**
   * @param closeOrder A mark to close the position
   */
  public void setCloseOrder(Boolean closeOrder) {
    this.closeOrder = closeOrder;
  }
  
  /**
   * @return Time the order created
   */
  public Long getCreatedAt(){
    return createdAt;
  }
  
  /**
   * @param createdAt Time the order created
   */
  public void setCreatedAt(Long createdAt) {
    this.createdAt = createdAt;
  }
  
  /**
   * @return End time
   */
  public Long getEndAt(){
    return endAt;
  }
  
  /**
   * @param endAt End time
   */
  public void setEndAt(Long endAt) {
    this.endAt = endAt;
  }
  
  /**
   * @return Executed order quantity
   */
  public BigDecimal getFilledSize(){
    return filledSize;
  }
  
  /**
   * @param filledSize Executed order quantity
   */
  public void setFilledSize(BigDecimal filledSize) {
    this.filledSize = filledSize;
  }
  
  /**
   * @return Value of the executed orders
   */
  public BigDecimal getFilledValue(){
    return filledValue;
  }
  
  /**
   * @param filledValue Value of the executed orders
   */
  public void setFilledValue(BigDecimal filledValue) {
    this.filledValue = filledValue;
  }
  
  /**
   * @return A mark to forcely hold the funds for an order
   */
  public Boolean isForceHold(){
    return forceHold;
  }
  
  /**
   * @param forceHold A mark to forcely hold the funds for an order
   */
  public void setForceHold(Boolean forceHold) {
    this.forceHold = forceHold;
  }
  
  /**
   * @return Hidden order
   */
  public Boolean isHidden(){
    return hidden;
  }
  
  /**
   * @param hidden Hidden order
   */
  public void setHidden(Boolean hidden) {
    this.hidden = hidden;
  }
  
  /**
   * @return Iceberg order
   */
  public Boolean isIceberg(){
    return iceberg;
  }
  
  /**
   * @param iceberg Iceberg order
   */
  public void setIceberg(Boolean iceberg) {
    this.iceberg = iceberg;
  }
  
  /**
   * @return Order ID, the ID of an order.
   */
  public String getId(){
    return id;
  }
  
  /**
   * @param id Order ID, the ID of an order.
   */
  public void setId(String id) {
    this.id = id;
  }
  
  /**
   * @return Order status, <strong>true</strong> and <strong>false</strong>. If <strong>true</strong>, the order is active, if <strong>false</strong>, the order is filled or cancelled
   */
  public Boolean isIsActive(){
    return isActive;
  }
  
  /**
   * @param isActive Order status, <strong>true</strong> and <strong>false</strong>. If <strong>true</strong>, the order is active, if <strong>false</strong>, the order is filled or cancelled
   */
  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }
  
  /**
   * @return Leverage of the order
   */
  public BigDecimal getLeverage(){
    return leverage;
  }
  
  /**
   * @param leverage Leverage of the order
   */
  public void setLeverage(BigDecimal leverage) {
    this.leverage = leverage;
  }
  
  /**
   * @return Order create time in nanoseconds
   */
  public Long getOrderTime(){
    return orderTime;
  }
  
  /**
   * @param orderTime Order create time in nanoseconds
   */
  public void setOrderTime(Long orderTime) {
    this.orderTime = orderTime;
  }
  
  /**
   * @return Post only
   */
  public Boolean isPostOnly(){
    return postOnly;
  }
  
  /**
   * @param postOnly Post only
   */
  public void setPostOnly(Boolean postOnly) {
    this.postOnly = postOnly;
  }
  
  /**
   * @return Order price
   */
  public BigDecimal getPrice(){
    return price;
  }
  
  /**
   * @param price Order price
   */
  public void setPrice(BigDecimal price) {
    this.price = price;
  }
  
  /**
   * @return A mark to reduce the position size only
   */
  public Boolean isReduceOnly(){
    return reduceOnly;
  }
  
  /**
   * @param reduceOnly A mark to reduce the position size only
   */
  public void setReduceOnly(Boolean reduceOnly) {
    this.reduceOnly = reduceOnly;
  }
  
  /**
   * @return Remark
   */
  public String getRemark(){
    return remark;
  }
  
  /**
   * @param remark Remark
   */
  public void setRemark(String remark) {
    this.remark = remark;
  }
  
  /**
   * @return Settlement currency
   */
  public String getSettleCurrency(){
    return settleCurrency;
  }
  
  /**
   * @param settleCurrency Settlement currency
   */
  public void setSettleCurrency(String settleCurrency) {
    this.settleCurrency = settleCurrency;
  }
  
  /**
   * @return Transaction side
   */
  public String getSide(){
    return side;
  }
  
  /**
   * @param side Transaction side
   */
  public void setSide(String side) {
    this.side = side;
  }
  
  /**
   * @return Order quantity
   */
  public BigDecimal getSize(){
    return size;
  }
  
  /**
   * @param size Order quantity
   */
  public void setSize(BigDecimal size) {
    this.size = size;
  }
  
  /**
   * @return order status: <strongg>open</strong> or <strong>done</strong>
   */
  public String getStatus(){
    return status;
  }
  
  /**
   * @param status order status: <strongg>open</strong> or <strong>done</strong>
   */
  public void setStatus(String status) {
    this.status = status;
  }
  
  /**
   * @return Stop type, include entry and loss
   */
  public String getStop(){
    return stop;
  }
  
  /**
   * @param stop Stop type, include entry and loss
   */
  public void setStop(String stop) {
    this.stop = stop;
  }
  
  /**
   * @return Stop price
   */
  public BigDecimal getStopPrice(){
    return stopPrice;
  }
  
  /**
   * @param stopPrice Stop price
   */
  public void setStopPrice(BigDecimal stopPrice) {
    this.stopPrice = stopPrice;
  }
  
  /**
   * @return Stop order is triggered or not
   */
  public Boolean isStopTriggered(){
    return stopTriggered;
  }
  
  /**
   * @param stopTriggered Stop order is triggered or not
   */
  public void setStopTriggered(Boolean stopTriggered) {
    this.stopTriggered = stopTriggered;
  }
  
  /**
   * @return Self trade prevention,include <strong>CN</strong>,<strong>CO</strong>,<strong>DC</strong>,<strong>CB</strong>
   */
  public String getStp(){
    return stp;
  }
  
  /**
   * @param stp Self trade prevention,include <strong>CN</strong>,<strong>CO</strong>,<strong>DC</strong>,<strong>CB</strong>
   */
  public void setStp(String stp) {
    this.stp = stp;
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
   * @return Tag order source
   */
  public String getTags(){
    return tags;
  }
  
  /**
   * @param tags Tag order source
   */
  public void setTags(String tags) {
    this.tags = tags;
  }
  
  /**
   * @return Time In Force,include <strong>GTC</strong>,<strong>GTT</strong>,<strong>IOC</strong>,<strong>FOK</strong>
   */
  public String getTimeInForce(){
    return timeInForce;
  }
  
  /**
   * @param timeInForce Time In Force,include <strong>GTC</strong>,<strong>GTT</strong>,<strong>IOC</strong>,<strong>FOK</strong>
   */
  public void setTimeInForce(String timeInForce) {
    this.timeInForce = timeInForce;
  }
  
  /**
   * @return The type of trading
   */
  public String getTradeType(){
    return tradeType;
  }
  
  /**
   * @param tradeType The type of trading
   */
  public void setTradeType(String tradeType) {
    this.tradeType = tradeType;
  }
  
  /**
   * @return Order type, <strong>market</strong> order or <strong>limit</strong> order
   */
  public String getType(){
    return type;
  }
  
  /**
   * @param type Order type, <strong>market</strong> order or <strong>limit</strong> order
   */
  public void setType(String type) {
    this.type = type;
  }
  
  /**
   * @return End time
   */
  public Long getUpdatedAt(){
    return updatedAt;
  }
  
  /**
   * @param updatedAt End time
   */
  public void setUpdatedAt(Long updatedAt) {
    this.updatedAt = updatedAt;
  }
  
  /**
   * @return Visible size of the iceberg order
   */
  public BigDecimal getVisibleSize(){
    return visibleSize;
  }
  
  /**
   * @param visibleSize Visible size of the iceberg order
   */
  public void setVisibleSize(BigDecimal visibleSize) {
    this.visibleSize = visibleSize;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
