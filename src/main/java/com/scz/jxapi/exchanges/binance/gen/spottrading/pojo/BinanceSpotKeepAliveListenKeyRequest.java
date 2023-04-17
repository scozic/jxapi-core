package com.scz.jxapi.exchanges.binance.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.binance.gen.spottrading.serializers.BinanceSpotKeepAliveListenKeyRequestSerializer;
import com.scz.jxapi.netutils.rest.RestEndpointUrlParameters;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Request for Binance SpotTrading API spotKeepAliveListenKey REST endpointKeepalive a user data stream to prevent a time out. User data streams will close after 60 minutes. It's recommended to send a ping about every 30 minutes.<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#listen-key-spot">API</a><br/>Remark: This API is used internally by websocket wrapper, which creates, keeps alive and eventually refreshes listeny key.<br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = BinanceSpotKeepAliveListenKeyRequestSerializer.class)
public class BinanceSpotKeepAliveListenKeyRequest implements RestEndpointUrlParameters {
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
    return EncodingUtil.createUrlQueryParameters("listenKey", listenKey);
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
