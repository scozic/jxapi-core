package org.jxapi.netutils.rest;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link DefaultHttpRequestInterceptor}.
 */
public class DefaultHttpRequestInterceptorTest {

  @Test
  public void testInterceptWithMessageSerializer() {
    HttpRequest request = new HttpRequest();
    request.setRequest("foo");
    request.setRequestSerializer(r -> "Serialized: " + r);
    
    DefaultHttpRequestInterceptor interceptor = new DefaultHttpRequestInterceptor();
    interceptor.intercept(request);
    
    // Assert that content type header is set to JSON
    List<String> contentTypeHeader = request.getHeaders().get("Content-Type");
    Assert.assertEquals(1, contentTypeHeader.size());
    Assert.assertEquals("application/json", contentTypeHeader.get(0));
    
    // Assert that request body is serialized (this would depend on the implementation of serializeRequestBody)
    Assert.assertEquals("Serialized: foo", request.getBody());
  }
  
  @Test
  public void testInterceptWithoutMessageSerializer() {
    HttpRequest request = new HttpRequest();
    request.setRequest("foo");
    
    DefaultHttpRequestInterceptor interceptor = new DefaultHttpRequestInterceptor();
    interceptor.intercept(request);
    
    // Assert that content type header is not set
    Assert.assertNull(request.getHeaders());
    
    // Assert that request body is not modified
    Assert.assertNull(request.getBody());
  }
  
}
