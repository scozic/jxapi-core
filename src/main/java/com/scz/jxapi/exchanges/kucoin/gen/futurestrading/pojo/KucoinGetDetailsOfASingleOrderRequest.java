package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers.KucoinGetDetailsOfASingleOrderRequestSerializer;
import com.scz.jxapi.netutils.rest.RestEndpointUrlParameters;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Request for Kucoin FuturesTrading API GetDetailsOfASingleOrder REST endpointGet a single order by order id (including a stop order), querying it by its exchange assigned <strong>orderId</strong>.<br/>See <a href="https://docs.kucoin.com/futures/#get-details-of-a-single-order">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinGetDetailsOfASingleOrderRequestSerializer.class)
public class KucoinGetDetailsOfASingleOrderRequest implements RestEndpointUrlParameters {
  private String orderId;
  
  /**
   * @return Order ID (exchange assigned).
   */
  public String getOrderId(){
    return orderId;
  }
  
  /**
   * @param orderId Order ID (exchange assigned).
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
