package com.scz.jxapi.exchanges.demo.gen.marketdata;

import java.util.Properties;

import com.scz.jxapi.exchange.AbstractExchangeApi;
import com.scz.jxapi.exchanges.demo.gen.DemoExchangeExchange;
import com.scz.jxapi.exchanges.demo.gen.DemoExchangeExchangeImpl;
import com.scz.jxapi.exchanges.demo.gen.marketdata.deserializers.DemoExchangeMarketDataExchangeInfoResponseDeserializer;
import com.scz.jxapi.exchanges.demo.gen.marketdata.deserializers.DemoExchangeMarketDataTickerStreamMessageDeserializer;
import com.scz.jxapi.exchanges.demo.gen.marketdata.deserializers.DemoExchangeMarketDataTickersResponseDeserializer;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoRequest;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataExchangeInfoResponse;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamMessage;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickersResponse;
import com.scz.jxapi.netutils.deserialization.MessageDeserializer;
import com.scz.jxapi.netutils.rest.FutureRestResponse;
import com.scz.jxapi.netutils.rest.HttpMethod;
import com.scz.jxapi.netutils.rest.HttpRequest;
import com.scz.jxapi.netutils.websocket.WebsocketEndpoint;
import com.scz.jxapi.netutils.websocket.WebsocketListener;
import com.scz.jxapi.netutils.websocket.WebsocketSubscribeRequest;
import com.scz.jxapi.util.EncodingUtil;
import com.scz.jxapi.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Actual implementation of {@link DemoExchangeMarketDataApi}<br>
 * <br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>
 */
public class DemoExchangeMarketDataApiImpl extends AbstractExchangeApi implements DemoExchangeMarketDataApi {
  
  private static final Logger log = LoggerFactory.getLogger(DemoExchangeMarketDataApiImpl.class);
  
  
  public static final String HTTP_URL = DemoExchangeExchangeImpl.HTTP_URL + "/marketData";
  
  public static final String EXCHANGE_INFO_URL = HTTP_URL + "/exchangeInfo";
  public static final String TICKERS_URL = HTTP_URL + "/tickers";
  public static final String WEBSOCKET_URL = DemoExchangeExchangeImpl.WEBSOCKET_URL;
  private final MessageDeserializer<DemoExchangeMarketDataExchangeInfoResponse> exchangeInfoResponseDeserializer = new DemoExchangeMarketDataExchangeInfoResponseDeserializer();
  private final MessageDeserializer<DemoExchangeMarketDataTickersResponse> tickersResponseDeserializer = new DemoExchangeMarketDataTickersResponseDeserializer();
  private final WebsocketEndpoint<DemoExchangeMarketDataTickerStreamMessage> tickerStreamWs;
  
  public DemoExchangeMarketDataApiImpl(String exchangeName, Properties properties) {
    super(ID, exchangeName, DemoExchangeExchange.ID, properties);
    createHttpRequestExecutor(null, -1L);
    createHttpRequestInterceptor("com.scz.jxapi.netutils.websocket.net.DemoExchangeHttpRequestInterceptorFactory");
    createWebsocketManager(WEBSOCKET_URL, null, "com.scz.jxapi.netutils.websocket.net.DemoExchangeWebsocketHookFactory");
    this.tickerStreamWs = createWebsocketEndpoint(TICKER_STREAM_WS_API, new DemoExchangeMarketDataTickerStreamMessageDeserializer());
  }
  @Override
  public FutureRestResponse<DemoExchangeMarketDataExchangeInfoResponse> exchangeInfo(DemoExchangeMarketDataExchangeInfoRequest request) {
    String urlParameters = EncodingUtil.createUrlQueryParameters("symbols", JsonUtil.pojoToJsonString(request.getSymbols()));
    if (log.isDebugEnabled())
      log.debug("GET exchangeInfo > " + request);
    return submit(HttpRequest.create(EXCHANGE_INFO_REST_API, EXCHANGE_INFO_URL + urlParameters, HttpMethod.GET, request, null, 0), exchangeInfoResponseDeserializer);
  }
  
  @Override
  public FutureRestResponse<DemoExchangeMarketDataTickersResponse> tickers() {
    if (log.isDebugEnabled())
      log.debug("GET tickers >");
    return submit(HttpRequest.create(TICKERS_REST_API, TICKERS_URL, HttpMethod.GET, null, null, 0), tickersResponseDeserializer);
  }
  
  @Override
  public String subscribeTickerStream(DemoExchangeMarketDataTickerStreamRequest request, WebsocketListener<DemoExchangeMarketDataTickerStreamMessage> listener) {
    String topic = EncodingUtil.substituteArguments("${symbol}@ticker", "symbol", request.getSymbol());
    if (log.isDebugEnabled())
      log.debug("subscribeTickerStream:request:" + request);
    WebsocketSubscribeRequest websocketSubscribeRequest = WebsocketSubscribeRequest.create(request, topic, topicMatcher("topic", "ticker", "symbol", "" + request.getSymbol()));
    websocketSubscribeRequest.setRequest(request);
    return tickerStreamWs.subscribe(websocketSubscribeRequest, listener);
  }
  
  @Override
  public boolean unsubscribeTickerStream(String subscriptionId) {
    if (log.isDebugEnabled())
      log.debug("unsubscribeTickerStream: subscriptionId:" + subscriptionId);
    return tickerStreamWs.unsubscribe(subscriptionId);
  }
  
}
