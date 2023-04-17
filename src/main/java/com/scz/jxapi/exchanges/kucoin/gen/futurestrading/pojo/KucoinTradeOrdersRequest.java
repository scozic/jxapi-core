package com.scz.jxapi.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.futurestrading.serializers.KucoinTradeOrdersRequestSerializer;
import com.scz.jxapi.netutils.websocket.WebsocketSubscribeParameters;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Subscription request toKucoin FuturesTrading API TradeOrders websocket endpoint<br/>Trade Orders websocket stream.<br/>See <a href="https://docs.kucoin.com/futures/#trade-orders">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinTradeOrdersRequestSerializer.class)
public class KucoinTradeOrdersRequest implements WebsocketSubscribeParameters {
  
  
  @Override
  public String getTopic() {
    return "/contractMarket/tradeOrders";
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
