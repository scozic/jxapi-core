package com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.serializers.KucoinCancelAnOrderResponseSerializer;
import com.scz.jcex.util.EncodingUtil;

/**
 * Response to Kucoin FuturesTrading API cancelAnOrder REST endpoint request<br/>Cancel an order (including a stop order).<br/> You will receive success message once the system has received the cancellation request. The cancellation request will be processed by matching engine in sequence. To know if the request has been processed, you may check the order status or update message from the pushes.<br/>The order id is the server-assigned order id, not the specified clientOid.<br/>If the order can not be canceled (already filled or previously canceled, etc), then an error response will indicate the reason in the message field.<br/>See <a href="https://docs.kucoin.com/futures/#cancel-an-order">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinCancelAnOrderResponseSerializer.class)
public class KucoinCancelAnOrderResponse {
  private String code;
  private KucoinCancelAnOrderResponseData data;
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
  public KucoinCancelAnOrderResponseData getData(){
    return data;
  }
  
  /**
   * @param data List of market information for each market symbol
   */
  public void setData(KucoinCancelAnOrderResponseData data) {
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
