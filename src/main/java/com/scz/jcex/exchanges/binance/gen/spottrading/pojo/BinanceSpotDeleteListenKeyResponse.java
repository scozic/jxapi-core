package com.scz.jcex.exchanges.binance.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.binance.gen.spottrading.serializers.BinanceSpotDeleteListenKeyResponseSerializer;
import com.scz.jcex.util.EncodingUtil;

/**
 * Response to Binance SpotTrading API spotDeleteListenKey REST endpoint request<br/>Close out a user data stream..<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#listen-key-spot">API</a><br/>Remark: This API is used internally by websocket wrapper, which creates, keeps alive and eventually refreshes listeny key.<br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = BinanceSpotDeleteListenKeyResponseSerializer.class)
public class BinanceSpotDeleteListenKeyResponse {
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
