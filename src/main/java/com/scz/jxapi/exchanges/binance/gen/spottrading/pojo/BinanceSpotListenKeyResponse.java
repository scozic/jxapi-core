package com.scz.jxapi.exchanges.binance.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.binance.gen.spottrading.serializers.BinanceSpotListenKeyResponseSerializer;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Response to Binance SpotTrading API spotListenKey REST endpoint request<br/>Start a new user data stream. The stream will close after 60 minutes unless a keepalive is sent. If the account has an active listenKey, that listenKey will be returned and its validity will be extended for 60 minutes.<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#listen-key-spot">API</a><br/>Remark: This API is used internally by websocket wrapper, which creates, keeps alive and eventually refreshes listeny key.<br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = BinanceSpotListenKeyResponseSerializer.class)
public class BinanceSpotListenKeyResponse {
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
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
