package org.jxapi.netutils.websocket.mock;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.jxapi.exchange.Exchange;
import org.jxapi.netutils.websocket.DefaultWebsocketManager;
import org.jxapi.netutils.websocket.WebsocketException;
import org.jxapi.netutils.websocket.WebsocketManager;

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
    public void testGetWebsocketManager() {
        WebsocketManager expectedWebsocketManager = new DefaultWebsocketManager((Exchange) null, new MockWebsocket(), mockWebsocketHook);
        mockWebsocketHook.init(expectedWebsocketManager);
        WebsocketManager actualWebsocketManager = mockWebsocketHook.getWebsocketManager();
        assertEquals(expectedWebsocketManager, actualWebsocketManager);
    }

    @Test
    public void testAfterInit() {
        WebsocketManager expectedWebsocketManager = new DefaultWebsocketManager((Exchange) null, new MockWebsocket(), mockWebsocketHook);
        MockWebsocketHookEvent event = mockWebsocketHook.pop();
        assertEquals(MockWebsocketHookEventType.INIT, event.getType());
        assertEquals(mockWebsocketHook, event.getSource());
        assertEquals(expectedWebsocketManager, event.getWebsocketManager());
        assertEquals(expectedWebsocketManager, mockWebsocketHook.getWebsocketManager());
    }

    @Test
    public void testBeforeConnect() throws WebsocketException {
        WebsocketManager expectedWebsocketManager = new DefaultWebsocketManager((Exchange) null, new MockWebsocket(), mockWebsocketHook);
        assertEquals(MockWebsocketHookEventType.INIT, mockWebsocketHook.pop().getType());
        mockWebsocketHook.beforeConnect();
        MockWebsocketHookEvent event = mockWebsocketHook.pop();
        assertEquals(MockWebsocketHookEventType.BEFORE_CONNECT, event.getType());
        assertEquals(mockWebsocketHook, event.getSource());
        assertEquals(expectedWebsocketManager, mockWebsocketHook.getWebsocketManager());
    }

    @Test
    public void testAfterConnect() throws WebsocketException {
        WebsocketManager expectedWebsocketManager = new DefaultWebsocketManager((Exchange) null, new MockWebsocket(), mockWebsocketHook);
        assertEquals(MockWebsocketHookEventType.INIT, mockWebsocketHook.pop().getType());
        mockWebsocketHook.afterConnect();
        MockWebsocketHookEvent event = mockWebsocketHook.pop();
        assertEquals(MockWebsocketHookEventType.AFTER_CONNECT, event.getType());
        assertEquals(mockWebsocketHook, event.getSource());
        assertEquals(expectedWebsocketManager, mockWebsocketHook.getWebsocketManager());
    }

    @Test
    public void testBeforeDisconnect() throws WebsocketException {
        WebsocketManager expectedWebsocketManager = new DefaultWebsocketManager((Exchange) null, new MockWebsocket(), mockWebsocketHook);
        assertEquals(MockWebsocketHookEventType.INIT, mockWebsocketHook.pop().getType());
        mockWebsocketHook.beforeDisconnect();
        MockWebsocketHookEvent event = mockWebsocketHook.pop();
        assertEquals(MockWebsocketHookEventType.BEFORE_DISCONNECT, event.getType());
        assertEquals(mockWebsocketHook, event.getSource());
        assertEquals(expectedWebsocketManager, mockWebsocketHook.getWebsocketManager());
    }

    @Test
    public void testAfterDisconnect() throws WebsocketException {
        WebsocketManager expectedWebsocketManager = new DefaultWebsocketManager((Exchange) null, new MockWebsocket(), mockWebsocketHook);
        assertEquals(MockWebsocketHookEventType.INIT, mockWebsocketHook.pop().getType());
        mockWebsocketHook.afterDisconnect();
        MockWebsocketHookEvent event = mockWebsocketHook.pop();
        assertEquals(MockWebsocketHookEventType.AFTER_DISCONNECT, event.getType());
        assertEquals(mockWebsocketHook, event.getSource());
        assertEquals(expectedWebsocketManager, mockWebsocketHook.getWebsocketManager());
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

