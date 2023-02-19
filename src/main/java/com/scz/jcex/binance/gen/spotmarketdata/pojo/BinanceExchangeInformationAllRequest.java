package com.scz.jcex.binance.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.binance.gen.spotmarketdata.serializers.BinanceExchangeInformationAllRequestSerializer;
import com.scz.jcex.netutils.rest.RestEndpointUrlParameters;
import com.scz.jcex.util.EncodingUtil;

/**
 * Request for Binance SpotMarketData API exchangeInformationAll REST endpointCurrent exchange trading rules and symbol information for all spot trading pairs<br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = BinanceExchangeInformationAllRequestSerializer.class)
public class BinanceExchangeInformationAllRequest implements RestEndpointUrlParameters {
  
  
  @Override
  public String getUrlParameters() {
    return "";
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
