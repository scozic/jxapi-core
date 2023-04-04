package com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers.KucoinPlaceNewOrderRequestSerializer;
import com.scz.jcex.netutils.rest.RestEndpointUrlParameters;
import com.scz.jcex.util.EncodingUtil;

/**
 * Request for Kucoin SpotTrading API PlaceNewOrder REST endpointYou can place two types of orders: limit and market. Orders can only be placed if your account has sufficient funds. Once an order is placed, your account funds will be put on hold for the duration of the order. How much and which funds are put on hold depends on the order type and parameters specified. See the Holds details below.<br/><strong>Placing an order will enable price protection. When the price of the limit order is outside the threshold range, the price protection mechanism will be triggered, causing the order to fail.</strong><br/>Please note that the system will frozen the fees from the orders that entered the order book in advance. Read List Fills to learn more.<br/>Before placing an order, please read <a href="https://docs.kucoin.com/#get-symbols-list">Get Symbol List</a> to understand the requirements for the quantity parameters for each trading pair..<br/>See <a href="https://docs.kucoin.com/#place-a-new-order">API</a><br/><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinPlaceNewOrderRequestSerializer.class)
public class KucoinPlaceNewOrderRequest implements RestEndpointUrlParameters {
  private long cancelAfter;
  private String clientOid;
  private String funds;
  private boolean hidden;
  private boolean iceberg;
  private boolean postOnly;
  private String price;
  private String remark;
  private String side;
  private String size;
  private String stp;
  private String symbol;
  private String timeInForce;
  private String tradeType;
  private String type;
  private String visibleSize;
  
  /**
   * @return <i>LIMIT order parameter</i><br/>[Optional] cancel after <strong>n</strong> seconds, requires timeInForce to be <strong>GTT</strong>.
   */
  public long getCancelAfter(){
    return cancelAfter;
  }
  
  /**
   * @param cancelAfter <i>LIMIT order parameter</i><br/>[Optional] cancel after <strong>n</strong> seconds, requires timeInForce to be <strong>GTT</strong>.
   */
  public void setCancelAfter(long cancelAfter) {
    this.cancelAfter = cancelAfter;
  }
  
  /**
   * @return Unique order id created by users to identify their orders, e.g. UUID, with a maximum length of 128 bits.
   */
  public String getClientOid(){
    return clientOid;
  }
  
  /**
   * @param clientOid Unique order id created by users to identify their orders, e.g. UUID, with a maximum length of 128 bits.
   */
  public void setClientOid(String clientOid) {
    this.clientOid = clientOid;
  }
  
  /**
   * @return <i>MARKET order parameter</i><br/>[Optional] The desired amount of quote currency to use
   */
  public String getFunds(){
    return funds;
  }
  
  /**
   * @param funds <i>MARKET order parameter</i><br/>[Optional] The desired amount of quote currency to use
   */
  public void setFunds(String funds) {
    this.funds = funds;
  }
  
  /**
   * @return <i>LIMIT order parameter</i><br/>[Optional] Order will not be displayed in the order book
   */
  public boolean isHidden(){
    return hidden;
  }
  
  /**
   * @param hidden <i>LIMIT order parameter</i><br/>[Optional] Order will not be displayed in the order book
   */
  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }
  
  /**
   * @return <i>LIMIT order parameter</i><br/>[Optional] Only a portion of the order is displayed in the order book
   */
  public boolean isIceberg(){
    return iceberg;
  }
  
  /**
   * @param iceberg <i>LIMIT order parameter</i><br/>[Optional] Only a portion of the order is displayed in the order book
   */
  public void setIceberg(boolean iceberg) {
    this.iceberg = iceberg;
  }
  
  /**
   * @return <i>LIMIT order parameter</i><br/>[Optional] Post only flag, invalid when timeInForce is <strong>IOC</strong> or <strong>FOK</strong>
   */
  public boolean isPostOnly(){
    return postOnly;
  }
  
  /**
   * @param postOnly <i>LIMIT order parameter</i><br/>[Optional] Post only flag, invalid when timeInForce is <strong>IOC</strong> or <strong>FOK</strong>
   */
  public void setPostOnly(boolean postOnly) {
    this.postOnly = postOnly;
  }
  
  /**
   * @return <i>LIMIT order parameter</i><br/>Price per base currency
   */
  public String getPrice(){
    return price;
  }
  
  /**
   * @param price <i>LIMIT order parameter</i><br/>Price per base currency
   */
  public void setPrice(String price) {
    this.price = price;
  }
  
  /**
   * @return [Optional] remark for the order, length cannot exceed 100 utf8 characters
   */
  public String getRemark(){
    return remark;
  }
  
  /**
   * @param remark [Optional] remark for the order, length cannot exceed 100 utf8 characters
   */
  public void setRemark(String remark) {
    this.remark = remark;
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
   * @return <ul><li>For LIMIT orders: price per base currency</li><li>For MARKET orders:[Optional should be provided unless <strong>funds</strong> parameter is] Desired amount in base currency
   */
  public String getSize(){
    return size;
  }
  
  /**
   * @param size <ul><li>For LIMIT orders: price per base currency</li><li>For MARKET orders:[Optional should be provided unless <strong>funds</strong> parameter is] Desired amount in base currency
   */
  public void setSize(String size) {
    this.size = size;
  }
  
  /**
   * @return [Optional] self trade prevention , <strong>CN</strong>, <strong>CO</strong>, <strong>CB</strong> or <strong>DC</strong>
   */
  public String getStp(){
    return stp;
  }
  
  /**
   * @param stp [Optional] self trade prevention , <strong>CN</strong>, <strong>CO</strong>, <strong>CB</strong> or <strong>DC</strong>
   */
  public void setStp(String stp) {
    this.stp = stp;
  }
  
  /**
   * @return A valid trading symbol code. e.g. ETH-BTC
   */
  public String getSymbol(){
    return symbol;
  }
  
  /**
   * @param symbol A valid trading symbol code. e.g. ETH-BTC
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
  
  /**
   * @return <i>LIMIT order parameter</i><br/>[Optional] <strong>GTC</strong>, <strong>GTT</strong>, <strong>IOC</strong>, or <strong>FOK</strong> (default is <strong>GTC</strong>), read <a href="https://docs.kucoin.com/#time-in-force">Time In Force</a>.
   */
  public String getTimeInForce(){
    return timeInForce;
  }
  
  /**
   * @param timeInForce <i>LIMIT order parameter</i><br/>[Optional] <strong>GTC</strong>, <strong>GTT</strong>, <strong>IOC</strong>, or <strong>FOK</strong> (default is <strong>GTC</strong>), read <a href="https://docs.kucoin.com/#time-in-force">Time In Force</a>.
   */
  public void setTimeInForce(String timeInForce) {
    this.timeInForce = timeInForce;
  }
  
  /**
   * @return [Optional] The type of trading : <strong>TRADE</strong> （Spot Trade）, <strong>MARGIN_TRADE</strong> (Margin Trade). Default is <strong>TRADE</strong>. <strong>Note: To improve the system performance and to accelerate order placing and processing, KuCoin has added a new interface for order placing of margin. For traders still using the current interface, please move to the new one as soon as possible. The current one will no longer accept margin orders by May 1st, 2021 (UTC). At the time, KuCoin will notify users via the announcement, please pay attention to it.</strong>
   */
  public String getTradeType(){
    return tradeType;
  }
  
  /**
   * @param tradeType [Optional] The type of trading : <strong>TRADE</strong> （Spot Trade）, <strong>MARGIN_TRADE</strong> (Margin Trade). Default is <strong>TRADE</strong>. <strong>Note: To improve the system performance and to accelerate order placing and processing, KuCoin has added a new interface for order placing of margin. For traders still using the current interface, please move to the new one as soon as possible. The current one will no longer accept margin orders by May 1st, 2021 (UTC). At the time, KuCoin will notify users via the announcement, please pay attention to it.</strong>
   */
  public void setTradeType(String tradeType) {
    this.tradeType = tradeType;
  }
  
  /**
   * @return [Optional] <strong>limit</strong> or <strong>market</strong> (default is <strong>limit</strong>
   */
  public String getType(){
    return type;
  }
  
  /**
   * @param type [Optional] <strong>limit</strong> or <strong>market</strong> (default is <strong>limit</strong>
   */
  public void setType(String type) {
    this.type = type;
  }
  
  /**
   * @return <i>LIMIT order parameter</i><br/>[Optional] The maximum visible size of an iceberg order
   */
  public String getVisibleSize(){
    return visibleSize;
  }
  
  /**
   * @param visibleSize <i>LIMIT order parameter</i><br/>[Optional] The maximum visible size of an iceberg order
   */
  public void setVisibleSize(String visibleSize) {
    this.visibleSize = visibleSize;
  }
  
  @Override
  public String getUrlParameters() {
    return com.scz.jcex.util.EncodingUtil.substituteArguments("", "clientOid", clientOid, "side", side, "symbol", symbol, "type", type, "remark", remark, "stp", stp, "tradeType", tradeType, "price", price, "size", size, "timeInForce", timeInForce, "cancelAfter", cancelAfter, "postOnly", postOnly, "hidden", hidden, "iceberg", iceberg, "visibleSize", visibleSize, "funds", funds);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
