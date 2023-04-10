package com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.serializers.KucoinStopOrderLifecycleEventMessageDataSerializer;
import com.scz.jcex.util.EncodingUtil;
import java.math.BigDecimal;

/**
 * 
 */
@JsonSerialize(using = KucoinStopOrderLifecycleEventMessageDataSerializer.class)
public class KucoinStopOrderLifecycleEventMessageData {
  private String error;
  private String orderId;
  private BigDecimal orderPrice;
  private String orderType;
  private String side;
  private BigDecimal size;
  private String status;
  private String stop;
  private BigDecimal stopPrice;
  private String stopPriceType;
  private String symbol;
  private Boolean triggerSuccess;
  private Long ts;
  private String type;
  
  /**
   * @return Error code, which is used only when the trigger of the triggered type of orders fails
   */
  public String getError(){
    return error;
  }
  
  /**
   * @param error Error code, which is used only when the trigger of the triggered type of orders fails
   */
  public void setError(String error) {
    this.error = error;
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
   * @return Order price. For market orders, the value is <code>null</code>
   */
  public BigDecimal getOrderPrice(){
    return orderPrice;
  }
  
  /**
   * @param orderPrice Order price. For market orders, the value is <code>null</code>
   */
  public void setOrderPrice(BigDecimal orderPrice) {
    this.orderPrice = orderPrice;
  }
  
  /**
   * @return Order type: stop order
   */
  public String getOrderType(){
    return orderType;
  }
  
  /**
   * @param orderType Order type: stop order
   */
  public void setOrderType(String orderType) {
    this.orderType = orderType;
  }
  
  /**
   * @return <strong>buy</strong> or <strong>sell</strong>
   */
  public String getSide(){
    return side;
  }
  
  /**
   * @param side <strong>buy</strong> or <strong>sell</strong>
   */
  public void setSide(String side) {
    this.side = side;
  }
  
  /**
   * @return Quantity
   */
  public BigDecimal getSize(){
    return size;
  }
  
  /**
   * @param size Quantity
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
   * @return Stop order types
   */
  public String getStop(){
    return stop;
  }
  
  /**
   * @param stop Stop order types
   */
  public void setStop(String stop) {
    this.stop = stop;
  }
  
  /**
   * @return Trigger price of stop orders
   */
  public BigDecimal getStopPrice(){
    return stopPrice;
  }
  
  /**
   * @param stopPrice Trigger price of stop orders
   */
  public void setStopPrice(BigDecimal stopPrice) {
    this.stopPrice = stopPrice;
  }
  
  /**
   * @return Trigger price type of stop orders
   */
  public String getStopPriceType(){
    return stopPriceType;
  }
  
  /**
   * @param stopPriceType Trigger price type of stop orders
   */
  public void setStopPriceType(String stopPriceType) {
    this.stopPriceType = stopPriceType;
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
   * @return Mark to show whether the order is triggered. Only for triggered type of orders
   */
  public Boolean isTriggerSuccess(){
    return triggerSuccess;
  }
  
  /**
   * @param triggerSuccess Mark to show whether the order is triggered. Only for triggered type of orders
   */
  public void setTriggerSuccess(Boolean triggerSuccess) {
    this.triggerSuccess = triggerSuccess;
  }
  
  /**
   * @return Timestamp - nanosecond
   */
  public Long getTs(){
    return ts;
  }
  
  /**
   * @param ts Timestamp - nanosecond
   */
  public void setTs(Long ts) {
    this.ts = ts;
  }
  
  /**
   * @return Message type: <strong>open</strong> (place order), <strong>triggered</strong> (order triggered), <strong>cancel</strong> (cancel order)
   */
  public String getType(){
    return type;
  }
  
  /**
   * @param type Message type: <strong>open</strong> (place order), <strong>triggered</strong> (order triggered), <strong>cancel</strong> (cancel order)
   */
  public void setType(String type) {
    this.type = type;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
