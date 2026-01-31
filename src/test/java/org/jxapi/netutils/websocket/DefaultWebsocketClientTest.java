package org.jxapi.netutils.websocket;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jxapi.netutils.websocket.mock.MockWebsocket;
import org.jxapi.netutils.websocket.mock.MockWebsocketEvent;
import org.jxapi.netutils.websocket.mock.MockWebsocketEventType;
import org.jxapi.netutils.websocket.mock.MockWebsocketHook;
import org.jxapi.netutils.websocket.mock.MockWebsocketHookEvent;
import org.jxapi.netutils.websocket.mock.MockWebsocketHookEventType;
import org.jxapi.netutils.websocket.multiplexing.WSMTMFUtil;
import org.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatcherFactory;
import org.jxapi.util.DemoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for {@link DefaultWebsocketClient}.
 */
public class DefaultWebsocketClientTest {

  
  private static final Logger log = LoggerFactory.getLogger(DefaultWebsocketClientTest.class);
  
  private static final long NO_EVENT_DELAY = 50;
  private static final long HEARTBEAT_INTERVAL = 200;

  private MockWebsocket ws;
  private MockWebsocketHook wsHook;
  private DefaultWebsocketClient wsManager;
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
    wsManager = new DefaultWebsocketClient(ws, wsHook);
    Assert.assertEquals(wsHook, wsManager.getWebsocketHook());
    Assert.assertEquals(ws, wsManager.getWebsocket());
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
  public void testSubscribeToSingleTopicReceiveOneMessageForTopicThenUnsubscribeThenReceiveMesssageForTopicNotDispatchedAsUnsubscribedThenDisposeWsManager() throws Exception{
    wsManager = new DefaultWebsocketClient(ws, wsHook);
    wsManager.subscribeErrorHandler(errorHandler);
    popWebsocketAddErrorHandlerEvent();
    popWebsocketAddMessageHandlerEvent();
    popWebsocketHookInitEvent();
    String topic = "topic1";
    String subscribeTopicMsg = "subscribe:topic1";
    String unsubscribeTopicMsg = "unsubscribe:topic1";
    wsHook.setSubscribeRequestMessage(topic, subscribeTopicMsg);
    wsHook.setUnSubscribeRequestMessage(topic, unsubscribeTopicMsg);
    WebsocketMessageTopicMatcherFactory topicMatcher = WSMTMFUtil.value("myTopic", topic);
    wsManager.subscribe(topic, topicMatcher, wsMessageHandler1);
    popWebsocketHookBeforeConnectEvent();
    popWebsocketConnectEvent();
    popWebsocketHookAfterConnectEvent();
    popWebsocketHookGetSubscribeRequestMessageEvent();
    popWebsocketSendMessageEvent(subscribeTopicMsg);
    String msg1 = "{\"myTopic\":\"topic1\", \"payload\":\"Hello!\"}";
    ws.dispatchMessage(msg1);
    Assert.assertEquals(msg1, wsMessageHandler1.waitUntilCount(1).pop());
    wsManager.unsubscribe(topic);
    popWebsocketHookGetUnsubscribeRequestMessageEvent();
    popWebsocketSendMessageEvent(unsubscribeTopicMsg);
    // 2nd message that will not be dispatched after unsubscription from topic
    String msg2 = "{\"myTopic\":\"topic1\", \"payload\":\"Hello World!\"}";
    ws.dispatchMessage(msg2);
    wsMessageHandler1.checkNoEvents(NO_EVENT_DELAY);
    checkNoEvents();
    wsManager.dispose();
    popWebsocketHookBeforeDisconnectEvent();
    popWebsocketRemoveMessageHandlerEvent();
    popWebsocketDisconnectEvent();
    popWebsocketRemoveErrorHandlerEvent();
    popWebsocketHookAfterDisconnectEvent();
    checkNoError();
  }
  
  @Test
  public void testSubscribeToSingleTopicReceiveOneMessageForTopicWithTopicFieldInSubObject() throws Exception{
    wsManager = new DefaultWebsocketClient(ws, wsHook);
    wsManager.subscribeErrorHandler(errorHandler);
    popWebsocketAddErrorHandlerEvent();
    popWebsocketAddMessageHandlerEvent();
    popWebsocketHookInitEvent();
    String topic = "topic1";
    String subscribeTopicMsg = "subscribe:topic1";
    String unsubscribeTopicMsg = "unsubscribe:topic1";
    wsHook.setSubscribeRequestMessage(topic, subscribeTopicMsg);
    wsHook.setUnSubscribeRequestMessage(topic, unsubscribeTopicMsg);
    WebsocketMessageTopicMatcherFactory topicMatcher = WSMTMFUtil.value("myTopic", topic);
    wsManager.subscribe(topic, topicMatcher, wsMessageHandler1);
    popWebsocketHookBeforeConnectEvent();
    popWebsocketConnectEvent();
    popWebsocketHookAfterConnectEvent();
    popWebsocketHookGetSubscribeRequestMessageEvent();
    popWebsocketSendMessageEvent(subscribeTopicMsg);
    
    String msg = "{\"payload\":\"Hello!\", \"sub\":{\"myTopic\":\"topic1\"}";
    ws.dispatchMessage(msg);
    Assert.assertEquals(msg, wsMessageHandler1.waitUntilCount(1).pop());
    
    checkNoEvents();
    checkNoError();
  }  
  
  @Test
  public void testSubscribeToSingleTopicReceiveOneMessageForDifferentTopic() throws Exception{
    wsManager = new DefaultWebsocketClient(ws, wsHook);
    wsManager.subscribeErrorHandler(errorHandler);
    popWebsocketAddErrorHandlerEvent();
    popWebsocketAddMessageHandlerEvent();
    popWebsocketHookInitEvent();
    String topic = "topic1";
    String subscribeTopicMsg = "subscribe:topic1";
    String unsubscribeTopicMsg = "unsubscribe:topic1";
    wsHook.setSubscribeRequestMessage(topic, subscribeTopicMsg);
    wsHook.setUnSubscribeRequestMessage(topic, unsubscribeTopicMsg);
    WebsocketMessageTopicMatcherFactory topicMatcher = WSMTMFUtil.value("myTopic", topic);
    wsManager.subscribe(topic, topicMatcher, wsMessageHandler1);
    popWebsocketHookBeforeConnectEvent();
    popWebsocketConnectEvent();
    popWebsocketHookAfterConnectEvent();
    popWebsocketHookGetSubscribeRequestMessageEvent();
    popWebsocketSendMessageEvent(subscribeTopicMsg);
    
    // Message that will not be dispatched because topic not matching
    String msg = "{\"myTopic\":\"otherTopic\", \"payload\":\"foo\"}";
    ws.dispatchMessage(msg);
    checkNoEvents();
  
    checkNoEvents();
    checkNoError();
  }
  
  @Test
  public void testSubscribeToSingleTopicReceiveOneMessageWithoutTopicButDifferentFieldsOfBooleanNumberAndSubObjectTypes() throws Exception{
    wsManager = new DefaultWebsocketClient(ws, wsHook);
    wsManager.subscribeErrorHandler(errorHandler);
    popWebsocketAddErrorHandlerEvent();
    popWebsocketAddMessageHandlerEvent();
    popWebsocketHookInitEvent();
    String topic = "topic1";
    String subscribeTopicMsg = "subscribe:topic1";
    String unsubscribeTopicMsg = "unsubscribe:topic1";
    wsHook.setSubscribeRequestMessage(topic, subscribeTopicMsg);
    wsHook.setUnSubscribeRequestMessage(topic, unsubscribeTopicMsg);
    WebsocketMessageTopicMatcherFactory topicMatcher = WSMTMFUtil.value("myTopic", topic);
    wsManager.subscribe(topic, topicMatcher, wsMessageHandler1);
    popWebsocketHookBeforeConnectEvent();
    popWebsocketConnectEvent();
    popWebsocketHookAfterConnectEvent();
    popWebsocketHookGetSubscribeRequestMessageEvent();
    popWebsocketSendMessageEvent(subscribeTopicMsg);
    
    // Message that will not be dispatched because not a message with expected 'topic' field
    String msg = "{\"foo\":\"bar\", \"flag1\":true, \"flag2\": false, \"count\":3, \"price\":3.24 \"sub\":{}";
    ws.dispatchMessage(msg);
    checkNoEvents();
    checkNoError();
  }
  
  @Test
  public void testSubscribeSecondListenersToSingleTopicTriggersError() throws Exception{
    wsManager = new DefaultWebsocketClient(ws, wsHook);
    wsManager.subscribeErrorHandler(errorHandler);
    popWebsocketAddErrorHandlerEvent();
    popWebsocketAddMessageHandlerEvent();
    popWebsocketHookInitEvent();
    String topic = "topic1";
    String subscribeTopicMsg = "subscribe:topic1";
    String unsubscribeTopicMsg = "unsubscribe:topic1";
    wsHook.setSubscribeRequestMessage(topic, subscribeTopicMsg);
    wsHook.setUnSubscribeRequestMessage(topic, unsubscribeTopicMsg);
    WebsocketMessageTopicMatcherFactory topicMatcher = WSMTMFUtil.value("myTopic", "topic1");
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
    wsManager = new DefaultWebsocketClient(ws, wsHook);
    wsManager.setReconnectDelay(NO_EVENT_DELAY * 2);
    wsManager.subscribeErrorHandler(errorHandler);
    popWebsocketAddErrorHandlerEvent();
    popWebsocketAddMessageHandlerEvent();
    popWebsocketHookInitEvent();
    String topic = "topic1";
    String subscribeTopicMsg = "subscribe:topic1";
    String unsubscribeTopicMsg = "unsubscribe:topic1";
    wsHook.setSubscribeRequestMessage(topic, subscribeTopicMsg);
    wsHook.setUnSubscribeRequestMessage(topic, unsubscribeTopicMsg);
    WebsocketMessageTopicMatcherFactory topicMatcher = WSMTMFUtil.value("myTopic", "topic1");
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
    wsManager = new DefaultWebsocketClient(ws, wsHook);
    wsManager.setReconnectDelay(-1L);
    wsManager.subscribeErrorHandler(errorHandler);
    popWebsocketAddErrorHandlerEvent();
    popWebsocketAddMessageHandlerEvent();
    popWebsocketHookInitEvent();
    String topic = "topic1";
    String subscribeTopicMsg = "subscribe:topic1";
    String unsubscribeTopicMsg = "unsubscribe:topic1";
    wsHook.setSubscribeRequestMessage(topic, subscribeTopicMsg);
    wsHook.setUnSubscribeRequestMessage(topic, unsubscribeTopicMsg);
    WebsocketMessageTopicMatcherFactory topicMatcher = WSMTMFUtil.value("myTopic", "topic1");
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
    wsManager = new DefaultWebsocketClient(ws, null);
    wsManager.subscribeErrorHandler(errorHandler);
    popWebsocketAddErrorHandlerEvent();
    popWebsocketAddMessageHandlerEvent();
    String topic = "topic1";
    WebsocketMessageTopicMatcherFactory topicMatcher = WSMTMFUtil.value("myTopic", topic);
    wsManager.subscribe(topic, topicMatcher, wsMessageHandler1);
    popWebsocketConnectEvent();
    checkNoEvents();
    String msg1 = "{\"myTopic\":\"topic1\", \"payload\":\"Hello!\"}";
    ws.dispatchMessage(msg1);
    Assert.assertEquals(msg1, wsMessageHandler1.waitUntilCount(1).pop());
    wsMessageHandler1.checkNoEvents(NO_EVENT_DELAY);
    
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
    wsManager = new DefaultWebsocketClient(ws, wsHook);
    wsManager.setHeartBeatInterval(HEARTBEAT_INTERVAL);
    long noHeartBeatResponseTimeout = HEARTBEAT_INTERVAL + 25;
    wsManager.setNoHeartBeatResponseTimeout(noHeartBeatResponseTimeout);
    wsManager.subscribeErrorHandler(errorHandler);
    popWebsocketAddErrorHandlerEvent();
    popWebsocketAddMessageHandlerEvent();
    popWebsocketHookInitEvent();
    String topic = "topic1";
    String subscribeTopicMsg = "subscribe:topic1";
    String unsubscribeTopicMsg = "unsubscribe:topic1";
    wsHook.setSubscribeRequestMessage(topic, subscribeTopicMsg);
    wsHook.setUnSubscribeRequestMessage(topic, unsubscribeTopicMsg);
    String heartBeatMsg = "{\"myTopic\":\"heartBeat\", \"payload\":\"PING\"}";
    wsHook.setHeartBeatMessage(heartBeatMsg);
    String heartBeatResponseMsg = "{\"myTopic\":\"heartBeat\", \"payload\":\"PONG\"}";
    WebsocketMessageTopicMatcherFactory topicMatcher = WSMTMFUtil.value("myTopic", "topic1");
    HeartBeatResponseSystemMsgHandler hbHandler = new HeartBeatResponseSystemMsgHandler();
    wsManager.addSystemMessageHandler("heartBeat",  WSMTMFUtil.value("myTopic", "heartBeat"), hbHandler);
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
    popWebsocketHookGetHeartbeatMessageEvent();
    popWebsocketSendMessageEvent(heartBeatMsg);
    
    // Dispatch heartBeat response
    ws.dispatchMessage(heartBeatResponseMsg);
    Assert.assertEquals(1, hbHandler.count());
    checkNoEvents();
    
    // 2nd heartbeat should be sent
    popWebsocketHookGetHeartbeatMessageEvent();
    popWebsocketSendMessageEvent(heartBeatMsg);
    
    // After no heartbeat timeout elapsed, error should be raised
    sleep(noHeartBeatResponseTimeout);
    popError();
  }
  
  @Test
  public void testHeartBeatInitiatedByServerHandledBySystemMsgHandlerThatRespondsWithHeartBeatResponseThenHeartBeatTimeoutReconnectionThenAnotherHeartBeatHandled() throws Exception {
    wsManager = new DefaultWebsocketClient(ws, null);
    long noHeartBeatResponseTimeout = HEARTBEAT_INTERVAL + 25;
    wsManager.setNoHeartBeatResponseTimeout(noHeartBeatResponseTimeout);
    wsManager.subscribeErrorHandler(errorHandler);
    wsManager.setReconnectDelay(NO_EVENT_DELAY * 2);
    popWebsocketAddErrorHandlerEvent();
    popWebsocketAddMessageHandlerEvent();
    String heartBeatMsg = "{\"myTopic\":\"heartBeat\", \"payload\":\"PING\"}";
    String heartBeatResponseMsg = "{\"myTopic\":\"heartBeat\", \"payload\":\"PONG\"}";
    WebsocketMessageTopicMatcherFactory topicMatcher = WSMTMFUtil.value("myTopic", "topic1");
    HeartBeatRequestSystemMsgHandler hbHandler = new HeartBeatRequestSystemMsgHandler();
    hbHandler.heartBeatResponseMsg = heartBeatResponseMsg;
    wsManager.addSystemMessageHandler("heartBeat",  WSMTMFUtil.value("myTopic", "heartBeat"), hbHandler);
    checkNoEvents();
    wsManager.subscribe(null, topicMatcher, wsMessageHandler1);
    popWebsocketConnectEvent();
    ws.dispatchMessage(heartBeatMsg);
    
    popWebsocketSendMessageEvent(heartBeatResponseMsg);
    Assert.assertEquals(1, hbHandler.pingCount());
    
    checkNoEvents();
    checkNoError();
    
    // Error raised after heart beat response timeout has elapsed
    sleep(noHeartBeatResponseTimeout);
    popError();
    
    // Disconnection should be initiated upon error
    popWebsocketDisconnectEvent();
    
    // Then reconnection should be initiated
    popWebsocketConnectEvent();
    // Topic resubscription should be performed
    checkNoError();
    ws.dispatchMessage(heartBeatMsg);
    popWebsocketSendMessageEvent(heartBeatResponseMsg);
    Assert.assertEquals(2, hbHandler.pingCount());
    checkNoEvents();
    checkNoError();
  }
  
  @Test
  public void testHeartBeatInitiatedByBothClientAndServerHandledBySystemMsgHandlerThatRespondsWithHeartBeatResponseThenHeartBeatTimeoutReconnectionThenAnotherHeartBeatHandled() throws Exception {
    wsManager = new DefaultWebsocketClient(ws, wsHook);
    wsManager.setHeartBeatInterval(HEARTBEAT_INTERVAL);
    long noHeartBeatResponseTimeout = HEARTBEAT_INTERVAL + 25;
    wsManager.setNoHeartBeatResponseTimeout(noHeartBeatResponseTimeout);
    wsManager.subscribeErrorHandler(errorHandler);
    wsManager.setReconnectDelay(NO_EVENT_DELAY * 2);
    popWebsocketAddErrorHandlerEvent();
    popWebsocketAddMessageHandlerEvent();
    popWebsocketHookInitEvent();
    String topic = "topic1";
    String subscribeTopicMsg = "subscribe:topic1";
    String unsubscribeTopicMsg = "unsubscribe:topic1";
    wsHook.setSubscribeRequestMessage(topic, subscribeTopicMsg);
    wsHook.setUnSubscribeRequestMessage(topic, unsubscribeTopicMsg);
    String heartBeatMsg = "{\"myTopic\":\"heartBeat\", \"payload\":\"PING\"}";
    wsHook.setHeartBeatMessage(heartBeatMsg);
    String heartBeatResponseMsg = "{\"myTopic\":\"heartBeat\", \"payload\":\"PONG\"}";
    WebsocketMessageTopicMatcherFactory topicMatcher = WSMTMFUtil.value("myTopic", "topic1");
    HeartBeatRequestSystemMsgHandler hbHandler = new HeartBeatRequestSystemMsgHandler();
    hbHandler.heartBeatResponseMsg = heartBeatResponseMsg;
    wsManager.addSystemMessageHandler("heartBeat",  WSMTMFUtil.value("myTopic", "heartBeat"), hbHandler);
    checkNoEvents();
    wsManager.subscribe(topic, topicMatcher, wsMessageHandler1);
    popWebsocketHookBeforeConnectEvent();
    popWebsocketConnectEvent();
    popWebsocketHookAfterConnectEvent();
    popWebsocketHookGetSubscribeRequestMessageEvent();
    popWebsocketSendMessageEvent(subscribeTopicMsg);
    ws.dispatchMessage(heartBeatMsg);
    
    popWebsocketSendMessageEvent(heartBeatResponseMsg);
    Assert.assertEquals(1, hbHandler.pingCount());
    popWebsocketHookGetHeartbeatMessageEvent();
    popWebsocketSendMessageEvent(heartBeatMsg);
    ws.dispatchMessage(heartBeatResponseMsg);
    Assert.assertEquals(1, hbHandler.pongCount());
    
    checkNoEvents();
    checkNoError();
    
    // Error raised after heart beat response timeout has elapsed
    sleep(noHeartBeatResponseTimeout);
    popWebsocketHookGetHeartbeatMessageEvent();
    popWebsocketSendMessageEvent(heartBeatMsg);
    popError();
    
    // Disconnection should be initiated upon error
    popWebsocketHookBeforeDisconnectEvent();
    popWebsocketDisconnectEvent();
    popWebsocketHookAfterDisconnectEvent();
    
    // Then reconnection should be initiated
    popWebsocketHookBeforeConnectEvent();
    popWebsocketConnectEvent();
    popWebsocketHookAfterConnectEvent();
    popWebsocketHookGetSubscribeRequestMessageEvent();
    // Topic resubscription should be performed
    popWebsocketSendMessageEvent(subscribeTopicMsg);
    checkNoError();
    ws.dispatchMessage(heartBeatMsg);
    popWebsocketSendMessageEvent(heartBeatResponseMsg);
    Assert.assertEquals(2, hbHandler.pingCount());
    popWebsocketHookGetHeartbeatMessageEvent();
    popWebsocketSendMessageEvent(heartBeatMsg);
    ws.dispatchMessage(heartBeatResponseMsg);
    Assert.assertEquals(2, hbHandler.pongCount());
    checkNoEvents();
    checkNoError();
  }
  
  @Test
  public void testHeartBeatInitiatedByClientNullHeartBeatMessageFromWebsocketHookTriggersErrorEventButNoDisconnectionThenDisconnectionOccursForHeartBeatTimeout() throws Exception {
    wsManager = new DefaultWebsocketClient(ws, wsHook);
    wsManager.setHeartBeatInterval(HEARTBEAT_INTERVAL);
    long noHeartBeatResponseTimeout = HEARTBEAT_INTERVAL + 25;
    wsManager.setNoHeartBeatResponseTimeout(noHeartBeatResponseTimeout);
    wsManager.subscribeErrorHandler(errorHandler);
    popWebsocketAddErrorHandlerEvent();
    popWebsocketAddMessageHandlerEvent();
    popWebsocketHookInitEvent();
    String topic = "topic1";
    String subscribeTopicMsg = "subscribe:topic1";
    String unsubscribeTopicMsg = "unsubscribe:topic1";
    wsHook.setSubscribeRequestMessage(topic, subscribeTopicMsg);
    wsHook.setUnSubscribeRequestMessage(topic, unsubscribeTopicMsg);
    wsHook.setHeartBeatMessage(null);
    WebsocketMessageTopicMatcherFactory topicMatcher = WSMTMFUtil.value("myTopic", "topic1");
    checkNoEvents();
    wsManager.subscribe(topic, topicMatcher, wsMessageHandler1);
    popWebsocketHookBeforeConnectEvent();
    popWebsocketConnectEvent();
    popWebsocketHookAfterConnectEvent();
    popWebsocketHookGetSubscribeRequestMessageEvent();
    popWebsocketSendMessageEvent(subscribeTopicMsg);
    
    // First heartbeat should not be sent immediately
    checkNoEvents();
    
    // Null heartbeat message returned by websocket hook should trigger error.
    popWebsocketHookGetHeartbeatMessageEvent();
    popError();
    checkNoEvents();
    
    // After no heartbeat timeout elapsed, error should be raised
    sleep(noHeartBeatResponseTimeout);
    popError();
  }
  
  @Test
  public void testSubscribeToTwoTopicsReceiveOneMessageOnBothThenUnsubscribe() throws Exception{
    // Init WS manager
    wsManager = new DefaultWebsocketClient(ws, wsHook);
    wsManager.subscribeErrorHandler(errorHandler);
    popWebsocketAddErrorHandlerEvent();
    popWebsocketAddMessageHandlerEvent();
    popWebsocketHookInitEvent();
    
    // Subscribe to first topic
    String topic1 = "topic1";
    String subscribeTopic1Msg = "subscribe:topic1";
    String unsubscribeTopic1Msg = "unsubscribe:topic1";
    wsHook.setSubscribeRequestMessage(topic1, subscribeTopic1Msg);
    wsHook.setUnSubscribeRequestMessage(topic1, unsubscribeTopic1Msg);
    WebsocketMessageTopicMatcherFactory topicMatcher = WSMTMFUtil.value("myTopic", topic1);
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
    WebsocketMessageTopicMatcherFactory topic2Matcher = WSMTMFUtil.value("myTopic", topic2);
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
  public void testSubscribeToTwoTopicWithOneCommonTopicFieldReceiveOneMessageOnBothThenUnsubscribe() throws Exception{
    // Init WS manager
    wsManager = new DefaultWebsocketClient(ws, wsHook);
    wsManager.subscribeErrorHandler(errorHandler);
    popWebsocketAddErrorHandlerEvent();
    popWebsocketAddMessageHandlerEvent();
    popWebsocketHookInitEvent();
    
    // Subscribe to first topic
    String topic1 = "topic1";
    String subscribeTopic1Msg = "subscribe:topic1";
    String unsubscribeTopic1Msg = "unsubscribe:topic1";
    wsHook.setSubscribeRequestMessage(topic1, subscribeTopic1Msg);
    wsHook.setUnSubscribeRequestMessage(topic1, unsubscribeTopic1Msg);
    WebsocketMessageTopicMatcherFactory topicMatcher = WSMTMFUtil.value("myTopic", topic1);
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
    WebsocketMessageTopicMatcherFactory topic2Matcher = WSMTMFUtil.and(List.of(WSMTMFUtil.value("myTopic", topic1), WSMTMFUtil.value("myOtherTopic", "foo")));
    wsManager.subscribe(topic2, topic2Matcher, wsMessageHandler2);
    popWebsocketHookGetSubscribeRequestMessageEvent();
    popWebsocketSendMessageEvent(subscribeTopic2Msg);
    
    // Dispatch a message for topic 2 that should be received by both topic1 and topic2 listeners
    String msg2Topic2 = "{\"myTopic\":\"topic1\", \"payload\":\"Hello#2!\", \"myOtherTopic\":\"foo\"}";
    ws.dispatchMessage(msg2Topic2);
    Assert.assertEquals(msg2Topic2, wsMessageHandler1.waitUntilCount(1).pop());
    Assert.assertEquals(msg2Topic2, wsMessageHandler2.waitUntilCount(1).pop());
    wsMessageHandler1.checkNoEvents(NO_EVENT_DELAY);
    wsMessageHandler2.checkNoEvents(NO_EVENT_DELAY);
    
    wsManager.unsubscribe(topic1);
    popWebsocketHookGetUnsubscribeRequestMessageEvent();
    popWebsocketSendMessageEvent(unsubscribeTopic1Msg);
    
    // Unsubscribe from topic2 
    wsManager.unsubscribe(topic2);
    popWebsocketHookGetUnsubscribeRequestMessageEvent();
    popWebsocketSendMessageEvent(unsubscribeTopic2Msg);
    
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
  public void testErrorOnHeartBeatSendTriggersErrorEventAndReconnectionAndHeartBeatTimeoutAfterReonnection() throws Exception {
    wsManager = new DefaultWebsocketClient(ws, wsHook);
    wsManager.setReconnectDelay(HEARTBEAT_INTERVAL);
    wsManager.setHeartBeatInterval(HEARTBEAT_INTERVAL);
    long noHeartBeatResponseTimeout = HEARTBEAT_INTERVAL + 25;
    wsManager.setNoHeartBeatResponseTimeout(noHeartBeatResponseTimeout);
    wsManager.subscribeErrorHandler(errorHandler);
    popWebsocketAddErrorHandlerEvent();
    popWebsocketAddMessageHandlerEvent();
    popWebsocketHookInitEvent();
    String topic = "topic1";
    String subscribeTopicMsg = "subscribe:topic1";
    String unsubscribeTopicMsg = "unsubscribe:topic1";
    wsHook.setSubscribeRequestMessage(topic, subscribeTopicMsg);
    wsHook.setUnSubscribeRequestMessage(topic, unsubscribeTopicMsg);
    String heartBeatMsg = "{\"myTopic\":\"heartBeat\", \"payload\":\"PING\"}";
    wsHook.setHeartBeatMessage(heartBeatMsg);
    String heartBeatResponseMsg = "{\"myTopic\":\"heartBeat\", \"payload\":\"PONG\"}";
    WebsocketMessageTopicMatcherFactory topicMatcher = WSMTMFUtil.value("myTopic", "topic1");
    HeartBeatResponseSystemMsgHandler hbHandler = new HeartBeatResponseSystemMsgHandler();
    wsManager.addSystemMessageHandler("heartBeat",  WSMTMFUtil.value("myTopic", "heartBeat"), hbHandler);
    checkNoEvents();
    
    // Subscribe to topic
    long startTime = System.currentTimeMillis();
    wsManager.subscribe(topic, topicMatcher, wsMessageHandler1);
    popWebsocketHookBeforeConnectEvent();
    popWebsocketConnectEvent();
    popWebsocketHookAfterConnectEvent();
    popWebsocketHookGetSubscribeRequestMessageEvent();
    popWebsocketSendMessageEvent(subscribeTopicMsg);
    
    // Prepare throw for next websocket.send() call
    ws.addExceptionToThrowOnSend("Failed to send heartbeat!");
    // First heartbeat should not be sent immediately
    checkNoEvents();
    
    // First heartbeat should be sent
    popWebsocketHookGetHeartbeatMessageEvent();
    popWebsocketSendMessageEvent(heartBeatMsg);
    popError();

    // Expect reconnection attempt
    // Disconnection should be initiated upon error
    popWebsocketHookBeforeDisconnectEvent();
    popWebsocketDisconnectEvent();
    popWebsocketHookAfterDisconnectEvent();
    
    // reconnect delay must be awaited before attempting reconnection
    long expectedReconnectTime = HEARTBEAT_INTERVAL - System.currentTimeMillis() + startTime;
    checkNoEvents(expectedReconnectTime - 15L); // 15ms tolerance
    
    // Then reconnection should be initiated
    popWebsocketHookBeforeConnectEvent();
    popWebsocketConnectEvent();
    popWebsocketHookAfterConnectEvent();
    popWebsocketHookGetSubscribeRequestMessageEvent();
    // Topic resubscription should be performed
    popWebsocketSendMessageEvent(subscribeTopicMsg);
    checkNoError();
    
    // Dispatch heartBeat response
    ws.dispatchMessage(heartBeatResponseMsg);
    Assert.assertEquals(1, hbHandler.count());
    checkNoEvents();
    
    // Heartbeat should be sent after heartbeat interval has elapsed
    popWebsocketHookGetHeartbeatMessageEvent();
    popWebsocketSendMessageEvent(heartBeatMsg);
    
    // After no heartbeat timeout elapsed, error should be raised
    sleep(noHeartBeatResponseTimeout);
    popError();
  }
  
  @Test
  public void testConnectionInitiatedBySubscribingToDefaultTopicNoMessageTimeoutTriggersReconnectionThenAnotherMessageTimeoutTriggersSecondReconnection() throws Exception {
    wsManager = new DefaultWebsocketClient(ws, null);
    popWebsocketAddErrorHandlerEvent();
    popWebsocketAddMessageHandlerEvent();
    wsManager.setReconnectDelay(HEARTBEAT_INTERVAL * 4);
    long noMessageTimeout = NO_EVENT_DELAY * 2 + 50L;
    wsManager.setNoMessageTimeout(noMessageTimeout);
    wsManager.subscribeErrorHandler(errorHandler);
    wsManager.subscribe(null, null, wsMessageHandler1);
    popWebsocketConnectEvent();
    
    checkNoEvents();
    checkNoEvents();
    // No message received for longer delay than noMessageTimeout 
    // triggers error and disconnection/reconnection attempt.
    popError();
    
    // Expect reconnection attempt
    // Disconnection should be initiated upon error
    popWebsocketDisconnectEvent();
    
    // Some delay must be awaited before attempting reconnection
    checkNoEvents();
    popWebsocketConnectEvent();
    
    checkNoEvents();
    checkNoEvents();
    // 2nd no message timeout triggers a 2nd reconnection.
    popError();
    
    // Expect reconnection attempt
    // Disconnection should be initiated upon error
    popWebsocketDisconnectEvent();
    
    // reconnect delay must be awaited before attempting reconnection
    checkNoEvents();
    
    // Then reconnection should be initiated
    popWebsocketConnectEvent();
  }
  
  @Test(expected = IllegalStateException.class)
  public void testHeartBeatIntervalSetButNoWebsocketHookThrowsException() {
    wsManager = new DefaultWebsocketClient(ws, null);
    wsManager.setHeartBeatInterval(HEARTBEAT_INTERVAL);
  }
  
  @Test
  public void testHeartBeatIntervalSetToNegativeValueWhenNoWebsocketHook() {
    wsManager = new DefaultWebsocketClient(ws, null);
    wsManager.setHeartBeatInterval(-2L);
    Assert.assertEquals(-2L, wsManager.getHeartBeatInterval());
  }
  
  @Test
  public void testErrorRaisedWhenSendingUnsubscriptionFromTopic() throws Exception {
    wsManager = new DefaultWebsocketClient(ws, wsHook);
    wsManager.subscribeErrorHandler(errorHandler);
    popWebsocketAddErrorHandlerEvent();
    popWebsocketAddMessageHandlerEvent();
    popWebsocketHookInitEvent();
    String topic = "topic1";
    String subscribeTopicMsg = "subscribe:topic1";
    String unsubscribeTopicMsg = "unsubscribe:topic1";
    wsHook.setSubscribeRequestMessage(topic, subscribeTopicMsg);
    wsHook.setUnSubscribeRequestMessage(topic, unsubscribeTopicMsg);
    WebsocketMessageTopicMatcherFactory topicMatcher = WSMTMFUtil.value("myTopic", topic);
    wsManager.subscribe(topic, topicMatcher, wsMessageHandler1);
    popWebsocketHookBeforeConnectEvent();
    popWebsocketConnectEvent();
    popWebsocketHookAfterConnectEvent();
    popWebsocketHookGetSubscribeRequestMessageEvent();
    popWebsocketSendMessageEvent(subscribeTopicMsg);
    String msg1 = "{\"myTopic\":\"topic1\", \"payload\":\"Hello!\"}";
    ws.dispatchMessage(msg1);
    Assert.assertEquals(msg1, wsMessageHandler1.waitUntilCount(1).pop());
    ws.addExceptionToThrowOnSend("Error unsubscribing from " + topic);
    wsManager.unsubscribe(topic);
    popWebsocketHookGetUnsubscribeRequestMessageEvent();
    popWebsocketSendMessageEvent(unsubscribeTopicMsg);
    popError();
  }
  
  @Test
  public void testErrorAndDisconnectionRaisedFromWebsocketHookOnFirstCallToBeforeConnectThenSuccessfulReconnection() throws Exception {
    wsManager = new DefaultWebsocketClient(ws, null);
    wsManager.subscribeErrorHandler(errorHandler);
    wsManager.setReconnectDelay(NO_EVENT_DELAY * 2);
    popWebsocketAddErrorHandlerEvent();
    popWebsocketAddMessageHandlerEvent();
    
    // Send first message. Will trigger connection of WS prior to message sending.
    String msg1 =  "{\"greetings\":\"Hi!\"";
    Assert.assertNull(wsManager.sendAsync(msg1).get());
    popWebsocketConnectEvent();
    popWebsocketSendMessageEvent(msg1);
    checkNoEvents();
    
    // Send 2nd message that will trigger exception on WS when sending.
    ws.addExceptionToThrowOnSend("Test error on send");
    Assert.assertNotNull(wsManager.sendAsync(msg1).get());
    popWebsocketSendMessageEvent(msg1);
    popError();
    popWebsocketDisconnectEvent();
    Assert.assertFalse(wsManager.isConnected());
    
    // Successful reconnection
    popWebsocketConnectEvent();
    checkNoEvents();
    Assert.assertTrue(wsManager.isConnected());
    Assert.assertNull(wsManager.sendAsync(msg1).get());
    popWebsocketSendMessageEvent(msg1);
    checkNoEvents();
    checkNoError();
  }
  
  @Test
  public void testErrorOnConnectionWhileSubscribingToSecondTopicSuccessfulReconnectionThenUnsubscribeAndDispose() throws Exception{
    wsManager = new DefaultWebsocketClient(ws, wsHook);
    wsManager.subscribeErrorHandler(errorHandler);
    wsManager.setReconnectDelay(NO_EVENT_DELAY * 2);
    popWebsocketAddErrorHandlerEvent();
    popWebsocketAddMessageHandlerEvent();
    popWebsocketHookInitEvent();
    String topic = "topic1";
    String subscribeTopicMsg = "subscribe:topic1";
    String unsubscribeTopicMsg = "unsubscribe:topic1";
    wsHook.setSubscribeRequestMessage(topic, subscribeTopicMsg);
    wsHook.setUnSubscribeRequestMessage(topic, unsubscribeTopicMsg);
    WebsocketMessageTopicMatcherFactory topicMatcher = WSMTMFUtil.value("myTopic", topic);
    ws.addExceptionToThrowOnConnect("Test error on connect");
    wsManager.subscribe(topic, topicMatcher, wsMessageHandler1);
    popWebsocketHookBeforeConnectEvent();
    popWebsocketConnectEvent();
    // Error raised in connect
    popError();
    
    // No event should be raised before reconnect delay has elapsed
    checkNoEvents();
    
    // Successful reconnection
    popWebsocketHookBeforeConnectEvent();
    popWebsocketConnectEvent();
    popWebsocketHookAfterConnectEvent();
    popWebsocketHookGetSubscribeRequestMessageEvent();
    popWebsocketSendMessageEvent(subscribeTopicMsg);
    

    
    String msg1 = "{\"myTopic\":\"topic1\", \"payload\":\"Hello!\"}";
    ws.dispatchMessage(msg1);
    Assert.assertEquals(msg1, wsMessageHandler1.waitUntilCount(1).pop());
    wsManager.unsubscribe(topic);
    popWebsocketHookGetUnsubscribeRequestMessageEvent();
    popWebsocketSendMessageEvent(unsubscribeTopicMsg);
    wsMessageHandler1.checkNoEvents(NO_EVENT_DELAY);
    checkNoEvents();
    
    wsManager.dispose();
    popWebsocketHookBeforeDisconnectEvent();
    popWebsocketRemoveMessageHandlerEvent();
    popWebsocketDisconnectEvent();
    popWebsocketRemoveErrorHandlerEvent();
    popWebsocketHookAfterDisconnectEvent();
    checkNoError();
  }
  
  @Test
  public void testSuccessfulConnectionThenDisposeManagerExceptionThrownDuringWebsocketHookBeforeDisconnectiIsNotifiedAndWebsocketIsDisconnected() throws Exception {
    wsHook = new MockWebsocketHook() {
      @Override
      public void beforeDisconnect() throws WebsocketException {
        super.beforeDisconnect();
        throw new WebsocketException("Error from WebsocketHook before disconnect");
      }
    };
    wsManager = new DefaultWebsocketClient(ws, wsHook);
    wsManager.subscribeErrorHandler(errorHandler);
    wsManager.setReconnectDelay(NO_EVENT_DELAY * 2);
    popWebsocketAddErrorHandlerEvent();
    popWebsocketAddMessageHandlerEvent();
    popWebsocketHookInitEvent();
    
    String subscribeMsg = "subscribe";
    String unsubscribeMsg = "unsubscribe";
    wsHook.setSubscribeRequestMessage(null, subscribeMsg);
    wsHook.setUnSubscribeRequestMessage(null, unsubscribeMsg);
    wsManager.subscribe(null, WebsocketMessageTopicMatcherFactory.ANY_MATCHER_FACTORY, wsMessageHandler1);
    popWebsocketHookBeforeConnectEvent();
    popWebsocketConnectEvent();
    popWebsocketHookAfterConnectEvent();
    popWebsocketHookGetSubscribeRequestMessageEvent();
    popWebsocketSendMessageEvent(subscribeMsg);
    checkNoEvents();
    wsManager.dispose();
    popWebsocketHookBeforeDisconnectEvent();
    popWebsocketHookAfterDisconnectEvent();
    popWebsocketRemoveMessageHandlerEvent();
    popWebsocketDisconnectEvent();
    popWebsocketRemoveErrorHandlerEvent();
    popError();
    Assert.assertFalse(wsManager.isConnected());
    Assert.assertFalse(ws.isConnected());
    checkNoEvents();
  }
  
  @Test
  public void testSuccessfulConnectionThenDisposeManagerExceptionThrownDuringWebsocketHookAfterDisconnectiIsNotifiedAndWebsocketIsDisconnected() throws Exception {
    wsHook = new MockWebsocketHook() {
      @Override
      public void afterDisconnect() throws WebsocketException {
        super.afterDisconnect();
        throw new WebsocketException("Error from WebsocketHook after disconnect");
      }
    };
    wsManager = new DefaultWebsocketClient(ws, wsHook);
    wsManager.subscribeErrorHandler(errorHandler);
    wsManager.setReconnectDelay(NO_EVENT_DELAY * 2);
    popWebsocketAddErrorHandlerEvent();
    popWebsocketAddMessageHandlerEvent();
    popWebsocketHookInitEvent();
    
    String subscribeMsg = "subscribe";
    String unsubscribeMsg = "unsubscribe";
    wsHook.setSubscribeRequestMessage(null, subscribeMsg);
    wsHook.setUnSubscribeRequestMessage(null, unsubscribeMsg);
    wsManager.subscribe(null, WebsocketMessageTopicMatcherFactory.ANY_MATCHER_FACTORY, wsMessageHandler1);
    popWebsocketHookBeforeConnectEvent();
    popWebsocketConnectEvent();
    popWebsocketHookAfterConnectEvent();
    popWebsocketHookGetSubscribeRequestMessageEvent();
    popWebsocketSendMessageEvent(subscribeMsg);
    checkNoEvents();
    log.info("Disposing {}", wsManager);
    wsManager.dispose();
    popWebsocketRemoveMessageHandlerEvent();
    popWebsocketDisconnectEvent();
    popWebsocketHookBeforeDisconnectEvent();
    popWebsocketHookAfterDisconnectEvent();
    popWebsocketRemoveErrorHandlerEvent();
    popError();
    Assert.assertFalse(wsManager.isConnected());
    Assert.assertFalse(ws.isConnected());
    checkNoEvents();
  }
  
  @Test
  public void testSendMessageWhileDisconnectedTriggersConnectionAndSendingOnWsThenDisposeWsManagerAndSendAsyncAndSyncMessageThatShouldNotBeSentToWs() throws Exception {
    wsManager = new DefaultWebsocketClient(ws, null);
    wsManager.subscribeErrorHandler(errorHandler);
    popWebsocketAddErrorHandlerEvent();
    popWebsocketAddMessageHandlerEvent();
    String msg =  "{\"greetings\":\"Hi!\"}";
    Assert.assertNull(wsManager.sendAsync(msg).get());
    popWebsocketConnectEvent();
    popWebsocketSendMessageEvent(msg);
    checkNoEvents();
    wsManager.dispose();
    popWebsocketRemoveMessageHandlerEvent();
    popWebsocketDisconnectEvent();
    popWebsocketRemoveErrorHandlerEvent();
    Assert.assertFalse(wsManager.isConnected());
    checkNoEvents();
    Assert.assertNotNull(wsManager.sendAsync(msg).get());
    try {
      wsManager.send(msg);
      Assert.fail("Should have raised IllegalStateException");
    } catch (IllegalStateException ex) {
      checkNoEvents();
    }
  }
  
  @Test
  public void testErrorOnConnectWhileSendingFirstMessageThenErrorOnReconnectAttemptThenSuccessfulConnectionAndMessageSent() throws Exception {
    wsManager = new DefaultWebsocketClient(ws, null);
    wsManager.setReconnectDelay(NO_EVENT_DELAY * 2);
    wsManager.subscribeErrorHandler(errorHandler);
    popWebsocketAddErrorHandlerEvent();
    popWebsocketAddMessageHandlerEvent();
    ws.addExceptionToThrowOnConnect("Error on first connection attempt");
    ws.addExceptionToThrowOnConnect("Error on second connection attempt");
    String msg =  "{\"greetings\":\"Hi!\"}";
    CompletableFuture<WebsocketException> sendMessageResult = wsManager.sendAsync(msg);
    popWebsocketConnectEvent();
    popError();
    Assert.assertNotNull(sendMessageResult.get());
    
    // First reconnection attempt performed quickly after error
    popWebsocketConnectEvent();
    popError();
    
    // Wait some time before 2nd reconnection attempt is performed
    checkNoEvents();
    popWebsocketConnectEvent();
    checkNoEvents();
    Assert.assertTrue(wsManager.isConnected());
  }
  
  @Test
  public void testRemoveErrorHandler() throws Exception {
    // Initialize and manager and connect WS by sending a message
    wsManager = new DefaultWebsocketClient(ws, null);
    wsManager.subscribeErrorHandler(errorHandler);
    popWebsocketAddErrorHandlerEvent();
    popWebsocketAddMessageHandlerEvent();
    String msg =  "{\"greetings\":\"Hi!\"}";
    Assert.assertNull(wsManager.sendAsync(msg).get());
    popWebsocketConnectEvent();
    popWebsocketSendMessageEvent(msg);
    checkNoEvents();
    
    // Unsubscribe error handler
    Assert.assertTrue(wsManager.unsubscribeErrorHandler(errorHandler));
    
    // Prepare error that will be raised on next call to send, and send another message that will trigger it.
    ws.addExceptionToThrowOnSend("Error while sending message");
    // Error is notified to returned CompletableFuture<WebsocketException> from sendAsync() but not notified to unsubscribed error handler
    Assert.assertNotNull(wsManager.sendAsync(msg).get());
    errorHandler.checkNoEvents(NO_EVENT_DELAY);
  }
  
  @Test
  public void testUnsubscribeInvalidTopicIsIgnored() throws Exception {
    wsManager = new DefaultWebsocketClient(ws, wsHook);
    wsManager.subscribeErrorHandler(errorHandler);
    wsManager.setReconnectDelay(NO_EVENT_DELAY * 2);
    popWebsocketAddErrorHandlerEvent();
    popWebsocketAddMessageHandlerEvent();
    popWebsocketHookInitEvent();
    
    String subscribeMsg = "subscribe";
    String unsubscribeMsg = "unsubscribe";
    wsHook.setSubscribeRequestMessage(null, subscribeMsg);
    wsHook.setUnSubscribeRequestMessage(null, unsubscribeMsg);
    wsManager.unsubscribe("foo");
    checkNoEvents();
  }
  
  @Test
  public void testDisposeDuringWaitReconnect() throws Exception {
    wsManager = new DefaultWebsocketClient(ws, null);
    wsManager.setReconnectDelay(NO_EVENT_DELAY * 10);
    wsManager.subscribeErrorHandler(errorHandler);
    popWebsocketAddErrorHandlerEvent();
    popWebsocketAddMessageHandlerEvent();
    ws.addExceptionToThrowOnConnect("Error on first connection attempt");
    ws.addExceptionToThrowOnConnect("Error on second connection attempt");
    String msg =  "{\"greetings\":\"Hi!\"}";
    CompletableFuture<WebsocketException> sendMessageResult = wsManager.sendAsync(msg);
    popWebsocketConnectEvent();
    popError();
    Assert.assertNotNull(sendMessageResult.get());
    
    wsManager.dispose();
    popWebsocketRemoveMessageHandlerEvent();
    popWebsocketRemoveErrorHandlerEvent();
    checkNoEvents();
    Assert.assertFalse(wsManager.isConnected());
    Assert.assertTrue(wsManager.isDisposed());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testSubscribeWhenDisposeThrows() {
    wsManager = new DefaultWebsocketClient(ws, null);
    wsManager.dispose();
    String topic = "topic1";
    String subscribeTopicMsg = "subscribe:topic1";
    WebsocketMessageTopicMatcherFactory topicMatcher = WSMTMFUtil.value("myTopic", topic);
    wsManager.subscribe(subscribeTopicMsg, topicMatcher, wsMessageHandler1);
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
  
  private MockWebsocketHookEvent popWebsocketHookInitEvent() throws TimeoutException {
    MockWebsocketHookEvent event = wsHook.waitUntilCount(1).pop();
    Assert.assertEquals(MockWebsocketHookEventType.INIT, event.getType());
    Assert.assertEquals(wsHook, event.getSource());
    Assert.assertEquals(wsManager, event.getWebsocketClient());
    return event;
  }
  
  private MockWebsocketHookEvent popWebsocketHookBeforeConnectEvent() throws TimeoutException {
    MockWebsocketHookEvent event = wsHook.waitUntilCount(1).pop();
    Assert.assertEquals(MockWebsocketHookEventType.BEFORE_CONNECT, event.getType());
    Assert.assertEquals(wsHook, event.getSource());
    return event;
  }
  
  private MockWebsocketHookEvent popWebsocketHookAfterConnectEvent() throws TimeoutException {
    MockWebsocketHookEvent event = wsHook.waitUntilCount(1).pop();
    Assert.assertEquals(MockWebsocketHookEventType.AFTER_CONNECT, event.getType());
    Assert.assertEquals(wsHook, event.getSource());
    return event;
  }
  
  private MockWebsocketHookEvent popWebsocketHookBeforeDisconnectEvent() throws TimeoutException {
    MockWebsocketHookEvent event = wsHook.waitUntilCount(1).pop();
    Assert.assertEquals(MockWebsocketHookEventType.BEFORE_DISCONNECT, event.getType());
    Assert.assertEquals(wsHook, event.getSource());
    return event;
  }
  
  private MockWebsocketHookEvent popWebsocketHookAfterDisconnectEvent() throws TimeoutException {
    MockWebsocketHookEvent event = wsHook.waitUntilCount(1).pop();
    Assert.assertEquals(MockWebsocketHookEventType.AFTER_DISCONNECT, event.getType());
    Assert.assertEquals(wsHook, event.getSource());
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
  
  private void checkNoEvents() throws InterruptedException {
    checkNoEvents(NO_EVENT_DELAY);
  }
  
  private void checkNoEvents(long delay) throws InterruptedException {
    if (delay <= 0L) {
      return;
    }
    log.debug("Check no events during {}ms", delay);
    sleep(delay);
    if (ws.size() > 0) {
      Assert.fail("Received unexpected WebsocketEvent" + ws.pop());
    }
    
    if (wsHook.size() > 0) {
      Assert.fail("Received unexpected WebsocketHookEvent:" + wsHook.pop());
    }
    
    if (wsMessageHandler1.size() > 0) {
      Assert.fail("Received unexpected message event on handler1:" + wsMessageHandler1.pop());
    }
    
    if (wsMessageHandler2.size() > 0) {
      Assert.fail("Received unexpected message event on handler2:" + wsMessageHandler2.pop());
    }
  }
  
  private void sleep(long delay) throws InterruptedException {
    DemoUtil.sleep(delay);
  }
  
  private WebsocketException popError() throws TimeoutException {
    WebsocketException ex = this.errorHandler.waitUntilCount(1).pop();
    Assert.assertNotNull(ex);
    return ex;
  }
  
  private void checkNoError() throws InterruptedException {
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
    
    AtomicInteger receivedHeartBeatPings = new AtomicInteger(0);
    AtomicInteger receivedHeartBeatPongs = new AtomicInteger(0);

    @Override
    public void handleWebsocketMessage(String message) {
      wsManager.hearbeatReceived();
      if (message.contains("PING")) {
        receivedHeartBeatPings.incrementAndGet();
        wsManager.sendAsync(heartBeatResponseMsg);
        wsManager.hearbeatReceived();
      } else if (message.contains("PONG")) {
        receivedHeartBeatPongs.incrementAndGet();
      }
      
      
    }
    
    public int pingCount() {
      return receivedHeartBeatPings.get();
    }
    
    public int pongCount() {
      return receivedHeartBeatPongs.get();
    }
  }


}
