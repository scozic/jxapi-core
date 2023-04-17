package com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.spotmarketdata.serializers.KucoinAllSymbolsTickerStreamRequestSerializer;
import com.scz.jxapi.netutils.websocket.WebsocketSubscribeParameters;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Subscription request toKucoin SpotMarketData API AllSymbolsTickerStream websocket endpoint<br/>Subscribe to this topic to get the push of all market symbols BBO change. Push frequency: once every 100ms. <br/>See <a href="https://docs.kucoin.com/#all-symbols-ticker">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinAllSymbolsTickerStreamRequestSerializer.class)
public class KucoinAllSymbolsTickerStreamRequest implements WebsocketSubscribeParameters {
  
  
  @Override
  public String getTopic() {
    return "/market/ticker:all";
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
