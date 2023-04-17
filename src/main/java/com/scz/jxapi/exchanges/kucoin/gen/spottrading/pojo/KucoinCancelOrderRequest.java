package com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.serializers.KucoinCancelOrderRequestSerializer;
import com.scz.jxapi.netutils.rest.RestEndpointUrlParameters;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Request for Kucoin SpotTrading API CancelOrder REST endpoint Request via this endpoint to cancel a single order previously placed.<p><i>This interface is only for cancellation requests. The cancellation result needs to be obtained by querying the order status or subscribing to websocket. It is recommended that you DO NOT cancel the order until receiving the Open message, otherwise the order cannot be cancelled successfully. </i></p> <br/>See <a href="https://docs.kucoin.com/#cancel-an-order">API</a><br/><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinCancelOrderRequestSerializer.class)
public class KucoinCancelOrderRequest implements RestEndpointUrlParameters {
  private String orderId;
  
  /**
   * @return <a href="https://docs.kucoin.com/#list-orders">Order ID</a>, unique ID of the order.
   */
  public String getOrderId(){
    return orderId;
  }
  
  /**
   * @param orderId <a href="https://docs.kucoin.com/#list-orders">Order ID</a>, unique ID of the order.
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
