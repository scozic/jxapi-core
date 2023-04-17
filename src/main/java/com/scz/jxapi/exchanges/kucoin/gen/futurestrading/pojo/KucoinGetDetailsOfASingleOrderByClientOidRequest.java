package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers.KucoinGetDetailsOfASingleOrderByClientOidRequestSerializer;
import com.scz.jxapi.netutils.rest.RestEndpointUrlParameters;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Request for Kucoin FuturesTrading API GetDetailsOfASingleOrderByClientOid REST endpointGet a single order by order id (including a stop order), querying it by its client side assigned <strong>clientOid</strong>.<br/>See <a href="https://docs.kucoin.com/futures/#get-details-of-a-single-order">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinGetDetailsOfASingleOrderByClientOidRequestSerializer.class)
public class KucoinGetDetailsOfASingleOrderByClientOidRequest implements RestEndpointUrlParameters {
  private String clientOid;
  
  /**
   * @return Order ID (exchange assigned).
   */
  public String getClientOid(){
    return clientOid;
  }
  
  /**
   * @param clientOid Order ID (exchange assigned).
   */
  public void setClientOid(String clientOid) {
    this.clientOid = clientOid;
  }
  
  @Override
  public String getUrlParameters() {
    return EncodingUtil.createUrlQueryParameters("clientOid", clientOid);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
