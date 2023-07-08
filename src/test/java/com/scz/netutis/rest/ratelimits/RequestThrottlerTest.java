package com.scz.netutis.rest.ratelimits;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.netutils.rest.FutureRestResponse;
import com.scz.jxapi.netutils.rest.RestEndpoint;
import com.scz.jxapi.netutils.rest.RestRequest;
import com.scz.jxapi.netutils.rest.RestResponse;
import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;
import com.scz.jxapi.netutils.rest.ratelimits.RequestThrottler;

public class RequestThrottlerTest {
	
	private static final AtomicInteger REQUEST_COUNTER = new AtomicInteger(0);

	@Test
	public void testSubmitOneRequest() {
		RequestThrottler throttler = new RequestThrottler();
		RestEndpointStub endpoint = new RestEndpointStub();
		RestRequestStub request = new RestRequestStub();
		checkCompletesIn(throttler.submit(request, endpoint), 0L);
	}
	
	@Test
	public void testSubmitRequestsWithSeveralRules() {
		RequestThrottler throttler = new RequestThrottler();
		RestEndpointStub endpoint = new RestEndpointStub();
		
		RateLimitRule rule1 = RateLimitRule.createRule("RULE1", 1000L, 1);
		RateLimitRule rule2 = RateLimitRule.createWeightedRule("RULE2", 3000L, 100);
		
		RestRequestStub request1 = new RestRequestStub(rule1, rule2);
		request1.setWeight(50);
		RestRequestStub request2 = new RestRequestStub(rule1, rule2);
		request2.setWeight(50);
		RestRequestStub request3 = new RestRequestStub(rule1, rule2);
		request3.setWeight(50);
		// Request1 enforces all rules and will be submitted immediately
		checkCompletesIn(throttler.submit(request1, endpoint), 0L);
		// Request2 has to wait 1s to enforce rule1 and but does not reached threshold for rule2
		long start = System.currentTimeMillis();
		FutureRestResponse<Long> response2 = throttler.submit(request2, endpoint);
		FutureRestResponse<Long> response3 = throttler.submit(request3, endpoint);
		checkCompletesIn(response2, 1000L, start);
		checkCompletesIn(response3, 3000L, start);
	}
	
	private void checkCompletesIn(FutureRestResponse<Long> response, long delay) {
		checkCompletesIn(response, delay, System.currentTimeMillis());
	}
	
	private void checkCompletesIn(FutureRestResponse<Long> response, long delay, long start) {
		long end = 0L;
		try {
			end = response.get().getResponse();
		} catch (InterruptedException | ExecutionException e) {
			Assert.fail("Error submitting:" + response);
		}
		long actualDelay = end - start;
		Assert.assertTrue("Response received after:" + actualDelay + ", instead of at least:" + delay, actualDelay >= delay - 500L && actualDelay <= delay + 500L);
	}
	
	private class RestEndpointStub implements RestEndpoint<String, Long> {

		@Override
		public FutureRestResponse<Long> call(RestRequest<String> request) {
			FutureRestResponse<Long> response = new FutureRestResponse<>();
			RestResponse<Long> restResponse = new RestResponse<>();
			restResponse.setResponse(System.currentTimeMillis());
			response.complete(restResponse);
			return response;
		}
		
	}
	
	private class RestRequestStub extends RestRequest<String> {
		public RestRequestStub(RateLimitRule...limitRules) {
			this.setRateLimits(List.of(limitRules));
			this.setUrl("url" + REQUEST_COUNTER.getAndIncrement());
		}
	}
}

