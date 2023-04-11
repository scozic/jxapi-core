package com.scz.jcex.exchanges.kucoin.gen.futurestrading;

import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinAccountBalanceEventsMessage;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinAccountBalanceEventsRequest;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetAccountOverviewRequest;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetAccountOverviewResponse;
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
