package com.scz.jxapi.exchanges.binance.gen.spottrading;

import com.scz.jxapi.exchanges.binance.gen.spottrading.deserializers.BinanceAccountResponseDeserializer;
import com.scz.jxapi.exchanges.binance.gen.spottrading.deserializers.BinanceBalanceUpdateUserDataStreamMessageDeserializer;
import com.scz.jxapi.exchanges.binance.gen.spottrading.deserializers.BinanceExecutionReportUserDataStreamMessageDeserializer;
import com.scz.jxapi.exchanges.binance.gen.spottrading.deserializers.BinanceListStatusUserDataStreamMessageDeserializer;
import com.scz.jxapi.exchanges.binance.gen.spottrading.deserializers.BinanceOutboundAccountPositionUserDataStreamMessageDeserializer;
import com.scz.jxapi.exchanges.binance.gen.spottrading.deserializers.BinanceSpotDeleteListenKeyResponseDeserializer;
import com.scz.jxapi.exchanges.binance.gen.spottrading.deserializers.BinanceSpotKeepAliveListenKeyResponseDeserializer;
import com.scz.jxapi.exchanges.binance.gen.spottrading.deserializers.BinanceSpotListenKeyResponseDeserializer;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceAccountRequest;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceAccountResponse;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceBalanceUpdateUserDataStreamMessage;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceBalanceUpdateUserDataStreamRequest;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceExecutionReportUserDataStreamMessage;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceExecutionReportUserDataStreamRequest;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceListStatusUserDataStreamMessage;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceListStatusUserDataStreamRequest;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamMessage;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamRequest;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceSpotDeleteListenKeyRequest;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceSpotDeleteListenKeyResponse;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceSpotKeepAliveListenKeyRequest;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceSpotKeepAliveListenKeyResponse;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceSpotListenKeyRequest;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceSpotListenKeyResponse;
import com.scz.jxapi.exchanges.binance.net.BinancePrivateApiRestEndpointFactory;
import com.scz.jxapi.exchanges.binance.net.BinancePrivateSpotApiWebsocketEndpointFactory;
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
 * Actual implementation of {@link BinanceSpotTradingApi}<br/>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class  BinanceSpotTradingApiImpl implements BinanceSpotTradingApi {
  private static final Logger log = LoggerFactory.getLogger(BinanceSpotTradingApiImpl.class);
  
  private final BinancePrivateApiRestEndpointFactory restEndpointFactory = new BinancePrivateApiRestEndpointFactory();
  
  private final BinancePrivateSpotApiWebsocketEndpointFactory websocketEndpointFactory = new BinancePrivateSpotApiWebsocketEndpointFactory();
  
  
  private final RestEndpoint<BinanceAccountRequest, BinanceAccountResponse> accountApi;
  
  @Override
  public BinanceAccountResponse account(BinanceAccountRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("GET account > " + request);
    BinanceAccountResponse response = accountApi.call(RestRequest.create("https://api.binance.com/api/v3/account", "GET", request));
    if (log.isDebugEnabled())
      log.debug("GET account < " + response);
    return response;
  }
  
  private final RestEndpoint<BinanceSpotListenKeyRequest, BinanceSpotListenKeyResponse> spotListenKeyApi;
  
  @Override
  public BinanceSpotListenKeyResponse spotListenKey(BinanceSpotListenKeyRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("POST spotListenKey > " + request);
    BinanceSpotListenKeyResponse response = spotListenKeyApi.call(RestRequest.create("https://api.binance.com/api/v3/userDataStream", "POST", request));
    if (log.isDebugEnabled())
      log.debug("POST spotListenKey < " + response);
    return response;
  }
  
  private final RestEndpoint<BinanceSpotKeepAliveListenKeyRequest, BinanceSpotKeepAliveListenKeyResponse> spotKeepAliveListenKeyApi;
  
  @Override
  public BinanceSpotKeepAliveListenKeyResponse spotKeepAliveListenKey(BinanceSpotKeepAliveListenKeyRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("PUT spotKeepAliveListenKey > " + request);
    BinanceSpotKeepAliveListenKeyResponse response = spotKeepAliveListenKeyApi.call(RestRequest.create("https://api.binance.com/api/v3/userDataStream", "PUT", request));
    if (log.isDebugEnabled())
      log.debug("PUT spotKeepAliveListenKey < " + response);
    return response;
  }
  
  private final RestEndpoint<BinanceSpotDeleteListenKeyRequest, BinanceSpotDeleteListenKeyResponse> spotDeleteListenKeyApi;
  
  @Override
  public BinanceSpotDeleteListenKeyResponse spotDeleteListenKey(BinanceSpotDeleteListenKeyRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("DELETE spotDeleteListenKey > " + request);
    BinanceSpotDeleteListenKeyResponse response = spotDeleteListenKeyApi.call(RestRequest.create("https://api.binance.com/api/v3/userDataStream", "DELETE", request));
    if (log.isDebugEnabled())
      log.debug("DELETE spotDeleteListenKey < " + response);
    return response;
  }
  
  private final WebsocketEndpoint<BinanceOutboundAccountPositionUserDataStreamRequest, BinanceOutboundAccountPositionUserDataStreamMessage> outboundAccountPositionUserDataStreamWs;
  
  
  @Override
  public String subscribeOutboundAccountPositionUserDataStream(BinanceOutboundAccountPositionUserDataStreamRequest request, WebsocketListener<BinanceOutboundAccountPositionUserDataStreamMessage> listener) {
    if (log.isDebugEnabled())
      log.debug("subscribeOutboundAccountPositionUserDataStream:request:" + request);
    WebsocketSubscribeRequest<BinanceOutboundAccountPositionUserDataStreamRequest> websocketSubscribeRequest = new WebsocketSubscribeRequest<>();
    websocketSubscribeRequest.setMessageTopicMatcher(new DefaultWebsocketMessageTopicMatcher(WebsocketMessageTopicMatcherField.createList("e", "outboundAccountPosition")));
    websocketSubscribeRequest.setParameters(request);
    return outboundAccountPositionUserDataStreamWs.subscribe(websocketSubscribeRequest, listener);
  }
  
  @Override
  public boolean unsubscribeOutboundAccountPositionUserDataStream(String subscriptionId) {
    if (log.isDebugEnabled())
      log.debug("unsubscribeOutboundAccountPositionUserDataStream: subscriptionId:" + subscriptionId);
    return outboundAccountPositionUserDataStreamWs.unsubscribe(subscriptionId);
  }
  
  private final WebsocketEndpoint<BinanceBalanceUpdateUserDataStreamRequest, BinanceBalanceUpdateUserDataStreamMessage> balanceUpdateUserDataStreamWs;
  
  
  @Override
  public String subscribeBalanceUpdateUserDataStream(BinanceBalanceUpdateUserDataStreamRequest request, WebsocketListener<BinanceBalanceUpdateUserDataStreamMessage> listener) {
    if (log.isDebugEnabled())
      log.debug("subscribeBalanceUpdateUserDataStream:request:" + request);
    WebsocketSubscribeRequest<BinanceBalanceUpdateUserDataStreamRequest> websocketSubscribeRequest = new WebsocketSubscribeRequest<>();
    websocketSubscribeRequest.setMessageTopicMatcher(new DefaultWebsocketMessageTopicMatcher(WebsocketMessageTopicMatcherField.createList("e", "balanceUpdate")));
    websocketSubscribeRequest.setParameters(request);
    return balanceUpdateUserDataStreamWs.subscribe(websocketSubscribeRequest, listener);
  }
  
  @Override
  public boolean unsubscribeBalanceUpdateUserDataStream(String subscriptionId) {
    if (log.isDebugEnabled())
      log.debug("unsubscribeBalanceUpdateUserDataStream: subscriptionId:" + subscriptionId);
    return balanceUpdateUserDataStreamWs.unsubscribe(subscriptionId);
  }
  
  private final WebsocketEndpoint<BinanceExecutionReportUserDataStreamRequest, BinanceExecutionReportUserDataStreamMessage> executionReportUserDataStreamWs;
  
  
  @Override
  public String subscribeExecutionReportUserDataStream(BinanceExecutionReportUserDataStreamRequest request, WebsocketListener<BinanceExecutionReportUserDataStreamMessage> listener) {
    if (log.isDebugEnabled())
      log.debug("subscribeExecutionReportUserDataStream:request:" + request);
    WebsocketSubscribeRequest<BinanceExecutionReportUserDataStreamRequest> websocketSubscribeRequest = new WebsocketSubscribeRequest<>();
    websocketSubscribeRequest.setMessageTopicMatcher(new DefaultWebsocketMessageTopicMatcher(WebsocketMessageTopicMatcherField.createList("e", "executionReport")));
    websocketSubscribeRequest.setParameters(request);
    return executionReportUserDataStreamWs.subscribe(websocketSubscribeRequest, listener);
  }
  
  @Override
  public boolean unsubscribeExecutionReportUserDataStream(String subscriptionId) {
    if (log.isDebugEnabled())
      log.debug("unsubscribeExecutionReportUserDataStream: subscriptionId:" + subscriptionId);
    return executionReportUserDataStreamWs.unsubscribe(subscriptionId);
  }
  
  private final WebsocketEndpoint<BinanceListStatusUserDataStreamRequest, BinanceListStatusUserDataStreamMessage> listStatusUserDataStreamWs;
  
  
  @Override
  public String subscribeListStatusUserDataStream(BinanceListStatusUserDataStreamRequest request, WebsocketListener<BinanceListStatusUserDataStreamMessage> listener) {
    if (log.isDebugEnabled())
      log.debug("subscribeListStatusUserDataStream:request:" + request);
    WebsocketSubscribeRequest<BinanceListStatusUserDataStreamRequest> websocketSubscribeRequest = new WebsocketSubscribeRequest<>();
    websocketSubscribeRequest.setMessageTopicMatcher(new DefaultWebsocketMessageTopicMatcher(WebsocketMessageTopicMatcherField.createList("e", "listStatus")));
    websocketSubscribeRequest.setParameters(request);
    return listStatusUserDataStreamWs.subscribe(websocketSubscribeRequest, listener);
  }
  
  @Override
  public boolean unsubscribeListStatusUserDataStream(String subscriptionId) {
    if (log.isDebugEnabled())
      log.debug("unsubscribeListStatusUserDataStream: subscriptionId:" + subscriptionId);
    return listStatusUserDataStreamWs.unsubscribe(subscriptionId);
  }
  public BinanceSpotTradingApiImpl(Properties properties) {
    this.restEndpointFactory.setProperties(properties);
    this.websocketEndpointFactory.setProperties(properties);
    this.accountApi = restEndpointFactory.createRestEndpoint(new BinanceAccountResponseDeserializer());
    this.spotListenKeyApi = restEndpointFactory.createRestEndpoint(new BinanceSpotListenKeyResponseDeserializer());
    this.spotKeepAliveListenKeyApi = restEndpointFactory.createRestEndpoint(new BinanceSpotKeepAliveListenKeyResponseDeserializer());
    this.spotDeleteListenKeyApi = restEndpointFactory.createRestEndpoint(new BinanceSpotDeleteListenKeyResponseDeserializer());
    this.outboundAccountPositionUserDataStreamWs = websocketEndpointFactory.createWebsocketEndpoint(new BinanceOutboundAccountPositionUserDataStreamMessageDeserializer());
    this.balanceUpdateUserDataStreamWs = websocketEndpointFactory.createWebsocketEndpoint(new BinanceBalanceUpdateUserDataStreamMessageDeserializer());
    this.executionReportUserDataStreamWs = websocketEndpointFactory.createWebsocketEndpoint(new BinanceExecutionReportUserDataStreamMessageDeserializer());
    this.listStatusUserDataStreamWs = websocketEndpointFactory.createWebsocketEndpoint(new BinanceListStatusUserDataStreamMessageDeserializer());
  }
}
