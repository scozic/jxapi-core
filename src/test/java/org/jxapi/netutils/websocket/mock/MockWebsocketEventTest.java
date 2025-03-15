package org.jxapi.netutils.websocket.mock;
import org.junit.Test;

import org.jxapi.netutils.websocket.GenericRawWebsocketMessageHandler;
import org.jxapi.netutils.websocket.GenericWebsocketErrorHandler;
import org.jxapi.netutils.websocket.RawWebsocketMessageHandler;
import org.jxapi.netutils.websocket.WebsocketErrorHandler;

import static org.junit.Assert.*;

/**
 * Unit test for {@link MockWebsocketEvent}
 */
public class MockWebsocketEventTest {

    @Test
    public void testCreateConnectEvent() {
        MockWebsocketEvent event = MockWebsocketEvent.createConnectEvent();
        assertEquals(MockWebsocketEventType.CONNECT, event.getType());
    }

    @Test
    public void testCreateDisconnectEvent() {
        MockWebsocketEvent event = MockWebsocketEvent.createDisconnectEvent();
        assertEquals(MockWebsocketEventType.DISCONNECT, event.getType());
    }

    @Test
    public void testCreateSendEvent() {
        String message = "Hello, world!";
        MockWebsocketEvent event = MockWebsocketEvent.createSendEvent(message);
        assertEquals(MockWebsocketEventType.SEND, event.getType());
        assertEquals(message, event.getMessage());
    }

    @Test
    public void testCreateAddMessageHandlerEvent() {
        RawWebsocketMessageHandler messageHandler = new GenericRawWebsocketMessageHandler();
        MockWebsocketEvent event = MockWebsocketEvent.createAddMessageHandlerEvent(messageHandler);
        assertEquals(MockWebsocketEventType.ADD_MESSAGE_HANDLER, event.getType());
        assertEquals(messageHandler, event.getMessageHandler());
    }

    @Test
    public void testCreateRemoveMessageHandlerEvent() {
        RawWebsocketMessageHandler messageHandler = new GenericRawWebsocketMessageHandler();
        MockWebsocketEvent event = MockWebsocketEvent.createRemoveMessageHandlerEvent(messageHandler);
        assertEquals(MockWebsocketEventType.REMOVE_MESSAGE_HANDLER, event.getType());
        assertEquals(messageHandler, event.getMessageHandler());
    }

    @Test
    public void testCreateAddErrorHandlerEvent() {
        WebsocketErrorHandler errorHandler = new GenericWebsocketErrorHandler();
        MockWebsocketEvent event = MockWebsocketEvent.createAddErrorHandlerEvent(errorHandler);
        assertEquals(MockWebsocketEventType.ADD_ERROR_HANDLER, event.getType());
        assertEquals(errorHandler, event.getErrorHandler());
    }

    @Test
    public void testCreateRemoveErrorHandlerEvent() {
        WebsocketErrorHandler errorHandler = new GenericWebsocketErrorHandler();
        MockWebsocketEvent event = MockWebsocketEvent.createRemoveErrorHandlerEvent(errorHandler);
        assertEquals(MockWebsocketEventType.REMOVE_ERROR_HANDLER, event.getType());
        assertEquals(errorHandler, event.getErrorHandler());
    }

    @Test
    public void testSetMessageHandler() {
        RawWebsocketMessageHandler messageHandler = new GenericRawWebsocketMessageHandler();
        MockWebsocketEvent event = new MockWebsocketEvent(MockWebsocketEventType.ADD_MESSAGE_HANDLER);
        event.setMessageHandler(messageHandler);
        assertEquals(messageHandler, event.getMessageHandler());
    }

    @Test
    public void testSetErrorHandler() {
        WebsocketErrorHandler errorHandler = new GenericWebsocketErrorHandler();
        MockWebsocketEvent event = new MockWebsocketEvent(MockWebsocketEventType.ADD_ERROR_HANDLER);
        event.setErrorHandler(errorHandler);
        assertEquals(errorHandler, event.getErrorHandler());
    }

    @Test
    public void testSetMessage() {
        String message = "Hello, world!";
        MockWebsocketEvent event = new MockWebsocketEvent(MockWebsocketEventType.SEND);
        event.setMessage(message);
        assertEquals(message, event.getMessage());
    }

    @Test
    public void testToString() {
        MockWebsocketEvent event = new MockWebsocketEvent(MockWebsocketEventType.CONNECT);
        String expected = "MockWebsocketEvent[CONNECT]";
        assertEquals(expected, event.toString());
    }
}



