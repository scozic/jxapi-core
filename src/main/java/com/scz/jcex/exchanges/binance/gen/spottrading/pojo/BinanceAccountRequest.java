package com.scz.jcex.exchanges.binance.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.binance.gen.spottrading.serializers.BinanceAccountRequestSerializer;
import com.scz.jcex.netutils.rest.RestEndpointUrlParameters;
import com.scz.jcex.util.EncodingUtil;

/**
 * Request for Binance SpotTrading API account REST endpointGet current account information.<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#account-information-user_data">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = BinanceAccountRequestSerializer.class)
public class BinanceAccountRequest implements RestEndpointUrlParameters {
  private Long recvWindow;
  private Long timestamp;
  
  /**
   * @return Server timezone
   */
  public Long getRecvWindow(){
    return recvWindow;
  }
  
  /**
   * @param recvWindow Server timezone
   */
  public void setRecvWindow(Long recvWindow) {
    this.recvWindow = recvWindow;
  }
  
  /**
   * @return 
   */
  public Long getTimestamp(){
    return timestamp;
  }
  
  /**
   * @param timestamp 
   */
  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }
  
  @Override
  public String getUrlParameters() {
    return EncodingUtil.createUrlQueryParameters("recvWindow", recvWindow,"timestamp", timestamp);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
