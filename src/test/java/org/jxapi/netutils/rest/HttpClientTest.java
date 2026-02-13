package org.jxapi.netutils.rest;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jxapi.netutils.rest.mock.MockFutureHttpResponse;
import org.jxapi.netutils.rest.mock.MockHttpRequestExecutor;
import org.jxapi.netutils.rest.mock.MockHttpRequestInterceptor;
import org.jxapi.netutils.rest.ratelimits.RateLimitReachedException;
import org.jxapi.netutils.rest.ratelimits.RateLimitRule;
import org.jxapi.netutils.rest.ratelimits.RequestThrottler;
import org.jxapi.netutils.rest.ratelimits.RequestThrottlingMode;



/**
 * Unit tests for {@link HttpClient}.
 */
public class HttpClientTest {

  private static final RateLimitRule ONE_REQUEST_PER_100MS = RateLimitRule.createRule("oneRequestPer100ms",100L, 1);
  
  private MockHttpRequestExecutor mockExecutor;
  private MockHttpRequestInterceptor mockInterceptor;
  private RequestThrottler requestThrottler;
  private int requestCount = 0;
  
  @Before
  public void setUp() {
    mockExecutor = new MockHttpRequestExecutor();
    mockInterceptor = new MockHttpRequestInterceptor();
    mockInterceptor.addPreparedInterceptor(r -> r.setBody("Intercepted:" + r.getBody()));
    requestThrottler = new RequestThrottler();
    requestThrottler.setThrottlingMode(RequestThrottlingMode.BLOCK);
    requestCount = 0;
  }
  
  @Test
  public void testGetters() {
    HttpClient client = new HttpClient(mockInterceptor, mockExecutor, requestThrottler);
    Assert.assertEquals(mockInterceptor, client.getInterceptor());
    Assert.assertEquals(mockExecutor, client.getExecutor());
    Assert.assertEquals(requestThrottler, client.getThrottler());
  }
  
  @After
  public void tearDown() {
    requestThrottler.dispose();
  }
  
  @Test
  public void testExecute_NoThrottler_NoInterceptor() {
    HttpClient client = new HttpClient(null, mockExecutor, null);
    for (int i = 0; i < 5; i++) {
      HttpRequest request = createTestRequest();
      client.execute(request);
      MockFutureHttpResponse mockRequest = mockExecutor.popRequest();
      Assert.assertEquals(request, mockRequest.getRequest());
    }
  }
  
  @Test
  public void testExecute_WithInterceptor_NoThrottler() {
    HttpClient client = new HttpClient(mockInterceptor, mockExecutor, null);
    HttpRequest request = createTestRequest();
    
    client.execute(request);
    MockFutureHttpResponse mockRequest = mockExecutor.popRequest();
    Assert.assertEquals("Intercepted:TestBody1", mockRequest.getRequest().getBody());
  }
  
  @Test
  public void testExecute_WithInterceptor_AndThrottler() throws InterruptedException, ExecutionException {
    HttpClient client = new HttpClient(mockInterceptor, mockExecutor, requestThrottler);
    HttpRequest request1 = createTestRequest();
    client.execute(request1);
    MockFutureHttpResponse mockRequest = mockExecutor.popRequest();
    Assert.assertEquals("Intercepted:TestBody1", mockRequest.getRequest().getBody());
    HttpRequest request2 = createTestRequest();
    FutureHttpResponse futureResponse = client.execute(request2);
    // Request should not have been executed yet due request blocked by rate limiter
    Assert.assertEquals(0, mockExecutor.size());
    HttpResponse response2 = futureResponse.get();
    Assert.assertEquals(request2, response2.getRequest());
    Assert.assertEquals(429, response2.getResponseCode());
    Assert.assertTrue(response2.getException() instanceof RateLimitReachedException);
  }
  
  private HttpRequest createTestRequest() {
    HttpRequest request = new HttpRequest();
    request.setUrl("http://example.com/api/test");
    request.setHttpMethod(HttpMethod.POST);
    request.setBody("TestBody" + (++requestCount));
    request.setRateLimits(List.of(ONE_REQUEST_PER_100MS));
    return request;
  }

}
