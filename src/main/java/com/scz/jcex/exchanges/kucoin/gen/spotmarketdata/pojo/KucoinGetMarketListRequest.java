package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.serializers.KucoinGetMarketListRequestSerializer;
import com.scz.jcex.netutils.rest.RestEndpointUrlParameters;
import com.scz.jcex.util.EncodingUtil;

/**
 * Request for Kucoin SpotMarketData API getMarketList REST endpointRequest via this endpoint to get the transaction currency for the entire trading market.<br/>See <a href="https://docs.kucoin.com/#get-market-list">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinGetMarketListRequestSerializer.class)
public class KucoinGetMarketListRequest implements RestEndpointUrlParameters {
  
  @Override
  public String getUrlParameters() {
    return "";
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
