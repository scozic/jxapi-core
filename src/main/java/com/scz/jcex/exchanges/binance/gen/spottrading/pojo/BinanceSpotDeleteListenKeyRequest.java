package com.scz.jcex.exchanges.binance.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.binance.gen.spottrading.serializers.BinanceSpotDeleteListenKeyRequestSerializer;
import com.scz.jcex.netutils.rest.RestEndpointUrlParameters;
import com.scz.jcex.util.EncodingUtil;

/**
 * Request for Binance SpotTrading API spotDeleteListenKey REST endpointClose out a user data stream..<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#listen-key-spot">API</a><br/>Remark: This API is used internally by websocket wrapper, which creates, keeps alive and eventually refreshes listeny key.<br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = BinanceSpotDeleteListenKeyRequestSerializer.class)
public class BinanceSpotDeleteListenKeyRequest implements RestEndpointUrlParameters {
  private String listenKey;
  
  /**
   * @return 
   */
  public String getListenKey(){
    return listenKey;
  }
  
  /**
   * @param listenKey 
   */
  public void setListenKey(String listenKey) {
    this.listenKey = listenKey;
  }
  
  
  @Override
  public String getUrlParameters() {
    return com.scz.jcex.util.EncodingUtil.substituteArguments("listenKey=${listenKey}", "listenKey", listenKey);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
