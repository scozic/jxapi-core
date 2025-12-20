package org.jxapi.netutils.rest;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.Exchange;

/**
 * Unit test for {@link HttpRequestExecutorFactory}
 */
public class HttpRequestExceutorFactoryTest {

  @Test
  public void testFromClassName() {
    HttpRequestExecutorFactory fac = HttpRequestExecutorFactory.fromClassName(TestHttpRequestExceutorFactory.class.getName());
    Assert.assertNotNull(fac);
    Assert.assertTrue(fac instanceof TestHttpRequestExceutorFactory);
  }
  
  public static class TestHttpRequestExceutorFactory implements HttpRequestExecutorFactory {

    @Override
    public HttpRequestExecutor createExecutor(Exchange exchange) {
      return null;
    }
    
  }
 }
