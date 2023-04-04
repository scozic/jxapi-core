package com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers.KucoinCancelSingleOrderByClientOidRequestSerializer;
import com.scz.jcex.netutils.rest.RestEndpointUrlParameters;
import com.scz.jcex.util.EncodingUtil;

/**
 * Request for Kucoin SpotTrading API CancelSingleOrderByClientOid REST endpoint Request via this endpoint to cancel a single order previously placed.<p><i>This interface is only for cancellation requests. The cancellation result needs to be obtained by querying the order status or subscribing to websocket. It is recommended that you DO NOT cancel the order until receiving the Open message, otherwise the order cannot be cancelled successfully. </i></p> <br/>See <a href="https://docs.kucoin.com/#cancel-an-order">API</a><br/><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
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
    return com.scz.jcex.util.EncodingUtil.substituteArguments("/${clientOid}", "clientOid", clientOid);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
