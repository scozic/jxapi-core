package org.jxapi.exchanges.demo.gen.marketdata;

import java.util.List;
import java.util.Map;

import javax.annotation.processing.Generated;
import org.jxapi.exchange.AbstractExchangeApi;
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
import org.jxapi.netutils.rest.HttpRequest;
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
  
  // REST endpoint URLs
  
  /**
   * URL for <i>exchangeInfo</i> REST endpoint.
   * @see DemoExchangeMarketDataApi#exchangeInfo(DemoExchangeMarketDataExchangeInfoRequest)
   */
  protected final String exchangeInfoHttpUrl;
  
  /**
   * URL for <i>tickers</i> REST endpoint.
   * @see DemoExchangeMarketDataApi#tickers()
   */
  protected final String tickersHttpUrl;
  
  /**
   * URL for <i>postRestRequestDataTypeInt</i> REST endpoint.
   * @see DemoExchangeMarketDataApi#postRestRequestDataTypeInt(Integer)
   */
  protected final String postRestRequestDataTypeIntHttpUrl;
  
  /**
   * URL for <i>getRestRequestDataTypePrimitiveWithMsgField</i> REST endpoint.
   * @see DemoExchangeMarketDataApi#getRestRequestDataTypePrimitiveWithMsgField(Integer)
   */
  protected final String getRestRequestDataTypePrimitiveWithMsgFieldHttpUrl;
  
  /**
   * URL for <i>postRestRequestDataTypeIntList</i> REST endpoint.
   * @see DemoExchangeMarketDataApi#postRestRequestDataTypeIntList(List)
   */
  protected final String postRestRequestDataTypeIntListHttpUrl;
  
  /**
   * URL for <i>postRestRequestDataTypeObjectListMap</i> REST endpoint.
   * @see DemoExchangeMarketDataApi#postRestRequestDataTypeObjectListMap(Map)
   */
  protected final String postRestRequestDataTypeObjectListMapHttpUrl;
  
  // Websocket endpoints
  private final WebsocketEndpoint<DemoExchangeMarketDataTickerStreamMessage> tickerStreamWs;
  
  // Request serializers
  private final MessageSerializer<Integer> postRestRequestDataTypeIntRequestSerializer = IntegerJsonValueSerializer.getInstance();
  private final MessageSerializer<List<Integer>> postRestRequestDataTypeIntListRequestSerializer = new ListJsonValueSerializer<>(IntegerJsonValueSerializer.getInstance());
  private final MessageSerializer<Map<String, List<SingleSymbol>>> postRestRequestDataTypeObjectListMapRequestSerializer = new MapJsonValueSerializer<>(new ListJsonValueSerializer<>(new SingleSymbolSerializer()));
  
  // Message deserializers
  private final MessageDeserializer<DemoExchangeMarketDataExchangeInfoResponse> exchangeInfoResponseDeserializer = new DemoExchangeMarketDataExchangeInfoResponseDeserializer();
  private final MessageDeserializer<DemoExchangeMarketDataTickersResponse> tickersResponseDeserializer = new DemoExchangeMarketDataTickersResponseDeserializer();
  private final MessageDeserializer<GenericResponse> postRestRequestDataTypeIntResponseDeserializer = new GenericResponseDeserializer();
  private final MessageDeserializer<GenericResponse> getRestRequestDataTypePrimitiveWithMsgFieldResponseDeserializer = new GenericResponseDeserializer();
  private final MessageDeserializer<GenericResponse> postRestRequestDataTypeIntListResponseDeserializer = new GenericResponseDeserializer();
  private final MessageDeserializer<GenericResponse> postRestRequestDataTypeObjectListMapResponseDeserializer = new GenericResponseDeserializer();
  
  // Constructor
  /**
   * Constructor
   */
  public DemoExchangeMarketDataApiImpl(DemoExchangeExchange exchange) {
    super(ID,
          exchange,
          null,
          "/marketData",
          null);
    createHttpRequestExecutor(null, -1L);
    createHttpRequestInterceptor("org.jxapi.exchanges.demo.net.DemoExchangeHttpRequestInterceptorFactory");
    this.exchangeInfoHttpUrl = EncodingUtil.buildUrl(this.getHttpUrl(), "/exchangeInfo");
    this.tickersHttpUrl = EncodingUtil.buildUrl(this.getHttpUrl(), "/tickers");
    this.postRestRequestDataTypeIntHttpUrl = EncodingUtil.buildUrl(this.getHttpUrl(), "/postInt");
    this.getRestRequestDataTypePrimitiveWithMsgFieldHttpUrl = EncodingUtil.buildUrl(this.getHttpUrl(), "/getIntWithMsgField");
    this.postRestRequestDataTypeIntListHttpUrl = EncodingUtil.buildUrl(this.getHttpUrl(), "/postIntList");
    this.postRestRequestDataTypeObjectListMapHttpUrl = EncodingUtil.buildUrl(this.getHttpUrl(), "/postObjectListMap");
    createWebsocketManager(this.wsUrl, null, "org.jxapi.exchanges.demo.net.DemoExchangeWebsocketHookFactory");
    this.tickerStreamWs = createWebsocketEndpoint(TICKER_STREAM_WS_API, new DemoExchangeMarketDataTickerStreamMessageDeserializer());
  }
  
  // REST endpoint method call implementations
  @Override
  public FutureRestResponse<DemoExchangeMarketDataExchangeInfoResponse> exchangeInfo(DemoExchangeMarketDataExchangeInfoRequest request) {
    String url = new StringBuilder(128).append(exchangeInfoHttpUrl)
      .append(EncodingUtil.createUrlQueryParameters("symbols", JsonUtil.pojoToJsonString(request.getSymbols()))).toString();
    return submit(HttpRequest.create(EXCHANGE_INFO_REST_API, url, HttpMethod.GET, request, null, 0), exchangeInfoResponseDeserializer);
  }
  
  @Override
  public FutureRestResponse<DemoExchangeMarketDataTickersResponse> tickers() {
    return submit(HttpRequest.create(TICKERS_REST_API, tickersHttpUrl, HttpMethod.GET, null, null, 0), tickersResponseDeserializer);
  }
  
  @Override
  public FutureRestResponse<GenericResponse> postRestRequestDataTypeInt(Integer request) {
    return submit(HttpRequest.create(POST_REST_REQUEST_DATA_TYPE_INT_REST_API, postRestRequestDataTypeIntHttpUrl, HttpMethod.POST, request, null, 0, postRestRequestDataTypeIntRequestSerializer.serialize(request)), postRestRequestDataTypeIntResponseDeserializer);
  }
  
  @Override
  public FutureRestResponse<GenericResponse> getRestRequestDataTypePrimitiveWithMsgField(Integer request) {
    String url = new StringBuilder(128).append(getRestRequestDataTypePrimitiveWithMsgFieldHttpUrl)
      .append(EncodingUtil.createUrlQueryParameters("a", request)).toString();
    return submit(HttpRequest.create(GET_REST_REQUEST_DATA_TYPE_PRIMITIVE_WITH_MSG_FIELD_REST_API, url, HttpMethod.GET, request, null, 0), getRestRequestDataTypePrimitiveWithMsgFieldResponseDeserializer);
  }
  
  @Override
  public FutureRestResponse<GenericResponse> postRestRequestDataTypeIntList(List<Integer> request) {
    return submit(HttpRequest.create(POST_REST_REQUEST_DATA_TYPE_INT_LIST_REST_API, postRestRequestDataTypeIntListHttpUrl, HttpMethod.POST, request, null, 0, postRestRequestDataTypeIntListRequestSerializer.serialize(request)), postRestRequestDataTypeIntListResponseDeserializer);
  }
  
  @Override
  public FutureRestResponse<GenericResponse> postRestRequestDataTypeObjectListMap(Map<String, List<SingleSymbol>> request) {
    return submit(HttpRequest.create(POST_REST_REQUEST_DATA_TYPE_OBJECT_LIST_MAP_REST_API, postRestRequestDataTypeObjectListMapHttpUrl, HttpMethod.POST, request, null, 0, postRestRequestDataTypeObjectListMapRequestSerializer.serialize(request)), postRestRequestDataTypeObjectListMapResponseDeserializer);
  }
  
  
  // Websocket endpoint subscribe/unsubscribe methods implementations
  @Override
  public String subscribeTickerStream(DemoExchangeMarketDataTickerStreamRequest request, WebsocketListener<DemoExchangeMarketDataTickerStreamMessage> listener) {
    String topic = EncodingUtil.substituteArguments("${request.symbol}@ticker", "request.symbol", request.getSymbol());
    WebsocketMessageTopicMatcherFactory topicMatcherFactory = WSMTMFUtil.and(List.of(
      WSMTMFUtil.value("t", "ticker"),
      WSMTMFUtil.value("s", EncodingUtil.substituteArguments("${request.symbol}", "request.symbol", request.getSymbol()))));
    WebsocketSubscribeRequest subscribeRequest = WebsocketSubscribeRequest.create(request, topic, topicMatcherFactory);
    return tickerStreamWs.subscribe(subscribeRequest, listener);
  }
  
  @Override
  public boolean unsubscribeTickerStream(String subscriptionId) {
    return tickerStreamWs.unsubscribe(subscriptionId);
  }
  
}
