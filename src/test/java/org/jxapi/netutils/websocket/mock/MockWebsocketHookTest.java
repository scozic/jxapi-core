package org.jxapi.netutils.websocket.mock;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.jxapi.netutils.websocket.DefaultWebsocketClient;
import org.jxapi.netutils.websocket.WebsocketException;
import org.jxapi.netutils.websocket.WebsocketClient;

/**
 * Unit test for {@link MockWebsocketHook}
 */
public class MockWebsocketHookTest {

    private MockWebsocketHook mockWebsocketHook;

    @Before
    public void setUp() {
        mockWebsocketHook = new MockWebsocketHook();
    }

    @Test
    public void testGetWebsocketClient() {
        WebsocketClient expectedWebsocketClient = new DefaultWebsocketClient(new MockWebsocket(), mockWebsocketHook);
        mockWebsocketHook.init(expectedWebsocketClient);
        WebsocketClient actualWebsocketClient = mockWebsocketHook.getWebsocketClient();
        assertEquals(expectedWebsocketClient, actualWebsocketClient);
    }

    @Test
    public void testAfterInit() {
        WebsocketClient expectedWebsocketClient = new DefaultWebsocketClient(new MockWebsocket(), mockWebsocketHook);
        MockWebsocketHookEvent event = mockWebsocketHook.pop();
        assertEquals(MockWebsocketHookEventType.INIT, event.getType());
        assertEquals(mockWebsocketHook, event.getSource());
        assertEquals(expectedWebsocketClient, event.getWebsocketClient());
        assertEquals(expectedWebsocketClient, mockWebsocketHook.getWebsocketClient());
    }

    @Test
    public void testBeforeConnect() throws WebsocketException {
        WebsocketClient expectedWebsocketClient = new DefaultWebsocketClient(new MockWebsocket(), mockWebsocketHook);
        assertEquals(MockWebsocketHookEventType.INIT, mockWebsocketHook.pop().getType());
        mockWebsocketHook.beforeConnect();
        MockWebsocketHookEvent event = mockWebsocketHook.pop();
        assertEquals(MockWebsocketHookEventType.BEFORE_CONNECT, event.getType());
        assertEquals(mockWebsocketHook, event.getSource());
        assertEquals(expectedWebsocketClient, mockWebsocketHook.getWebsocketClient());
    }

    @Test
    public void testAfterConnect() throws WebsocketException {
        WebsocketClient expectedWebsocketClient = new DefaultWebsocketClient(new MockWebsocket(), mockWebsocketHook);
        assertEquals(MockWebsocketHookEventType.INIT, mockWebsocketHook.pop().getType());
        mockWebsocketHook.afterConnect();
        MockWebsocketHookEvent event = mockWebsocketHook.pop();
        assertEquals(MockWebsocketHookEventType.AFTER_CONNECT, event.getType());
        assertEquals(mockWebsocketHook, event.getSource());
        assertEquals(expectedWebsocketClient, mockWebsocketHook.getWebsocketClient());
    }

    @Test
    public void testBeforeDisconnect() throws WebsocketException {
        WebsocketClient expectedWebsocketClient = new DefaultWebsocketClient(new MockWebsocket(), mockWebsocketHook);
        assertEquals(MockWebsocketHookEventType.INIT, mockWebsocketHook.pop().getType());
        mockWebsocketHook.beforeDisconnect();
        MockWebsocketHookEvent event = mockWebsocketHook.pop();
        assertEquals(MockWebsocketHookEventType.BEFORE_DISCONNECT, event.getType());
        assertEquals(mockWebsocketHook, event.getSource());
        assertEquals(expectedWebsocketClient, mockWebsocketHook.getWebsocketClient());
    }

    @Test
    public void testAfterDisconnect() throws WebsocketException {
        WebsocketClient expectedWebsocketClient = new DefaultWebsocketClient(new MockWebsocket(), mockWebsocketHook);
        assertEquals(MockWebsocketHookEventType.INIT, mockWebsocketHook.pop().getType());
        mockWebsocketHook.afterDisconnect();
        MockWebsocketHookEvent event = mockWebsocketHook.pop();
        assertEquals(MockWebsocketHookEventType.AFTER_DISCONNECT, event.getType());
        assertEquals(mockWebsocketHook, event.getSource());
        assertEquals(expectedWebsocketClient, mockWebsocketHook.getWebsocketClient());
    }

    @Test
    public void testSetHeartBeatMessage() {
        String expectedMessage = "Heartbeat message";
        mockWebsocketHook.setHeartBeatMessage(expectedMessage);
        assertEquals(expectedMessage, mockWebsocketHook.getHeartBeatMessage());
        MockWebsocketHookEvent event = mockWebsocketHook.pop();
        assertEquals(MockWebsocketHookEventType.GET_HEARTBEAT_MESSAGE, event.getType());
        assertEquals(mockWebsocketHook, event.getSource());
    }

    @Test
    public void testSetSubscribeRequestMessage() {
        String topic = "topic";
        String expectedMessage = "Subscribe request message";
        mockWebsocketHook.setSubscribeRequestMessage(topic, expectedMessage);
        assertEquals(expectedMessage, mockWebsocketHook.getSubscribeRequestMessage(topic));
        MockWebsocketHookEvent event = mockWebsocketHook.pop();
        assertEquals(MockWebsocketHookEventType.GET_SUBSCRIBE_REQUEST_MESSAGE, event.getType());
        assertEquals(mockWebsocketHook, event.getSource());
    }

    @Test
    public void testSetUnSubscribeRequestMessage() {
        String topic = "topic";
        String expectedMessage = "Unsubscribe request message";
        mockWebsocketHook.setUnSubscribeRequestMessage(topic, expectedMessage);
        assertEquals(expectedMessage, mockWebsocketHook.getUnSubscribeRequestMessage(topic));
        MockWebsocketHookEvent event = mockWebsocketHook.pop();
        assertEquals(MockWebsocketHookEventType.GET_UNSUBSCRIBE_REQUEST_MESSAGE, event.getType());
        assertEquals(mockWebsocketHook, event.getSource());
    }
}

