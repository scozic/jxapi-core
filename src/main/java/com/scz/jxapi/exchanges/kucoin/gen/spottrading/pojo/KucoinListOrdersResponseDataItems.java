package com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.serializers.KucoinListOrdersResponseDataItemsSerializer;
import com.scz.jxapi.util.EncodingUtil;

import java.math.BigDecimal;

/**
 * Order list
 */
@JsonSerialize(using = KucoinListOrdersResponseDataItemsSerializer.class)
public class KucoinListOrdersResponseDataItems {
  private BigDecimal cancelAfter;
  private Boolean cancelExist;
  private String channel;
  private String clientOid;
  private Long createdAt;
  private BigDecimal dealFunds;
  private BigDecimal dealSize;
  private BigDecimal fee;
  private String feeCurrency;
  private BigDecimal funds;
  private Boolean hidden;
  private Boolean iceberg;
  private String id;
  private Boolean isActive;
  private String opType;
  private Boolean postOnly;
  private BigDecimal price;
  private String remark;
  private String side;
  private BigDecimal size;
  private String stop;
  private BigDecimal stopPrice;
  private Boolean stopTriggered;
  private String stp;
  private String symbol;
  private String tags;
  private String timeInForce;
  private String tradeType;
  private String type;
  private BigDecimal visibleSize;
  
  /**
   * @return Cancel orders time，requires timeInForce to be <strong>GTT</strong>
   */
  public BigDecimal getCancelAfter(){
    return cancelAfter;
  }
  
  /**
   * @param cancelAfter Cancel orders time，requires timeInForce to be <strong>GTT</strong>
   */
  public void setCancelAfter(BigDecimal cancelAfter) {
    this.cancelAfter = cancelAfter;
  }
  
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
   * @return Order source
   */
  public String getChannel(){
    return channel;
  }
  
  /**
   * @param channel Order source
   */
  public void setChannel(String channel) {
    this.channel = channel;
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
   * @return Create time
   */
  public Long getCreatedAt(){
    return createdAt;
  }
  
  /**
   * @param createdAt Create time
   */
  public void setCreatedAt(Long createdAt) {
    this.createdAt = createdAt;
  }
  
  /**
   * @return Executed size of funds
   */
  public BigDecimal getDealFunds(){
    return dealFunds;
  }
  
  /**
   * @param dealFunds Executed size of funds
   */
  public void setDealFunds(BigDecimal dealFunds) {
    this.dealFunds = dealFunds;
  }
  
  /**
   * @return Executed quantity
   */
  public BigDecimal getDealSize(){
    return dealSize;
  }
  
  /**
   * @param dealSize Executed quantity
   */
  public void setDealSize(BigDecimal dealSize) {
    this.dealSize = dealSize;
  }
  
  /**
   * @return Fee
   */
  public BigDecimal getFee(){
    return fee;
  }
  
  /**
   * @param fee Fee
   */
  public void setFee(BigDecimal fee) {
    this.fee = fee;
  }
  
  /**
   * @return Charg fee currency
   */
  public String getFeeCurrency(){
    return feeCurrency;
  }
  
  /**
   * @param feeCurrency Charg fee currency
   */
  public void setFeeCurrency(String feeCurrency) {
    this.feeCurrency = feeCurrency;
  }
  
  /**
   * @return Order funds
   */
  public BigDecimal getFunds(){
    return funds;
  }
  
  /**
   * @param funds Order funds
   */
  public void setFunds(BigDecimal funds) {
    this.funds = funds;
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
   * @return Operation type: DEAL
   */
  public String getOpType(){
    return opType;
  }
  
  /**
   * @param opType Operation type: DEAL
   */
  public void setOpType(String opType) {
    this.opType = opType;
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
   * @return Transaction direction,include buy and sell
   */
  public String getSide(){
    return side;
  }
  
  /**
   * @param side Transaction direction,include buy and sell
   */
  public void setSide(String side) {
    this.side = side;
  }
  
  /**
   * @return Order size
   */
  public BigDecimal getSize(){
    return size;
  }
  
  /**
   * @param size Order size
   */
  public void setSize(BigDecimal size) {
    this.size = size;
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
   * @return symbol
   */
  public String getSymbol(){
    return symbol;
  }
  
  /**
   * @param symbol symbol
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
   * @return Order type
   */
  public String getType(){
    return type;
  }
  
  /**
   * @param type Order type
   */
  public void setType(String type) {
    this.type = type;
  }
  
  /**
   * @return Displayed quantity for iceberg order
   */
  public BigDecimal getVisibleSize(){
    return visibleSize;
  }
  
  /**
   * @param visibleSize Displayed quantity for iceberg order
   */
  public void setVisibleSize(BigDecimal visibleSize) {
    this.visibleSize = visibleSize;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
