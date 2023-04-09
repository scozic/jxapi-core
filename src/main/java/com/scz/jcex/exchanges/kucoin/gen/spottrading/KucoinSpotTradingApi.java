package com.scz.jcex.exchanges.kucoin.gen.spottrading;

import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinAccountBalanceNoticeMessage;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinAccountBalanceNoticeRequest;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateRequest;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateResponse;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelAllOrdersRequest;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelAllOrdersResponse;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelOrderRequest;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelOrderResponse;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelSingleOrderByClientOidRequest;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelSingleOrderByClientOidResponse;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListAccountsRequest;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListAccountsResponse;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersRequest;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersResponse;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinPlaceNewOrderRequest;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinPlaceNewOrderResponse;
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
   * You can place two types of orders: limit and market. Orders can only be placed if your account has sufficient funds. Once an order is placed, your account funds will be put on hold for the duration of the order. How much and which funds are put on hold depends on the order type and parameters specified. See the Holds details below.<br/><strong>Placing an order will enable price protection. When the price of the limit order is outside the threshold range, the price protection mechanism will be triggered, causing the order to fail.</strong><br/>Please note that the system will frozen the fees from the orders that entered the order book in advance. Read List Fills to learn more.<br/>Before placing an order, please read <a href="https://docs.kucoin.com/#get-symbols-list">Get Symbol List</a> to understand the requirements for the quantity parameters for each trading pair..<br/>See <a href="https://docs.kucoin.com/#place-a-new-order">API</a><br/>
   */
  KucoinPlaceNewOrderResponse placeNewOrder(KucoinPlaceNewOrderRequest request) throws IOException;
  /**
   *  Request via this endpoint to cancel a single order previously placed.<p><i>This interface is only for cancellation requests. The cancellation result needs to be obtained by querying the order status or subscribing to websocket. It is recommended that you DO NOT cancel the order until receiving the Open message, otherwise the order cannot be cancelled successfully. </i></p> <br/>See <a href="https://docs.kucoin.com/#cancel-an-order">API</a><br/>
   */
  KucoinCancelOrderResponse cancelOrder(KucoinCancelOrderRequest request) throws IOException;
  /**
   *  Request via this interface to cancel an order via the clientOid.<br/>See <a href="https://docs.kucoin.com/#cancel-single-order-by-clientoid">API</a><br/>
   */
  KucoinCancelSingleOrderByClientOidResponse cancelSingleOrderByClientOid(KucoinCancelSingleOrderByClientOidRequest request) throws IOException;
  /**
   * Request via this endpoint to cancel all open orders. The response is a list of ids of the canceled orders.<br/>See <a href="https://docs.kucoin.com/#cancel-all-orders">API</a><br/>
   */
  KucoinCancelAllOrdersResponse cancelAllOrders(KucoinCancelAllOrdersRequest request) throws IOException;
  /**
   * Request via this endpoint to get your current order list. Items are paginated and sorted to show the latest first. See the <a href="https://docs.kucoin.com/#pagination">Pagination</a> section for retrieving additional entries after the first page.</p> <br/>See <a href="https://docs.kucoin.com/#list-orders">API</a><br/>
   */
  KucoinListOrdersResponse listOrders(KucoinListOrdersRequest request) throws IOException;
  
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
