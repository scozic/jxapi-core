package com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.serializers.KucoinCancelSingleOrderByClientOidRequestSerializer;
import com.scz.jxapi.netutils.rest.RestEndpointUrlParameters;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Request for Kucoin SpotTrading API CancelSingleOrderByClientOid REST endpoint Request via this interface to cancel an order via the clientOid.<br/>See <a href="https://docs.kucoin.com/#cancel-single-order-by-clientoid">API</a><br/><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinCancelSingleOrderByClientOidRequestSerializer.class)
public class KucoinCancelSingleOrderByClientOidRequest implements RestEndpointUrlParameters {
  private String clientOid;
  
  /**
   * @return Unique order id created by users to identify their orders.
   */
  public String getClientOid(){
    return clientOid;
  }
  
  /**
   * @param clientOid Unique order id created by users to identify their orders.
   */
  public void setClientOid(String clientOid) {
    this.clientOid = clientOid;
  }
  
  @Override
  public String getUrlParameters() {
    return EncodingUtil.substituteArguments("/${clientOid}", "clientOid", clientOid);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
