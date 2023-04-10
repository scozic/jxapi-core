package com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.serializers.KucoinStopOrderLifecycleEventRequestSerializer;
import com.scz.jcex.netutils.websocket.WebsocketSubscribeParameters;
import com.scz.jcex.util.EncodingUtil;

/**
 * Subscription request toKucoin FuturesTrading API StopOrderLifecycleEvent websocket endpoint<br/>Stop Order Lifecycle Event websocket stream.<br/>See <a href="https://docs.kucoin.com/futures/#stop-order-lifecycle-event">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinStopOrderLifecycleEventRequestSerializer.class)
public class KucoinStopOrderLifecycleEventRequest implements WebsocketSubscribeParameters {
  
  
  @Override
  public String getTopic() {
    return "/contractMarket/advancedOrders";
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
