package org.jxapi.netutils.websocket.mock;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jxapi.exchange.ExchangeStub;
import org.jxapi.netutils.websocket.WebsocketHook;

/**
 * Unit test for {@link MockWebsocketHookFactory}
 */
public class MockWebsocketHookFactoryTest {

    @Test
    public void testCreateHook() {
        MockWebsocketHookFactory factory = new MockWebsocketHookFactory();
        WebsocketHook hook = factory.createWebsocketHook(ExchangeStub.INSTANCE);
        assertNotNull(hook);
        assertTrue(hook instanceof MockWebsocketHook);
    }

}
