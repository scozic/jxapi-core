package org.jxapi.netutils.websocket.mock;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit test for {@link MockWebsocketListener}
 */
public class MockWebsocketListenerTest {

    @Test
    public void testOnMessage() {
        MockWebsocketListener<String> listener = new MockWebsocketListener<>();
        String message = "Hello";
        listener.handleMessage(message);
        Assert.assertEquals(1, listener.size());
        Assert.assertEquals(message, listener.pop());
    }
}
