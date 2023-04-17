package com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.serializers.KucoinGetAccountOverviewRequestSerializer;
import com.scz.jcex.netutils.rest.RestEndpointUrlParameters;
import com.scz.jcex.util.EncodingUtil;

/**
 * Request for Kucoin FuturesTrading API GetAccountOverview REST endpointGet account overview.<br/>See <a href="https://docs.kucoin.com/futures/#get-account-overview">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinGetAccountOverviewRequestSerializer.class)
public class KucoinGetAccountOverviewRequest implements RestEndpointUrlParameters {
  private String currency;
  
  /**
   * @return [Optional] Currecny ,including XBT,USDT,Default XBT
   */
  public String getCurrency(){
    return currency;
  }
  
  /**
   * @param currency [Optional] Currecny ,including XBT,USDT,Default XBT
   */
  public void setCurrency(String currency) {
    this.currency = currency;
  }
  
  @Override
  public String getUrlParameters() {
    return EncodingUtil.createUrlQueryParameters("currency", currency);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
