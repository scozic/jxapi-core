package com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.serializers.KucoinPlaceAnOrderRequestSerializer;
import com.scz.jcex.netutils.rest.RestEndpointUrlParameters;
import com.scz.jcex.util.EncodingUtil;
import java.math.BigDecimal;

/**
 * Request for Kucoin FuturesTrading API PlaceAnOrder REST endpointYou can place two types of orders: limit and market. Orders can only be placed if your account has sufficient funds. Once an order is placed, your funds will be put on hold for the duration of the order. The amount of funds on hold depends on the order type and parameters specified. <br/>Please be noted that the system would hold the fees from the orders entered the orderbook in advance. Read Get Fills to learn more.<br/>See <a href="https://docs.kucoin.com/futures/#place-an-order">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinPlaceAnOrderRequestSerializer.class)
public class KucoinPlaceAnOrderRequest implements RestEndpointUrlParameters {
  private String clientOid;
  private Boolean closeOrder;
  private Boolean forceHold;
  private Boolean hidden;
  private Boolean iceberg;
  private BigDecimal leverage;
  private Boolean postOnly;
  private BigDecimal price;
  private Boolean reduceOnly;
  private String remark;
  private String side;
  private Integer size;
  private String stop;
  private BigDecimal stopPrice;
  private String stopPriceType;
  private String symbol;
  private String timeInForce;
  private String type;
  private BigDecimal visibleSize;
  
  /**
   * @return Unique order id created by users to identify their orders, the maximum length cannot exceed 40, e.g. UUID, Only allows numbers, characters, underline(_), and separator(-)
   */
  public String getClientOid(){
    return clientOid;
  }
  
  /**
   * @param clientOid Unique order id created by users to identify their orders, the maximum length cannot exceed 40, e.g. UUID, Only allows numbers, characters, underline(_), and separator(-)
   */
  public void setClientOid(String clientOid) {
    this.clientOid = clientOid;
  }
  
  /**
   * @return [optional] A mark to close the position. Set to false by default. It will close all the positions when closeOrder is true.
   */
  public Boolean isCloseOrder(){
    return closeOrder;
  }
  
  /**
   * @param closeOrder [optional] A mark to close the position. Set to false by default. It will close all the positions when closeOrder is true.
   */
  public void setCloseOrder(Boolean closeOrder) {
    this.closeOrder = closeOrder;
  }
  
  /**
   * @return [optional] A mark to forcely hold the funds for an order, even though it's an order to reduce the position size. This helps the order stay on the order book and not get canceled when the position size changes. Set to <strong>false</strong> by default.
   */
  public Boolean isForceHold(){
    return forceHold;
  }
  
  /**
   * @param forceHold [optional] A mark to forcely hold the funds for an order, even though it's an order to reduce the position size. This helps the order stay on the order book and not get canceled when the position size changes. Set to <strong>false</strong> by default.
   */
  public void setForceHold(Boolean forceHold) {
    this.forceHold = forceHold;
  }
  
  /**
   * @return <strong>limit</strong> order parameter. [optional] Orders not displaying in order book. When hidden chose, not allowed choose postOnly.
   */
  public Boolean isHidden(){
    return hidden;
  }
  
  /**
   * @param hidden <strong>limit</strong> order parameter. [optional] Orders not displaying in order book. When hidden chose, not allowed choose postOnly.
   */
  public void setHidden(Boolean hidden) {
    this.hidden = hidden;
  }
  
  /**
   * @return <strong>limit</strong> order parameter. [optional] Only visible portion of the order is displayed in the order book. When iceberg chose, not allowed choose postOnly.
   */
  public Boolean isIceberg(){
    return iceberg;
  }
  
  /**
   * @param iceberg <strong>limit</strong> order parameter. [optional] Only visible portion of the order is displayed in the order book. When iceberg chose, not allowed choose postOnly.
   */
  public void setIceberg(Boolean iceberg) {
    this.iceberg = iceberg;
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
   * @return <strong>limit</strong> order parameter. [optional] Post only flag, invalid when <strong>timeInForce</strong> is <strong>IOC</strong>. When postOnly chose, not allowed choose hidden or iceberg.
   */
  public Boolean isPostOnly(){
    return postOnly;
  }
  
  /**
   * @param postOnly <strong>limit</strong> order parameter. [optional] Post only flag, invalid when <strong>timeInForce</strong> is <strong>IOC</strong>. When postOnly chose, not allowed choose hidden or iceberg.
   */
  public void setPostOnly(Boolean postOnly) {
    this.postOnly = postOnly;
  }
  
  /**
   * @return <strong>limit</strong> order parameter. Limit price
   */
  public BigDecimal getPrice(){
    return price;
  }
  
  /**
   * @param price <strong>limit</strong> order parameter. Limit price
   */
  public void setPrice(BigDecimal price) {
    this.price = price;
  }
  
  /**
   * @return [optional] A mark to reduce the position size only. Set to false by default. Need to set the position size when <strong>reduceOnly</strong> is <strong>true</strong>.
   */
  public Boolean isReduceOnly(){
    return reduceOnly;
  }
  
  /**
   * @param reduceOnly [optional] A mark to reduce the position size only. Set to false by default. Need to set the position size when <strong>reduceOnly</strong> is <strong>true</strong>.
   */
  public void setReduceOnly(Boolean reduceOnly) {
    this.reduceOnly = reduceOnly;
  }
  
  /**
   * @return [optional] remark for the order, length cannot exceed 100 utf8 characters
   */
  public String getRemark(){
    return remark;
  }
  
  /**
   * @param remark [optional] remark for the order, length cannot exceed 100 utf8 characters
   */
  public void setRemark(String remark) {
    this.remark = remark;
  }
  
  /**
   * @return <strong>buy</strong> or sell
   */
  public String getSide(){
    return side;
  }
  
  /**
   * @param side <strong>buy</strong> or sell
   */
  public void setSide(String side) {
    this.side = side;
  }
  
  /**
   * @return Order size (amount of contract to buy or sell). Must be a positive number.
   */
  public Integer getSize(){
    return size;
  }
  
  /**
   * @param size Order size (amount of contract to buy or sell). Must be a positive number.
   */
  public void setSize(Integer size) {
    this.size = size;
  }
  
  /**
   * @return [optional] Either <strong>down</strong> or <strong>up</strong>. Requires <strong>stopPrice</strong> and <strong>stopPriceType</strong> to be defined
   */
  public String getStop(){
    return stop;
  }
  
  /**
   * @param stop [optional] Either <strong>down</strong> or <strong>up</strong>. Requires <strong>stopPrice</strong> and <strong>stopPriceType</strong> to be defined
   */
  public void setStop(String stop) {
    this.stop = stop;
  }
  
  /**
   * @return [optional] Need to be defined if <strong>stop</strong> is specified.
   */
  public BigDecimal getStopPrice(){
    return stopPrice;
  }
  
  /**
   * @param stopPrice [optional] Need to be defined if <strong>stop</strong> is specified.
   */
  public void setStopPrice(BigDecimal stopPrice) {
    this.stopPrice = stopPrice;
  }
  
  /**
   * @return [optional] Either <strong>TP</strong>, <strong>IP</strong> or <strong>MP</strong>, Need to be defined if stop is specified.
   */
  public String getStopPriceType(){
    return stopPriceType;
  }
  
  /**
   * @param stopPriceType [optional] Either <strong>TP</strong>, <strong>IP</strong> or <strong>MP</strong>, Need to be defined if stop is specified.
   */
  public void setStopPriceType(String stopPriceType) {
    this.stopPriceType = stopPriceType;
  }
  
  /**
   * @return a valid contract code. e.g. XBTUSDM
   */
  public String getSymbol(){
    return symbol;
  }
  
  /**
   * @param symbol a valid contract code. e.g. XBTUSDM
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
  
  /**
   * @return <strong>limit</strong> order parameter. [optional] <strong>GTC</strong>, <strong>IOC</strong>(default is GTC), read <a href="https://docs.kucoin.com/futures/#time-in-force">Time In Force</a>
   */
  public String getTimeInForce(){
    return timeInForce;
  }
  
  /**
   * @param timeInForce <strong>limit</strong> order parameter. [optional] <strong>GTC</strong>, <strong>IOC</strong>(default is GTC), read <a href="https://docs.kucoin.com/futures/#time-in-force">Time In Force</a>
   */
  public void setTimeInForce(String timeInForce) {
    this.timeInForce = timeInForce;
  }
  
  /**
   * @return [optional] Either <strong>limit<strong> or <strong>market</strong>
   */
  public String getType(){
    return type;
  }
  
  /**
   * @param type [optional] Either <strong>limit<strong> or <strong>market</strong>
   */
  public void setType(String type) {
    this.type = type;
  }
  
  /**
   * @return <strong>limit</strong> order parameter. [optional] The maximum visible size of an iceberg order
   */
  public BigDecimal getVisibleSize(){
    return visibleSize;
  }
  
  /**
   * @param visibleSize <strong>limit</strong> order parameter. [optional] The maximum visible size of an iceberg order
   */
  public void setVisibleSize(BigDecimal visibleSize) {
    this.visibleSize = visibleSize;
  }
  
  @Override
  public String getUrlParameters() {
    return "";
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
