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
import org.jxapi.netutils.rest.mock.MockHttpResponseInterceptor;
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
  private MockHttpResponseInterceptor mockResponseInterceptor;
  private RequestThrottler requestThrottler;
  private int requestCount = 0;
  
  @Before
  public void setUp() {
    mockExecutor = new MockHttpRequestExecutor();
    mockInterceptor = new MockHttpRequestInterceptor();
    mockInterceptor.addPreparedInterceptor(r -> r.setBody("Intercepted:" + r.getBody()));
    mockResponseInterceptor = new MockHttpResponseInterceptor();
    mockResponseInterceptor.addPreparedInterceptor(r -> r.setBody("Intercepted response:" + r.getBody()));
    requestThrottler = new RequestThrottler();
    requestThrottler.setThrottlingMode(RequestThrottlingMode.BLOCK);
    requestCount = 0;
  }
  
  @Test
  public void testGetters() {
    HttpClient client = new HttpClient(mockInterceptor, mockExecutor, requestThrottler, mockResponseInterceptor);
    Assert.assertEquals(mockInterceptor, client.getInterceptor());
    Assert.assertEquals(mockExecutor, client.getExecutor());
    Assert.assertEquals(requestThrottler, client.getThrottler());
    Assert.assertEquals(mockResponseInterceptor, client.getResponseInterceptor());
  }
  
  @After
  public void tearDown() {
    requestThrottler.dispose();
  }
  
  @Test
  public void testExecute_NoThrottler_NoInterceptor() {
    HttpClient client = new HttpClient(null, mockExecutor, null, null);
    for (int i = 0; i < 5; i++) {
      HttpRequest request = createTestRequest();
      client.execute(request);
      MockFutureHttpResponse mockRequest = mockExecutor.popRequest();
      Assert.assertEquals(request, mockRequest.getRequest());
    }
  }
  
  @Test
  public void testExecute_WithRequestAndResponseInterceptor_NoThrottler() throws InterruptedException, ExecutionException {
    HttpClient client = new HttpClient(mockInterceptor, mockExecutor, null, mockResponseInterceptor);
    HttpRequest request = createTestRequest();
    
    FutureHttpResponse futureResponse = client.execute(request);
    MockFutureHttpResponse mockRequest = mockExecutor.popRequest();
    Assert.assertEquals("Intercepted:TestBody1", mockRequest.getRequest().getBody());
    HttpResponse response = new HttpResponse();
    response.setRequest(mockRequest.getRequest());
    response.setBody("Test response body");
    mockRequest.complete(response);
    HttpResponse finalResponse = futureResponse.get();
    Assert.assertEquals("Intercepted response:Test response body", finalResponse.getBody());
  }
  
  @Test
  public void testExecute_WithResponseInterceptor_NoThrottler_ErrorInResponseInterceptor() throws InterruptedException, ExecutionException {
    HttpClient client = new HttpClient(null, mockExecutor, null, mockResponseInterceptor);
    HttpRequest request = createTestRequest();
    mockResponseInterceptor.addPreparedInterceptor(r -> { throw new RuntimeException("Response interceptor error"); });
    FutureHttpResponse futureResponse = client.execute(request);
    MockFutureHttpResponse mockRequest = mockExecutor.popRequest();
    HttpResponse response = new HttpResponse();
    response.setRequest(mockRequest.getRequest());
    response.setBody("Test response body");
    mockRequest.complete(response);
    HttpResponse finalResponse = futureResponse.get();
    Assert.assertEquals("Response interceptor error", finalResponse.getException().getMessage());
  }
  
  @Test
  public void testExecute_WithResponseInterceptor_NoThrottler_ErrorInReturnedResponse() throws InterruptedException, ExecutionException {
    HttpClient client = new HttpClient(null, mockExecutor, null, mockResponseInterceptor);
    HttpRequest request = createTestRequest();
    mockResponseInterceptor.addPreparedInterceptor(r -> { throw new RuntimeException("Response interceptor error"); });
    FutureHttpResponse futureResponse = client.execute(request);
    MockFutureHttpResponse mockRequest = mockExecutor.popRequest();
    HttpResponse response = new HttpResponse();
    response.setException(new RuntimeException("Response network error"));
    response.setRequest(mockRequest.getRequest());
    response.setBody("Test response body");
    mockRequest.complete(response);
    HttpResponse finalResponse = futureResponse.get();
    Assert.assertEquals("Response network error", finalResponse.getException().getMessage());
  }
  
  @Test
  public void testExecute_WithInterceptor_AndThrottler() throws InterruptedException, ExecutionException {
    HttpClient client = new HttpClient(mockInterceptor, mockExecutor, requestThrottler, null);
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
