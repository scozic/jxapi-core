package com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.serializers.KucoinAccountBalanceNoticeRequestSerializer;
import com.scz.jcex.netutils.websocket.WebsocketSubscribeParameters;
import com.scz.jcex.util.EncodingUtil;

/**
 * Subscription request toKucoin SpotTrading API AccountBalanceNotice websocket endpoint<br/>You will receive this message when an account balance changes. The message contains the details of the change.<br/>See <a href="https://docs.kucoin.com/#account-balance-notice">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = KucoinAccountBalanceNoticeRequestSerializer.class)
public class KucoinAccountBalanceNoticeRequest implements WebsocketSubscribeParameters {
  
  
  @Override
  public String getTopic() {
    return "/account/balance";
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
