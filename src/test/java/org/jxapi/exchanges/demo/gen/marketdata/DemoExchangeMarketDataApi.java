package org.jxapi.exchanges.demo.gen.marketdata;

import java.util.List;
import java.util.Map;

import javax.annotation.processing.Generated;
import org.jxapi.exchange.ExchangeApi;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoRequest;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponse;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamMessage;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponse;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.GenericResponse;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.SingleSymbol;
import org.jxapi.netutils.rest.FutureRestResponse;
import org.jxapi.netutils.websocket.WebsocketListener;

/**
 * DemoExchange MarketData API<br>
 * Demo exchange market data API
 */
@Generated("org.jxapi.generator.java.exchange.api.ExchangeApiInterfaceGenerator")
public interface DemoExchangeMarketDataApi extends ExchangeApi {
  
  /**
   * Name of 'MarketData' API group.
   */
  String ID = "MarketData";
  /**
   * Name of <code>exchangeInfo</code> RestApi.
   */
   String EXCHANGE_INFO_REST_API = "exchangeInfo";
  
  /**
   * Name of <code>tickers</code> RestApi.
   */
   String TICKERS_REST_API = "tickers";
  
  /**
   * Name of <code>postRestRequestDataTypeInt</code> RestApi.
   */
   String POST_REST_REQUEST_DATA_TYPE_INT_REST_API = "postRestRequestDataTypeInt";
  
  /**
   * Name of <code>getRestRequestDataTypePrimitiveWithMsgField</code> RestApi.
   */
   String GET_REST_REQUEST_DATA_TYPE_PRIMITIVE_WITH_MSG_FIELD_REST_API = "getRestRequestDataTypePrimitiveWithMsgField";
  
  /**
   * Name of <code>postRestRequestDataTypeIntList</code> RestApi.
   */
   String POST_REST_REQUEST_DATA_TYPE_INT_LIST_REST_API = "postRestRequestDataTypeIntList";
  
  /**
   * Name of <code>postRestRequestDataTypeObjectListMap</code> RestApi.
   */
   String POST_REST_REQUEST_DATA_TYPE_OBJECT_LIST_MAP_REST_API = "postRestRequestDataTypeObjectListMap";
  /**
   * Name of <code>tickerStream</code> WsApi.
   */
   String TICKER_STREAM_WS_API = "tickerStream";
  
  /**
   * Fetch market information of symbols that can be traded
   * @param request request
   * @return A {@link FutureRestResponse} that will complete when request submitted asynchronously has been processed.
   * @see <a href="https://docs.myexchange.com/api/rest/marketData/exchangeInfo">Reference documentation</a>
   */
  FutureRestResponse<DemoExchangeMarketDataExchangeInfoResponse> exchangeInfo(DemoExchangeMarketDataExchangeInfoRequest request);
  
  /**
   * Fetch current tickers for all markets
   * @return A {@link FutureRestResponse} that will complete when request submitted asynchronously has been processed
   */
  FutureRestResponse<DemoExchangeMarketDataTickersResponse> tickers();
  
  /**
   * A sample REST endpoint using INT response data type
   * @param request request
   * @return A {@link FutureRestResponse} that will complete when request submitted asynchronously has been processed
   */
  FutureRestResponse<GenericResponse> postRestRequestDataTypeInt(Integer request);
  
  /**
   * A sample REST endpoint using GET (hence url query params) primitive request type, with 'msgField' property defined. That msgField value should be used as query param argument name
   * @param age request
   * @return A {@link FutureRestResponse} that will complete when request submitted asynchronously has been processed
   */
  FutureRestResponse<GenericResponse> getRestRequestDataTypePrimitiveWithMsgField(Integer age);
  
  /**
   * A sample REST endpoint using INT_LIST request data type
   * @param request request
   * @return A {@link FutureRestResponse} that will complete when request submitted asynchronously has been processed
   */
  FutureRestResponse<GenericResponse> postRestRequestDataTypeIntList(List<Integer> request);
  
  /**
   * A sample REST endpoint using OBJECT_LIST_MAP request data type
   * @param request request
   * @return A {@link FutureRestResponse} that will complete when request submitted asynchronously has been processed
   */
  FutureRestResponse<GenericResponse> postRestRequestDataTypeObjectListMap(Map<String, List<SingleSymbol>> request);
  
  /**
   * Subscribe to tickerStream stream.<br>
   * Subscribe to ticker stream
   * 
   * @param request request
   * @param listener listener that will receive incoming messages
   * @return client subscriptionId to use for unsubscription using {@link #unsubscribeTickerStream(String)}
   * @see <a href="https://docs.myexchange.com/api/ws/marketData/tickerStream">Reference documentation</a>
   */
  String subscribeTickerStream(DemoExchangeMarketDataTickerStreamRequest request, WebsocketListener<DemoExchangeMarketDataTickerStreamMessage> listener);
  
  /**
   * Unsubscribe from tickerStream stream.
   * 
   * @param subscriptionId client subscription ID
   * @return <code>true</code> if given <code>subscriptionId</code> was found.
   * @see #subscribeTickerStream(DemoExchangeMarketDataTickerStreamRequest, WebsocketListener)
   */
  boolean unsubscribeTickerStream(String subscriptionId);
}
