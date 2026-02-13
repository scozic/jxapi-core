package org.jxapi.netutils.rest.mock;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.Exchange;
import org.jxapi.netutils.rest.HttpRequestExecutor;

/**
 * Unit test for {@link MockHttpRequestExecutorFactory}
 */
public class MockHttpRequestExecutorFactoryTest {

    @Test
    public void testCreateExecutor() {
       MockHttpRequestExecutorFactory factory = new MockHttpRequestExecutorFactory();
       HttpRequestExecutor executor = factory.createExecutor((Exchange) null);
       Assert.assertNotNull(executor);
       Assert.assertTrue(executor instanceof MockHttpRequestExecutor);
    }
}
