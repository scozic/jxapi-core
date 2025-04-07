package org.jxapi.netutils.websocket.mock;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jxapi.exchange.AbstractExchangeApi;
import org.jxapi.exchange.ExchangeStub;
import org.jxapi.netutils.websocket.WebsocketHook;

/**
 * Unit test for {@link MockWebsocketHookFactory}
 */
public class MockWebsocketHookFactoryTest {

    @Test
    public void testCreateHook() {
        MockWebsocketHookFactory factory = new MockWebsocketHookFactory();
        WebsocketHook hook = factory.createWebsocketHook(new MockExchangeApi());
        assertNotNull(hook);
        assertTrue(hook instanceof MockWebsocketHook);
    }

        // Mock implementation of ExchangeApi for testing purposes
    private static class MockExchangeApi extends AbstractExchangeApi {

        public MockExchangeApi() {
            super("TestApi", ExchangeStub.INSTANCE);
        }
    }
}
