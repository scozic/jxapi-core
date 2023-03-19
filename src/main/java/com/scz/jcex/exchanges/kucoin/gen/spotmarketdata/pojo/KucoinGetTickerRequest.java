package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.serializers.KucoinGetTickerRequestSerializer;
import com.scz.jcex.netutils.rest.RestEndpointUrlParameters;
import com.scz.jcex.util.EncodingUtil;

/**
 * Request for Kucoin SpotMarketData API getTicker REST endpointRequest via this endpoint to get Level 1 Market Data. The returned value includes the best bid price and size, the best ask price and size as well as the last traded price and the last traded size.<br/>See <a href="https://docs.kucoin.com/#get-ticker">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinGetTickerRequestSerializer.class)
public class KucoinGetTickerRequest implements RestEndpointUrlParameters {
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
    return com.scz.jcex.util.EncodingUtil.substituteArguments("symbol=${symbol}", "symbol", symbol);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
