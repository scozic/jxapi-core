package com.scz.jxapi.exchanges.demo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.scz.jxapi.exchanges.demo.gen.DemoExchangeConstants;
import com.scz.jxapi.exchanges.demo.gen.DemoExchangeProperties;
import com.scz.jxapi.exchanges.demo.gen.marketdata.demo.DemoExchangeMarketDataTickerStreamDemo;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamMessage;
import com.scz.jxapi.exchanges.demo.gen.marketdata.pojo.DemoExchangeMarketDataTickerStreamRequest;
import com.scz.jxapi.netutils.rest.javanet.MockHttpServer;
import com.scz.jxapi.netutils.websocket.mock.MockWebsocketListener;
import com.scz.jxapi.netutils.websocket.mock.server.MockWebsocketServer;
import com.scz.jxapi.netutils.websocket.mock.server.MockWebsocketServerEvent;
import com.scz.jxapi.netutils.websocket.mock.server.MockWebsocketServerEventType;
import com.scz.jxapi.netutils.websocket.mock.server.MockWebsocketServerSession;
import com.scz.jxapi.util.DemoProperties;
import com.scz.jxapi.util.DemoUtil;
import com.scz.jxapi.util.JsonUtil;

/**
 * Integration test for demo exchange websocket endpoint demo snippet.
 */
public class DemoExchangeWebsocketEndpointDemoTest {
	
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
	private MockWebsocketListener<DemoExchangeMarketDataTickerStreamMessage> msgListener;
	Properties config = null;
	
	
	@Before
	public void setUp() throws Exception {
		port = MockHttpServer.findAvailablePort();
		server = new MockWebsocketServer(port, appName);
		server.start();
		config = new Properties();
		config.setProperty(DemoExchangeProperties.WEBSOCKET_PORT.getName(), String.valueOf(this.port));
		config.setProperty(DemoProperties.DEMO_WS_SUBSCRIPTION_DURATION_PROPERTY.getName(), String.valueOf(300L));
		msgListener = new MockWebsocketListener<>();
	}
	
	@After
	public void tearDown() {
		server.stop();
	}
	
	@Test
	public void testDemoExchangeMarketDataTickerStreamDemo() throws Exception {
		DemoExchangeMarketDataTickerStreamRequest request = new DemoExchangeMarketDataTickerStreamRequest();
		request.setSymbol("BTC_USDT");
		CompletableFuture<Exception> execution = runSubsribeDemoAsync();
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
		msg2.setSymbol("BTC_USDT");
		msg2.setHigh(new BigDecimal("103686.00"));
		msg2.setLast(new BigDecimal("103171.20"));
		msg2.setLow(new BigDecimal("101048.10"));
		msg2.setVolume(new BigDecimal("153380000.00"));
		msg2.setTime(getTime("2024-12-16T00:07:14.222"));
		server.sendMessageToClients(JsonUtil.pojoToJsonString(msg2));
		DemoExchangeMarketDataTickerStreamMessage actualMsg3 = popReceivedMessage();
		Assert.assertEquals(msg2, actualMsg3);
		popMessageReceivedFromClient(DemoExchangeConstants.WEBSOCKET_LOGOUT_MESSAGE);
		popClientDisconnectEvent();
		Assert.assertNull(execution.get());
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
		return msgListener.waitUntilCount(1).pop();
	}
	
	private CompletableFuture<Exception> runSubsribeDemoAsync() {
		CompletableFuture<Exception> future = new CompletableFuture<>();
		new Thread(() -> {
			try {
				DemoExchangeMarketDataTickerStreamDemo.subscribe(
						DemoExchangeMarketDataTickerStreamDemo.createRequest(), 
						msgListener, 
						config, 
						DemoUtil::logWsApiEvent);
				future.complete(null);
			} catch (Exception e) {
				future.complete(e);
			}
		}).start();

		return future;
	}
}
