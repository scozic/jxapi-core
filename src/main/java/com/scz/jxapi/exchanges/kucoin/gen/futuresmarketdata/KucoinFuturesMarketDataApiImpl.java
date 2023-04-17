package com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata;

import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.deserializers.KucoinGetOpenContractListResponseDeserializer;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.deserializers.KucoinGetRealTimeTickerResponseDeserializer;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.deserializers.KucoinRealTimeSymbolTickerV2MessageDeserializer;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetOpenContractListRequest;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetOpenContractListResponse;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetRealTimeTickerRequest;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinGetRealTimeTickerResponse;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinRealTimeSymbolTickerV2Message;
import com.scz.jxapi.exchanges.kucoin.gen.futuresmarketdata.pojo.KucoinRealTimeSymbolTickerV2Request;
import com.scz.jxapi.exchanges.kucoin.net.KucoinFuturesPublicWebsocketEndpointFactory;
import com.scz.jxapi.exchanges.kucoin.net.KucoinPublicApiRestEndpointFactory;
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
 * Actual implementation of {@link KucoinFuturesMarketDataApi}<br/>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class  KucoinFuturesMarketDataApiImpl implements KucoinFuturesMarketDataApi {
  private static final Logger log = LoggerFactory.getLogger(KucoinFuturesMarketDataApiImpl.class);
  
  private final KucoinPublicApiRestEndpointFactory restEndpointFactory = new KucoinPublicApiRestEndpointFactory();
  
  private final KucoinFuturesPublicWebsocketEndpointFactory websocketEndpointFactory = new KucoinFuturesPublicWebsocketEndpointFactory();
  
  
  private final RestEndpoint<KucoinGetOpenContractListRequest, KucoinGetOpenContractListResponse> getOpenContractListApi;
  
  @Override
  public KucoinGetOpenContractListResponse getOpenContractList(KucoinGetOpenContractListRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("GET GetOpenContractList > " + request);
    KucoinGetOpenContractListResponse response = getOpenContractListApi.call(RestRequest.create("https://api-futures.kucoin.com/api/v1/contracts/active", "GET", request));
    if (log.isDebugEnabled())
      log.debug("GET GetOpenContractList < " + response);
    return response;
  }
  
  private final RestEndpoint<KucoinGetRealTimeTickerRequest, KucoinGetRealTimeTickerResponse> getRealTimeTickerApi;
  
  @Override
  public KucoinGetRealTimeTickerResponse getRealTimeTicker(KucoinGetRealTimeTickerRequest request) throws IOException {
    if (log.isDebugEnabled())
      log.debug("GET GetRealTimeTicker > " + request);
    KucoinGetRealTimeTickerResponse response = getRealTimeTickerApi.call(RestRequest.create("https://api-futures.kucoin.com/api/v1/ticker", "GET", request));
    if (log.isDebugEnabled())
      log.debug("GET GetRealTimeTicker < " + response);
    return response;
  }
  
  private final WebsocketEndpoint<KucoinRealTimeSymbolTickerV2Request, KucoinRealTimeSymbolTickerV2Message> realTimeSymbolTickerV2Ws;
  
  
  @Override
  public String subscribeRealTimeSymbolTickerV2(KucoinRealTimeSymbolTickerV2Request request, WebsocketListener<KucoinRealTimeSymbolTickerV2Message> listener) {
    if (log.isDebugEnabled())
      log.debug("subscribeRealTimeSymbolTickerV2:request:" + request);
    WebsocketSubscribeRequest<KucoinRealTimeSymbolTickerV2Request> websocketSubscribeRequest = new WebsocketSubscribeRequest<>();
    websocketSubscribeRequest.setMessageTopicMatcher(new DefaultWebsocketMessageTopicMatcher(WebsocketMessageTopicMatcherField.createList("topic", "" + request.getTopic() + "")));
    websocketSubscribeRequest.setParameters(request);
    return realTimeSymbolTickerV2Ws.subscribe(websocketSubscribeRequest, listener);
  }
  
  @Override
  public boolean unsubscribeRealTimeSymbolTickerV2(String subscriptionId) {
    if (log.isDebugEnabled())
      log.debug("unsubscribeRealTimeSymbolTickerV2: subscriptionId:" + subscriptionId);
    return realTimeSymbolTickerV2Ws.unsubscribe(subscriptionId);
  }
  public KucoinFuturesMarketDataApiImpl(Properties properties) {
    this.restEndpointFactory.setProperties(properties);
    this.websocketEndpointFactory.setProperties(properties);
    this.getOpenContractListApi = restEndpointFactory.createRestEndpoint(new KucoinGetOpenContractListResponseDeserializer());
    this.getRealTimeTickerApi = restEndpointFactory.createRestEndpoint(new KucoinGetRealTimeTickerResponseDeserializer());
    this.realTimeSymbolTickerV2Ws = websocketEndpointFactory.createWebsocketEndpoint(new KucoinRealTimeSymbolTickerV2MessageDeserializer());
  }
}
