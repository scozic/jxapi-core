package org.jxapi.netutils.rest.mock;

import org.junit.Assert;
import org.junit.Test;

import org.jxapi.netutils.rest.HttpRequestInterceptor;

/**
 * Unit test for {@link MockHttpRequestInterceptorFactory}
 */
public class MockHttpRequuestInterceptorFactoryTest {

    @Test
    public void testCreateInterceptor() {
       MockHttpRequestInterceptorFactory factory = new MockHttpRequestInterceptorFactory();
       HttpRequestInterceptor interceptor = factory.createInterceptor(null);
       Assert.assertNotNull(interceptor);
       Assert.assertTrue(interceptor instanceof MockHttpRequestInterceptor);
    }
}
