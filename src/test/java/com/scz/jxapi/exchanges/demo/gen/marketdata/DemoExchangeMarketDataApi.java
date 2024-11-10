package com.scz.jxapi.exchanges.demo.gen.marketdata;

import com.scz.jxapi.exchange.ExchangeApi;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoRequest;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponse;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamMessage;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponse;
import com.scz.jxapi.netutils.rest.FutureRestResponse;
import com.scz.jxapi.netutils.websocket.WebsocketListener;

/**
 * DemoExchange MarketData API</br>
 * Demo exchange market data API to retrieve
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public interface DemoExchangeMarketDataApi extends ExchangeApi {
  String ID = "MarketData";
  String EXCHANGE_INFO_REST_API = "exchangeInfo";
  String TICKERS_REST_API = "tickers";
  String TICKER_STREAM_WS_API = "tickerStream";
  /**
   * Fetch market information of symbols that can be traded
   * @return A {@link FutureRestResponse} that will complete when request submitted asynchronously has been processed.@see <a href="https://docs.myexchange.com/api/rest/marketData/exchangeInfo">Reference documentation</a>
   */
  FutureRestResponse<DemoExchangeMarketDataExchangeInfoResponse> exchangeInfo(DemoExchangeMarketDataExchangeInfoRequest request);
  /**
   * Fetch current tickers for all markets
   * @return A {@link FutureRestResponse} that will complete when request submitted asynchronously has been processed
   */
  FutureRestResponse<DemoExchangeMarketDataTickersResponse> tickers();
  
  /**
   * Subscribe to tickerStream stream.<br>
   * <a href="https://docs.myexchange.com/api/ws/marketData/tickerStream">Reference documentation</a>subscribeTickerStream()
   * Subscribe to ticker stream
   * 
   * @return client subscriptionId to use for unsubscription
   */
  String subscribeTickerStream(DemoExchangeMarketDataTickerStreamRequest request, WebsocketListener<DemoExchangeMarketDataTickerStreamMessage> listener);
  
  /**
   * Unsubscribe from tickerStream stream.
   * <a href="https://docs.myexchange.com/api/ws/marketData/tickerStream">Reference documentation</a>subscribeTickerStream()
   * 
   * @param subscriptionId ID of subscription returned by #subscribeTickerStream()
   */
  boolean unsubscribeTickerStream(String subscriptionId);
}
