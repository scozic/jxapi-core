package org.jxapi.netutils.rest;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link DefaultHttpResponseInterceptor}
 */
public class DefaultHttpResponseInterceptorTest {

  @Test
  public void testIntercept() {
    HttpResponse response = new HttpResponse();
    HttpRequest request = new HttpRequest();
    request.setResponseDeserializer(s -> s + "!" );
    response.setRequest(request);
    response.setBody("Hello");
    DefaultHttpResponseInterceptor interceptor = new DefaultHttpResponseInterceptor();
    interceptor.intercept(response);
    Assert.assertEquals("Hello!", response.getResponse());
  }
}
