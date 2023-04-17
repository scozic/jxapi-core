package com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.serializers.KucoinGet24hrStatsRequestSerializer;
import com.scz.jxapi.netutils.rest.RestEndpointUrlParameters;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Request for Kucoin SpotMarketData API get24hrStats REST endpointRequest via this endpoint to get the statistics of the specified ticker in the last 24 hours.<br/>See <a href="https://docs.kucoin.com/#get-24hr-stats">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinGet24hrStatsRequestSerializer.class)
public class KucoinGet24hrStatsRequest implements RestEndpointUrlParameters {
  private String symbol;
  
  /**
   * @return <a href="https://docs.kucoin.com/#get-symbols-list">symbol</a>
   */
  public String getSymbol(){
    return symbol;
  }
  
  /**
   * @param symbol <a href="https://docs.kucoin.com/#get-symbols-list">symbol</a>
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }
  
  @Override
  public String getUrlParameters() {
    return EncodingUtil.createUrlQueryParameters("symbol", symbol);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
