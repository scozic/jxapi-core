package com.scz.jcex.binance.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.binance.gen.spottrading.serializers.BinanceAccountRequestSerializer;
import com.scz.jcex.netutils.rest.RestEndpointUrlParameters;
import com.scz.jcex.util.EncodingUtil;

/**
 * Request for Binance SpotTrading API account REST endpointGet current account information.<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#account-information-user_data">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = BinanceAccountRequestSerializer.class)
public class BinanceAccountRequest implements RestEndpointUrlParameters {
  private long recvWindow;
  private long timestamp;
  
  /**
   * @return Server timezone
   */
  public long getRecvWindow(){
    return recvWindow;
  }
  
  /**
   * @param recvWindow Server timezone
   */
  public void setRecvWindow(long recvWindow) {
    this.recvWindow = recvWindow;
  }
  
  /**
   * @return 
   */
  public long getTimestamp(){
    return timestamp;
  }
  
  /**
   * @param timestamp 
   */
  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }
  
  
  @Override
  public String getUrlParameters() {
    return com.scz.jcex.util.EncodingUtil.substituteArguments("recvWindow=${recvWindow}&timestamp=${timestamp}", "recvWindow", recvWindow, "timestamp", timestamp);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
