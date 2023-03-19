package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.serializers.KucoinApplyConnectTokenPublicRequestSerializer;
import com.scz.jcex.netutils.rest.RestEndpointUrlParameters;
import com.scz.jcex.util.EncodingUtil;

/**
 * Request for Kucoin SpotMarketData API ApplyConnectTokenPublic REST endpointYou need to apply for one of the two tokens below to create a websocket connection..<br/>See <a href="https://docs.kucoin.com/#apply-connect-token">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinApplyConnectTokenPublicRequestSerializer.class)
public class KucoinApplyConnectTokenPublicRequest implements RestEndpointUrlParameters {
  
  
  @Override
  public String getUrlParameters() {
    return "";
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
