package com.scz.jxapi.netutils.rest.ratelimits;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.scz.jxapi.netutils.rest.FutureRestResponse;
import com.scz.jxapi.netutils.rest.HttpRequest;
import com.scz.jxapi.util.DefaultDisposable;
import com.scz.jxapi.util.ThreadUtil;

/**
 * Enforces a list of applicable {@link RateLimitRule} to incoming requests.
 * Submitted requests may be throttled before being actually sent, see
 * {@link #submit(HttpRequest, Function)}. In case a request is throttled,
 * as {@link ScheduledExecutorService} for scheduling retry of submission of
 * request is instantated. It wiil be shutdown only upon call to
 * {@link #dispose()} which is duty of client to call when disposing resources.
 */
public class RequestThrottler extends DefaultDisposable {
	
	private static final Logger log = LoggerFactory.getLogger(RequestThrottler.class);

	private final Map<String, RateLimitThrottling> rateLimitManagers = new HashMap<>();
	
	private ScheduledExecutorService throttlingExecutor = null;

	private final String apiName;
	
	/**
	 * Creates a new instance of {@link RequestThrottler} with no API name.
	 */
	public RequestThrottler() {
		this(null);
	}
	
	/**
	 * Creates a new instance of {@link RequestThrottler} with given API name.
	 * 
	 * @param apiName Name of the API this throttler is used for.
	 */
	public RequestThrottler(String apiName) {
		this.apiName = apiName;
	}
	
	/**
	 * @return Name of the API this throttler is used for.
	 */
	public String getApiName() {
		return apiName;
	}
	
	/**
	 * Submits a {@link HttpRequest} for asynchronous execution, enforcing rate
	 * limits applicable for given request which could mean scheduling execution at
	 * later date if a rate limit is reached. A {@link RateLimitManager} for each
	 * {@link RateLimitRule} of the request will be created when a rate limit
	 * with given id is first submitted. Further request submitted with same rate
	 * limit will be checked for this limiit using same
	 * {@link RateLimitManager}.<br>
	 * When one of {@link RateLimitManager} managing a rate lmit applicable to a
	 * request returns a positive delay to wait (see
	 * {@link RateLimitManager#requestCall()} the request is scheduled for retry
	 * after delay where it could be submitted to enforce this rate limit.<br>
	 * If there is already a request queued to wait for some delay to enforce a rate
	 * limit when a request is submitted that is subject to same limit, this newly
	 * submitted request will not be retried after the waiting one. This is to make
	 * sure a weighted request that has smaller weight and could be executed without
	 * waiting, will not be executed before a 'heavier' request that has been
	 * submitted before but has to wait.
	 * 
	 * @param <A>          Type of response payload
	 * @param request      the request submitted for execution
	 * @param executor     the function to execute this request. Should wrap actual call to REST API.
	 * @return Callback that will complete once response is received.
	 * 
	 * @throws IllegalStateException if in 'disposed' state when called(see {@link #isDisposed()})
	 */
	public synchronized <A> FutureRestResponse<A> submit(HttpRequest request, Function<HttpRequest, FutureRestResponse<A>> executor) {
		checkNotDisposed();
		List<RateLimitRule> rateLimits = request.getRateLimits();
		if (CollectionUtils.isEmpty(rateLimits)) {
			log.debug("No rate limit set, submitting now:,{}", request);
			return executor.apply(request);
		}
		for (RateLimitRule rateLimit: rateLimits) {
			final RateLimitThrottling rlManager = getOrCreateRateLimit(rateLimit);
			FutureRestResponse<?> queued = rlManager.queued;
			if (queued != null) {
				log.debug("Already has a queued request for {}, request:{} will be submitted again after it completes", 
						  rlManager.rateLimitManager.getRateLimit(), request);
				FutureRestResponse<A> r = new FutureRestResponse<>();
				queued.thenRun(() -> submit(request, executor).thenAccept(r::complete));
				return r;
			}
			
			long delay = rlManager.rateLimitManager.requestCall(request.getWeight());
			if (delay > 0) {
				log.debug("Rate limit {} reached, scheduling request:{} for later execution in {}ms", 
						  rlManager.rateLimitManager.getRateLimit(), request, delay);
				return queue(request, executor, delay, rlManager);
			}
		}
		log.debug("All request rules checked, submitting now:{}", request);
		return executor.apply(request);
	}
	
	private RateLimitThrottling getOrCreateRateLimit(RateLimitRule rateLimit) {
		return rateLimitManagers.computeIfAbsent(rateLimit.getId(), k -> new RateLimitThrottling(rateLimit));
	}
	
	private <A> FutureRestResponse<A> queue(HttpRequest request, Function<HttpRequest, FutureRestResponse<A>> executor, long delay, RateLimitThrottling rateLimit) {
		FutureRestResponse<A> r = new FutureRestResponse<>();
		rateLimit.queued = r;
		if (throttlingExecutor == null) {
			String namePrefix = "THROTTLE";
			if (this.apiName != null) {
				namePrefix = this.apiName + "-" + namePrefix;
			}
			throttlingExecutor = Executors.newSingleThreadScheduledExecutor(ThreadUtil.createNamePrefixThreadFactory(namePrefix));
		}
		throttlingExecutor.schedule(() -> {
			queuedRequestCompleted(rateLimit);
			submit(request, executor).thenAccept(r::complete);
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
	
	@Override
	protected void doDispose() {
		rateLimitManagers.clear();
		if (throttlingExecutor != null) {
			throttlingExecutor.shutdown();
		}
	}

}
