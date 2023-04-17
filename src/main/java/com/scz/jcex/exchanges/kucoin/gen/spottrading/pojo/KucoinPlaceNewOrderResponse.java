package com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers.KucoinPlaceNewOrderResponseSerializer;
import com.scz.jcex.util.EncodingUtil;

/**
 * Response to Kucoin SpotTrading API PlaceNewOrder REST endpoint request<br/>You can place two types of orders: limit and market. Orders can only be placed if your account has sufficient funds. Once an order is placed, your account funds will be put on hold for the duration of the order. How much and which funds are put on hold depends on the order type and parameters specified. See the Holds details below.<br/><strong>Placing an order will enable price protection. When the price of the limit order is outside the threshold range, the price protection mechanism will be triggered, causing the order to fail.</strong><br/>Please note that the system will frozen the fees from the orders that entered the order book in advance. Read List Fills to learn more.<br/>Before placing an order, please read <a href="https://docs.kucoin.com/#get-symbols-list">Get Symbol List</a> to understand the requirements for the quantity parameters for each trading pair..<br/>See <a href="https://docs.kucoin.com/#place-a-new-order">API</a><br/><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinPlaceNewOrderResponseSerializer.class)
public class KucoinPlaceNewOrderResponse {
  private String orderId;
  
  /**
   * @return The ID of the order
   */
  public String getOrderId(){
    return orderId;
  }
  
  /**
   * @param orderId The ID of the order
   */
  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
