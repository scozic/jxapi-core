package org.jxapi.exchanges.demo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.jxapi.exchanges.demo.gen.DemoExchangeConstants;
import org.jxapi.exchanges.demo.gen.DemoExchangeExchange;
import org.jxapi.exchanges.demo.gen.DemoExchangeExchangeImpl;
import org.jxapi.exchanges.demo.gen.DemoExchangeProperties;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamMessage;
import org.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest;
import org.jxapi.netutils.rest.javanet.HttpServerUtil;
import org.jxapi.netutils.websocket.mock.MockWebsocketListener;
import org.jxapi.netutils.websocket.mock.server.MockWebsocketServer;
import org.jxapi.netutils.websocket.mock.server.MockWebsocketServerEvent;
import org.jxapi.netutils.websocket.mock.server.MockWebsocketServerEventType;
import org.jxapi.netutils.websocket.mock.server.MockWebsocketServerSession;
import org.jxapi.util.JsonUtil;

public class DemoExchangeWebsocketEndpointTest {
  
  private static final String TEST_EXCHANGE_NAME = "myExchange";
  private static final long NO_EVENTS_DELAY = 200L;
  
  private static Long getTime(String date) {
    try {
      return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(date).getTime();
    } catch (ParseException e) {
      throw new IllegalArgumentException("Invalid date:[" + date + "]");
    }
  }
  
  private int port = 8088;
  private MockWebsocketServer server;
  private String appName = "demo";
  private DemoExchangeExchange exchange;
  private MockWebsocketListener<DemoExchangeMarketDataTickerStreamMessage> msgListener;
  
  
  @Before
  public void setUp() throws Exception {
    port = HttpServerUtil.findAvailablePort();
    server = new MockWebsocketServer(port, appName);
    server.start();
    Properties config = new Properties();
    config.setProperty(DemoExchangeProperties.BASE_WEBSOCKET_URL.getName(), server.getUrl());
    exchange = new DemoExchangeExchangeImpl(TEST_EXCHANGE_NAME, config);
    msgListener = new MockWebsocketListener<>();
  }
  
  @After
  public void tearDown() {
    server.stop();
  }

  @Test
  public void testSubscribeDemoExchangeMarketDataTickerStreamReceiveMessagesThenDispose() throws Exception {
    DemoExchangeMarketDataTickerStreamRequest request = new DemoExchangeMarketDataTickerStreamRequest();
    request.setSymbol("BTC_USDT");
    String clientSubscription = exchange.getMarketDataApi().subscribeTickerStream(request, msgListener);
    popClientConnectEvent();
    popMessageReceivedFromClient(DemoExchangeConstants.WEBSOCKET_LOGIN_MESSAGE);
    
    DemoExchangeMarketDataTickerStreamMessage msg1 = new DemoExchangeMarketDataTickerStreamMessage();
    msg1.setTopic("ticker");
    msg1.setSymbol("BTC_USDT");
    msg1.setHigh(new BigDecimal("103486.00"));
    msg1.setLast(new BigDecimal("103271.20"));
    msg1.setLow(new BigDecimal("101049.10"));
    msg1.setVolume(new BigDecimal("153280000.00"));
    msg1.setTime(getTime("2024-12-16T00:05:14.222"));
    server.sendMessageToClients(JsonUtil.pojoToJsonString(msg1));
    DemoExchangeMarketDataTickerStreamMessage actualMsg1 = popReceivedMessage();
    Assert.assertEquals(msg1, actualMsg1);
    
    DemoExchangeMarketDataTickerStreamMessage msg2 = new DemoExchangeMarketDataTickerStreamMessage();
    msg2.setTopic("ticker");
    msg2.setSymbol("ETH_USDT");
    msg2.setHigh(new BigDecimal("3919.71"));
    msg2.setLast(new BigDecimal("3915.17"));
    msg2.setLow(new BigDecimal("3832.01"));
    msg2.setVolume(new BigDecimal("120920000.00"));
    msg2.setTime(getTime("2024-12-16T00:06:14.333"));
    server.sendMessageToClients(JsonUtil.pojoToJsonString(msg2));
    
    checkNoMessageReceived();
    
    DemoExchangeMarketDataTickerStreamMessage msg3 = new DemoExchangeMarketDataTickerStreamMessage();
    msg3.setTopic("ticker");
    msg3.setSymbol("BTC_USDT");
    msg3.setHigh(new BigDecimal("103686.00"));
    msg3.setLast(new BigDecimal("103171.20"));
    msg3.setLow(new BigDecimal("101048.10"));
    msg3.setVolume(new BigDecimal("153380000.00"));
    msg3.setTime(getTime("2024-12-16T00:07:14.222"));
    server.sendMessageToClients(JsonUtil.pojoToJsonString(msg3));
    DemoExchangeMarketDataTickerStreamMessage actualMsg3 = popReceivedMessage();
    Assert.assertEquals(msg3, actualMsg3);
    
    exchange.getMarketDataApi().unsubscribeTickerStream(clientSubscription);
    checkNoMessageReceived();
    
    exchange.dispose();
    popMessageReceivedFromClient(DemoExchangeConstants.WEBSOCKET_LOGOUT_MESSAGE);
    popClientDisconnectEvent();
  }
  
  private MockWebsocketServerSession popClientConnectEvent() throws TimeoutException {
    MockWebsocketServerEvent event = server.waitUntilCount(1).pop();
    Assert.assertEquals(MockWebsocketServerEventType.CLIENT_CONNECT, event.getType());
    MockWebsocketServerSession session = event.getSession();
    Assert.assertNotNull(session);
    return session;
  }
  
  private MockWebsocketServerSession popMessageReceivedFromClient(String msg) throws TimeoutException {
    MockWebsocketServerEvent event = server.waitUntilCount(1).pop();
    Assert.assertEquals(MockWebsocketServerEventType.MESSAGE_RECEIVED, event.getType());
    MockWebsocketServerSession session = event.getSession();
    Assert.assertNotNull(session);
    Assert.assertEquals(msg, event.getMessage());
    return session;
  }
  
  private MockWebsocketServerSession popClientDisconnectEvent() throws TimeoutException {
    MockWebsocketServerEvent event = server.waitUntilCount(1).pop();
    Assert.assertEquals(MockWebsocketServerEventType.CLIENT_DISCONNECT, event.getType());
    MockWebsocketServerSession session = event.getSession();
    Assert.assertNotNull(session);
    return session;
  }

  private DemoExchangeMarketDataTickerStreamMessage popReceivedMessage() throws TimeoutException {
    return msgListener.waitUntilCount(1, 1000).pop();
  }
  
  private void checkNoMessageReceived() throws InterruptedException {
    msgListener.checkNoEvents(NO_EVENTS_DELAY);
  }
}
