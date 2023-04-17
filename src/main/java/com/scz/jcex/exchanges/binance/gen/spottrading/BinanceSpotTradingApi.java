package com.scz.jcex.exchanges.binance.gen.spottrading;

import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceAccountRequest;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceAccountResponse;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceBalanceUpdateUserDataStreamMessage;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceBalanceUpdateUserDataStreamRequest;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceExecutionReportUserDataStreamMessage;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceExecutionReportUserDataStreamRequest;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceListStatusUserDataStreamMessage;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceListStatusUserDataStreamRequest;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamMessage;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamRequest;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceSpotDeleteListenKeyRequest;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceSpotDeleteListenKeyResponse;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceSpotKeepAliveListenKeyRequest;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceSpotKeepAliveListenKeyResponse;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceSpotListenKeyRequest;
import com.scz.jcex.exchanges.binance.gen.spottrading.pojo.BinanceSpotListenKeyResponse;
import com.scz.jcex.netutils.websocket.WebsocketListener;
import java.io.IOException;

/**
 * SpotTrading CEX SpotTrading API</br>
 * Binance spot trading API
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public interface  BinanceSpotTradingApi {
  /**
   * Get current account information.<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#account-information-user_data">API</a>
   */
  BinanceAccountResponse account(BinanceAccountRequest request) throws IOException;
  /**
   * Start a new user data stream. The stream will close after 60 minutes unless a keepalive is sent. If the account has an active listenKey, that listenKey will be returned and its validity will be extended for 60 minutes.<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#listen-key-spot">API</a><br/>Remark: This API is used internally by websocket wrapper, which creates, keeps alive and eventually refreshes listeny key.
   */
  BinanceSpotListenKeyResponse spotListenKey(BinanceSpotListenKeyRequest request) throws IOException;
  /**
   * Keepalive a user data stream to prevent a time out. User data streams will close after 60 minutes. It's recommended to send a ping about every 30 minutes.<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#listen-key-spot">API</a><br/>Remark: This API is used internally by websocket wrapper, which creates, keeps alive and eventually refreshes listeny key.
   */
  BinanceSpotKeepAliveListenKeyResponse spotKeepAliveListenKey(BinanceSpotKeepAliveListenKeyRequest request) throws IOException;
  /**
   * Close out a user data stream..<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#listen-key-spot">API</a><br/>Remark: This API is used internally by websocket wrapper, which creates, keeps alive and eventually refreshes listeny key.
   */
  BinanceSpotDeleteListenKeyResponse spotDeleteListenKey(BinanceSpotDeleteListenKeyRequest request) throws IOException;
  
  /**
   * Subscribe to outboundAccountPositionUserDataStream stream.<br/>
   * User data Stream.<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#user-data-streams">API</a>
   * 
   * @return client subscriptionId to use for unsubscription
   */
  String subscribeOutboundAccountPositionUserDataStream(BinanceOutboundAccountPositionUserDataStreamRequest request, WebsocketListener<BinanceOutboundAccountPositionUserDataStreamMessage> listener);
  
  /**
   * Unsubscribe from outboundAccountPositionUserDataStream stream.
   * 
   * @param subscriptionId ID of subscription returned by #subscribeOutboundAccountPositionUserDataStream()
   */
  boolean unsubscribeOutboundAccountPositionUserDataStream(String subscriptionId);
  
  /**
   * Subscribe to balanceUpdateUserDataStream stream.<br/>
   * User data Stream.<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#user-data-streams">API</a>
   * 
   * @return client subscriptionId to use for unsubscription
   */
  String subscribeBalanceUpdateUserDataStream(BinanceBalanceUpdateUserDataStreamRequest request, WebsocketListener<BinanceBalanceUpdateUserDataStreamMessage> listener);
  
  /**
   * Unsubscribe from balanceUpdateUserDataStream stream.
   * 
   * @param subscriptionId ID of subscription returned by #subscribeBalanceUpdateUserDataStream()
   */
  boolean unsubscribeBalanceUpdateUserDataStream(String subscriptionId);
  
  /**
   * Subscribe to executionReportUserDataStream stream.<br/>
   * User data Stream.<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#user-data-streams">API</a>
   * 
   * @return client subscriptionId to use for unsubscription
   */
  String subscribeExecutionReportUserDataStream(BinanceExecutionReportUserDataStreamRequest request, WebsocketListener<BinanceExecutionReportUserDataStreamMessage> listener);
  
  /**
   * Unsubscribe from executionReportUserDataStream stream.
   * 
   * @param subscriptionId ID of subscription returned by #subscribeExecutionReportUserDataStream()
   */
  boolean unsubscribeExecutionReportUserDataStream(String subscriptionId);
  
  /**
   * Subscribe to listStatusUserDataStream stream.<br/>
   * User data Stream.<br/>See <a href="https://binance-docs.github.io/apidocs/spot/en/#user-data-streams">API</a>
   * 
   * @return client subscriptionId to use for unsubscription
   */
  String subscribeListStatusUserDataStream(BinanceListStatusUserDataStreamRequest request, WebsocketListener<BinanceListStatusUserDataStreamMessage> listener);
  
  /**
   * Unsubscribe from listStatusUserDataStream stream.
   * 
   * @param subscriptionId ID of subscription returned by #subscribeListStatusUserDataStream()
   */
  boolean unsubscribeListStatusUserDataStream(String subscriptionId);
}
