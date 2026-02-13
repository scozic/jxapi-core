package org.jxapi.netutils.websocket.mock;

import org.junit.Assert;
import org.junit.Test;

import org.jxapi.netutils.websocket.RawWebsocketMessageHandler;
import org.jxapi.netutils.websocket.WebsocketErrorHandler;
import org.jxapi.netutils.websocket.WebsocketException;
import org.jxapi.observability.GenericObserver;

/**
 * Unit test for {@link MockWebsocket}
 */
public class MockWebsocketTest {
    @Test
    public void testCreateWebsocket() {
        MockWebsocket websocket = new MockWebsocket();
        Assert.assertNotNull(websocket);
    }

    @Test
    public void testConnect() throws WebsocketException {
        MockWebsocket websocket = new MockWebsocket();
        websocket.connect();
        Assert.assertTrue(websocket.isConnected());
        MockWebsocketEvent event = websocket.pop();
        Assert.assertEquals(MockWebsocketEventType.CONNECT ,  event.getType());
    }

    @Test
    public void testDisconnect() throws WebsocketException {
        MockWebsocket websocket = new MockWebsocket();
        websocket.connect();
        Assert.assertEquals(MockWebsocketEventType.CONNECT ,  websocket.pop().getType());
        websocket.disconnect();
        Assert.assertFalse(websocket.isConnected());
        MockWebsocketEvent event = websocket.pop();
        Assert.assertEquals(MockWebsocketEventType.DISCONNECT,  event.getType());
    }

    @Test
    public void testSend() throws WebsocketException {
        MockWebsocket websocket = new MockWebsocket();
        websocket.connect();
        Assert.assertEquals(MockWebsocketEventType.CONNECT ,  websocket.pop().getType());
        String message = "Hello";
        websocket.send(message);
        MockWebsocketEvent event = websocket.pop();
        Assert.assertEquals(MockWebsocketEventType.SEND,  event.getType());
        Assert.assertEquals(message, event.getMessage());
    }

    @Test
    public void testAddMessageHandler() throws WebsocketException {
        MockWebsocket websocket = new MockWebsocket();
        websocket.connect();
        Assert.assertEquals(MockWebsocketEventType.CONNECT ,  websocket.pop().getType());
        TestRawWebsocketMessageHandler messageHandler = new TestRawWebsocketMessageHandler();
        websocket.addMessageHandler(messageHandler);
        MockWebsocketEvent event = websocket.pop();
        Assert.assertEquals(MockWebsocketEventType.ADD_MESSAGE_HANDLER,  event.getType());
        Assert.assertEquals(messageHandler, event.getMessageHandler());
    }

    @Test
    public void testDispatchMessage() throws WebsocketException {
        MockWebsocket websocket = new MockWebsocket();
        websocket.connect();
        TestRawWebsocketMessageHandler messageHandler = new TestRawWebsocketMessageHandler();
        websocket.addMessageHandler(messageHandler);    
        String message = "Hello";
        websocket.dispatchMessage(message);
        Assert.assertEquals(message, messageHandler.pop());
    }

    @Test
    public void testRemoveMessageHandler() {
        MockWebsocket websocket = new MockWebsocket();
        TestRawWebsocketMessageHandler messageHandler = new TestRawWebsocketMessageHandler();
        websocket.addMessageHandler(messageHandler);
        Assert.assertEquals(MockWebsocketEventType.ADD_MESSAGE_HANDLER,  websocket.pop().getType());
        websocket.removeMessageHandler(messageHandler);
        MockWebsocketEvent event = websocket.pop();
        Assert.assertEquals(MockWebsocketEventType.REMOVE_MESSAGE_HANDLER,  event.getType());
        Assert.assertEquals(messageHandler, event.getMessageHandler());
        String message = "Hello";
        websocket.dispatchMessage(message);
        Assert.assertEquals(0, messageHandler.size());
    }

    @Test
    public void testDispatchError() throws WebsocketException {
        MockWebsocket websocket = new MockWebsocket();
        websocket.connect();
        TestWebsocketHerrorHAandler errorHandler = new TestWebsocketHerrorHAandler();
        websocket.addErrorHandler(errorHandler);
        WebsocketException error = new WebsocketException("Test error");
        websocket.dispatchError(error);
        Assert.assertEquals(error, errorHandler.pop());
    }

    @Test
    public void testAddErrorHandler() {
        MockWebsocket websocket = new MockWebsocket();
        TestWebsocketHerrorHAandler errorHandler = new TestWebsocketHerrorHAandler();
        websocket.addErrorHandler(errorHandler);
        MockWebsocketEvent event = websocket.pop();
        Assert.assertEquals(MockWebsocketEventType.ADD_ERROR_HANDLER,  event.getType());
        Assert.assertEquals(errorHandler, event.getErrorHandler());
    }

    @Test
    public void testRemoveErrorHandler() {
        MockWebsocket websocket = new MockWebsocket();
        TestWebsocketHerrorHAandler errorHandler = new TestWebsocketHerrorHAandler();
        websocket.addErrorHandler(errorHandler);
        Assert.assertEquals(MockWebsocketEventType.ADD_ERROR_HANDLER,  websocket.pop().getType());
        websocket.removeErrorHandler(errorHandler);
        MockWebsocketEvent event = websocket.pop();
        Assert.assertEquals(MockWebsocketEventType.REMOVE_ERROR_HANDLER,  event.getType());
        Assert.assertEquals(errorHandler, event.getErrorHandler());
        WebsocketException error = new WebsocketException("Test error");
        websocket.dispatchError(error);
        Assert.assertEquals(0, errorHandler.size());
    }

    @Test
    public void testSetUrl() {
        MockWebsocket websocket = new MockWebsocket();
        String url = "wss://localhost:8080";
        websocket.setUrl(url);
        Assert.assertEquals(url, websocket.getUrl());
    }

    @Test
    public void testToString() {
        MockWebsocket websocket = new MockWebsocket();
        String url = "wss://localhost:8080";
        websocket.setUrl(url);
        Assert.assertEquals("MockWebsocket[wss://localhost:8080]", websocket.toString());
    }

    private class TestRawWebsocketMessageHandler extends GenericObserver<String> implements RawWebsocketMessageHandler {
        
        @Override
        public void handleWebsocketMessage(String message) {
            super.handleEvent(message);
        }
    }

    private class TestWebsocketHerrorHAandler extends GenericObserver<WebsocketException> implements WebsocketErrorHandler {
        
        @Override
        public void handleWebsocketError(WebsocketException error) {
            super.handleEvent(error);
        }
    }
}
