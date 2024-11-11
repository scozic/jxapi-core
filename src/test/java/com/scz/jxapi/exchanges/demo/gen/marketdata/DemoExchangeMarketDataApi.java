package com.scz.jxapi.exchanges.demo.gen.marketdata;

import java.util.List;

import com.scz.jxapi.exchange.ExchangeApi;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoRequest;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponse;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamMessage;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponse;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.GenericResponse;
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
  String POST_REST_REQUEST_DATA_TYPE_INT_REST_API = "postRestRequestDataTypeInt";
  String GET_REST_REQUEST_DATA_TYPE_PRIMITIVE_WITH_MSG_FIELD_REST_API = "getRestRequestDataTypePrimitiveWithMsgField";
  String POST_REST_REQUEST_DATA_TYPE_INT_LIST_REST_API = "postRestRequestDataTypeIntList";
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
   * A sample REST endpoint using INT response data type
   * @return A {@link FutureRestResponse} that will complete when request submitted asynchronously has been processed
   */
  FutureRestResponse<GenericResponse> postRestRequestDataTypeInt(Integer request);
  /**
   * A sample REST endpoint using GET (hence url query params) primitive request type, with 'msgField' property defined. That msgField value should be used as query param argument name
   * @return A {@link FutureRestResponse} that will complete when request submitted asynchronously has been processed
   */
  FutureRestResponse<GenericResponse> getRestRequestDataTypePrimitiveWithMsgField(Integer age);
  /**
   * A sample REST endpoint using INT_LIST request data type
   * @return A {@link FutureRestResponse} that will complete when request submitted asynchronously has been processed
   */
  FutureRestResponse<GenericResponse> postRestRequestDataTypeIntList(List<Integer> request);
  
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
