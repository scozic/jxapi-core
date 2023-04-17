package com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.serializers.KucoinPrivateOrderChangeV2RequestSerializer;
import com.scz.jxapi.netutils.websocket.WebsocketSubscribeParameters;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Subscription request toKucoin SpotTrading API PrivateOrderChangeV2 websocket endpoint<br/>This topic will push all change events of your orders. Compared with v1, v2 adds an Order Status: "new", there is no difference in push speed. <br/>See <a href="https://docs.kucoin.com/#private-order-change-v2">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinPrivateOrderChangeV2RequestSerializer.class)
public class KucoinPrivateOrderChangeV2Request implements WebsocketSubscribeParameters {
  
  
  @Override
  public String getTopic() {
    return "/spotMarket/tradeOrdersV2";
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
