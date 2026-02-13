package org.jxapi.netutils.websocket.mock;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.jxapi.netutils.websocket.DefaultWebsocketClient;
import org.jxapi.netutils.websocket.WebsocketClient;

/**
 * Unit test for {@link MockWebsocketHookEvent}
 */
public class MockWebsocketHookEventTest {

    private MockWebsocketHookEvent mockWebsocketHookEvent;
    private MockWebsocketHook mockWebsocketHook;
    private WebsocketClient websocketClient;

    @Before
    public void setUp() {
        mockWebsocketHook = new MockWebsocketHook();
        websocketClient = new DefaultWebsocketClient(new MockWebsocket(), mockWebsocketHook);
        mockWebsocketHookEvent = new MockWebsocketHookEvent(MockWebsocketHookEventType.INIT, mockWebsocketHook);
        mockWebsocketHookEvent.setWebsocketClient(websocketClient);
    }

    @Test
    public void testGetType() {
        assertEquals(MockWebsocketHookEventType.INIT, mockWebsocketHookEvent.getType());
    }

    @Test
    public void testGetSource() {
        assertEquals(mockWebsocketHook, mockWebsocketHookEvent.getSource());
    }

    @Test
    public void testGetWebsocketClient() {
        assertEquals(websocketClient, mockWebsocketHookEvent.getWebsocketClient());
    }

    @Test
    public void testSetWebsocketClient() {
        WebsocketClient newWebsocketClient = new DefaultWebsocketClient(new MockWebsocket(), mockWebsocketHook);
        mockWebsocketHookEvent.setWebsocketClient(newWebsocketClient);
        assertEquals(newWebsocketClient, mockWebsocketHookEvent.getWebsocketClient());
    }

    @Test
    public void testGetTopic() {
        String topic = "testTopic";
        mockWebsocketHookEvent.setTopic(topic);
        assertEquals(topic, mockWebsocketHookEvent.getTopic());
    }

    @Test
    public void testSetTopic() {
        String newTopic = "newTestTopic";
        mockWebsocketHookEvent.setTopic(newTopic);
        assertEquals(newTopic, mockWebsocketHookEvent.getTopic());
    }

    @Test
    public void testToString() {
        assertEquals("MockWebsocketHookEvent[INIT]", mockWebsocketHookEvent.toString());
    }
    
    @Test
    public void testCreateInitEvent() {
        MockWebsocketHookEvent event = MockWebsocketHookEvent.createInitEvent(mockWebsocketHook, websocketClient);
        assertEquals(MockWebsocketHookEventType.INIT, event.getType());
        assertEquals(mockWebsocketHook, event.getSource());
        assertEquals(websocketClient, event.getWebsocketClient());
    }

    @Test
    public void testCreateGetSubscribeRequestMessageEvent() {
        String topic = "testTopic";
        MockWebsocketHookEvent event = MockWebsocketHookEvent.createGetSubscribeRequestMessageEvent(mockWebsocketHook, topic);
        assertEquals(MockWebsocketHookEventType.GET_SUBSCRIBE_REQUEST_MESSAGE, event.getType());
        assertEquals(mockWebsocketHook, event.getSource());
        assertEquals(topic, event.getTopic());
    }

    @Test
    public void testCreateGetUnSubscribeRequestMessageEvent() {
        String topic = "testTopic";
        MockWebsocketHookEvent event = MockWebsocketHookEvent.createGetUnSubscribeRequestMessageEvent(mockWebsocketHook, topic);
        assertEquals(MockWebsocketHookEventType.GET_UNSUBSCRIBE_REQUEST_MESSAGE, event.getType());
        assertEquals(mockWebsocketHook, event.getSource());
        assertEquals(topic, event.getTopic());
    }

    @Test
    public void testCreateGetHeartbeatEvent() {
        MockWebsocketHookEvent event = MockWebsocketHookEvent.createGetHeartbeatEvent(mockWebsocketHook);
        assertEquals(MockWebsocketHookEventType.GET_HEARTBEAT_MESSAGE, event.getType());
        assertEquals(mockWebsocketHook, event.getSource());
    }

    @Test
    public void testCreateBeforeConnectEvent() {
        MockWebsocketHookEvent event = MockWebsocketHookEvent.createBeforeConnectEvent(mockWebsocketHook);
        assertEquals(MockWebsocketHookEventType.BEFORE_CONNECT, event.getType());
        assertEquals(mockWebsocketHook, event.getSource());
    }

    @Test
    public void testCreateAfterConnectEvent() {
        MockWebsocketHookEvent event = MockWebsocketHookEvent.createAfterConnectEvent(mockWebsocketHook);
        assertEquals(MockWebsocketHookEventType.AFTER_CONNECT, event.getType());
        assertEquals(mockWebsocketHook, event.getSource());
    }

    @Test
    public void testCreateBeforeDisconnectEvent() {
        MockWebsocketHookEvent event = MockWebsocketHookEvent.createBeforeDisconnectEvent(mockWebsocketHook);
        assertEquals(MockWebsocketHookEventType.BEFORE_DISCONNECT, event.getType());
        assertEquals(mockWebsocketHook, event.getSource());
    }

    @Test
    public void testCreateAfterDisconnectEvent() {
        MockWebsocketHookEvent event = MockWebsocketHookEvent.createAfterDisconnectEvent(mockWebsocketHook);
        assertEquals(MockWebsocketHookEventType.AFTER_DISCONNECT, event.getType());
        assertEquals(mockWebsocketHook, event.getSource());
    }
}

