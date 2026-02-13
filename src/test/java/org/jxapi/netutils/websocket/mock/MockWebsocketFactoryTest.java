package org.jxapi.netutils.websocket.mock;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jxapi.exchange.ExchangeStub;
import org.jxapi.netutils.websocket.Websocket;

/**
 * Unit test for {@link MockWebsocketFactory}
 */
public class MockWebsocketFactoryTest {

    @Test
    public void testCreateWebsocket() {
        MockWebsocketFactory websocketFactory = new MockWebsocketFactory();
        Websocket websocket = websocketFactory.createWebsocket(new ExchangeStub("FooExchange"));

        // Assert that the returned Websocket is not null and is an instance of MockWebsocket
        assertNotNull(websocket);
        assertTrue(websocket instanceof MockWebsocket);
    }
}

