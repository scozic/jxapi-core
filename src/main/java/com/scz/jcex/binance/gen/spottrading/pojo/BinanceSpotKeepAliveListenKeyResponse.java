package com.scz.jcex.binance.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.binance.gen.spottrading.serializers.BinanceSpotKeepAliveListenKeyResponseSerializer;
import com.scz.jcex.util.EncodingUtil;

/**
 * Response to Binance SpotTrading API spotKeepAliveListenKey REST endpoint request<br/>Keepalive a user data stream to prevent a time out. User data streams will close after 60 minutes. It's recommended to send a ping about every 30 minutes.<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#listen-key-spot">API</a><br/>Remark: This API is used internally by websocket wrapper, which creates, keeps alive and eventually refreshes listeny key.<br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = BinanceSpotKeepAliveListenKeyResponseSerializer.class)
public class BinanceSpotKeepAliveListenKeyResponse {
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
