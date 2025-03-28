package org.jxapi.netutils.websocket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link AbstractWebsocket}
 */
public class AbstractWebsocketTest {
  
    private static class WebsocketStub extends AbstractWebsocket {
      
      int doConnectCallCount = 0;
      int doDisconnectCallCount = 0;
      List<String> sentMessages = new ArrayList<>();
      
      @Override
        protected void doConnect() throws WebsocketException {
            doConnectCallCount++;
        }

        @Override
        protected void doDisconnect() {
            doDisconnectCallCount++;
        }

        @Override
        protected void doSend(String message) throws WebsocketException {
            sentMessages.add(message);
        }
        
        @Override
        protected void dispatchMessage(String message) {
        super.dispatchMessage(message);
      }
      
        @Override
      protected void dispatchError(String msg, Throwable t) {
        super.dispatchError(msg, t);
      }
      
      @Override
      protected void dispatchError(WebsocketException error) {
        super.dispatchError(error);
      }
    }

        private WebsocketStub websocket;

        @Before
        public void setUp() {
            websocket = new WebsocketStub();
        }

        @Test
        public void testConnect() throws WebsocketException {
            websocket.connect();
            Assert.assertTrue(websocket.isConnected());
            Assert.assertEquals(1, websocket.doConnectCallCount);
            websocket.connect();
            Assert.assertTrue(websocket.isConnected());
            Assert.assertEquals(1, websocket.doConnectCallCount);
        }

        @Test
        public void testDisconnect() throws WebsocketException {
            websocket.connect();
            websocket.disconnect();
            Assert.assertFalse(websocket.isConnected());
            Assert.assertEquals(1, websocket.doDisconnectCallCount);
            websocket.disconnect();
            Assert.assertFalse(websocket.isConnected());
            Assert.assertEquals(1, websocket.doDisconnectCallCount);
        }

        @Test
        public void testSendWhenConnected() throws WebsocketException {
            websocket.connect();
            try {
                websocket.send("Test message 1");
                Assert.assertEquals(1, websocket.sentMessages.size());
                Assert.assertEquals("Test message 1", websocket.sentMessages.get(0));
                websocket.send("Test message 2");
                Assert.assertEquals(2, websocket.sentMessages.size());
                Assert.assertEquals("Test message 2", websocket.sentMessages.get(1));
            } catch (WebsocketException e) {
                Assert.fail("Exception should not be thrown when connected");
            }
        }

        @Test
        public void testSendWhenNotConnected() {
            try {
                websocket.send("Test message");
                Assert.fail("Exception should be thrown when not connected");
            } catch (WebsocketException e) {
                Assert.assertEquals("Not connected:" + websocket, e.getMessage());
            }
            Assert.assertEquals(0, websocket.sentMessages.size());
        }

        @Test
        public void testSetAndGetUrl() {
            String url = "ws://example.com";
            websocket.setUrl(url);
            Assert.assertEquals(url, websocket.getUrl());
        }

        @Test
        public void testAddAndRemoveMessageHandler() {
            RawWebsocketMessageHandler handler = message -> {};
            websocket.addMessageHandler(handler);
            Assert.assertTrue(websocket.removeMessageHandler(handler));
        }

        @Test
        public void testAddAndRemoveErrorHandler() {
            WebsocketErrorHandler handler = error -> {};
            websocket.addErrorHandler(handler);
            Assert.assertTrue(websocket.removeErrorHandler(handler));
        }

        @Test
        public void testDispatchMessage() {
            String message = "Test message";
            AtomicReference<String> receivedMessage = new AtomicReference<>();
            websocket.addMessageHandler(receivedMessage::set);
            websocket.dispatchMessage(message);
            Assert.assertEquals(message, receivedMessage.get());
        }

        @Test
        public void testDispatchErrorWithMessageAndThrowable() {
            String message = "Test error message";
            Throwable t = new RuntimeException("Test exception");
            AtomicReference<WebsocketException> receivedError = new AtomicReference<>();
            websocket.addErrorHandler(e -> receivedError.set(e));
            websocket.dispatchError(message, t);
            Assert.assertEquals(message, receivedError.get().getMessage());
            Assert.assertEquals(t, receivedError.get().getCause());
        }

        @Test
        public void testToString() {
            String url = "ws://example.com";
            websocket.setUrl(url);
            Assert.assertEquals("WebsocketStub[ws://example.com]", websocket.toString());
        }
        

}
