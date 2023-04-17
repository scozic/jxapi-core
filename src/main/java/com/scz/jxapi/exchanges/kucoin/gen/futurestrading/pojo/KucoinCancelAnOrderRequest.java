package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers.KucoinCancelAnOrderRequestSerializer;
import com.scz.jxapi.netutils.rest.RestEndpointUrlParameters;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Request for Kucoin FuturesTrading API CancelAnOrder REST endpointCancel an order (including a stop order).<br/> You will receive success message once the system has received the cancellation request. The cancellation request will be processed by matching engine in sequence. To know if the request has been processed, you may check the order status or update message from the pushes.<br/>The order id is the server-assigned order id, not the specified clientOid.<br/>If the order can not be canceled (already filled or previously canceled, etc), then an error response will indicate the reason in the message field.<br/>See <a href="https://docs.kucoin.com/futures/#cancel-an-order">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinCancelAnOrderRequestSerializer.class)
public class KucoinCancelAnOrderRequest implements RestEndpointUrlParameters {
  private String orderId;
  
  /**
   * @return The order id is the server-assigned order id, not the specified clientOid.
   */
  public String getOrderId(){
    return orderId;
  }
  
  /**
   * @param orderId The order id is the server-assigned order id, not the specified clientOid.
   */
  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }
  
  @Override
  public String getUrlParameters() {
    return EncodingUtil.substituteArguments("/${orderId}", "orderId", orderId);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
