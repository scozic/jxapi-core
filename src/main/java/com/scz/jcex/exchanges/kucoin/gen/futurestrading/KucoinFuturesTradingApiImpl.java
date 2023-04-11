package com.scz.jcex.exchanges.kucoin.gen.futurestrading;

import com.scz.jcex.exchanges.kucoin.gen.futurestrading.deserializers.KucoinAccountBalanceEventsMessageDeserializer;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.deserializers.KucoinGetAccountOverviewResponseDeserializer;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.deserializers.KucoinPositionChangeEventsMessageDeserializer;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.deserializers.KucoinStopOrderLifecycleEventMessageDeserializer;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.deserializers.KucoinTradeOrdersMessageDeserializer;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinAccountBalanceEventsMessage;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinAccountBalanceEventsRequest;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetAccountOverviewRequest;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinGetAccountOverviewResponse;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinPositionChangeEventsMessage;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinPositionChangeEventsRequest;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinStopOrderLifecycleEventMessage;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinStopOrderLifecycleEventRequest;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinTradeOrdersMessage;
import com.scz.jcex.exchanges.kucoin.gen.futurestrading.pojo.KucoinTradeOrdersRequest;
import com.scz.jcex.exchanges.kucoin.net.KucoinFuturesPrivateWebsocketEndpointFactory;
import com.scz.jcex.exchanges.kucoin.net.KucoinPrivateApiRestEndpointFactory;
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
 * Actual implementation of {@link KucoinFuturesTradingApi}<br/>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class  KucoinFuturesTradingApiImpl implements KucoinFuturesTradingApi {
  private static final Logger log = LoggerFactory.getLogger(KucoinFuturesTradingApiImpl.class);
  
  private final KucoinPrivateApiRestEndpointFactory restEndpointFactory = new KucoinPrivateApiRestEndpointFactory();
  
  private final KucoinFuturesPrivateWebsocketEndpointFactory websocketEndpointFactory = new KucoinFuturesPrivateWebsocketEndpointFactory();
  
  
  private final RestEndpoint<KucoinGetAccountOverviewRequest, KucoinGetAccountOverviewResponse> getAccountOverviewApi;
  
  @Override
  public KucoinGetAccountOverviewResponse getAccountOverview(KucoinGetAccountOverviewRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("GET GetAccountOverview > " + request);
    KucoinGetAccountOverviewResponse response = getAccountOverviewApi.call(RestRequest.create("https://api-futures.kucoin.com/api/v1/account-overview", "GET", request));
    if (log.isDebugEnabled())
      log.debug("GET GetAccountOverview < " + response);
    return response;
  }
  
  private final WebsocketEndpoint<KucoinTradeOrdersRequest, KucoinTradeOrdersMessage> tradeOrdersWs;
  
  
  @Override
  public String subscribeTradeOrders(KucoinTradeOrdersRequest request, WebsocketListener<KucoinTradeOrdersMessage> listener) {
    if (log.isDebugEnabled())
      log.debug("subscribeTradeOrders:request:" + request);
    WebsocketSubscribeRequest<KucoinTradeOrdersRequest> websocketSubscribeRequest = new WebsocketSubscribeRequest<>();
    websocketSubscribeRequest.setMessageTopicMatcher(new DefaultWebsocketMessageTopicMatcher(WebsocketMessageTopicMatcherField.createList("topic", "" + request.getTopic() + "")));
    websocketSubscribeRequest.setParameters(request);
    return tradeOrdersWs.subscribe(websocketSubscribeRequest, listener);
  }
  
  @Override
  public boolean unsubscribeTradeOrders(String subscriptionId) {
    if (log.isDebugEnabled())
      log.debug("unsubscribeTradeOrders: subscriptionId:" + subscriptionId);
    return tradeOrdersWs.unsubscribe(subscriptionId);
  }
  
  private final WebsocketEndpoint<KucoinStopOrderLifecycleEventRequest, KucoinStopOrderLifecycleEventMessage> stopOrderLifecycleEventWs;
  
  
  @Override
  public String subscribeStopOrderLifecycleEvent(KucoinStopOrderLifecycleEventRequest request, WebsocketListener<KucoinStopOrderLifecycleEventMessage> listener) {
    if (log.isDebugEnabled())
      log.debug("subscribeStopOrderLifecycleEvent:request:" + request);
    WebsocketSubscribeRequest<KucoinStopOrderLifecycleEventRequest> websocketSubscribeRequest = new WebsocketSubscribeRequest<>();
    websocketSubscribeRequest.setMessageTopicMatcher(new DefaultWebsocketMessageTopicMatcher(WebsocketMessageTopicMatcherField.createList("topic", "" + request.getTopic() + "")));
    websocketSubscribeRequest.setParameters(request);
    return stopOrderLifecycleEventWs.subscribe(websocketSubscribeRequest, listener);
  }
  
  @Override
  public boolean unsubscribeStopOrderLifecycleEvent(String subscriptionId) {
    if (log.isDebugEnabled())
      log.debug("unsubscribeStopOrderLifecycleEvent: subscriptionId:" + subscriptionId);
    return stopOrderLifecycleEventWs.unsubscribe(subscriptionId);
  }
  
  private final WebsocketEndpoint<KucoinAccountBalanceEventsRequest, KucoinAccountBalanceEventsMessage> accountBalanceEventsWs;
  
  
  @Override
  public String subscribeAccountBalanceEvents(KucoinAccountBalanceEventsRequest request, WebsocketListener<KucoinAccountBalanceEventsMessage> listener) {
    if (log.isDebugEnabled())
      log.debug("subscribeAccountBalanceEvents:request:" + request);
    WebsocketSubscribeRequest<KucoinAccountBalanceEventsRequest> websocketSubscribeRequest = new WebsocketSubscribeRequest<>();
    websocketSubscribeRequest.setMessageTopicMatcher(new DefaultWebsocketMessageTopicMatcher(WebsocketMessageTopicMatcherField.createList("topic", "" + request.getTopic() + "")));
    websocketSubscribeRequest.setParameters(request);
    return accountBalanceEventsWs.subscribe(websocketSubscribeRequest, listener);
  }
  
  @Override
  public boolean unsubscribeAccountBalanceEvents(String subscriptionId) {
    if (log.isDebugEnabled())
      log.debug("unsubscribeAccountBalanceEvents: subscriptionId:" + subscriptionId);
    return accountBalanceEventsWs.unsubscribe(subscriptionId);
  }
  
  private final WebsocketEndpoint<KucoinPositionChangeEventsRequest, KucoinPositionChangeEventsMessage> positionChangeEventsWs;
  
  
  @Override
  public String subscribePositionChangeEvents(KucoinPositionChangeEventsRequest request, WebsocketListener<KucoinPositionChangeEventsMessage> listener) {
    if (log.isDebugEnabled())
      log.debug("subscribePositionChangeEvents:request:" + request);
    WebsocketSubscribeRequest<KucoinPositionChangeEventsRequest> websocketSubscribeRequest = new WebsocketSubscribeRequest<>();
    websocketSubscribeRequest.setMessageTopicMatcher(new DefaultWebsocketMessageTopicMatcher(WebsocketMessageTopicMatcherField.createList("topic", "" + request.getTopic() + "")));
    websocketSubscribeRequest.setParameters(request);
    return positionChangeEventsWs.subscribe(websocketSubscribeRequest, listener);
  }
  
  @Override
  public boolean unsubscribePositionChangeEvents(String subscriptionId) {
    if (log.isDebugEnabled())
      log.debug("unsubscribePositionChangeEvents: subscriptionId:" + subscriptionId);
    return positionChangeEventsWs.unsubscribe(subscriptionId);
  }
  public KucoinFuturesTradingApiImpl(Properties properties) {
    this.restEndpointFactory.setProperties(properties);
    this.websocketEndpointFactory.setProperties(properties);
    this.getAccountOverviewApi = restEndpointFactory.createRestEndpoint(new KucoinGetAccountOverviewResponseDeserializer());
    this.tradeOrdersWs = websocketEndpointFactory.createWebsocketEndpoint(new KucoinTradeOrdersMessageDeserializer());
    this.stopOrderLifecycleEventWs = websocketEndpointFactory.createWebsocketEndpoint(new KucoinStopOrderLifecycleEventMessageDeserializer());
    this.accountBalanceEventsWs = websocketEndpointFactory.createWebsocketEndpoint(new KucoinAccountBalanceEventsMessageDeserializer());
    this.positionChangeEventsWs = websocketEndpointFactory.createWebsocketEndpoint(new KucoinPositionChangeEventsMessageDeserializer());
  }
}
