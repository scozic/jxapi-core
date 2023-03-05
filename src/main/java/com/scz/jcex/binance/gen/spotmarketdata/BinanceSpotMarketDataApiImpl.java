package com.scz.jcex.binance.gen.spotmarketdata;

import com.scz.jcex.binance.gen.spotmarketdata.deserializers.BinanceAllMarketTickersStreamMessageDeserializer;
import com.scz.jcex.binance.gen.spotmarketdata.deserializers.BinanceExchangeInformationAllResponseDeserializer;
import com.scz.jcex.binance.gen.spotmarketdata.deserializers.BinanceExchangeInformationResponseDeserializer;
import com.scz.jcex.binance.gen.spotmarketdata.deserializers.BinanceIndividualSymbolTickerStreamsMessageDeserializer;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceAllMarketTickersStreamMessage;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceAllMarketTickersStreamRequest;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationAllRequest;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationAllResponse;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationRequest;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponse;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceIndividualSymbolTickerStreamsMessage;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceIndividualSymbolTickerStreamsRequest;
import com.scz.jcex.binance.net.BinancePublicApiRestEndpointFactory;
import com.scz.jcex.binance.net.BinancePublicWebsocketEndpointFactory;
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
 * Actual implementation of {@link BinanceSpotMarketDataApi}<br/>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class  BinanceSpotMarketDataApiImpl implements BinanceSpotMarketDataApi {
  private static final Logger log = LoggerFactory.getLogger(BinanceSpotMarketDataApiImpl.class);
  
  private final BinancePublicApiRestEndpointFactory restEndpointFactory = new BinancePublicApiRestEndpointFactory();
  
  private final BinancePublicWebsocketEndpointFactory websocketEndpointFactory = new BinancePublicWebsocketEndpointFactory();
  
  
  private final RestEndpoint<BinanceExchangeInformationAllRequest, BinanceExchangeInformationAllResponse> exchangeInformationAllApi;
  
  @Override
  public BinanceExchangeInformationAllResponse exchangeInformationAll(BinanceExchangeInformationAllRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("GET exchangeInformationAll > " + request);
    BinanceExchangeInformationAllResponse response = exchangeInformationAllApi.call(RestRequest.create("https://api.binance.com/api/v3/exchangeInfo", "GET", request));
    if (log.isDebugEnabled())
      log.debug("GET exchangeInformationAll < " + response);
    return response;
  }
  
  private final RestEndpoint<BinanceExchangeInformationRequest, BinanceExchangeInformationResponse> exchangeInformationApi;
  
  @Override
  public BinanceExchangeInformationResponse exchangeInformation(BinanceExchangeInformationRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("GET exchangeInformation > " + request);
    BinanceExchangeInformationResponse response = exchangeInformationApi.call(RestRequest.create("https://api.binance.com/api/v3/exchangeInfo", "GET", request));
    if (log.isDebugEnabled())
      log.debug("GET exchangeInformation < " + response);
    return response;
  }
  
  private final WebsocketEndpoint<BinanceAllMarketTickersStreamRequest, BinanceAllMarketTickersStreamMessage> allMarketTickersStreamWs;
  
  
  @Override
  public String subscribeAllMarketTickersStream(BinanceAllMarketTickersStreamRequest request, WebsocketListener<BinanceAllMarketTickersStreamMessage> listener) {
    if (log.isDebugEnabled())
      log.debug("subscribeAllMarketTickersStream:request:" + request);
    WebsocketSubscribeRequest<BinanceAllMarketTickersStreamRequest> websocketSubscribeRequest = new WebsocketSubscribeRequest<>();
    websocketSubscribeRequest.setMessageTopicMatcher(new DefaultWebsocketMessageTopicMatcher(WebsocketMessageTopicMatcherField.createList("e", "24hrTicker")));
    websocketSubscribeRequest.setParameters(request);
    return allMarketTickersStreamWs.subscribe(websocketSubscribeRequest, listener);
  }
  
  @Override
  public boolean unsubscribeAllMarketTickersStream(String subscriptionId) {
    if (log.isDebugEnabled())
      log.debug("unsubscribeAllMarketTickersStream: subscriptionId:" + subscriptionId);
    return allMarketTickersStreamWs.unsubscribe(subscriptionId);
  }
  
  private final WebsocketEndpoint<BinanceIndividualSymbolTickerStreamsRequest, BinanceIndividualSymbolTickerStreamsMessage> individualSymbolTickerStreamsWs;
  
  
  @Override
  public String subscribeIndividualSymbolTickerStreams(BinanceIndividualSymbolTickerStreamsRequest request, WebsocketListener<BinanceIndividualSymbolTickerStreamsMessage> listener) {
    if (log.isDebugEnabled())
      log.debug("subscribeIndividualSymbolTickerStreams:request:" + request);
    WebsocketSubscribeRequest<BinanceIndividualSymbolTickerStreamsRequest> websocketSubscribeRequest = new WebsocketSubscribeRequest<>();
    websocketSubscribeRequest.setMessageTopicMatcher(new DefaultWebsocketMessageTopicMatcher(WebsocketMessageTopicMatcherField.createList("e", "24hTicker", "s", "" + request.getSymbol() + "")));
    websocketSubscribeRequest.setParameters(request);
    return individualSymbolTickerStreamsWs.subscribe(websocketSubscribeRequest, listener);
  }
  
  @Override
  public boolean unsubscribeIndividualSymbolTickerStreams(String subscriptionId) {
    if (log.isDebugEnabled())
      log.debug("unsubscribeIndividualSymbolTickerStreams: subscriptionId:" + subscriptionId);
    return individualSymbolTickerStreamsWs.unsubscribe(subscriptionId);
  }
  public BinanceSpotMarketDataApiImpl(Properties properties) {
    this.restEndpointFactory.setProperties(properties);
    this.exchangeInformationAllApi = restEndpointFactory.createRestEndpoint(new BinanceExchangeInformationAllResponseDeserializer());
    this.exchangeInformationApi = restEndpointFactory.createRestEndpoint(new BinanceExchangeInformationResponseDeserializer());
    this.allMarketTickersStreamWs = websocketEndpointFactory.createWebsocketEndpoint(new BinanceAllMarketTickersStreamMessageDeserializer());
    this.individualSymbolTickerStreamsWs = websocketEndpointFactory.createWebsocketEndpoint(new BinanceIndividualSymbolTickerStreamsMessageDeserializer());
  }
}
