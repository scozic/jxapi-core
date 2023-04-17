package com.scz.jxapi.exchanges.binance.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.binance.gen.spottrading.serializers.BinanceOutboundAccountPositionUserDataStreamRequestSerializer;
import com.scz.jxapi.netutils.websocket.WebsocketSubscribeParameters;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Subscription request toBinance SpotTrading API outboundAccountPositionUserDataStream websocket endpoint<br/>User data Stream.<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#user-data-streams">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = BinanceOutboundAccountPositionUserDataStreamRequestSerializer.class)
public class BinanceOutboundAccountPositionUserDataStreamRequest implements WebsocketSubscribeParameters {
  
  
  @Override
  public String getTopic() {
    return "outboundAccountPosition";
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
