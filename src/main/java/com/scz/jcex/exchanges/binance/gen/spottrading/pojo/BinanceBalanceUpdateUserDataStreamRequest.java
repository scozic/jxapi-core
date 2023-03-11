package com.scz.jcex.exchanges.binance.gen.spottrading.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scz.jcex.exchanges.binance.gen.spottrading.serializers.BinanceBalanceUpdateUserDataStreamRequestSerializer;
import com.scz.jcex.netutils.websocket.WebsocketSubscribeParameters;
import com.scz.jcex.util.EncodingUtil;

/**
 * Subscription request toBinance SpotTrading API balanceUpdateUserDataStream websocket endpoint<br/>User data Stream.<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#user-data-streams">API</a><br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
@JsonSerialize(using = BinanceBalanceUpdateUserDataStreamRequestSerializer.class)
public class BinanceBalanceUpdateUserDataStreamRequest implements WebsocketSubscribeParameters {
  
  
  @Override
  public String getTopic() {
    return "balanceUpdate";
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
