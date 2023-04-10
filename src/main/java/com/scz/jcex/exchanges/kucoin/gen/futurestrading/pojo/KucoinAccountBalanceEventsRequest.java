package com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.serializers.KucoinAccountBalanceEventsRequestSerializer;
import com.scz.jcex.netutils.websocket.WebsocketSubscribeParameters;
import com.scz.jcex.util.EncodingUtil;

/**
 * Subscription request toKucoin FuturesTrading API AccountBalanceEvents websocket endpoint<br/>Stop Order Lifecycle Event websocket stream.<br/>See <a href="https://docs.kucoin.com/futures/#stop-order-lifecycle-event">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinAccountBalanceEventsRequestSerializer.class)
public class KucoinAccountBalanceEventsRequest implements WebsocketSubscribeParameters {
  
  
  @Override
  public String getTopic() {
    return "/contractAccount/wallet";
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
