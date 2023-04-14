package com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.serializers.KucoinGetListOfOrdersCompletedIn24hRequestSerializer;
import com.scz.jcex.netutils.rest.RestEndpointUrlParameters;
import com.scz.jcex.util.EncodingUtil;

/**
 * Request for Kucoin FuturesTrading API GetListOfOrdersCompletedIn24h REST endpointGet a list of recent 1000 orders in the last 24 hours.<br/>If you need to get your recent traded order history with low latency, you may query this endpoint.<br/>See <a href="https://docs.kucoin.com/futures/#get-list-of-orders-completed-in-24h">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinGetListOfOrdersCompletedIn24hRequestSerializer.class)
public class KucoinGetListOfOrdersCompletedIn24hRequest implements RestEndpointUrlParameters {
  private String symbol;
  
  /**
   * @return [Optional] Symbol of the contract
   */
  public String getSymbol(){
    return symbol;
  }
  
  /**
   * @param symbol [Optional] Symbol of the contract
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
