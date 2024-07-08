package com.scz.jxapi.netutils.websocket.mock;
import org.junit.Test;

import com.scz.jxapi.netutils.websocket.GenericRawWebsocketMessageHandler;
import com.scz.jxapi.netutils.websocket.GenericWebsocketErrorHandler;
import com.scz.jxapi.netutils.websocket.RawWebsocketMessageHandler;
import com.scz.jxapi.netutils.websocket.WebsocketErrorHandler;

import static org.junit.Assert.*;

/**
 * Unit test for {@link MockWebsocketEvent}
 */
public class MockWebsocketEventTest {

    @Test
    public void testCreateConnectEvent() {
        MockWebsocket source = new MockWebsocket();
        MockWebsocketEvent event = MockWebsocketEvent.createConnectEvent(source);
        assertEquals(MockWebsocketEventType.CONNECT, event.getType());
        assertEquals(source, event.getSource());
    }

    @Test
    public void testCreateDisconnectEvent() {
        MockWebsocket source = new MockWebsocket();
        MockWebsocketEvent event = MockWebsocketEvent.createDisconnectEvent(source);
        assertEquals(MockWebsocketEventType.DISCONNECT, event.getType());
        assertEquals(source, event.getSource());
    }

    @Test
    public void testCreateSendEvent() {
        MockWebsocket source = new MockWebsocket();
        String message = "Hello, world!";
        MockWebsocketEvent event = MockWebsocketEvent.createSendEvent(source, message);
        assertEquals(MockWebsocketEventType.SEND, event.getType());
        assertEquals(source, event.getSource());
        assertEquals(message, event.getMessage());
    }

    @Test
    public void testCreateAddMessageHandlerEvent() {
        MockWebsocket source = new MockWebsocket();
        RawWebsocketMessageHandler messageHandler = new GenericRawWebsocketMessageHandler();
        MockWebsocketEvent event = MockWebsocketEvent.createAddMessageHandlerEvent(source, messageHandler);
        assertEquals(MockWebsocketEventType.ADD_MESSAGE_HANDLER, event.getType());
        assertEquals(source, event.getSource());
        assertEquals(messageHandler, event.getMessageHandler());
    }

    @Test
    public void testCreateRemoveMessageHandlerEvent() {
        MockWebsocket source = new MockWebsocket();
        RawWebsocketMessageHandler messageHandler = new GenericRawWebsocketMessageHandler();
        MockWebsocketEvent event = MockWebsocketEvent.createRemoveMessageHandlerEvent(source, messageHandler);
        assertEquals(MockWebsocketEventType.REMOVE_MESSAGE_HANDLER, event.getType());
        assertEquals(source, event.getSource());
        assertEquals(messageHandler, event.getMessageHandler());
    }

    @Test
    public void testCreateAddErrorHandlerEvent() {
        MockWebsocket source = new MockWebsocket();
        WebsocketErrorHandler errorHandler = new GenericWebsocketErrorHandler();
        MockWebsocketEvent event = MockWebsocketEvent.createAddErrorHandlerEvent(source, errorHandler);
        assertEquals(MockWebsocketEventType.ADD_ERROR_HANDLER, event.getType());
        assertEquals(source, event.getSource());
        assertEquals(errorHandler, event.getErrorHandler());
    }

    @Test
    public void testCreateRemoveErrorHandlerEvent() {
        MockWebsocket source = new MockWebsocket();
        WebsocketErrorHandler errorHandler = new GenericWebsocketErrorHandler();
        MockWebsocketEvent event = MockWebsocketEvent.createRemoveErrorHandlerEvent(source, errorHandler);
        assertEquals(MockWebsocketEventType.REMOVE_ERROR_HANDLER, event.getType());
        assertEquals(source, event.getSource());
        assertEquals(errorHandler, event.getErrorHandler());
    }

    @Test
    public void testSetMessageHandler() {
        MockWebsocket source = new MockWebsocket();
        RawWebsocketMessageHandler messageHandler = new GenericRawWebsocketMessageHandler();
        MockWebsocketEvent event = new MockWebsocketEvent(MockWebsocketEventType.CONNECT, source);
        event.setMessageHandler(messageHandler);
        assertEquals(messageHandler, event.getMessageHandler());
    }

    @Test
    public void testSetErrorHandler() {
        MockWebsocket source = new MockWebsocket();
        WebsocketErrorHandler errorHandler = new GenericWebsocketErrorHandler();
        MockWebsocketEvent event = new MockWebsocketEvent(MockWebsocketEventType.CONNECT, source);
        event.setErrorHandler(errorHandler);
        assertEquals(errorHandler, event.getErrorHandler());
    }

    @Test
    public void testSetMessage() {
        MockWebsocket source = new MockWebsocket();
        String message = "Hello, world!";
        MockWebsocketEvent event = new MockWebsocketEvent(MockWebsocketEventType.CONNECT, source);
        event.setMessage(message);
        assertEquals(message, event.getMessage());
    }

    @Test
    public void testToString() {
        MockWebsocket source = new MockWebsocket();
        MockWebsocketEvent event = new MockWebsocketEvent(MockWebsocketEventType.CONNECT, source);
        String expected = "MockWebsocketEvent{\"type\":\"CONNECT\",\"source\":{\"defaulTimeout\":2000,\"connected\":false,\"url\":null,\"allEvents\":[]},\"messageHandler\":null,\"errorHandler\":null,\"message\":null}";
        assertEquals(expected, event.toString());
    }
}



