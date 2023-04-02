package com.scz.jcex.exchanges.kucoin.gen.spottrading;

import com.scz.jcex.exchanges.kucoin.gen.spottrading.deserializers.KucoinAccountBalanceNoticeMessageDeserializer;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.deserializers.KucoinApplyConnectTokenPrivateResponseDeserializer;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.deserializers.KucoinListAccountsResponseDeserializer;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.deserializers.KucoinPlaceNewOrderResponseDeserializer;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.deserializers.KucoinPrivateOrderChangeV2MessageDeserializer;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinAccountBalanceNoticeMessage;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinAccountBalanceNoticeRequest;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateRequest;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateResponse;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListAccountsRequest;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinListAccountsResponse;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinPlaceNewOrderRequest;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinPlaceNewOrderResponse;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinPrivateOrderChangeV2Message;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinPrivateOrderChangeV2Request;
import com.scz.jcex.exchanges.kucoin.net.KucoinPrivateApiRestEndpointFactory;
import com.scz.jcex.exchanges.kucoin.net.KucoinPrivateWebsocketEndpointFactory;
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
 * Actual implementation of {@link KucoinSpotTradingApi}<br/>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class  KucoinSpotTradingApiImpl implements KucoinSpotTradingApi {
  private static final Logger log = LoggerFactory.getLogger(KucoinSpotTradingApiImpl.class);
  
  private final KucoinPrivateApiRestEndpointFactory restEndpointFactory = new KucoinPrivateApiRestEndpointFactory();
  
  private final KucoinPrivateWebsocketEndpointFactory websocketEndpointFactory = new KucoinPrivateWebsocketEndpointFactory();
  
  
  private final RestEndpoint<KucoinApplyConnectTokenPrivateRequest, KucoinApplyConnectTokenPrivateResponse> applyConnectTokenPrivateApi;
  
  @Override
  public KucoinApplyConnectTokenPrivateResponse applyConnectTokenPrivate(KucoinApplyConnectTokenPrivateRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("POST ApplyConnectTokenPrivate > " + request);
    KucoinApplyConnectTokenPrivateResponse response = applyConnectTokenPrivateApi.call(RestRequest.create("https://api.kucoin.com/api/v1/bullet-public", "POST", request));
    if (log.isDebugEnabled())
      log.debug("POST ApplyConnectTokenPrivate < " + response);
    return response;
  }
  
  private final RestEndpoint<KucoinListAccountsRequest, KucoinListAccountsResponse> listAccountsApi;
  
  @Override
  public KucoinListAccountsResponse listAccounts(KucoinListAccountsRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("GET ListAccounts > " + request);
    KucoinListAccountsResponse response = listAccountsApi.call(RestRequest.create("https://api.kucoin.com/api/v1/accounts", "GET", request));
    if (log.isDebugEnabled())
      log.debug("GET ListAccounts < " + response);
    return response;
  }
  
  private final RestEndpoint<KucoinPlaceNewOrderRequest, KucoinPlaceNewOrderResponse> placeNewOrderApi;
  
  @Override
  public KucoinPlaceNewOrderResponse placeNewOrder(KucoinPlaceNewOrderRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("POST PlaceNewOrder > " + request);
    KucoinPlaceNewOrderResponse response = placeNewOrderApi.call(RestRequest.create("https://api.binance.com/api/v1/orders", "POST", request));
    if (log.isDebugEnabled())
      log.debug("POST PlaceNewOrder < " + response);
    return response;
  }
  
  private final WebsocketEndpoint<KucoinPrivateOrderChangeV2Request, KucoinPrivateOrderChangeV2Message> privateOrderChangeV2Ws;
  
  
  @Override
  public String subscribePrivateOrderChangeV2(KucoinPrivateOrderChangeV2Request request, WebsocketListener<KucoinPrivateOrderChangeV2Message> listener) {
    if (log.isDebugEnabled())
      log.debug("subscribePrivateOrderChangeV2:request:" + request);
    WebsocketSubscribeRequest<KucoinPrivateOrderChangeV2Request> websocketSubscribeRequest = new WebsocketSubscribeRequest<>();
    websocketSubscribeRequest.setMessageTopicMatcher(new DefaultWebsocketMessageTopicMatcher(WebsocketMessageTopicMatcherField.createList("topic", "" + request.getTopic() + "")));
    websocketSubscribeRequest.setParameters(request);
    return privateOrderChangeV2Ws.subscribe(websocketSubscribeRequest, listener);
  }
  
  @Override
  public boolean unsubscribePrivateOrderChangeV2(String subscriptionId) {
    if (log.isDebugEnabled())
      log.debug("unsubscribePrivateOrderChangeV2: subscriptionId:" + subscriptionId);
    return privateOrderChangeV2Ws.unsubscribe(subscriptionId);
  }
  
  private final WebsocketEndpoint<KucoinAccountBalanceNoticeRequest, KucoinAccountBalanceNoticeMessage> accountBalanceNoticeWs;
  
  
  @Override
  public String subscribeAccountBalanceNotice(KucoinAccountBalanceNoticeRequest request, WebsocketListener<KucoinAccountBalanceNoticeMessage> listener) {
    if (log.isDebugEnabled())
      log.debug("subscribeAccountBalanceNotice:request:" + request);
    WebsocketSubscribeRequest<KucoinAccountBalanceNoticeRequest> websocketSubscribeRequest = new WebsocketSubscribeRequest<>();
    websocketSubscribeRequest.setMessageTopicMatcher(new DefaultWebsocketMessageTopicMatcher(WebsocketMessageTopicMatcherField.createList("topic", "" + request.getTopic() + "")));
    websocketSubscribeRequest.setParameters(request);
    return accountBalanceNoticeWs.subscribe(websocketSubscribeRequest, listener);
  }
  
  @Override
  public boolean unsubscribeAccountBalanceNotice(String subscriptionId) {
    if (log.isDebugEnabled())
      log.debug("unsubscribeAccountBalanceNotice: subscriptionId:" + subscriptionId);
    return accountBalanceNoticeWs.unsubscribe(subscriptionId);
  }
  public KucoinSpotTradingApiImpl(Properties properties) {
    this.restEndpointFactory.setProperties(properties);
    this.websocketEndpointFactory.setProperties(properties);
    this.applyConnectTokenPrivateApi = restEndpointFactory.createRestEndpoint(new KucoinApplyConnectTokenPrivateResponseDeserializer());
    this.listAccountsApi = restEndpointFactory.createRestEndpoint(new KucoinListAccountsResponseDeserializer());
    this.placeNewOrderApi = restEndpointFactory.createRestEndpoint(new KucoinPlaceNewOrderResponseDeserializer());
    this.privateOrderChangeV2Ws = websocketEndpointFactory.createWebsocketEndpoint(new KucoinPrivateOrderChangeV2MessageDeserializer());
    this.accountBalanceNoticeWs = websocketEndpointFactory.createWebsocketEndpoint(new KucoinAccountBalanceNoticeMessageDeserializer());
  }
}
