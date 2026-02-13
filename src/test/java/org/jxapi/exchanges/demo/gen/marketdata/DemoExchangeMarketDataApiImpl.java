package org.jxapi.exchanges.demo.gen.marketdata;

import java.util.List;
import java.util.Map;

import javax.annotation.processing.Generated;
import org.jxapi.exchange.AbstractExchangeApi;
import org.jxapi.exchange.ExchangeObserver;
import org.jxapi.exchanges.demo.gen.DemoExchangeExchange;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoRequest;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponse;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamMessage;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponse;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.GenericResponse;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.SingleSymbol;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.deserializers.DemoExchangeMarketDataExchangeInfoResponseDeserializer;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.deserializers.DemoExchangeMarketDataTickerStreamMessageDeserializer;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.deserializers.DemoExchangeMarketDataTickersResponseDeserializer;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.deserializers.GenericResponseDeserializer;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.serializers.SingleSymbolSerializer;
import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.netutils.rest.FutureRestResponse;
import org.jxapi.netutils.rest.HttpMethod;
import org.jxapi.netutils.rest.HttpRequestUrlParamsSerializer;
import org.jxapi.netutils.rest.RestEndpoint;
import org.jxapi.netutils.serialization.MessageSerializer;
import org.jxapi.netutils.serialization.json.IntegerJsonValueSerializer;
import org.jxapi.netutils.serialization.json.ListJsonValueSerializer;
import org.jxapi.netutils.serialization.json.MapJsonValueSerializer;
import org.jxapi.netutils.websocket.WebsocketEndpoint;
import org.jxapi.netutils.websocket.WebsocketListener;
import org.jxapi.netutils.websocket.WebsocketSubscribeRequest;
import org.jxapi.netutils.websocket.multiplexing.WSMTMFUtil;
import org.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatcherFactory;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.JsonUtil;

/**
 * Actual implementation of {@link DemoExchangeMarketDataApi}<br>
 */
@Generated("org.jxapi.generator.java.exchange.api.ExchangeApiInterfaceImplementationGenerator")
public class DemoExchangeMarketDataApiImpl extends AbstractExchangeApi implements DemoExchangeMarketDataApi {
  
  // REST endpoints URL parameter serializers
  private static final HttpRequestUrlParamsSerializer<DemoExchangeMarketDataExchangeInfoRequest> EXCHANGE_INFO_REST_API_URL_PARAMS_SERIALIZER = (request, url) -> new StringBuilder(128).append(url)
    .append(EncodingUtil.createUrlQueryParameters("symbols", JsonUtil.pojoToJsonString(request.getSymbols()))).toString();
  private static final HttpRequestUrlParamsSerializer<Integer> POST_REST_REQUEST_DATA_TYPE_INT_REST_API_URL_PARAMS_SERIALIZER = HttpRequestUrlParamsSerializer.noParams();
  private static final HttpRequestUrlParamsSerializer<Integer> GET_REST_REQUEST_DATA_TYPE_PRIMITIVE_WITH_MSG_FIELD_REST_API_URL_PARAMS_SERIALIZER = (request, url) -> new StringBuilder(128).append(url)
    .append(EncodingUtil.createUrlQueryParameters("a", request)).toString();
  private static final HttpRequestUrlParamsSerializer<List<Integer>> POST_REST_REQUEST_DATA_TYPE_INT_LIST_REST_API_URL_PARAMS_SERIALIZER = HttpRequestUrlParamsSerializer.noParams();
  private static final HttpRequestUrlParamsSerializer<Map<String, List<SingleSymbol>>> POST_REST_REQUEST_DATA_TYPE_OBJECT_LIST_MAP_REST_API_URL_PARAMS_SERIALIZER = HttpRequestUrlParamsSerializer.noParams();
  
  // Request serializers
  private static final MessageSerializer<Integer> POST_REST_REQUEST_DATA_TYPE_INT_REST_API_REQUEST_SERIALIZER = IntegerJsonValueSerializer.getInstance();
  private static final MessageSerializer<List<Integer>> POST_REST_REQUEST_DATA_TYPE_INT_LIST_REST_API_REQUEST_SERIALIZER = new ListJsonValueSerializer<>(IntegerJsonValueSerializer.getInstance());
  private static final MessageSerializer<Map<String, List<SingleSymbol>>> POST_REST_REQUEST_DATA_TYPE_OBJECT_LIST_MAP_REST_API_REQUEST_SERIALIZER = new MapJsonValueSerializer<>(new ListJsonValueSerializer<>(new SingleSymbolSerializer()));
  
  // Message deserializers
  private static final MessageDeserializer<DemoExchangeMarketDataExchangeInfoResponse> EXCHANGE_INFO_REST_API_RESPONSE_DESERIALIZER = new DemoExchangeMarketDataExchangeInfoResponseDeserializer();
  private static final MessageDeserializer<DemoExchangeMarketDataTickersResponse> TICKERS_REST_API_RESPONSE_DESERIALIZER = new DemoExchangeMarketDataTickersResponseDeserializer();
  private static final MessageDeserializer<GenericResponse> POST_REST_REQUEST_DATA_TYPE_INT_REST_API_RESPONSE_DESERIALIZER = new GenericResponseDeserializer();
  private static final MessageDeserializer<GenericResponse> GET_REST_REQUEST_DATA_TYPE_PRIMITIVE_WITH_MSG_FIELD_REST_API_RESPONSE_DESERIALIZER = new GenericResponseDeserializer();
  private static final MessageDeserializer<GenericResponse> POST_REST_REQUEST_DATA_TYPE_INT_LIST_REST_API_RESPONSE_DESERIALIZER = new GenericResponseDeserializer();
  private static final MessageDeserializer<GenericResponse> POST_REST_REQUEST_DATA_TYPE_OBJECT_LIST_MAP_REST_API_RESPONSE_DESERIALIZER = new GenericResponseDeserializer();
  private static final MessageDeserializer<DemoExchangeMarketDataTickerStreamMessage> TICKER_STREAM_WS_API_MESSAGE_DESERIALIZER = new DemoExchangeMarketDataTickerStreamMessageDeserializer();
  
  // REST endpoints
  private final RestEndpoint<DemoExchangeMarketDataExchangeInfoRequest, DemoExchangeMarketDataExchangeInfoResponse> exchangeInfoRestEndpoint;
  private final RestEndpoint<Void, DemoExchangeMarketDataTickersResponse> tickersRestEndpoint;
  private final RestEndpoint<Integer, GenericResponse> postRestRequestDataTypeIntRestEndpoint;
  private final RestEndpoint<Integer, GenericResponse> getRestRequestDataTypePrimitiveWithMsgFieldRestEndpoint;
  private final RestEndpoint<List<Integer>, GenericResponse> postRestRequestDataTypeIntListRestEndpoint;
  private final RestEndpoint<Map<String, List<SingleSymbol>>, GenericResponse> postRestRequestDataTypeObjectListMapRestEndpoint;
  
  // Websocket endpoints
  private final WebsocketEndpoint<DemoExchangeMarketDataTickerStreamMessage> tickerStreamWsEndpoint;
  
  // Constructor
  /**
   * Constructor
   * 
   * @param exchange the exchange instance
   * @param exchangeObserver the exchange API observer to dispatch endpoint events to
   * 
   */
  public DemoExchangeMarketDataApiImpl(DemoExchangeExchange exchange, ExchangeObserver exchangeObserver) {
    super(ID,
          exchange,
          exchangeObserver,
          "/marketData");
    this.exchangeInfoRestEndpoint = createRestEndpoint(EXCHANGE_INFO_REST_API, DemoExchangeExchange.HTTP_DEFAULT_HTTP_CLIENT);
    this.exchangeInfoRestEndpoint.setHttpMethod(HttpMethod.GET);
    this.exchangeInfoRestEndpoint.setUrl(EncodingUtil.buildUrl(this.httpUrl, "/exchangeInfo"));
    this.exchangeInfoRestEndpoint.setUrlParamsSerializer(EXCHANGE_INFO_REST_API_URL_PARAMS_SERIALIZER);
    this.exchangeInfoRestEndpoint.setDeserializer(EXCHANGE_INFO_REST_API_RESPONSE_DESERIALIZER);
    
    this.tickersRestEndpoint = createRestEndpoint(TICKERS_REST_API, DemoExchangeExchange.HTTP_DEFAULT_HTTP_CLIENT);
    this.tickersRestEndpoint.setHttpMethod(HttpMethod.GET);
    this.tickersRestEndpoint.setUrl(EncodingUtil.buildUrl(this.httpUrl, "/tickers"));
    this.tickersRestEndpoint.setUrlParamsSerializer(HttpRequestUrlParamsSerializer.noParams());
    this.tickersRestEndpoint.setDeserializer(TICKERS_REST_API_RESPONSE_DESERIALIZER);
    
    this.postRestRequestDataTypeIntRestEndpoint = createRestEndpoint(POST_REST_REQUEST_DATA_TYPE_INT_REST_API, DemoExchangeExchange.HTTP_DEFAULT_HTTP_CLIENT);
    this.postRestRequestDataTypeIntRestEndpoint.setHttpMethod(HttpMethod.POST);
    this.postRestRequestDataTypeIntRestEndpoint.setUrl(EncodingUtil.buildUrl(this.httpUrl, "/postInt"));
    this.postRestRequestDataTypeIntRestEndpoint.setUrlParamsSerializer(POST_REST_REQUEST_DATA_TYPE_INT_REST_API_URL_PARAMS_SERIALIZER);
    this.postRestRequestDataTypeIntRestEndpoint.setSerializer(POST_REST_REQUEST_DATA_TYPE_INT_REST_API_REQUEST_SERIALIZER);
    this.postRestRequestDataTypeIntRestEndpoint.setDeserializer(POST_REST_REQUEST_DATA_TYPE_INT_REST_API_RESPONSE_DESERIALIZER);
    
    this.getRestRequestDataTypePrimitiveWithMsgFieldRestEndpoint = createRestEndpoint(GET_REST_REQUEST_DATA_TYPE_PRIMITIVE_WITH_MSG_FIELD_REST_API, DemoExchangeExchange.HTTP_DEFAULT_HTTP_CLIENT);
    this.getRestRequestDataTypePrimitiveWithMsgFieldRestEndpoint.setHttpMethod(HttpMethod.GET);
    this.getRestRequestDataTypePrimitiveWithMsgFieldRestEndpoint.setUrl(EncodingUtil.buildUrl(this.httpUrl, "/getIntWithMsgField"));
    this.getRestRequestDataTypePrimitiveWithMsgFieldRestEndpoint.setUrlParamsSerializer(GET_REST_REQUEST_DATA_TYPE_PRIMITIVE_WITH_MSG_FIELD_REST_API_URL_PARAMS_SERIALIZER);
    this.getRestRequestDataTypePrimitiveWithMsgFieldRestEndpoint.setDeserializer(GET_REST_REQUEST_DATA_TYPE_PRIMITIVE_WITH_MSG_FIELD_REST_API_RESPONSE_DESERIALIZER);
    
    this.postRestRequestDataTypeIntListRestEndpoint = createRestEndpoint(POST_REST_REQUEST_DATA_TYPE_INT_LIST_REST_API, DemoExchangeExchange.HTTP_DEFAULT_HTTP_CLIENT);
    this.postRestRequestDataTypeIntListRestEndpoint.setHttpMethod(HttpMethod.POST);
    this.postRestRequestDataTypeIntListRestEndpoint.setUrl(EncodingUtil.buildUrl(this.httpUrl, "/postIntList"));
    this.postRestRequestDataTypeIntListRestEndpoint.setUrlParamsSerializer(POST_REST_REQUEST_DATA_TYPE_INT_LIST_REST_API_URL_PARAMS_SERIALIZER);
    this.postRestRequestDataTypeIntListRestEndpoint.setSerializer(POST_REST_REQUEST_DATA_TYPE_INT_LIST_REST_API_REQUEST_SERIALIZER);
    this.postRestRequestDataTypeIntListRestEndpoint.setDeserializer(POST_REST_REQUEST_DATA_TYPE_INT_LIST_REST_API_RESPONSE_DESERIALIZER);
    
    this.postRestRequestDataTypeObjectListMapRestEndpoint = createRestEndpoint(POST_REST_REQUEST_DATA_TYPE_OBJECT_LIST_MAP_REST_API, DemoExchangeExchange.HTTP_DEFAULT_HTTP_CLIENT);
    this.postRestRequestDataTypeObjectListMapRestEndpoint.setHttpMethod(HttpMethod.POST);
    this.postRestRequestDataTypeObjectListMapRestEndpoint.setUrl(EncodingUtil.buildUrl(this.httpUrl, "/postObjectListMap"));
    this.postRestRequestDataTypeObjectListMapRestEndpoint.setUrlParamsSerializer(POST_REST_REQUEST_DATA_TYPE_OBJECT_LIST_MAP_REST_API_URL_PARAMS_SERIALIZER);
    this.postRestRequestDataTypeObjectListMapRestEndpoint.setSerializer(POST_REST_REQUEST_DATA_TYPE_OBJECT_LIST_MAP_REST_API_REQUEST_SERIALIZER);
    this.postRestRequestDataTypeObjectListMapRestEndpoint.setDeserializer(POST_REST_REQUEST_DATA_TYPE_OBJECT_LIST_MAP_REST_API_RESPONSE_DESERIALIZER);
    
    this.tickerStreamWsEndpoint = createWebsocketEndpoint(TICKER_STREAM_WS_API, DemoExchangeExchange.WS_DEFAULT_WEBSOCKET_CLIENT, TICKER_STREAM_WS_API_MESSAGE_DESERIALIZER);
  }
  
  // REST endpoint method call implementations
  @Override
  public FutureRestResponse<DemoExchangeMarketDataExchangeInfoResponse> exchangeInfo(DemoExchangeMarketDataExchangeInfoRequest request) {
    return exchangeInfoRestEndpoint.submit(request);
  }
  
  @Override
  public FutureRestResponse<DemoExchangeMarketDataTickersResponse> tickers() {
    return tickersRestEndpoint.submit(null);
  }
  
  @Override
  public FutureRestResponse<GenericResponse> postRestRequestDataTypeInt(Integer request) {
    return postRestRequestDataTypeIntRestEndpoint.submit(request);
  }
  
  @Override
  public FutureRestResponse<GenericResponse> getRestRequestDataTypePrimitiveWithMsgField(Integer request) {
    return getRestRequestDataTypePrimitiveWithMsgFieldRestEndpoint.submit(request);
  }
  
  @Override
  public FutureRestResponse<GenericResponse> postRestRequestDataTypeIntList(List<Integer> request) {
    return postRestRequestDataTypeIntListRestEndpoint.submit(request);
  }
  
  @Override
  public FutureRestResponse<GenericResponse> postRestRequestDataTypeObjectListMap(Map<String, List<SingleSymbol>> request) {
    return postRestRequestDataTypeObjectListMapRestEndpoint.submit(request);
  }
  
  
  // Websocket endpoint subscribe/unsubscribe methods implementations
  @Override
  public String subscribeTickerStream(DemoExchangeMarketDataTickerStreamRequest request, WebsocketListener<DemoExchangeMarketDataTickerStreamMessage> listener) {
    String topic = EncodingUtil.substituteArguments("${request.symbol}@ticker", "request.symbol", request.getSymbol());
    WebsocketMessageTopicMatcherFactory topicMatcherFactory = WSMTMFUtil.and(List.of(
      WSMTMFUtil.value("t", "ticker"),
      WSMTMFUtil.value("s", EncodingUtil.substituteArguments("${request.symbol}", "request.symbol", request.getSymbol()))));
    WebsocketSubscribeRequest subscribeRequest = WebsocketSubscribeRequest.create(request, topic, topicMatcherFactory);
    return tickerStreamWsEndpoint.subscribe(subscribeRequest, listener);
  }
  
  @Override
  public boolean unsubscribeTickerStream(String subscriptionId) {
    return tickerStreamWsEndpoint.unsubscribe(subscriptionId);
  }
  
}
