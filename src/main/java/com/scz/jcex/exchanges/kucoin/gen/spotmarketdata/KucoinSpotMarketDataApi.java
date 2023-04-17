package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata;

import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinAllSymbolsTickerStreamMessage;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinAllSymbolsTickerStreamRequest;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicRequest;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicResponse;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGet24hrStatsRequest;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGet24hrStatsResponse;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetAllTickersRequest;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetAllTickersResponse;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetMarketListRequest;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetMarketListResponse;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetSymbolsListRequest;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetSymbolsListResponse;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetTickerRequest;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetTickerResponse;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinIndividualSymbolTickerStreamsMessage;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinIndividualSymbolTickerStreamsRequest;
import com.scz.jcex.netutils.websocket.WebsocketListener;
import java.io.IOException;

/**
 * SpotMarketData CEX SpotMarketData API</br>
 * Kucoin spot market data API
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public interface  KucoinSpotMarketDataApi {
  /**
   * Request via this endpoint to get a list of available currency pairs for trading. If you want to get the market information of the trading symbol, please use Get All Tickers.<br/>See <a href="https://docs.kucoin.com/#get-symbols-list">API</a>
   */
  KucoinGetSymbolsListResponse getSymbolsList(KucoinGetSymbolsListRequest request) throws IOException;
  /**
   * Request via this endpoint to get Level 1 Market Data. The returned value includes the best bid price and size, the best ask price and size as well as the last traded price and the last traded size.<br/>See <a href="https://docs.kucoin.com/#get-ticker">API</a>
   */
  KucoinGetTickerResponse getTicker(KucoinGetTickerRequest request) throws IOException;
  /**
   * Request market tickers for all the trading pairs in the market (including 24h volume). On the rare occasion that we will change the currency name, if you still want the changed symbol name, you can use the symbolName field instead of the symbol field via 'Get all tickers' endpoint.<br/>See <a href="https://docs.kucoin.com/#get-all-tickers">API</a>
   */
  KucoinGetAllTickersResponse getAllTickers(KucoinGetAllTickersRequest request) throws IOException;
  /**
   * Request via this endpoint to get the statistics of the specified ticker in the last 24 hours.<br/>See <a href="https://docs.kucoin.com/#get-24hr-stats">API</a>
   */
  KucoinGet24hrStatsResponse get24hrStats(KucoinGet24hrStatsRequest request) throws IOException;
  /**
   * Request via this endpoint to get the transaction currency for the entire trading market.<br/>See <a href="https://docs.kucoin.com/#get-market-list">API</a>
   */
  KucoinGetMarketListResponse getMarketList(KucoinGetMarketListRequest request) throws IOException;
  /**
   * You need to apply for one of the two tokens below to create a websocket connection..<br/>See <a href="https://docs.kucoin.com/#apply-connect-token">API</a>
   */
  KucoinApplyConnectTokenPublicResponse applyConnectTokenPublic(KucoinApplyConnectTokenPublicRequest request) throws IOException;
  
  /**
   * Subscribe to AllSymbolsTickerStream stream.<br/>
   * Subscribe to this topic to get the push of all market symbols BBO change. Push frequency: once every 100ms. <br/>See <a href="https://docs.kucoin.com/#all-symbols-ticker">API</a>
   * 
   * @return client subscriptionId to use for unsubscription
   */
  String subscribeAllSymbolsTickerStream(KucoinAllSymbolsTickerStreamRequest request, WebsocketListener<KucoinAllSymbolsTickerStreamMessage> listener);
  
  /**
   * Unsubscribe from AllSymbolsTickerStream stream.
   * 
   * @param subscriptionId ID of subscription returned by #subscribeAllSymbolsTickerStream()
   */
  boolean unsubscribeAllSymbolsTickerStream(String subscriptionId);
  
  /**
   * Subscribe to IndividualSymbolTickerStreams stream.<br/>
   * 24hr rolling window ticker statistics for a single symbol. These are NOT the statistics of the UTC day, but a 24hr rolling window for the previous 24hrs.<br/>See <a href="https://docs.kucoin.com/#symbol-ticker">API</a>
   * 
   * @return client subscriptionId to use for unsubscription
   */
  String subscribeIndividualSymbolTickerStreams(KucoinIndividualSymbolTickerStreamsRequest request, WebsocketListener<KucoinIndividualSymbolTickerStreamsMessage> listener);
  
  /**
   * Unsubscribe from IndividualSymbolTickerStreams stream.
   * 
   * @param subscriptionId ID of subscription returned by #subscribeIndividualSymbolTickerStreams()
   */
  boolean unsubscribeIndividualSymbolTickerStreams(String subscriptionId);
}
