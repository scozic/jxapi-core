package com.scz.jcex.exchanges.kucoin.gen.spotmarketdata;

import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.deserializers.KucoinAllSymbolsTickerStreamMessageDeserializer;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.deserializers.KucoinApplyConnectTokenPublicResponseDeserializer;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.deserializers.KucoinGet24hrStatsResponseDeserializer;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.deserializers.KucoinGetAllTickersResponseDeserializer;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.deserializers.KucoinGetSymbolsListResponseDeserializer;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.deserializers.KucoinGetTickerResponseDeserializer;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.deserializers.KucoinIndividualSymbolTickerStreamsMessageDeserializer;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinAllSymbolsTickerStreamMessage;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinAllSymbolsTickerStreamRequest;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicRequest;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicResponse;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGet24hrStatsRequest;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGet24hrStatsResponse;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetAllTickersRequest;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetAllTickersResponse;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetSymbolsListRequest;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetSymbolsListResponse;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetTickerRequest;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinGetTickerResponse;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinIndividualSymbolTickerStreamsMessage;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinIndividualSymbolTickerStreamsRequest;
import com.scz.jcex.exchanges.kucoin.net.KucoinPublicApiRestEndpointFactory;
import com.scz.jcex.exchanges.kucoin.net.KucoinPublicWebsocketEndpointFactory;
import com.scz.jcex.netutils.rest.RestEndpoint;
import com.scz.jcex.netutils.rest.RestRequest;
import com.scz.jcex.netutils.websocket.DefaultWebsocketMessageTopicMatcher;
import com.scz.jcex.netutils.websocket.WebsocketEndpoint;
import com.scz.jcex.netutils.websocket.WebsocketListener;
import com.scz.jcex.netutils.websocket.WebsocketMessageTopicMatcherField;
import com.scz.jcex.netutils.websocket.WebsocketSubscribeRequest;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Actual implementation of {@link KucoinSpotMarketDataApi}<br/>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class  KucoinSpotMarketDataApiImpl implements KucoinSpotMarketDataApi {
  private static final Logger log = LoggerFactory.getLogger(KucoinSpotMarketDataApiImpl.class);
  
  private final KucoinPublicApiRestEndpointFactory restEndpointFactory = new KucoinPublicApiRestEndpointFactory();
  
  private final KucoinPublicWebsocketEndpointFactory websocketEndpointFactory = new KucoinPublicWebsocketEndpointFactory();
  
  
  private final RestEndpoint<KucoinGetSymbolsListRequest, KucoinGetSymbolsListResponse> getSymbolsListApi;
  
  @Override
  public KucoinGetSymbolsListResponse getSymbolsList(KucoinGetSymbolsListRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("GET getSymbolsList > " + request);
    KucoinGetSymbolsListResponse response = getSymbolsListApi.call(RestRequest.create("https://api.kucoin.com/api/v2/symbols", "GET", request));
    if (log.isDebugEnabled())
      log.debug("GET getSymbolsList < " + response);
    return response;
  }
  
  private final RestEndpoint<KucoinGetTickerRequest, KucoinGetTickerResponse> getTickerApi;
  
  @Override
  public KucoinGetTickerResponse getTicker(KucoinGetTickerRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("GET getTicker > " + request);
    KucoinGetTickerResponse response = getTickerApi.call(RestRequest.create("https://api.kucoin.com/api/v1/market/orderbook/level1", "GET", request));
    if (log.isDebugEnabled())
      log.debug("GET getTicker < " + response);
    return response;
  }
  
  private final RestEndpoint<KucoinGetAllTickersRequest, KucoinGetAllTickersResponse> getAllTickersApi;
  
  @Override
  public KucoinGetAllTickersResponse getAllTickers(KucoinGetAllTickersRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("GET getAllTickers > " + request);
    KucoinGetAllTickersResponse response = getAllTickersApi.call(RestRequest.create("https://api.kucoin.com/api/v1/market/allTickers", "GET", request));
    if (log.isDebugEnabled())
      log.debug("GET getAllTickers < " + response);
    return response;
  }
  
  private final RestEndpoint<KucoinGet24hrStatsRequest, KucoinGet24hrStatsResponse> get24hrStatsApi;
  
  @Override
  public KucoinGet24hrStatsResponse get24hrStats(KucoinGet24hrStatsRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("GET get24hrStats > " + request);
    KucoinGet24hrStatsResponse response = get24hrStatsApi.call(RestRequest.create("https://api.kucoin.com/api/v1/market/stats", "GET", request));
    if (log.isDebugEnabled())
      log.debug("GET get24hrStats < " + response);
    return response;
  }
  
  private final RestEndpoint<KucoinApplyConnectTokenPublicRequest, KucoinApplyConnectTokenPublicResponse> applyConnectTokenPublicApi;
  
  @Override
  public KucoinApplyConnectTokenPublicResponse applyConnectTokenPublic(KucoinApplyConnectTokenPublicRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("POST ApplyConnectTokenPublic > " + request);
    KucoinApplyConnectTokenPublicResponse response = applyConnectTokenPublicApi.call(RestRequest.create("https://api.kucoin.com/api/v1/bullet-public", "POST", request));
    if (log.isDebugEnabled())
      log.debug("POST ApplyConnectTokenPublic < " + response);
    return response;
  }
  
  private final WebsocketEndpoint<KucoinAllSymbolsTickerStreamRequest, KucoinAllSymbolsTickerStreamMessage> allSymbolsTickerStreamWs;
  
  
  @Override
  public String subscribeAllSymbolsTickerStream(KucoinAllSymbolsTickerStreamRequest request, WebsocketListener<KucoinAllSymbolsTickerStreamMessage> listener) {
    if (log.isDebugEnabled())
      log.debug("subscribeAllSymbolsTickerStream:request:" + request);
    WebsocketSubscribeRequest<KucoinAllSymbolsTickerStreamRequest> websocketSubscribeRequest = new WebsocketSubscribeRequest<>();
    websocketSubscribeRequest.setMessageTopicMatcher(new DefaultWebsocketMessageTopicMatcher(WebsocketMessageTopicMatcherField.createList("topic", "" + request.getTopic() + "")));
    websocketSubscribeRequest.setParameters(request);
    return allSymbolsTickerStreamWs.subscribe(websocketSubscribeRequest, listener);
  }
  
  @Override
  public boolean unsubscribeAllSymbolsTickerStream(String subscriptionId) {
    if (log.isDebugEnabled())
      log.debug("unsubscribeAllSymbolsTickerStream: subscriptionId:" + subscriptionId);
    return allSymbolsTickerStreamWs.unsubscribe(subscriptionId);
  }
  
  private final WebsocketEndpoint<KucoinIndividualSymbolTickerStreamsRequest, KucoinIndividualSymbolTickerStreamsMessage> individualSymbolTickerStreamsWs;
  
  
  @Override
  public String subscribeIndividualSymbolTickerStreams(KucoinIndividualSymbolTickerStreamsRequest request, WebsocketListener<KucoinIndividualSymbolTickerStreamsMessage> listener) {
    if (log.isDebugEnabled())
      log.debug("subscribeIndividualSymbolTickerStreams:request:" + request);
    WebsocketSubscribeRequest<KucoinIndividualSymbolTickerStreamsRequest> websocketSubscribeRequest = new WebsocketSubscribeRequest<>();
    websocketSubscribeRequest.setMessageTopicMatcher(new DefaultWebsocketMessageTopicMatcher(WebsocketMessageTopicMatcherField.createList("topic", "" + request.getTopic() + "")));
    websocketSubscribeRequest.setParameters(request);
    return individualSymbolTickerStreamsWs.subscribe(websocketSubscribeRequest, listener);
  }
  
  @Override
  public boolean unsubscribeIndividualSymbolTickerStreams(String subscriptionId) {
    if (log.isDebugEnabled())
      log.debug("unsubscribeIndividualSymbolTickerStreams: subscriptionId:" + subscriptionId);
    return individualSymbolTickerStreamsWs.unsubscribe(subscriptionId);
  }
  public KucoinSpotMarketDataApiImpl(Properties properties) {
    this.restEndpointFactory.setProperties(properties);
    this.websocketEndpointFactory.setProperties(properties);
    this.getSymbolsListApi = restEndpointFactory.createRestEndpoint(new KucoinGetSymbolsListResponseDeserializer());
    this.getTickerApi = restEndpointFactory.createRestEndpoint(new KucoinGetTickerResponseDeserializer());
    this.getAllTickersApi = restEndpointFactory.createRestEndpoint(new KucoinGetAllTickersResponseDeserializer());
    this.get24hrStatsApi = restEndpointFactory.createRestEndpoint(new KucoinGet24hrStatsResponseDeserializer());
    this.applyConnectTokenPublicApi = restEndpointFactory.createRestEndpoint(new KucoinApplyConnectTokenPublicResponseDeserializer());
    this.allSymbolsTickerStreamWs = websocketEndpointFactory.createWebsocketEndpoint(new KucoinAllSymbolsTickerStreamMessageDeserializer());
    this.individualSymbolTickerStreamsWs = websocketEndpointFactory.createWebsocketEndpoint(new KucoinIndividualSymbolTickerStreamsMessageDeserializer());
  }
}
