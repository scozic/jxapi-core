package com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.serializers.KucoinPlaceAnOrderResponseSerializer;
import com.scz.jcex.util.EncodingUtil;

/**
 * Response to Kucoin FuturesTrading API PlaceAnOrder REST endpoint request<br/>You can place two types of orders: limit and market. Orders can only be placed if your account has sufficient funds. Once an order is placed, your funds will be put on hold for the duration of the order. The amount of funds on hold depends on the order type and parameters specified. <br/>Please be noted that the system would hold the fees from the orders entered the orderbook in advance. Read Get Fills to learn more.<br/>See <a href="https://docs.kucoin.com/futures/#place-an-order">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinPlaceAnOrderResponseSerializer.class)
public class KucoinPlaceAnOrderResponse {
  private String code;
  private KucoinPlaceAnOrderResponseData data;
  private String msg;
  
  /**
   * @return System error code, see <a href="https://docs.kucoin.com/#request">API</a>
   */
  public String getCode(){
    return code;
  }
  
  /**
   * @param code System error code, see <a href="https://docs.kucoin.com/#request">API</a>
   */
  public void setCode(String code) {
    this.code = code;
  }
  
  /**
   * @return List of market information for each market symbol
   */
  public KucoinPlaceAnOrderResponseData getData(){
    return data;
  }
  
  /**
   * @param data List of market information for each market symbol
   */
  public void setData(KucoinPlaceAnOrderResponseData data) {
    this.data = data;
  }
  
  /**
   * @return System error code description, provided in error response when code is not '0', see <a href="https://docs.kucoin.com/#request">API</a>
   */
  public String getMsg(){
    return msg;
  }
  
  /**
   * @param msg System error code description, provided in error response when code is not '0', see <a href="https://docs.kucoin.com/#request">API</a>
   */
  public void setMsg(String msg) {
    this.msg = msg;
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
