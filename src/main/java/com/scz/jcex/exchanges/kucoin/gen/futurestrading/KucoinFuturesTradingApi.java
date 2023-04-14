package com.scz.jcex.exchanges.kucoin.gen.futurestrading;

import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinAccountBalanceEventsMessage;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinAccountBalanceEventsRequest;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinCancelAnOrderRequest;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinCancelAnOrderResponse;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetAccountOverviewRequest;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetAccountOverviewResponse;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetOrderListRequest;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetOrderListResponse;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetUntriggeredStopOrderListRequest;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetUntriggeredStopOrderListResponse;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinPlaceAnOrderRequest;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinPlaceAnOrderResponse;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinPositionChangeEventsMessage;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinPositionChangeEventsRequest;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinStopOrderLifecycleEventMessage;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinStopOrderLifecycleEventRequest;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinTradeOrdersMessage;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinTradeOrdersRequest;
import com.scz.jcex.netutils.websocket.WebsocketListener;
import java.io.IOException;

/**
 * FuturesTrading CEX FuturesTrading API</br>
 * Kucoin Futures trading API
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public interface  KucoinFuturesTradingApi {
  /**
   * Get account overview.<br/>See <a href="https://docs.kucoin.com/futures/#get-account-overview">API</a>
   */
  KucoinGetAccountOverviewResponse getAccountOverview(KucoinGetAccountOverviewRequest request) throws IOException;
  /**
   * You can place two types of orders: limit and market. Orders can only be placed if your account has sufficient funds. Once an order is placed, your funds will be put on hold for the duration of the order. The amount of funds on hold depends on the order type and parameters specified. <br/>Please be noted that the system would hold the fees from the orders entered the orderbook in advance. Read Get Fills to learn more.<br/>See <a href="https://docs.kucoin.com/futures/#place-an-order">API</a>
   */
  KucoinPlaceAnOrderResponse placeAnOrder(KucoinPlaceAnOrderRequest request) throws IOException;
  /**
   * Cancel an order (including a stop order).<br/> You will receive success message once the system has received the cancellation request. The cancellation request will be processed by matching engine in sequence. To know if the request has been processed, you may check the order status or update message from the pushes.<br/>The order id is the server-assigned order id, not the specified clientOid.<br/>If the order can not be canceled (already filled or previously canceled, etc), then an error response will indicate the reason in the message field.<br/>See <a href="https://docs.kucoin.com/futures/#cancel-an-order">API</a>
   */
  KucoinCancelAnOrderResponse cancelAnOrder(KucoinCancelAnOrderRequest request) throws IOException;
  /**
   * List your current orders. <br/>See <a href="https://docs.kucoin.com/futures/#get-order-list">API</a>
   */
  KucoinGetOrderListResponse getOrderList(KucoinGetOrderListRequest request) throws IOException;
  /**
   * Get the un-triggered stop orders list. <br/>See <a href="https://docs.kucoin.com/futures/#get-untriggered-stop-order-list">API</a>
   */
  KucoinGetUntriggeredStopOrderListResponse getUntriggeredStopOrderList(KucoinGetUntriggeredStopOrderListRequest request) throws IOException;
  
  /**
   * Subscribe to TradeOrders stream.<br/>
   * Trade Orders websocket stream.<br/>See <a href="https://docs.kucoin.com/futures/#trade-orders">API</a>
   * 
   * @return client subscriptionId to use for unsubscription
   */
  String subscribeTradeOrders(KucoinTradeOrdersRequest request, WebsocketListener<KucoinTradeOrdersMessage> listener);
  
  /**
   * Unsubscribe from TradeOrders stream.
   * 
   * @param subscriptionId ID of subscription returned by #subscribeTradeOrders()
   */
  boolean unsubscribeTradeOrders(String subscriptionId);
  
  /**
   * Subscribe to StopOrderLifecycleEvent stream.<br/>
   * Stop Order Lifecycle Event websocket stream.<br/>See <a href="https://docs.kucoin.com/futures/#stop-order-lifecycle-event">API</a>
   * 
   * @return client subscriptionId to use for unsubscription
   */
  String subscribeStopOrderLifecycleEvent(KucoinStopOrderLifecycleEventRequest request, WebsocketListener<KucoinStopOrderLifecycleEventMessage> listener);
  
  /**
   * Unsubscribe from StopOrderLifecycleEvent stream.
   * 
   * @param subscriptionId ID of subscription returned by #subscribeStopOrderLifecycleEvent()
   */
  boolean unsubscribeStopOrderLifecycleEvent(String subscriptionId);
  
  /**
   * Subscribe to AccountBalanceEvents stream.<br/>
   * Stop Order Lifecycle Event websocket stream.<br/>See <a href="https://docs.kucoin.com/futures/#account-balance-events">API</a>
   * 
   * @return client subscriptionId to use for unsubscription
   */
  String subscribeAccountBalanceEvents(KucoinAccountBalanceEventsRequest request, WebsocketListener<KucoinAccountBalanceEventsMessage> listener);
  
  /**
   * Unsubscribe from AccountBalanceEvents stream.
   * 
   * @param subscriptionId ID of subscription returned by #subscribeAccountBalanceEvents()
   */
  boolean unsubscribeAccountBalanceEvents(String subscriptionId);
  
  /**
   * Subscribe to PositionChangeEvents stream.<br/>
   * Stop Order Lifecycle Event websocket stream.<br/>See <a href="https://docs.kucoin.com/futures/#account-balance-events">API</a>
   * 
   * @return client subscriptionId to use for unsubscription
   */
  String subscribePositionChangeEvents(KucoinPositionChangeEventsRequest request, WebsocketListener<KucoinPositionChangeEventsMessage> listener);
  
  /**
   * Unsubscribe from PositionChangeEvents stream.
   * 
   * @param subscriptionId ID of subscription returned by #subscribePositionChangeEvents()
   */
  boolean unsubscribePositionChangeEvents(String subscriptionId);
}
