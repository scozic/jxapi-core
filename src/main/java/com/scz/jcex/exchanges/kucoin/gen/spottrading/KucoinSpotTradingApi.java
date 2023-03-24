package com.scz.jcex.exchanges.kucoin.gen.spottrading;

import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinAccountBalanceNoticeMessage;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinAccountBalanceNoticeRequest;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateRequest;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateResponse;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListAccountsRequest;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListAccountsResponse;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinPrivateOrderChangeV2Message;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinPrivateOrderChangeV2Request;
import com.scz.jcex.netutils.websocket.WebsocketListener;
import java.io.IOException;

/**
 * SpotTrading CEX SpotTrading API</br>
 * Kucoin spot trading API
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public interface  KucoinSpotTradingApi {
  /**
   * You need to apply for one of the two tokens below to create a websocket connection..<br/>See <a href="https://docs.kucoin.com/#apply-connect-token">API</a>
   */
  KucoinApplyConnectTokenPrivateResponse applyConnectTokenPrivate(KucoinApplyConnectTokenPrivateRequest request) throws IOException;
  /**
   * Get a list of accounts. Please deposit funds to the main account firstly, then transfer the funds to the trade account via Inner Transfer before transaction..<br/>See <a href="https://docs.kucoin.com/#list-accounts">API</a>
   */
  KucoinListAccountsResponse listAccounts(KucoinListAccountsRequest request) throws IOException;
  
  /**
   * Subscribe to PrivateOrderChangeV2 stream.<br/>
   * This topic will push all change events of your orders. Compared with v1, v2 adds an Order Status: "new", there is no difference in push speed. <br/>See <a href="https://docs.kucoin.com/#private-order-change-v2">API</a>
   * 
   * @return client subscriptionId to use for unsubscription
   */
  String subscribePrivateOrderChangeV2(KucoinPrivateOrderChangeV2Request request, WebsocketListener<KucoinPrivateOrderChangeV2Message> listener);
  
  /**
   * Unsubscribe from PrivateOrderChangeV2 stream.
   * 
   * @param subscriptionId ID of subscription returned by #subscribePrivateOrderChangeV2()
   */
  boolean unsubscribePrivateOrderChangeV2(String subscriptionId);
  
  /**
   * Subscribe to AccountBalanceNotice stream.<br/>
   * You will receive this message when an account balance changes. The message contains the details of the change.<br/>See <a href="https://docs.kucoin.com/#account-balance-notice">API</a>
   * 
   * @return client subscriptionId to use for unsubscription
   */
  String subscribeAccountBalanceNotice(KucoinAccountBalanceNoticeRequest request, WebsocketListener<KucoinAccountBalanceNoticeMessage> listener);
  
  /**
   * Unsubscribe from AccountBalanceNotice stream.
   * 
   * @param subscriptionId ID of subscription returned by #subscribeAccountBalanceNotice()
   */
  boolean unsubscribeAccountBalanceNotice(String subscriptionId);
}
