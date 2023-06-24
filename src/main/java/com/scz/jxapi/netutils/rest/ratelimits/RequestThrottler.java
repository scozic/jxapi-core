package com.scz.jxapi.netutils.rest.ratelimits;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.netutils.rest.FutureRestResponse;
import com.scz.jxapi.netutils.rest.RestEndpoint;
import com.scz.jxapi.netutils.rest.RestRequest;
import com.scz.jxapi.util.ThreadUtil;

/**
 * Enforces a list of applicable {@link RateLimitRule} to incoming requests. Submitted requests may be throttled before being actually sent, see {@link #submit(RestRequest, RestEndpoint)} 
 */
public class RequestThrottler {
	
	private static final Logger log = LoggerFactory.getLogger(RequestThrottler.class);

	private final Map<String, RateLimitThrottling> rateLimitManagers = new HashMap<>();
	
	private ScheduledExecutorService throttlingExecutor = null;
	
	/**
	 * Submits a {@link RestRequest} for asynchronous execution, enforcing rate
	 * limits applicable for given request which could mean scheduling execution at
	 * later date if a rate limit is reached. A {@link RateLimitManager} for each
	 * {@link RateLimitRule} of the request will be created when a rate limit
	 * with given id is first submitted. Further request submitted with same rate
	 * limit will be checked for this limiit using same
	 * {@link RateLimitManager}.<br/>
	 * When one of {@link RateLimitManager} managing a rate lmit applicable to a
	 * request returns a positive delay to wait (see
	 * {@link RateLimitManager#requestCall()} the request is scheduled for retry
	 * after delay where it could be submitted to enforce this rate limit.<br/>
	 * If there is already a request queued to wait for some delay to enforce a rate
	 * limit when a request is submitted that is subject to same limit, this newly
	 * submitted request will not be retried after the waiting one. This is to make
	 * sure a weighted request that has smaller weight and could be executed without
	 * waiting, will not be executed before a 'heavier' request that has been
	 * submitted before but has to wait.
	 * 
	 * @param <R>          Type of REST request payload
	 * @param <A>          Type of response payload
	 * @param request      the request submitted for execution
	 * @param restEndpoint the endpoint to execute this request
	 * @return Callback that will complete once response is received.
	 */
	public synchronized <R, A> FutureRestResponse<A> submit(RestRequest<R> request, RestEndpoint<R, A> restEndpoint) {
		List<RateLimitRule> rateLimits = request.getRateLimits();
		if (rateLimits == null || rateLimits.isEmpty()) {
			if (log.isDebugEnabled())
				log.debug("No rate limit set, submitting now:" + request);
			return restEndpoint.call(request);
		}
		for (RateLimitRule rateLimit: rateLimits) {
			final RateLimitThrottling rlManager = getOrCreateRateLimit(rateLimit);
			FutureRestResponse<?> queued = rlManager.queued;
			if (queued != null) {
				if (log.isDebugEnabled()) {
					log.debug("Already has a queued request for " + rlManager.rateLimitManager.getRateLimit()
								+ ", request:" + request + " will be submitted again after it completes");
				}
				FutureRestResponse<A> r = new FutureRestResponse<>();
				queued.thenRun(() -> submit(request, restEndpoint).thenAccept(restResponse -> r.complete(restResponse)));
				return r;
			}
			
			long delay = rlManager.rateLimitManager.requestCall(request.getWeight());
			if (delay > 0) {
				if (log.isDebugEnabled()) {
					log.debug("Rate limit " + rlManager.rateLimitManager.getRateLimit()
								+ " reached, scheduling request:" + request + " for later execution in " + delay + "ms");
				}
				return queue(request, restEndpoint, delay, rlManager);
			}
		}
		if (log.isDebugEnabled())
			log.debug("All request rules checked, submitting now:" + request);
		return restEndpoint.call(request);
	}
	
	private RateLimitThrottling getOrCreateRateLimit(RateLimitRule rateLimit) {
		String id = rateLimit.getId();
		RateLimitThrottling r = rateLimitManagers.get(id);
		if (r == null) {
			r = new RateLimitThrottling(rateLimit);
			rateLimitManagers.put(id, r);
		}
		return r;
	}
	
	private <R, A> FutureRestResponse<A> queue(RestRequest<R> request, RestEndpoint<R, A> restEndpoint, long delay, RateLimitThrottling rateLimit) {
		FutureRestResponse<A> r = new FutureRestResponse<>();
		rateLimit.queued = r;
		if (throttlingExecutor == null) {
			throttlingExecutor = Executors.newSingleThreadScheduledExecutor(ThreadUtil.createNamePrefixThreadFactory("THROTTLE"));
		}
		throttlingExecutor.schedule(() -> {
			queuedRequestCompleted(rateLimit);
			submit(request, restEndpoint).thenAccept(httpResponse -> {
				r.complete(httpResponse);
			});
		}, delay, TimeUnit.MILLISECONDS);
		return r;
	}
	
	private synchronized void queuedRequestCompleted(RateLimitThrottling rateLimit) {
		rateLimit.queued = null;
	}
	
	private class RateLimitThrottling {
		final RateLimitManager rateLimitManager;
		FutureRestResponse<?> queued = null;
		
		public RateLimitThrottling(RateLimitRule rateLimit) {
			rateLimitManager = new RateLimitManager(rateLimit);
		}
	}

}
