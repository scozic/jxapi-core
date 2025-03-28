package org.jxapi.netutils.websocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jxapi.exchange.ExchangeApiEvent;
import org.jxapi.exchange.ExchangeApiEventType;
import org.jxapi.exchange.ExchangeApiObserver;
import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.netutils.websocket.mock.MockWebsocket;
import org.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatcherFactory;
import org.jxapi.observability.GenericObserver;
import org.jxapi.observability.MockExchangeApiObserver;
import org.jxapi.util.JsonUtil;

/**
 * Unit test for {@link DefaultWebsocketEndpoint}
 */
public class DefaultWebsocketEndpointTest {
  
  private MockWebsocketManager websocketManager;
  private MockExchangeApiObserver apiObserver;
  private TestWebsocketEndpoint websocketEndpoint;
  
  @Before
  public void setUp() {
    websocketManager = new MockWebsocketManager();
    apiObserver = new MockExchangeApiObserver();
    websocketEndpoint = new TestWebsocketEndpoint("myWsEndpoint", websocketManager, apiObserver);
  }
  
  @After
  public void tearDown() {
    websocketManager.dispose();
    websocketManager = null;
    apiObserver = null;
    websocketEndpoint = null;
  }
  
  @Test
  public void testGetEndpointName() {
    Assert.assertEquals("myWsEndpoint", websocketEndpoint.getEndpointName());
  }

  @Test
  public void testGenerateSubscriptionId() {
    WebsocketSubscribeRequest request = new WebsocketSubscribeRequest();
    request.setTopic("myTopic");
    Assert.assertEquals("myTopic-0", websocketEndpoint.generateSubscriptionId(request));
    Assert.assertEquals("myTopic-1", websocketEndpoint.generateSubscriptionId(request));
  }

  @Test
  public void testDispatchApiEvent() {
    ExchangeApiEvent event = ExchangeApiEvent.createWebsocketSubscribeEvent(new WebsocketSubscribeRequest(), "subId");
    websocketEndpoint.dispatchApiEvent(event);
    Assert.assertEquals(1, apiObserver.size());
    Assert.assertEquals(event, apiObserver.pop());
  }

  @Test
  public void testSubscribeToSingleTopic() {
    String topic = "topic1";
    WebsocketSubscribeRequest subscribeRequest = WebsocketSubscribeRequest.create(null, topic, WebsocketMessageTopicMatcherFactory.ANY_MATCHER_FACTORY);
    TestMessageListener listener = new TestMessageListener();
    String subId = websocketEndpoint.subscribe(subscribeRequest, listener);
    Assert.assertNotNull(subId);
    popWebsocketSubscribeRequestEvent(subscribeRequest, subId);
    TestMessage testMessage = TestMessage.create(topic, "foo");
    websocketManager.dispatchMessage(topic, testMessage);
    Assert.assertEquals(1, listener.size());
    Assert.assertEquals(testMessage, listener.pop());
  }
  
  @Test
  public void testSubscribeTwiceToSingleTopic() {
    String topic = "topic1";
    WebsocketSubscribeRequest subscribeRequest = WebsocketSubscribeRequest.create(null, topic, WebsocketMessageTopicMatcherFactory.ANY_MATCHER_FACTORY);
    TestMessageListener listener1 = new TestMessageListener();
    TestMessageListener listener2 = new TestMessageListener();
    String subId1 = websocketEndpoint.subscribe(subscribeRequest, listener1);
    Assert.assertNotNull(subId1);
    popWebsocketSubscribeRequestEvent(subscribeRequest, subId1);
    Assert.assertEquals(1, websocketManager.subscribeRequests.size());
    Assert.assertTrue(websocketManager.subscribeRequests.containsKey(topic));
    String subId2 = websocketEndpoint.subscribe(subscribeRequest, listener2);
    popWebsocketSubscribeRequestEvent(subscribeRequest, subId2);
    Assert.assertNotNull(subId2);
    Assert.assertNotEquals(subId1, subId2);
    TestMessage testMessage = TestMessage.create(topic, "foo");
    websocketManager.dispatchMessage(topic, testMessage);
    Assert.assertEquals(1, listener1.size());
    Assert.assertEquals(testMessage, listener1.pop());
    Assert.assertEquals(1, listener2.size());
    Assert.assertEquals(testMessage, listener2.pop());
  }
  
  @Test
  public void testUnSubscribeFromSingleTopic() throws Exception {
    String topic = "topic1";
    WebsocketSubscribeRequest subscribeRequest = WebsocketSubscribeRequest.create(null, topic, WebsocketMessageTopicMatcherFactory.ANY_MATCHER_FACTORY);
    TestMessageListener listener1 = new TestMessageListener();
    TestMessageListener listener2 = new TestMessageListener();
    String subId1 = websocketEndpoint.subscribe(subscribeRequest, listener1);
    popWebsocketSubscribeRequestEvent(subscribeRequest, subId1);
    Assert.assertEquals(1, websocketManager.subscribeRequests.size());
    Assert.assertTrue(websocketManager.subscribeRequests.containsKey(topic));
    Assert.assertNotNull(subId1);
    String subId2 = websocketEndpoint.subscribe(subscribeRequest, listener2);
    popWebsocketSubscribeRequestEvent(subscribeRequest, subId2);
    Assert.assertNotNull(subId2);
    Assert.assertNotEquals(subId1, subId2);
    
    websocketEndpoint.unsubscribe(subId1);
    popWebsocketUnsubscribeRequestEvent(subId1);
    Assert.assertEquals(0, websocketManager.unsubscribeRequests.size());
    
    TestMessage testMessage = TestMessage.create(topic, "foo");
    websocketManager.dispatchMessage(topic, testMessage);
    Assert.assertEquals(0, listener1.size());
    Assert.assertEquals(1, listener2.size());
    Assert.assertEquals(testMessage, listener2.pop());
    popWebsocketMessageEvent(testMessage);
    
    websocketEndpoint.unsubscribe(subId2);
    popWebsocketUnsubscribeRequestEvent(subId2);
    Assert.assertEquals(1, websocketManager.unsubscribeRequests.size());
    Assert.assertEquals(topic, websocketManager.unsubscribeRequests.get(0));
    
    TestMessage testMessage3 = TestMessage.create(topic, "bar");
    websocketManager.dispatchMessage(topic, testMessage3);
    Assert.assertEquals(0, listener1.size());
    Assert.assertEquals(0, listener2.size());
    checkNoExchangeApiEvents();
  }
  
  @Test
  public void testUnsubscribeInvalidSubscriptionId() {
    String topic = "topic1";
    WebsocketSubscribeRequest subscribeRequest = WebsocketSubscribeRequest.create(null, topic, WebsocketMessageTopicMatcherFactory.ANY_MATCHER_FACTORY);
    TestMessageListener listener = new TestMessageListener();
    websocketEndpoint.subscribe(subscribeRequest, listener);
    Assert.assertFalse(websocketEndpoint.unsubscribe("foo"));
  }
  
  @Test
  public void testDispatchInvalidMessage() {
    String topic = "topic1";
    WebsocketSubscribeRequest subscribeRequest = WebsocketSubscribeRequest.create(null, topic, WebsocketMessageTopicMatcherFactory.ANY_MATCHER_FACTORY);
    TestMessageListener listener = new TestMessageListener();
    String subId = websocketEndpoint.subscribe(subscribeRequest, listener);
    Assert.assertNotNull(subId);
    popWebsocketSubscribeRequestEvent(subscribeRequest, subId);
    
    // Invalid message because it has additionnal 'success' field not expected by message deserializer for TestMessage class
    TestMessageChild testMessage = TestMessageChild.create(topic, "foo", true);
    websocketManager.dispatchMessage(topic, testMessage);
    Assert.assertEquals(0, listener.size());
    popWebsocketErrorEvent();
  }
  
  @Test
  public void testSubscribeAndReceiveMessageAndUnsubscribeNoExchangeApiObserver() {
    websocketEndpoint = new TestWebsocketEndpoint("myWsEndpoint", websocketManager, null);
    String topic = "topic1";
    WebsocketSubscribeRequest subscribeRequest = WebsocketSubscribeRequest.create(null, topic, WebsocketMessageTopicMatcherFactory.ANY_MATCHER_FACTORY);
    TestMessageListener listener = new TestMessageListener();
    String subId = websocketEndpoint.subscribe(subscribeRequest, listener);
    Assert.assertNotNull(subId);
    TestMessage testMessage = TestMessage.create(topic, "foo");
    websocketManager.dispatchMessage(topic, testMessage);
    Assert.assertEquals(1, listener.size());
    Assert.assertEquals(testMessage, listener.pop());
    websocketEndpoint.unsubscribe(subId);
    
  }
  private void popWebsocketSubscribeRequestEvent(WebsocketSubscribeRequest request, String subscriptionId) {
    ExchangeApiEvent e = apiObserver.pop();
    Assert.assertEquals(ExchangeApiEventType.WEBSOCKET_SUBSCRIBE, e.getType());
    Assert.assertEquals(request, e.getWebsocketSubscribeRequest());
    Assert.assertEquals(subscriptionId, e.getWebsocketSubscriptionId());
  }
  
  private void popWebsocketUnsubscribeRequestEvent(String subscriptionId) {
    ExchangeApiEvent e = apiObserver.pop();
    Assert.assertEquals(ExchangeApiEventType.WEBSOCKET_UNSUBSCRIBE, e.getType());
    Assert.assertEquals(subscriptionId, e.getWebsocketSubscriptionId());
  }
  
  private void popWebsocketMessageEvent(TestMessage tm) {
    ExchangeApiEvent e = apiObserver.pop();
    Assert.assertEquals(ExchangeApiEventType.WEBSOCKET_MESSAGE, e.getType());
    Assert.assertEquals(tm.toString(), e.getWebsocketMessage());
    Assert.assertEquals(tm.getMyTopic(), e.getWebsocketSubscribeRequest().getTopic());
  }
  
  private void popWebsocketErrorEvent() {
    ExchangeApiEvent e = apiObserver.pop();
    Assert.assertEquals(ExchangeApiEventType.WEBSOCKET_ERROR, e.getType());
    Assert.assertNotNull(e.getWebsocketError());
  }
  
  private void checkNoExchangeApiEvents() throws InterruptedException {
    apiObserver.checkNoEvents(0);
  }
  
  private class TestWebsocketEndpoint extends DefaultWebsocketEndpoint<TestMessage> {

    public TestWebsocketEndpoint(String endpointName, 
                   WebsocketManager websocketManager, 
                   ExchangeApiObserver observer) {
      super(endpointName, websocketManager,  new TestMessageDeserializer(), observer);
    }
    
    @Override
    public String generateSubscriptionId(WebsocketSubscribeRequest request) {
      return super.generateSubscriptionId(request);
    }
    
    @Override
    public void dispatchApiEvent(ExchangeApiEvent event) {
      super.dispatchApiEvent(event);
    }
    
  }

  private static class MockWebsocketManager extends DefaultWebsocketManager {

    public MockWebsocketManager() {
      super(null, new MockWebsocket(), null);
    }
    
    final Map<String, RawWebsocketMessageHandler> subscribeRequests = new HashMap<>();
    final List<String> unsubscribeRequests = new ArrayList<>();
    
    @Override
    public void subscribe(String topic, 
                WebsocketMessageTopicMatcherFactory matcherFactory,
                RawWebsocketMessageHandler messageHandler) {
      if (subscribeRequests.containsKey(topic)) {
        throw new IllegalStateException("Already have a subscription for topic:" + topic);
      }
      subscribeRequests.put(topic, messageHandler);
    }
    
    @Override
    public void unsubscribe(String topic) {
      unsubscribeRequests.add(topic);
    }
    
    public void dispatchMessage(String topic, TestMessage m) {
      RawWebsocketMessageHandler h = subscribeRequests.get(topic);
      if (h == null) {
        throw new IllegalStateException("No subscription for topic:" + topic);
      }
      h.handleWebsocketMessage(m.toString());
    }
    
  }
  
  public static class TestMessage {
    
    public static TestMessage create(String topic, String payload) {
      TestMessage tm = new TestMessage();
      tm.myTopic = topic;
      tm.payload = payload;
      return tm;
    }
    
    private String myTopic;
    private String payload;
    public String getMyTopic() {
      return myTopic;
    }
    public void setMyTopic(String myTopic) {
      this.myTopic = myTopic;
    }
    public String getPayload() {
      return payload;
    }
    public void setPayload(String payload) {
      this.payload = payload;
    }
    
    public String toString() {
      return JsonUtil.pojoToJsonString(this);
    }
    
    @Override
    public boolean equals(Object o) {
      if (o == null) {
        return false;
      }
      return toString().equals(o.toString());
    }
  }
  
  public static class TestMessageChild extends TestMessage {
    
    public static TestMessageChild create(String topic, String payload, boolean success) {
      TestMessageChild tm = new TestMessageChild();
      tm.setMyTopic(topic);
      tm.setPayload(payload);
      tm.success = success;
      return tm;
    }
    
    private boolean success;

    public boolean isSuccess() {
      return success;
    }

    public void setSuccess(boolean success) {
      this.success = success;
    }
    
  }
  
  public static class TestMessageDeserializer implements MessageDeserializer<TestMessage> {

    @Override
    public TestMessage deserialize(String msg) {
      try {
        return new ObjectMapper().readerFor(TestMessage.class).readValue(msg);
      } catch (JsonProcessingException e) {
        throw new RuntimeException("Error deserializing:" + msg, e);
      }
    }
    
  }
  
  private static class TestMessageListener extends GenericObserver<TestMessage> implements WebsocketListener<TestMessage> {

    @Override
    public void handleMessage(TestMessage message) {
      super.handleEvent(message);
      
    }
    
  }
}
