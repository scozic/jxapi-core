package org.jxapi.netutils.rest;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link AbstractHttpRequestExecutor}
 */
public class AbstractHttpRequestExecutorTest {

  @Test
  public void testSetRequestTimeout() {
    HttpRequestExecutor executor = new HttpRequestExecutorStub();
    Assert.assertEquals(HttpRequestExecutor.DEFAULT_REQUEST_TIMEOUT, executor.getRequestTimeout());
    executor.setRequestTimeout(1000);
    Assert.assertEquals(1000, executor.getRequestTimeout());
  }
  
  private static class HttpRequestExecutorStub extends AbstractHttpRequestExecutor {

    @Override
    public FutureHttpResponse execute(HttpRequest request) {
      return null;
    }
    
  }
}
