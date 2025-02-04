package com.scz.jxapi.netutils.websocket.mock;

import com.scz.jxapi.exchange.AbstractExchangeApi;
import com.scz.jxapi.exchange.ExchangeApi;
import com.scz.jxapi.netutils.websocket.Websocket;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Properties;

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
            super("TestApi", "TestExchange", "TEST_EXCHANGE", new Properties());
        }
    }
}

