package com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.serializers.KucoinGetOpenContractListRequestSerializer;
import com.scz.jxapi.netutils.rest.RestEndpointUrlParameters;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Request for Kucoin FuturesMarketData API GetOpenContractList REST endpointSubmit request to get the info of all open contracts.<br/>See <a href="https://docs.kucoin.com/futures/#get-open-contract-list">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinGetOpenContractListRequestSerializer.class)
public class KucoinGetOpenContractListRequest implements RestEndpointUrlParameters {
  
  @Override
  public String getUrlParameters() {
    return "";
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
