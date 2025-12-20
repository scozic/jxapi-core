package org.jxapi.netutils.rest;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.Exchange;

/**
 * Unit test for {@link HttpRequestInterceptorFactory}
 */
public class HttpRequestInterceptorFactoryTest {

  @Test
  public void testFromClassName() {
    HttpRequestInterceptorFactory fac = HttpRequestInterceptorFactory.fromClassName(TestHttpRequestInterceptorFactory.class.getName());
    Assert.assertNotNull(fac);
    Assert.assertTrue(fac instanceof TestHttpRequestInterceptorFactory);
  }
  
  public static class TestHttpRequestInterceptorFactory implements HttpRequestInterceptorFactory {

    @Override
    public HttpRequestInterceptor createInterceptor(Exchange exchange) {
      return null;
    }
    
  }
}
