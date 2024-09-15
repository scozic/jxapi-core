package com.scz.jxapi.netutils.websocket;

import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.netutils.websocket.mock.MockWebsocket;
import com.scz.jxapi.netutils.websocket.mock.MockWebsocketEvent;
import com.scz.jxapi.netutils.websocket.mock.MockWebsocketEventType;
import com.scz.jxapi.netutils.websocket.mock.MockWebsocketHook;
import com.scz.jxapi.netutils.websocket.mock.MockWebsocketHookEvent;
import com.scz.jxapi.netutils.websocket.mock.MockWebsocketHookEventType;

/**
 * Unit test for {@link DefaultWebsocketManager}
 */
public class DefaultWebsocketManagerTest {
	
	private static final Logger log = LoggerFactory.getLogger(DefaultWebsocketManagerTest.class);
	
	private static final long NO_EVENT_DELAY = 50;

	private MockWebsocket ws;
	private MockWebsocketHook wsHook;
	private DefaultWebsocketManager wsManager;
	private GenericRawWebsocketMessageHandler wsMessageHandler1;
	private GenericRawWebsocketMessageHandler wsMessageHandler2;
	private GenericWebsocketErrorHandler errorHandler;
	
	@Before
	public void setUp() {
		 ws = new MockWebsocket();
		 wsHook = new MockWebsocketHook();
		 wsMessageHandler1 = new GenericRawWebsocketMessageHandler();
		 wsMessageHandler2 = new GenericRawWebsocketMessageHandler();
		 errorHandler = new GenericWebsocketErrorHandler();
	}

	@After
	public void tearDown() {
		if (wsManager != null) {
			wsManager.dispose();
			wsManager = null;
		}
		ws = null;
		wsHook = null;
		wsMessageHandler2 = null;
	}
	
	
	@Test
	public void testGettersAndSetters() throws Exception {
		wsManager = new DefaultWebsocketManager(ws, null);
		popWebsocketAddErrorHandlerEvent();
		popWebsocketAddMessageHandlerEvent();
		long heartBeatInterval = 1000L;
		wsManager.setHeartBeatInterval(heartBeatInterval);
		Assert.assertEquals(heartBeatInterval, wsManager.getHeartBeatInterval());

		long noHeartBeatResponseTimeout = 3000L;
		wsManager.setNoHeartBeatResponseTimeout(noHeartBeatResponseTimeout);
		Assert.assertEquals(noHeartBeatResponseTimeout, wsManager.getNoHeartBeatResponseTimeout());

		long noMessageTimeout = 5000L;
		wsManager.setNoMessageTimeout(noMessageTimeout);
		Assert.assertEquals(noMessageTimeout, wsManager.getNoMessageTimeout());

		long reconnectDelay = 10000L;
		wsManager.setReconnectDelay(reconnectDelay);
		Assert.assertEquals(reconnectDelay, wsManager.getReconnectDelay());

		String url = "wss://localhost:8080";
		wsManager.setUrl(url);
		Assert.assertEquals(url, wsManager.getUrl());
		
		wsManager.dispose();
		Assert.assertTrue(wsManager.isDisposed());
		popWebsocketRemoveMessageHandlerEvent();
		popWebsocketRemoveErrorHandlerEvent();
	}
	
	@Test
	public void testSubscribeToSingleTopicReceiveMessageThenUnsubscribe() throws Exception{
		wsManager = new DefaultWebsocketManager(ws, wsHook);
		wsManager.subscribeErrorHandler(errorHandler);
		popWebsocketAddErrorHandlerEvent();
		popWebsocketAddMessageHandlerEvent();
		popWebsocketHookAfterInitEvent();
		String topic = "topic1";
		String subscribeTopicMsg = "subscribe:topic1";
		String unsubscribeTopicMsg = "unsubscribe:topic1";
		wsHook.setSubscribeRequestMessage(topic, subscribeTopicMsg);
		wsHook.setUnSubscribeRequestMessage(topic, unsubscribeTopicMsg);
		WebsocketMessageTopicMatcher topicMatcher = DefaultWebsocketMessageTopicMatcher.create("myTopic", "topic1");
		wsManager.subscribe(topic, topicMatcher, wsMessageHandler1);
		popWebsocketHookBeforeConnectEvent();
		popWebsocketConnectEvent();
		popWebsocketHookAfterConnectEvent();
		popWebsocketHookGetSubscribeRequestMessageEvent();
		popWebsocketSendMessageEvent(subscribeTopicMsg);
		String msg1 = "{\"myTopic\":\"topic1\", \"payload\":\"Hello!\"}";
		ws.dispatchMessage(msg1);
		Assert.assertEquals(msg1, wsMessageHandler1.waitUntilCount(1).pop());
		
		// 2nd message that will not be dispatched because topic not matching
		String msg2 = "{\"myTopic\":\"otherTopic\", \"payload\":\"foo\"}";
		ws.dispatchMessage(msg2);
		wsMessageHandler1.checkNoEvents(NO_EVENT_DELAY);
		
		wsManager.unsubscribe(topic);
		popWebsocketHookGetUnsubscribeRequestMessageEvent();
		popWebsocketSendMessageEvent(unsubscribeTopicMsg);
		// 3rd message that will not be dispatched after unsubscription from topic
		String msg3 = "{\"myTopic\":\"topic1\", \"payload\":\"Hello World!\"}";
		ws.dispatchMessage(msg3);
		wsMessageHandler1.checkNoEvents(NO_EVENT_DELAY);
		wsManager.dispose();
		popWebsocketHookBeforeDisconnectEvent();
		popWebsocketRemoveMessageHandlerEvent();
		popWebsocketRemoveErrorHandlerEvent();
		popWebsocketDisconnectEvent();
		popWebsocketHookAfterDisconnectEvent();
		checkNoError();
	}
	
	@Test
	public void testSubscribeSecondListenersToSingleTopicTriggersError() throws Exception{
		wsManager = new DefaultWebsocketManager(ws, wsHook);
		wsManager.subscribeErrorHandler(errorHandler);
		popWebsocketAddErrorHandlerEvent();
		popWebsocketAddMessageHandlerEvent();
		popWebsocketHookAfterInitEvent();
		String topic = "topic1";
		String subscribeTopicMsg = "subscribe:topic1";
		String unsubscribeTopicMsg = "unsubscribe:topic1";
		wsHook.setSubscribeRequestMessage(topic, subscribeTopicMsg);
		wsHook.setUnSubscribeRequestMessage(topic, unsubscribeTopicMsg);
		WebsocketMessageTopicMatcher topicMatcher = DefaultWebsocketMessageTopicMatcher.create("myTopic", "topic1");
		wsManager.subscribe(topic, topicMatcher, wsMessageHandler1);
		popWebsocketHookBeforeConnectEvent();
		popWebsocketConnectEvent();
		popWebsocketHookAfterConnectEvent();
		popWebsocketHookGetSubscribeRequestMessageEvent();
		popWebsocketSendMessageEvent(subscribeTopicMsg);
		wsManager.subscribe(topic, topicMatcher, wsMessageHandler2);
		popError();
		checkNoError();
	}
	
	@Test
	public void testWebsocketErrorWhileConnectedTriggersReconnection() throws Exception{
		wsManager = new DefaultWebsocketManager(ws, wsHook);
		wsManager.setReconnectDelay(NO_EVENT_DELAY * 2);
		wsManager.subscribeErrorHandler(errorHandler);
		popWebsocketAddErrorHandlerEvent();
		popWebsocketAddMessageHandlerEvent();
		popWebsocketHookAfterInitEvent();
		String topic = "topic1";
		String subscribeTopicMsg = "subscribe:topic1";
		String unsubscribeTopicMsg = "unsubscribe:topic1";
		wsHook.setSubscribeRequestMessage(topic, subscribeTopicMsg);
		wsHook.setUnSubscribeRequestMessage(topic, unsubscribeTopicMsg);
		WebsocketMessageTopicMatcher topicMatcher = DefaultWebsocketMessageTopicMatcher.create("myTopic", "topic1");
		wsManager.subscribe(topic, topicMatcher, wsMessageHandler1);
		popWebsocketHookBeforeConnectEvent();
		popWebsocketConnectEvent();
		popWebsocketHookAfterConnectEvent();
		popWebsocketHookGetSubscribeRequestMessageEvent();
		popWebsocketSendMessageEvent(subscribeTopicMsg);
		
		// FIXME
		log.debug("Dispatching error");
		ws.dispatchError(new WebsocketException("foo"));
		popError();
		
		// Disconnection should be initiated upon error
		popWebsocketHookBeforeDisconnectEvent();
		popWebsocketDisconnectEvent();
		popWebsocketHookAfterDisconnectEvent();
		
		// reconnect delay must be awaited before attempting reconnection
		// FIXME
		log.debug("Checking no connection event is received before delay before reconnection has elapsed");
		checkNoWebsocketEvent();
		
		// Then reconnection should be initiated
		popWebsocketHookBeforeConnectEvent();
		popWebsocketConnectEvent();
		popWebsocketHookAfterConnectEvent();
		popWebsocketHookGetSubscribeRequestMessageEvent();
		// Topic resubscription should be performed
		popWebsocketSendMessageEvent(subscribeTopicMsg);
		checkNoError();
	}
	
	private MockWebsocketEvent popWebsocketConnectEvent() throws TimeoutException {
		MockWebsocketEvent event = ws.waitUntilCount(1).pop();
		Assert.assertEquals(MockWebsocketEventType.CONNECT, event.getType());
		return event;
	}
	
	private MockWebsocketEvent popWebsocketDisconnectEvent() throws TimeoutException {
		MockWebsocketEvent event = ws.waitUntilCount(1).pop();
		Assert.assertEquals(MockWebsocketEventType.DISCONNECT, event.getType());
		return event;
	}
	
	private MockWebsocketEvent popWebsocketSendMessageEvent(String expectedSentMessage) throws TimeoutException {
		MockWebsocketEvent event = ws.waitUntilCount(1).pop();
		Assert.assertEquals(MockWebsocketEventType.SEND, event.getType());
		Assert.assertEquals(expectedSentMessage, event.getMessage());
		return event;
	}
	
	private MockWebsocketEvent popWebsocketAddMessageHandlerEvent() throws TimeoutException {
		MockWebsocketEvent event = ws.waitUntilCount(1).pop();
		Assert.assertEquals(MockWebsocketEventType.ADD_MESSAGE_HANDLER, event.getType());
		Assert.assertNotNull(event.getMessageHandler());
		return event;
	}
	
	private MockWebsocketEvent popWebsocketRemoveMessageHandlerEvent() throws TimeoutException {
		MockWebsocketEvent event = ws.waitUntilCount(1).pop();
		Assert.assertEquals(MockWebsocketEventType.REMOVE_MESSAGE_HANDLER, event.getType());
		Assert.assertNotNull(event.getMessageHandler());
		return event;
	}
	
	private MockWebsocketEvent popWebsocketAddErrorHandlerEvent() throws TimeoutException {
		MockWebsocketEvent event = ws.waitUntilCount(1).pop();
		Assert.assertEquals(MockWebsocketEventType.ADD_ERROR_HANDLER, event.getType());
		Assert.assertNotNull(event.getErrorHandler());
		return event;
	}
	
	private MockWebsocketEvent popWebsocketRemoveErrorHandlerEvent() throws TimeoutException {
		MockWebsocketEvent event = ws.waitUntilCount(1).pop();
		Assert.assertEquals(MockWebsocketEventType.REMOVE_ERROR_HANDLER, event.getType());
		Assert.assertNotNull(event.getErrorHandler());
		return event;
	}
	
	private void checkNoWebsocketEvent() {
		ws.checkNoEvents(NO_EVENT_DELAY);
	}
	
	
	private MockWebsocketHookEvent popWebsocketHookAfterInitEvent() throws TimeoutException {
		MockWebsocketHookEvent event = wsHook.waitUntilCount(1).pop();
		Assert.assertEquals(MockWebsocketHookEventType.AFTER_INIT, event.getType());
		Assert.assertEquals(wsHook, event.getSource());
		Assert.assertEquals(wsManager, event.getWebsocketManager());
		return event;
	}
	
	private MockWebsocketHookEvent popWebsocketHookBeforeConnectEvent() throws TimeoutException {
		MockWebsocketHookEvent event = wsHook.waitUntilCount(1).pop();
		Assert.assertEquals(MockWebsocketHookEventType.BEFORE_CONNECT, event.getType());
		Assert.assertEquals(wsHook, event.getSource());
		Assert.assertEquals(wsManager, event.getWebsocketManager());
		return event;
	}
	
	private MockWebsocketHookEvent popWebsocketHookAfterConnectEvent() throws TimeoutException {
		MockWebsocketHookEvent event = wsHook.waitUntilCount(1).pop();
		Assert.assertEquals(MockWebsocketHookEventType.AFTER_CONNECT, event.getType());
		Assert.assertEquals(wsHook, event.getSource());
		Assert.assertEquals(wsManager, event.getWebsocketManager());
		return event;
	}
	
	private MockWebsocketHookEvent popWebsocketHookBeforeDisconnectEvent() throws TimeoutException {
		MockWebsocketHookEvent event = wsHook.waitUntilCount(1).pop();
		Assert.assertEquals(MockWebsocketHookEventType.BEFORE_DISCONNECT, event.getType());
		Assert.assertEquals(wsHook, event.getSource());
		Assert.assertEquals(wsManager, event.getWebsocketManager());
		return event;
	}
	
	private MockWebsocketHookEvent popWebsocketHookAfterDisconnectEvent() throws TimeoutException {
		MockWebsocketHookEvent event = wsHook.waitUntilCount(1).pop();
		Assert.assertEquals(MockWebsocketHookEventType.AFTER_DISCONNECT, event.getType());
		Assert.assertEquals(wsHook, event.getSource());
		Assert.assertEquals(wsManager, event.getWebsocketManager());
		return event;
	}
	
	private MockWebsocketHookEvent popWebsocketHookGetSubscribeRequestMessageEvent() throws TimeoutException {
		MockWebsocketHookEvent event = wsHook.waitUntilCount(1).pop();
		Assert.assertEquals(MockWebsocketHookEventType.GET_SUBSCRIBE_REQUEST_MESSAGE, event.getType());
		Assert.assertEquals(wsHook, event.getSource());
		return event;
	}
	
	private MockWebsocketHookEvent popWebsocketHookGetUnsubscribeRequestMessageEvent() throws TimeoutException {
		MockWebsocketHookEvent event = wsHook.waitUntilCount(1).pop();
		Assert.assertEquals(MockWebsocketHookEventType.GET_UNSUBSCRIBE_REQUEST_MESSAGE, event.getType());
		Assert.assertEquals(wsHook, event.getSource());
		return event;
	}
	
	private MockWebsocketHookEvent popWebsocketHookGetHeartbeatMessageEvent() throws TimeoutException {
		MockWebsocketHookEvent event = wsHook.waitUntilCount(1).pop();
		Assert.assertEquals(MockWebsocketHookEventType.GET_HEARTBEAT_MESSAGE, event.getType());
		Assert.assertEquals(wsHook, event.getSource());
		return event;
	}
	
	private void checkNoWebsocketHookEvent() {
		wsHook.checkNoEvents(NO_EVENT_DELAY);
	}
	
	private WebsocketException popError() throws TimeoutException {
		WebsocketException ex = this.errorHandler.waitUntilCount(1).pop();
		Assert.assertNotNull(ex);
		return ex;
	}
	
	private void checkNoError() {
		errorHandler.checkNoEvents(NO_EVENT_DELAY);
	}

}

