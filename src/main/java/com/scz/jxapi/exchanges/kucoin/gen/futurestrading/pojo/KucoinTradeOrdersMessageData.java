package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers.KucoinTradeOrdersMessageDataSerializer;
import com.scz.jxapi.util.EncodingUtil;

import java.math.BigDecimal;

/**
 * 
 */
@JsonSerialize(using = KucoinTradeOrdersMessageDataSerializer.class)
public class KucoinTradeOrdersMessageData {
  private BigDecimal canceledSize;
  private String clientOId;
  private BigDecimal filledSize;
  private String liquidity;
  private BigDecimal matchPrice;
  private BigDecimal matchSize;
  private BigDecimal oldSize;
  private String orderId;
  private Long orderTime;
  private String orderType;
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
   * @return In the update message, the size of order reduced
   */
  public BigDecimal getCanceledSize(){
    return canceledSize;
  }
  
  /**
   * @param canceledSize In the update message, the size of order reduced
   */
  public void setCanceledSize(BigDecimal canceledSize) {
    this.canceledSize = canceledSize;
  }
  
  /**
   * @return Client order ID
   */
  public String getClientOId(){
    return clientOId;
  }
  
  /**
   * @param clientOId Client order ID
   */
  public void setClientOId(String clientOId) {
    this.clientOId = clientOId;
  }
  
  /**
   * @return Filled size
   */
  public BigDecimal getFilledSize(){
    return filledSize;
  }
  
  /**
   * @param filledSize Filled size
   */
  public void setFilledSize(BigDecimal filledSize) {
    this.filledSize = filledSize;
  }
  
  /**
   * @return Trading direction, buy or sell in taker
   */
  public String getLiquidity(){
    return liquidity;
  }
  
  /**
   * @param liquidity Trading direction, buy or sell in taker
   */
  public void setLiquidity(String liquidity) {
    this.liquidity = liquidity;
  }
  
  /**
   * @return Match Price (when the type is <strong>match</strong>
   */
  public BigDecimal getMatchPrice(){
    return matchPrice;
  }
  
  /**
   * @param matchPrice Match Price (when the type is <strong>match</strong>
   */
  public void setMatchPrice(BigDecimal matchPrice) {
    this.matchPrice = matchPrice;
  }
  
  /**
   * @return Match Size (when the type is <strong>match</strong>
   */
  public BigDecimal getMatchSize(){
    return matchSize;
  }
  
  /**
   * @param matchSize Match Size (when the type is <strong>match</strong>
   */
  public void setMatchSize(BigDecimal matchSize) {
    this.matchSize = matchSize;
  }
  
  /**
   * @return Size Before Update (when the type is <strongg>update</strong>)
   */
  public BigDecimal getOldSize(){
    return oldSize;
  }
  
  /**
   * @param oldSize Size Before Update (when the type is <strongg>update</strong>)
   */
  public void setOldSize(BigDecimal oldSize) {
    this.oldSize = oldSize;
  }
  
  /**
   * @return Order ID
   */
  public String getOrderId(){
    return orderId;
  }
  
  /**
   * @param orderId Order ID
   */
  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }
  
  /**
   * @return Order Time
   */
  public Long getOrderTime(){
    return orderTime;
  }
  
  /**
   * @param orderTime Order Time
   */
  public void setOrderTime(Long orderTime) {
    this.orderTime = orderTime;
  }
  
  /**
   * @return Order type, <strong>market</strong> or <strong>limit</strong>
   */
  public String getOrderType(){
    return orderType;
  }
  
  /**
   * @param orderType Order type, <strong>market</strong> or <strong>limit</strong>
   */
  public void setOrderType(String orderType) {
    this.orderType = orderType;
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
   * @return Remaining size for trading
   */
  public BigDecimal getRemainSize(){
    return remainSize;
  }
  
  /**
   * @param remainSize Remaining size for trading
   */
  public void setRemainSize(BigDecimal remainSize) {
    this.remainSize = remainSize;
  }
  
  /**
   * @return Trading direction, <strong>buy</strong> or <strong>sell</strong>
   */
  public String getSide(){
    return side;
  }
  
  /**
   * @param side Trading direction, <strong>buy</strong> or <strong>sell</strong>
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
   * @return Order Status Descriptions<ul><li><strong>match</strong>: when taker order executes with orders in the order book, the taker order status is “match”;</li>  <li><strong>open</strong>: the order is in the order book;</li><li><strong>done</strong>: the order is fully executed successfully;</li></ul> 
   */
  public String getStatus(){
    return status;
  }
  
  /**
   * @param status Order Status Descriptions<ul><li><strong>match</strong>: when taker order executes with orders in the order book, the taker order status is “match”;</li>  <li><strong>open</strong>: the order is in the order book;</li><li><strong>done</strong>: the order is fully executed successfully;</li></ul> 
   */
  public void setStatus(String status) {
    this.status = status;
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
   * @return Trade ID (when the type is <strong>match</strong>
   */
  public String getTradeId(){
    return tradeId;
  }
  
  /**
   * @param tradeId Trade ID (when the type is <strong>match</strong>
   */
  public void setTradeId(String tradeId) {
    this.tradeId = tradeId;
  }
  
  /**
   * @return Timestamp
   */
  public Long getTs(){
    return ts;
  }
  
  /**
   * @param ts Timestamp
   */
  public void setTs(Long ts) {
    this.ts = ts;
  }
  
  /**
   * @return Type Descriptions:<ul><li><strong>open</strong>: when the order enters into the order book;</li><li><strong>match</strong>: when the order has been executed;</li><li><strong>filled</strong>: when the order has been executed and its status was changed into DONE;</li><li><strong>canceled</strong>: when the order has been cancelled and its status was changed into DONE;</li><li><strong>update</strong>: when the order has been updated;</li></ul>
   */
  public String getType(){
    return type;
  }
  
  /**
   * @param type Type Descriptions:<ul><li><strong>open</strong>: when the order enters into the order book;</li><li><strong>match</strong>: when the order has been executed;</li><li><strong>filled</strong>: when the order has been executed and its status was changed into DONE;</li><li><strong>canceled</strong>: when the order has been cancelled and its status was changed into DONE;</li><li><strong>update</strong>: when the order has been updated;</li></ul>
   */
  public void setType(String type) {
    this.type = type;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
