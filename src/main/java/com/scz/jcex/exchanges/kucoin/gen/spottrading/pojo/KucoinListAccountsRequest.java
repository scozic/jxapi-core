package com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers.KucoinListAccountsRequestSerializer;
import com.scz.jcex.netutils.rest.RestEndpointUrlParameters;
import com.scz.jcex.util.EncodingUtil;

/**
 * Request for Kucoin SpotTrading API ListAccounts REST endpointGet a list of accounts. Please deposit funds to the main account firstly, then transfer the funds to the trade account via Inner Transfer before transaction..<br/>See <a href="https://docs.kucoin.com/#list-accounts">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinListAccountsRequestSerializer.class)
public class KucoinListAccountsRequest implements RestEndpointUrlParameters {
  private String type;
  
  /**
   * @return Account type: <strong>main</strong>, <strong>trade</strong>, <strong>margin</strong>
   */
  public String getType(){
    return type;
  }
  
  /**
   * @param type Account type: <strong>main</strong>, <strong>trade</strong>, <strong>margin</strong>
   */
  public void setType(String type) {
    this.type = type;
  }
  
  @Override
  public String getUrlParameters() {
    return com.scz.jcex.util.EncodingUtil.substituteArguments("type=${type}", "type", type);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
