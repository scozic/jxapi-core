package org.jxapi.netutils.rest;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.netutils.rest.mock.MockHttpResponseInterceptor;
import org.jxapi.netutils.rest.mock.MockHttpResponseInterceptorFactory;

/**
 * Unit tests for {@link MockHttpResponseInterceptorFactory}
 */
public class MockHttpResponseInterceptorFactoryTest {

  @Test
  public void testCreateInterceptor() {
    MockHttpResponseInterceptorFactory factory = new MockHttpResponseInterceptorFactory();
    HttpResponseInterceptor interceptor = factory.createInterceptor(null);
    Assert.assertNotNull(interceptor);
    Assert.assertTrue(interceptor instanceof MockHttpResponseInterceptor);
  }
}
