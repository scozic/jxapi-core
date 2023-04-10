package com.scz.jcex.exchanges.kucoin.gen.futurestrading;

import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetAccountOverviewRequest;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetAccountOverviewResponse;
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
   * Trade Orders.<br/>See <a href="https://docs.kucoin.com/futures/#trade-orders">API</a>
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
}
