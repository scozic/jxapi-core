package com.scz.jxapi.netutils.websocket;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

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
	private static final long HEARTBEAT_INTERVAL = 200;

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
		wsManager = new DefaultWebsocketManager(ws, wsHook);
		Assert.assertEquals(wsHook, wsManager.getWebsocketHook());
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
	
	@Test(expected = IllegalArgumentException.class)
	public void testSubscribeNullTopic() throws Exception{
		wsManager = new DefaultWebsocketManager(ws, null);
		WebsocketMessageTopicMatcher topicMatcher = DefaultWebsocketMessageTopicMatcher.create("myTopic", "topic1");
		wsManager.subscribe(null, topicMatcher, wsMessageHandler1);
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
		WebsocketMessageTopicMatcher topicMatcher = DefaultWebsocketMessageTopicMatcher.create("myTopic", topic);
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
		popWebsocketDisconnectEvent();
		popWebsocketRemoveErrorHandlerEvent();
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
		
		ws.dispatchError(new WebsocketException("foo"));
		popError();
		
		// Disconnection should be initiated upon error
		popWebsocketHookBeforeDisconnectEvent();
		popWebsocketDisconnectEvent();
		popWebsocketHookAfterDisconnectEvent();
		
		// reconnect delay must be awaited before attempting reconnection
		checkNoEvents();
		
		// Then reconnection should be initiated
		popWebsocketHookBeforeConnectEvent();
		popWebsocketConnectEvent();
		popWebsocketHookAfterConnectEvent();
		popWebsocketHookGetSubscribeRequestMessageEvent();
		// Topic resubscription should be performed
		popWebsocketSendMessageEvent(subscribeTopicMsg);
		checkNoError();
	}
	
	@Test
	public void testWebsocketErrorNoReconnectWhenReconnectDelayInfZero() throws Exception {
		wsManager = new DefaultWebsocketManager(ws, wsHook);
		wsManager.setReconnectDelay(-1L);
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
		
		ws.dispatchError(new WebsocketException("foo"));
		popError();
		
		// No reconnect delay set -> no disconnection / reconnection attempted
		checkNoEvents();
	}
	
	@Test
	public void testSubscribeUnsubscribeSingleTopicNoWebsocketHook() throws Exception {
		wsManager = new DefaultWebsocketManager(ws, null);
		wsManager.subscribeErrorHandler(errorHandler);
		popWebsocketAddErrorHandlerEvent();
		popWebsocketAddMessageHandlerEvent();
		String topic = "topic1";
		WebsocketMessageTopicMatcher topicMatcher = DefaultWebsocketMessageTopicMatcher.create("myTopic", topic);
		wsManager.subscribe(topic, topicMatcher, wsMessageHandler1);
		popWebsocketConnectEvent();
		checkNoEvents();
		String msg1 = "{\"myTopic\":\"topic1\", \"payload\":\"Hello!\"}";
		ws.dispatchMessage(msg1);
		Assert.assertEquals(msg1, wsMessageHandler1.waitUntilCount(1).pop());
		
		// 2nd message that will not be dispatched because topic not matching
		String msg2 = "{\"myTopic\":\"otherTopic\", \"payload\":\"foo\"}";
		ws.dispatchMessage(msg2);
		wsMessageHandler1.checkNoEvents(NO_EVENT_DELAY);
		
		wsManager.unsubscribe(topic);
		wsMessageHandler1.checkNoEvents(NO_EVENT_DELAY);
		// 3rd message that will not be dispatched after unsubscription from topic
		String msg3 = "{\"myTopic\":\"topic1\", \"payload\":\"Hello World!\"}";
		ws.dispatchMessage(msg3);
		wsMessageHandler1.checkNoEvents(NO_EVENT_DELAY);
		wsManager.dispose();
		Assert.assertTrue(wsManager.isDisposed());
		popWebsocketRemoveMessageHandlerEvent();
		popWebsocketDisconnectEvent();
		popWebsocketRemoveErrorHandlerEvent();
		checkNoError();
	}
	
	@Test
	public void testHeartBeatInitiatedByClientResponseReceivedAndHandledBySystemMsgHandlerThenHeartBeatTimeout() throws Exception {
		wsManager = new DefaultWebsocketManager(ws, wsHook);
		wsManager.setHeartBeatInterval(HEARTBEAT_INTERVAL);
		long noHeartBeatResponseTimeout = HEARTBEAT_INTERVAL + 25;
		wsManager.setNoHeartBeatResponseTimeout(noHeartBeatResponseTimeout);
		wsManager.subscribeErrorHandler(errorHandler);
		popWebsocketAddErrorHandlerEvent();
		popWebsocketAddMessageHandlerEvent();
		popWebsocketHookAfterInitEvent();
		String topic = "topic1";
		String subscribeTopicMsg = "subscribe:topic1";
		String unsubscribeTopicMsg = "unsubscribe:topic1";
		wsHook.setSubscribeRequestMessage(topic, subscribeTopicMsg);
		wsHook.setUnSubscribeRequestMessage(topic, unsubscribeTopicMsg);
		String heartBeatMsg = "{\"myTopic\":\"heartBeat\", \"payload\":\"PING\"}";
		wsHook.setHeartBeatMessage(heartBeatMsg);
		String heartBeatResponseMsg = "{\"myTopic\":\"heartBeat\", \"payload\":\"PONG\"}";
		WebsocketMessageTopicMatcher topicMatcher = DefaultWebsocketMessageTopicMatcher.create("myTopic", "topic1");
		HeartBeatResponseSystemMsgHandler hbHandler = new HeartBeatResponseSystemMsgHandler();
		wsManager.addSystemMessageHandler("heartBeat",  DefaultWebsocketMessageTopicMatcher.create("myTopic", "heartBeat"), hbHandler);
		checkNoEvents();
		wsManager.subscribe(topic, topicMatcher, wsMessageHandler1);
		popWebsocketHookBeforeConnectEvent();
		popWebsocketConnectEvent();
		popWebsocketHookAfterConnectEvent();
		popWebsocketHookGetSubscribeRequestMessageEvent();
		popWebsocketSendMessageEvent(subscribeTopicMsg);
		
		// First heartbeat should not be sent immediately
		checkNoEvents();
		
		// First heartbeat should be sent
		popWebsocketHookGetHeartbeatMessageEvent(heartBeatMsg);
		popWebsocketSendMessageEvent(heartBeatMsg);
		
		// Dispatch heartBeat response
		ws.dispatchMessage(heartBeatResponseMsg);
		Assert.assertEquals(1, hbHandler.count());
		checkNoEvents();
		
		// 2nd heartbeat should be sent
		popWebsocketHookGetHeartbeatMessageEvent(heartBeatMsg);
		popWebsocketSendMessageEvent(heartBeatMsg);
		
		// After no heartbeat timeout elapsed, error should be raised
		Thread.sleep(noHeartBeatResponseTimeout);
		popError();
	}
	
	@Test
	public void testHeartBeatInitiatedByServerHandledBySystemMsgHandlerThatRespondsWithHeartBeatResponse() throws Exception {
		wsManager = new DefaultWebsocketManager(ws, wsHook);
		wsManager.setHeartBeatInterval(HEARTBEAT_INTERVAL);
		long noHeartBeatResponseTimeout = HEARTBEAT_INTERVAL + 25;
		wsManager.setNoHeartBeatResponseTimeout(noHeartBeatResponseTimeout);
		wsManager.subscribeErrorHandler(errorHandler);
		popWebsocketAddErrorHandlerEvent();
		popWebsocketAddMessageHandlerEvent();
		popWebsocketHookAfterInitEvent();
		String topic = "topic1";
		String subscribeTopicMsg = "subscribe:topic1";
		String unsubscribeTopicMsg = "unsubscribe:topic1";
		wsHook.setSubscribeRequestMessage(topic, subscribeTopicMsg);
		wsHook.setUnSubscribeRequestMessage(topic, unsubscribeTopicMsg);
		String heartBeatMsg = "{\"myTopic\":\"heartBeat\", \"payload\":\"PING\"}";
		wsHook.setHeartBeatMessage(null);
		String heartBeatResponseMsg = "{\"myTopic\":\"heartBeat\", \"payload\":\"PONG\"}";
		WebsocketMessageTopicMatcher topicMatcher = DefaultWebsocketMessageTopicMatcher.create("myTopic", "topic1");
		HeartBeatRequestSystemMsgHandler hbHandler = new HeartBeatRequestSystemMsgHandler();
		hbHandler.heartBeatResponseMsg = heartBeatResponseMsg;
		wsManager.addSystemMessageHandler("heartBeat",  DefaultWebsocketMessageTopicMatcher.create("myTopic", "heartBeat"), hbHandler);
		checkNoEvents();
		wsManager.subscribe(topic, topicMatcher, wsMessageHandler1);
		popWebsocketHookBeforeConnectEvent();
		popWebsocketConnectEvent();
		popWebsocketHookAfterConnectEvent();
		popWebsocketHookGetSubscribeRequestMessageEvent();
		popWebsocketSendMessageEvent(subscribeTopicMsg);
		ws.dispatchMessage(heartBeatMsg);
		popWebsocketSendMessageEvent(heartBeatResponseMsg);
		Assert.assertEquals(1, hbHandler.count());
		checkNoEvents();
		checkNoError();
	}
	
	@Test
	public void testSubscribeToTwoTopicsReceiveOneMessageOnBothThenUnsubscribe() throws Exception{
		// Init WS manager
		wsManager = new DefaultWebsocketManager(ws, wsHook);
		wsManager.subscribeErrorHandler(errorHandler);
		popWebsocketAddErrorHandlerEvent();
		popWebsocketAddMessageHandlerEvent();
		popWebsocketHookAfterInitEvent();
		
		// Subscribe to first topic
		String topic1 = "topic1";
		String subscribeTopic1Msg = "subscribe:topic1";
		String unsubscribeTopic1Msg = "unsubscribe:topic1";
		wsHook.setSubscribeRequestMessage(topic1, subscribeTopic1Msg);
		wsHook.setUnSubscribeRequestMessage(topic1, unsubscribeTopic1Msg);
		WebsocketMessageTopicMatcher topicMatcher = DefaultWebsocketMessageTopicMatcher.create("myTopic", topic1);
		wsManager.subscribe(topic1, topicMatcher, wsMessageHandler1);
		popWebsocketHookBeforeConnectEvent();
		popWebsocketConnectEvent();
		popWebsocketHookAfterConnectEvent();
		popWebsocketHookGetSubscribeRequestMessageEvent();
		popWebsocketSendMessageEvent(subscribeTopic1Msg);
		
		// Dispatch a message for topic1 that should be dispatched to listener for topic1
		String msg1Topic1 = "{\"myTopic\":\"topic1\", \"payload\":\"Hello#1!\"}";
		ws.dispatchMessage(msg1Topic1);
		Assert.assertEquals(msg1Topic1, wsMessageHandler1.waitUntilCount(1).pop());
		
		// Subscribe to topic2
		String topic2 = "topic2";
		String subscribeTopic2Msg = "subscribe:topic2";
		String unsubscribeTopic2Msg = "unsubscribe:topic2";
		wsHook.setSubscribeRequestMessage(topic2, subscribeTopic2Msg);
		wsHook.setUnSubscribeRequestMessage(topic2, unsubscribeTopic2Msg);
		WebsocketMessageTopicMatcher topic2Matcher = DefaultWebsocketMessageTopicMatcher.create("myTopic", topic2);
		wsManager.subscribe(topic2, topic2Matcher, wsMessageHandler2);
		popWebsocketHookGetSubscribeRequestMessageEvent();
		popWebsocketSendMessageEvent(subscribeTopic2Msg);
		
		// Dispatch a message for topic 2 that should be received by topic2 listener but not topic1 listener
		String msg2Topic2 = "{\"myTopic\":\"topic2\", \"payload\":\"Hello#2!\"}";
		ws.dispatchMessage(msg2Topic2);
		Assert.assertEquals(msg2Topic2, wsMessageHandler2.waitUntilCount(1).pop());
		wsMessageHandler1.checkNoEvents(NO_EVENT_DELAY);
		
		wsManager.unsubscribe(topic1);
		popWebsocketHookGetUnsubscribeRequestMessageEvent();
		popWebsocketSendMessageEvent(unsubscribeTopic1Msg);
		
		// 3rd message that will not be dispatched to listener because it comes after unsubscription from topic1.
		String msg3Topic1 = "{\"myTopic\":\"topic1\", \"payload\":\"Hello#3!\"}";
		ws.dispatchMessage(msg3Topic1);
		wsMessageHandler1.checkNoEvents(NO_EVENT_DELAY);
		wsMessageHandler2.checkNoEvents(NO_EVENT_DELAY);
		
		// 4th message that will be still dispatched to listener for topic2 after unsubscription from topic1
		String msg4Topic2 = "{\"myTopic\":\"topic2\", \"payload\":\"Hello#4!\"}";
		ws.dispatchMessage(msg4Topic2);
		Assert.assertEquals(msg4Topic2, wsMessageHandler2.waitUntilCount(1).pop());
		wsMessageHandler2.checkNoEvents(NO_EVENT_DELAY);
		
		// Unsubscribe from topic2 
		wsManager.unsubscribe(topic2);
		popWebsocketHookGetUnsubscribeRequestMessageEvent();
		popWebsocketSendMessageEvent(unsubscribeTopic2Msg);
		
		// 5th message not dispatched to either listener for topic1 or topic2 after both have been unsubscribed.
		String msg5Topic2 = "{\"myTopic\":\"topic2\", \"payload\":\"Hello#5!\"}";
		ws.dispatchMessage(msg5Topic2);
		wsMessageHandler1.checkNoEvents(NO_EVENT_DELAY);
		wsMessageHandler2.checkNoEvents(NO_EVENT_DELAY);
		
		// Dispose WS manager
		wsManager.dispose();
		popWebsocketHookBeforeDisconnectEvent();
		popWebsocketRemoveMessageHandlerEvent();
		popWebsocketDisconnectEvent();
		popWebsocketRemoveErrorHandlerEvent();
		popWebsocketHookAfterDisconnectEvent();
		checkNoError();
	}
	
	@Test
	public void testErrorOnHeartBeatSendTriggersErrorEventAndReconnectionAndHeartBeatTimeoutAfterReonnection() {
		// TODO Reconnection should not be attempted before hb timeout has elapsed starting from reconnection
	}
	
	@Test
	public void testNoMessageTimeoutTriggersReconnectionThenAnotherMessageTimeoutTriggersSecondReconnection() {
		// TODO
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
	
//	private void checkNoWebsocketEvent() {
//		ws.checkNoEvents(NO_EVENT_DELAY);
//	}
	
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
	
	private MockWebsocketHookEvent popWebsocketHookGetHeartbeatMessageEvent(String heartBeatMessage) throws TimeoutException {
		MockWebsocketHookEvent event = wsHook.waitUntilCount(1).pop();
		Assert.assertEquals(MockWebsocketHookEventType.GET_HEARTBEAT_MESSAGE, event.getType());
		Assert.assertEquals(wsHook, event.getSource());
		return event;
	}
	
	private void checkNoEvents() throws InterruptedException {
		if (log.isDebugEnabled()) {
			log.debug("Check no events during " + NO_EVENT_DELAY + "ms");
		}
		Thread.sleep(NO_EVENT_DELAY);
		if (ws.size() > 0) {
			Assert.fail("Received unexpected WebsocketEvent" + ws.pop());
		}
		
		if (wsHook.size() > 0) {
			Assert.assertEquals("Received unexpected WebsocketHookEvent", 0, wsHook.size());
		}
	}
	
	private WebsocketException popError() throws TimeoutException {
		WebsocketException ex = this.errorHandler.waitUntilCount(1).pop();
		Assert.assertNotNull(ex);
		return ex;
	}
	
	private void checkNoError() {
		errorHandler.checkNoEvents(NO_EVENT_DELAY);
	}
	
	private class HeartBeatResponseSystemMsgHandler implements RawWebsocketMessageHandler {
		
		AtomicInteger receivedHeartBeatResponses = new AtomicInteger(0);

		@Override
		public void handleWebsocketMessage(String message) {
			receivedHeartBeatResponses.incrementAndGet();
			wsManager.hearbeatReceived();
		}
		
		public int count() {
			return receivedHeartBeatResponses.get();
		}
	}
	
	private class HeartBeatRequestSystemMsgHandler implements RawWebsocketMessageHandler {
		
		String heartBeatResponseMsg;
		
		AtomicInteger receivedHeartBeatResponses = new AtomicInteger(0);

		@Override
		public void handleWebsocketMessage(String message) {
			receivedHeartBeatResponses.incrementAndGet();
			wsManager.hearbeatReceived();
			wsManager.sendAsync(heartBeatResponseMsg);
		}
		
		public int count() {
			return receivedHeartBeatResponses.get();
		}
	}

}

