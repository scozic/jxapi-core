package org.jxapi.netutils.websocket.mock;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jxapi.exchange.AbstractExchangeApi;
import org.jxapi.exchange.ExchangeApi;
import org.jxapi.exchange.ExchangeStub;
import org.jxapi.netutils.websocket.Websocket;

/**
 * Unit test for {@link MockWebsocketFactory}
 */
public class MockWebsocketFactoryTest {

    @Test
    public void testCreateWebsocket() {
        ExchangeApi exchangeApi = new MockExchangeApi();
        MockWebsocketFactory websocketFactory = new MockWebsocketFactory();
        Websocket websocket = websocketFactory.createWebsocket(exchangeApi);

        // Assert that the returned Websocket is not null and is an instance of MockWebsocket
        assertNotNull(websocket);
        assertTrue(websocket instanceof MockWebsocket);
    }

    // Mock implementation of ExchangeApi for testing purposes
    private static class MockExchangeApi extends AbstractExchangeApi {

        public MockExchangeApi() {
          super("TestApi", ExchangeStub.INSTANCE);
        }
    }
}

