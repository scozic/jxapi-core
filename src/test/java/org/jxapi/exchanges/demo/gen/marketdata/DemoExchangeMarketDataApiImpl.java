package org.jxapi.exchanges.demo.gen.marketdata;

import java.util.List;
import java.util.Map;

import javax.annotation.processing.Generated;
import org.jxapi.exchange.AbstractExchangeApi;
import org.jxapi.exchanges.demo.gen.DemoExchangeExchange;
import org.jxapi.exchanges.demo.gen.DemoExchangeExchangeImpl;
import org.jxapi.exchanges.demo.gen.marketdata.deserializers.DemoExchangeMarketDataExchangeInfoResponseDeserializer;
import org.jxapi.exchanges.demo.gen.marketdata.deserializers.DemoExchangeMarketDataTickerStreamMessageDeserializer;
import org.jxapi.exchanges.demo.gen.marketdata.deserializers.DemoExchangeMarketDataTickersResponseDeserializer;
import org.jxapi.exchanges.demo.gen.marketdata.deserializers.GenericResponseDeserializer;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoRequest;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponse;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamMessage;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponse;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.GenericResponse;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.SingleSymbol;
import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.netutils.rest.FutureRestResponse;
import org.jxapi.netutils.rest.HttpMethod;
import org.jxapi.netutils.rest.HttpRequest;
import org.jxapi.netutils.websocket.WebsocketEndpoint;
import org.jxapi.netutils.websocket.WebsocketListener;
import org.jxapi.netutils.websocket.WebsocketSubscribeRequest;
import org.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatcherFactory;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.JsonUtil;

/**
 * Actual implementation of {@link DemoExchangeMarketDataApi}<br>
 */
@Generated("org.jxapi.generator.java.exchange.api.ExchangeApiInterfaceImplementationGenerator")
public class DemoExchangeMarketDataApiImpl extends AbstractExchangeApi implements DemoExchangeMarketDataApi {
  
  
  /**
   * Base URL for <i>DemoExchange</i> exchange <i>MarketData</i> API REST endpoints
   */
  public static final String HTTP_URL = DemoExchangeExchangeImpl.HTTP_URL + "/marketData";
  
  /**
   * Base URL for <i>DemoExchange</i> exchange <i>MarketData</i> API Websocket endpoints
   */
  public static final String WEBSOCKET_URL = DemoExchangeExchangeImpl.WEBSOCKET_URL;
  
  // REST endpoint URLs
  
  /**
   * URL for <i>exchangeInfo</i> REST endpoint.
   * @see DemoExchangeMarketDataApi#exchangeInfo(DemoExchangeMarketDataExchangeInfoRequest)
   */
  public static final String EXCHANGE_INFO_URL = HTTP_URL + "/exchangeInfo";
  
  /**
   * URL for <i>tickers</i> REST endpoint.
   * @see DemoExchangeMarketDataApi#tickers()
   */
  public static final String TICKERS_URL = HTTP_URL + "/tickers";
  
  /**
   * URL for <i>postRestRequestDataTypeInt</i> REST endpoint.
   * @see DemoExchangeMarketDataApi#postRestRequestDataTypeInt(Integer)
   */
  public static final String POST_REST_REQUEST_DATA_TYPE_INT_URL = HTTP_URL + "/postInt";
  
  /**
   * URL for <i>getRestRequestDataTypePrimitiveWithMsgField</i> REST endpoint.
   * @see DemoExchangeMarketDataApi#getRestRequestDataTypePrimitiveWithMsgField(Integer)
   */
  public static final String GET_REST_REQUEST_DATA_TYPE_PRIMITIVE_WITH_MSG_FIELD_URL = HTTP_URL + "/getIntWithMsgField";
  
  /**
   * URL for <i>postRestRequestDataTypeIntList</i> REST endpoint.
   * @see DemoExchangeMarketDataApi#postRestRequestDataTypeIntList(List)
   */
  public static final String POST_REST_REQUEST_DATA_TYPE_INT_LIST_URL = HTTP_URL + "/postIntList";
  
  /**
   * URL for <i>postRestRequestDataTypeObjectListMap</i> REST endpoint.
   * @see DemoExchangeMarketDataApi#postRestRequestDataTypeObjectListMap(Map)
   */
  public static final String POST_REST_REQUEST_DATA_TYPE_OBJECT_LIST_MAP_URL = HTTP_URL + "/postObjectListMap";
  
  // Websocket endpoints
  private final WebsocketEndpoint<DemoExchangeMarketDataTickerStreamMessage> tickerStreamWs;
  
  // Message deserializers
  private final MessageDeserializer<DemoExchangeMarketDataExchangeInfoResponse> exchangeInfoResponseDeserializer = new DemoExchangeMarketDataExchangeInfoResponseDeserializer();
  private final MessageDeserializer<DemoExchangeMarketDataTickersResponse> tickersResponseDeserializer = new DemoExchangeMarketDataTickersResponseDeserializer();
  private final MessageDeserializer<GenericResponse> postRestRequestDataTypeIntResponseDeserializer = new GenericResponseDeserializer();
  private final MessageDeserializer<GenericResponse> getRestRequestDataTypePrimitiveWithMsgFieldResponseDeserializer = new GenericResponseDeserializer();
  private final MessageDeserializer<GenericResponse> postRestRequestDataTypeIntListResponseDeserializer = new GenericResponseDeserializer();
  private final MessageDeserializer<GenericResponse> postRestRequestDataTypeObjectListMapResponseDeserializer = new GenericResponseDeserializer();
  
  // Constructor
  public DemoExchangeMarketDataApiImpl(DemoExchangeExchange exchange) {
    super(ID, exchange);
    createHttpRequestExecutor(null, -1L);
    createHttpRequestInterceptor("org.jxapi.exchanges.demo.net.DemoExchangeHttpRequestInterceptorFactory");
    createWebsocketManager(WEBSOCKET_URL, null, "org.jxapi.exchanges.demo.net.DemoExchangeWebsocketHookFactory");
    this.tickerStreamWs = createWebsocketEndpoint(TICKER_STREAM_WS_API, new DemoExchangeMarketDataTickerStreamMessageDeserializer());
  }
  
  // REST endpoint method call implementations
  @Override
  public FutureRestResponse<DemoExchangeMarketDataExchangeInfoResponse> exchangeInfo(DemoExchangeMarketDataExchangeInfoRequest request) {
    String urlParameters = EncodingUtil.createUrlQueryParameters("symbols", JsonUtil.pojoToJsonString(request.getSymbols()));
    return submit(HttpRequest.create(EXCHANGE_INFO_REST_API, EXCHANGE_INFO_URL + urlParameters, HttpMethod.GET, request, null, 0), exchangeInfoResponseDeserializer);
  }
  
  @Override
  public FutureRestResponse<DemoExchangeMarketDataTickersResponse> tickers() {
    return submit(HttpRequest.create(TICKERS_REST_API, TICKERS_URL, HttpMethod.GET, null, null, 0), tickersResponseDeserializer);
  }
  
  @Override
  public FutureRestResponse<GenericResponse> postRestRequestDataTypeInt(Integer request) {
    return submit(HttpRequest.create(POST_REST_REQUEST_DATA_TYPE_INT_REST_API, POST_REST_REQUEST_DATA_TYPE_INT_URL, HttpMethod.POST, request, null, 0), postRestRequestDataTypeIntResponseDeserializer);
  }
  
  @Override
  public FutureRestResponse<GenericResponse> getRestRequestDataTypePrimitiveWithMsgField(Integer request) {
    String urlParameters = EncodingUtil.createUrlQueryParameters("a", request);
    return submit(HttpRequest.create(GET_REST_REQUEST_DATA_TYPE_PRIMITIVE_WITH_MSG_FIELD_REST_API, GET_REST_REQUEST_DATA_TYPE_PRIMITIVE_WITH_MSG_FIELD_URL + urlParameters, HttpMethod.GET, request, null, 0), getRestRequestDataTypePrimitiveWithMsgFieldResponseDeserializer);
  }
  
  @Override
  public FutureRestResponse<GenericResponse> postRestRequestDataTypeIntList(List<Integer> request) {
    return submit(HttpRequest.create(POST_REST_REQUEST_DATA_TYPE_INT_LIST_REST_API, POST_REST_REQUEST_DATA_TYPE_INT_LIST_URL, HttpMethod.POST, request, null, 0), postRestRequestDataTypeIntListResponseDeserializer);
  }
  
  @Override
  public FutureRestResponse<GenericResponse> postRestRequestDataTypeObjectListMap(Map<String, List<SingleSymbol>> request) {
    return submit(HttpRequest.create(POST_REST_REQUEST_DATA_TYPE_OBJECT_LIST_MAP_REST_API, POST_REST_REQUEST_DATA_TYPE_OBJECT_LIST_MAP_URL, HttpMethod.POST, request, null, 0), postRestRequestDataTypeObjectListMapResponseDeserializer);
  }
  
  
  // Websocket endpoint subscribe/unsubscribe methods implementations
  @Override
  public String subscribeTickerStream(DemoExchangeMarketDataTickerStreamRequest request, WebsocketListener<DemoExchangeMarketDataTickerStreamMessage> listener) {
    String topic = EncodingUtil.substituteArguments("${symbol}@ticker", "symbol", request.getSymbol());
    WebsocketSubscribeRequest subscribeRequest = WebsocketSubscribeRequest.create(request, topic, WebsocketMessageTopicMatcherFactory.create("t", "ticker", "s", "" + request.getSymbol()));
    return tickerStreamWs.subscribe(subscribeRequest, listener);
  }
  
  @Override
  public boolean unsubscribeTickerStream(String subscriptionId) {
    return tickerStreamWs.unsubscribe(subscriptionId);
  }
  
}
