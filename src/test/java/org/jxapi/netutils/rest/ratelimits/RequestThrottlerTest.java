package org.jxapi.netutils.rest.ratelimits;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jxapi.netutils.rest.FutureHttpResponse;
import org.jxapi.netutils.rest.HttpRequest;
import org.jxapi.netutils.rest.HttpResponse;

/**
 * Unit test for {@link RequestThrottler}
 */
public class RequestThrottlerTest {
  
  private static final AtomicInteger REQUEST_COUNTER = new AtomicInteger(0);
  
  private RequestThrottler throttler;
  
  @Before
  public void setUp() {
    throttler = new RequestThrottler("myApi");
  }

  @After
  public void tearDown() {
    throttler.dispose();
  }
  
  @Test
  public void testSubmitOneRequest() throws InterruptedException, ExecutionException, TimeoutException {
    RestEndpointStub endpoint = new RestEndpointStub();
    HttpRequestStub request = new HttpRequestStub();
    checkCompletesIn(throttler.submit(request, endpoint), 0L);
  }
  
  @Test
  public void testGetApiName() {
    Assert.assertEquals("myApi", throttler.getApiName());
  }
  
  @Test
  public void testGetApiName_nullByDefault() {
    RequestThrottler throttler2 = new RequestThrottler();
    Assert.assertNull(throttler2.getApiName());
    throttler2.dispose();
  }
  
  @Test
  public void testSubmitRequestsWithSeveralRulesSomeRequestsThrottled() throws InterruptedException, ExecutionException, TimeoutException {
    Assert.assertEquals(RequestThrottlingMode.THROTTLE, throttler.getThrottlingMode());
    Assert.assertEquals(-1L, throttler.getMaxThrottleDelay());
    RestEndpointStub endpoint = new RestEndpointStub();
    
    RateLimitRule rule1 = RateLimitRule.createRule("RULE1", 1000L, 1);
    RateLimitRule rule2 = RateLimitRule.createWeightedRule("RULE2", 3000L, 100);
    
    HttpRequestStub request1 = new HttpRequestStub(rule1, rule2);
    request1.setWeight(50);
    HttpRequestStub request2 = new HttpRequestStub(rule1, rule2);
    request2.setWeight(50);
    HttpRequestStub request3 = new HttpRequestStub(rule1, rule2);
    request3.setWeight(50);
    // Request1 enforces all rules and will be submitted immediately
    checkCompletesIn(throttler.submit(request1, endpoint), 0L);
    // Request2 has to wait 1s to enforce rule1 and but does not reached threshold for rule2
    long start = System.currentTimeMillis();
    FutureHttpResponse response2 = throttler.submit(request2, endpoint);
    FutureHttpResponse response3 = throttler.submit(request3, endpoint);
    checkCompletesIn(response2, 1000L, start);
    checkCompletesIn(response3, 3000L, start);
  }
  
  @Test
  public void testSubmitRequestsWithSeveralRulesSomeRequestsThrottledMaxThrottleDelaySetButNoExceeded() throws InterruptedException, ExecutionException, TimeoutException {
    Assert.assertEquals(RequestThrottlingMode.THROTTLE, throttler.getThrottlingMode());
    throttler.setMaxThrottleDelay(5000L);
    Assert.assertEquals(5000L, throttler.getMaxThrottleDelay());
    RestEndpointStub endpoint = new RestEndpointStub();
    
    RateLimitRule rule1 = RateLimitRule.createRule("RULE1", 1000L, 1);
    RateLimitRule rule2 = RateLimitRule.createWeightedRule("RULE2", 3000L, 100);
    
    HttpRequestStub request1 = new HttpRequestStub(rule1, rule2);
    request1.setWeight(50);
    HttpRequestStub request2 = new HttpRequestStub(rule1, rule2);
    request2.setWeight(50);
    HttpRequestStub request3 = new HttpRequestStub(rule1, rule2);
    request3.setWeight(50);
    // Request1 enforces all rules and will be submitted immediately
    checkCompletesIn(throttler.submit(request1, endpoint), 0L);
    // Request2 has to wait 1s to enforce rule1 and but does not reached threshold for rule2
    long start = System.currentTimeMillis();
    FutureHttpResponse response2 = throttler.submit(request2, endpoint);
    FutureHttpResponse response3 = throttler.submit(request3, endpoint);
    checkCompletesIn(response2, 1000L, start);
    checkCompletesIn(response3, 3000L, start);
  }
  
  @Test
  public void testSubmitRequestsWithSeveralRulesBlockingMode() throws InterruptedException, ExecutionException, TimeoutException {
    throttler.setThrottlingMode(RequestThrottlingMode.BLOCK);
    RestEndpointStub endpoint = new RestEndpointStub();
    
    RateLimitRule rule1 = RateLimitRule.createRule("RULE1", 1000L, 1);
    RateLimitRule rule2 = RateLimitRule.createWeightedRule("RULE2", 3000L, 100);
    
    HttpRequestStub request1 = new HttpRequestStub(rule1, rule2);
    request1.setWeight(50);
    HttpRequestStub request2 = new HttpRequestStub(rule1, rule2);
    request2.setWeight(50);
    // Request1 enforces all rules and will be submitted immediately
    checkCompletesIn(throttler.submit(request1, endpoint), 0L);
    // Request2 has to wait 1s to enforce rule1 and will be rejected because BLOCK mode is used
    FutureHttpResponse response2 = throttler.submit(request2, endpoint);
    HttpResponse restResponse2 = response2.get(5000L, TimeUnit.MILLISECONDS);
    Assert.assertEquals(429, restResponse2.getResponseCode());
    RateLimitReachedException ex = (RateLimitReachedException) restResponse2.getException();
    Assert.assertNotNull(ex);
  }
  
  @Test
  public void testSubmitRequestsWithSeveralRulesThrottlingModeAndMaxThrottlingDelayExceeded() throws InterruptedException, ExecutionException, TimeoutException {
    throttler.setThrottlingMode(RequestThrottlingMode.THROTTLE);
    throttler.setMaxThrottleDelay(500L);
    Assert.assertEquals(500L, throttler.getMaxThrottleDelay());
    RestEndpointStub endpoint = new RestEndpointStub();
    
    RateLimitRule rule1 = RateLimitRule.createRule("RULE1", 1000L, 1);
    RateLimitRule rule2 = RateLimitRule.createWeightedRule("RULE2", 3000L, 100);
    
    HttpRequestStub request1 = new HttpRequestStub(rule1, rule2);
    request1.setWeight(50);
    HttpRequestStub request2 = new HttpRequestStub(rule1, rule2);
    request2.setWeight(50);
    // Request1 enforces all rules and will be submitted immediately
    checkCompletesIn(throttler.submit(request1, endpoint), 0L);
    // Request2 has to wait 1s to enforce rule1 and will be rejected because BLOCK mode is used
    FutureHttpResponse response2 = throttler.submit(request2, endpoint);
    HttpResponse restResponse2 = response2.get(5000L, TimeUnit.MILLISECONDS);
    Assert.assertEquals(429, restResponse2.getResponseCode());
    RateLimitReachedException ex = (RateLimitReachedException) restResponse2.getException();
    Assert.assertNotNull(ex);
  }
  
  @Test
  public void testSubmitRequestsWithSeveralRulesNoneMode() throws InterruptedException, ExecutionException, TimeoutException {
    RestEndpointStub endpoint = new RestEndpointStub();
    throttler.setThrottlingMode(RequestThrottlingMode.NONE);
    RateLimitRule rule1 = RateLimitRule.createRule("RULE1", 1000L, 1);
    RateLimitRule rule2 = RateLimitRule.createWeightedRule("RULE2", 3000L, 100);
    
    HttpRequestStub request1 = new HttpRequestStub(rule1, rule2);
    request1.setWeight(50);
    HttpRequestStub request2 = new HttpRequestStub(rule1, rule2);
    request2.setWeight(50);
    HttpRequestStub request3 = new HttpRequestStub(rule1, rule2);
    request3.setWeight(50);
    // Request1 enforces all rules and will be submitted immediately
    checkCompletesIn(throttler.submit(request1, endpoint), 0L);
    // Request2 has to wait 1s to enforce rule1 and but does not reached threshold for rule2
    long start = System.currentTimeMillis();
    FutureHttpResponse response2 = throttler.submit(request2, endpoint);
    FutureHttpResponse response3 = throttler.submit(request3, endpoint);
    checkCompletesIn(response2, 0L, start);
    checkCompletesIn(response3, 0L, start);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testSubmitRequestOnDisposedThrottlerThrowsExceptin() {
    throttler.dispose();
    RestEndpointStub endpoint = new RestEndpointStub();
    HttpRequestStub request = new HttpRequestStub();
    throttler.submit(request, endpoint);
  }
  
  private void checkCompletesIn(FutureHttpResponse response, long delay) throws InterruptedException, ExecutionException, TimeoutException {
    checkCompletesIn(response, delay, System.currentTimeMillis());
  }
  
  private void checkCompletesIn(FutureHttpResponse response, long delay, long start) throws InterruptedException, ExecutionException, TimeoutException {
    long end = response.get(5000L, TimeUnit.MILLISECONDS).getTime().getTime();
    long actualDelay = end - start;
    Assert.assertTrue("Response received after:" + actualDelay + ", instead of at least:" + delay, 
              actualDelay >= delay - 500L && actualDelay <= delay + 500L);
  }
  
  private class RestEndpointStub implements Function<HttpRequest, FutureHttpResponse> {

    @Override
    public FutureHttpResponse apply(HttpRequest request) {
      HttpResponse httpResponse = new HttpResponse();
      httpResponse.setRequest(request);
      httpResponse.setTime(new Date());
      FutureHttpResponse response = new FutureHttpResponse();
      response.complete(httpResponse);
      return response;
    }
    
  }
  
  private class HttpRequestStub extends HttpRequest {
    public HttpRequestStub(RateLimitRule...limitRules) {
      this.setRateLimits(List.of(limitRules));
      this.setUrl("url" + REQUEST_COUNTER.getAndIncrement());
    }
  }
}

