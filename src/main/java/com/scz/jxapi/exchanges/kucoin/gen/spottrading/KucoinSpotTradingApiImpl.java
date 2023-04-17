package com.scz.jxapi.exchanges.kucoin.gen.spottrading;

import com.scz.jxapi.exchanges.kucoin.gen.spottrading.deserializers.KucoinAccountBalanceNoticeMessageDeserializer;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.deserializers.KucoinApplyConnectTokenPrivateResponseDeserializer;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.deserializers.KucoinCancelAllOrdersResponseDeserializer;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.deserializers.KucoinCancelOrderResponseDeserializer;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.deserializers.KucoinCancelSingleOrderByClientOidResponseDeserializer;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.deserializers.KucoinListAccountsResponseDeserializer;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.deserializers.KucoinListOrdersResponseDeserializer;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.deserializers.KucoinPlaceNewOrderResponseDeserializer;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.deserializers.KucoinPrivateOrderChangeV2MessageDeserializer;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinAccountBalanceNoticeMessage;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinAccountBalanceNoticeRequest;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateRequest;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateResponse;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelAllOrdersRequest;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelAllOrdersResponse;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelOrderRequest;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelOrderResponse;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelSingleOrderByClientOidRequest;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinCancelSingleOrderByClientOidResponse;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinListAccountsRequest;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinListAccountsResponse;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersRequest;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinListOrdersResponse;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinPlaceNewOrderRequest;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinPlaceNewOrderResponse;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinPrivateOrderChangeV2Message;
import com.scz.jxapi.exchanges.kucoin.gen.spottrading.pojo.KucoinPrivateOrderChangeV2Request;
import com.scz.jxapi.exchanges.kucoin.net.KucoinPrivateApiRestEndpointFactory;
import com.scz.jxapi.exchanges.kucoin.net.KucoinPrivateWebsocketEndpointFactory;
import com.scz.jxapi.netutils.rest.RestEndpoint;
import com.scz.jxapi.netutils.rest.RestRequest;
import com.scz.jxapi.netutils.websocket.DefaultWebsocketMessageTopicMatcher;
import com.scz.jxapi.netutils.websocket.WebsocketEndpoint;
import com.scz.jxapi.netutils.websocket.WebsocketListener;
import com.scz.jxapi.netutils.websocket.WebsocketMessageTopicMatcherField;
import com.scz.jxapi.netutils.websocket.WebsocketSubscribeRequest;

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
    KucoinPlaceNewOrderResponse response = placeNewOrderApi.call(RestRequest.create("https://api.kucoin.com/api/v1/orders", "POST", request));
    if (log.isDebugEnabled())
      log.debug("POST PlaceNewOrder < " + response);
    return response;
  }
  
  private final RestEndpoint<KucoinCancelOrderRequest, KucoinCancelOrderResponse> cancelOrderApi;
  
  @Override
  public KucoinCancelOrderResponse cancelOrder(KucoinCancelOrderRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("DELETE CancelOrder > " + request);
    KucoinCancelOrderResponse response = cancelOrderApi.call(RestRequest.create("https://api.kucoin.com/api/v1/orders", "DELETE", request));
    if (log.isDebugEnabled())
      log.debug("DELETE CancelOrder < " + response);
    return response;
  }
  
  private final RestEndpoint<KucoinCancelSingleOrderByClientOidRequest, KucoinCancelSingleOrderByClientOidResponse> cancelSingleOrderByClientOidApi;
  
  @Override
  public KucoinCancelSingleOrderByClientOidResponse cancelSingleOrderByClientOid(KucoinCancelSingleOrderByClientOidRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("DELETE CancelSingleOrderByClientOid > " + request);
    KucoinCancelSingleOrderByClientOidResponse response = cancelSingleOrderByClientOidApi.call(RestRequest.create("https://api.kucoin.com/api/v1/orders/client-order", "DELETE", request));
    if (log.isDebugEnabled())
      log.debug("DELETE CancelSingleOrderByClientOid < " + response);
    return response;
  }
  
  private final RestEndpoint<KucoinCancelAllOrdersRequest, KucoinCancelAllOrdersResponse> cancelAllOrdersApi;
  
  @Override
  public KucoinCancelAllOrdersResponse cancelAllOrders(KucoinCancelAllOrdersRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("DELETE CancelAllOrders > " + request);
    KucoinCancelAllOrdersResponse response = cancelAllOrdersApi.call(RestRequest.create("https://api.kucoin.com/api/v1/orders", "DELETE", request));
    if (log.isDebugEnabled())
      log.debug("DELETE CancelAllOrders < " + response);
    return response;
  }
  
  private final RestEndpoint<KucoinListOrdersRequest, KucoinListOrdersResponse> listOrdersApi;
  
  @Override
  public KucoinListOrdersResponse listOrders(KucoinListOrdersRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("GET ListOrders > " + request);
    KucoinListOrdersResponse response = listOrdersApi.call(RestRequest.create("https://api.kucoin.com/api/v1/orders", "GET", request));
    if (log.isDebugEnabled())
      log.debug("GET ListOrders < " + response);
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
    this.cancelOrderApi = restEndpointFactory.createRestEndpoint(new KucoinCancelOrderResponseDeserializer());
    this.cancelSingleOrderByClientOidApi = restEndpointFactory.createRestEndpoint(new KucoinCancelSingleOrderByClientOidResponseDeserializer());
    this.cancelAllOrdersApi = restEndpointFactory.createRestEndpoint(new KucoinCancelAllOrdersResponseDeserializer());
    this.listOrdersApi = restEndpointFactory.createRestEndpoint(new KucoinListOrdersResponseDeserializer());
    this.privateOrderChangeV2Ws = websocketEndpointFactory.createWebsocketEndpoint(new KucoinPrivateOrderChangeV2MessageDeserializer());
    this.accountBalanceNoticeWs = websocketEndpointFactory.createWebsocketEndpoint(new KucoinAccountBalanceNoticeMessageDeserializer());
  }
}
