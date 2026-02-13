package org.jxapi.netutils.rest.ratelimits;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.jxapi.netutils.rest.FutureHttpResponse;
import org.jxapi.netutils.rest.HttpRequest;
import org.jxapi.netutils.rest.HttpResponse;
import org.jxapi.util.DefaultDisposable;
import org.jxapi.util.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

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
  
  private long maxThrottleDelay = -1L;
  
  private RequestThrottlingMode throttlingMode = RequestThrottlingMode.THROTTLE;
  
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
   * {@link RateLimitRule} of the request will be created when a rate limit with
   * given id is first submitted. Further request submitted with same rate limit
   * will be checked for this limiit using same {@link RateLimitManager}.<br>
   * When one of {@link RateLimitManager} managing a rate lmit applicable to a
   * request returns a positive delay to wait (see
   * {@link RateLimitManager#requestCall()}, the result depends on configured
   * 'throttling mode' (see {@link #getThrottlingMode()}):
   * <ul>
   * <li><strong>If it is {@link RequestThrottlingMode#THROTTLE} (default) and
   * delay before resubmit is &lt; to configured max throttle delay (see
   * {@link #getMaxThrottleDelay()})</strong> : the request is scheduled for retry
   * after delay where it could be submitted to enforce this rate limit.<br>
   * If there is already a request queued to wait for some delay to enforce a rate
   * limit when a request is submitted that is subject to same limit, this newly
   * submitted request will not be retried after the waiting one. This is to make
   * sure a weighted request that has smaller weight and could be executed without
   * waiting, will not be executed before a 'heavier' request that has been
   * submitted before but has to wait.
   * <li><strong>If it is {@link RequestThrottlingMode#BLOCK} or
   * {@link RequestThrottlingMode#THROTTLE} and delay before resubmit is &gt; to
   * configured max throttle delay (see {@link #getMaxThrottleDelay()})</strong> :
   * The request is completed with HTTP status 409 (TOO MANY REQUESTS) and a
   * {@link RateLimitReachedException} that contains the minimum time to wait
   * before trying to resubmit request.
   * <li><strong>If it is {@link RequestThrottlingMode#NONE}</strong> : No
   * throttling is applied and request is submitted regardless of rate limits
   * breached.
   * </ul>
   * 
   * @param request  the request submitted for execution
   * @param executor the function to execute this request. Should wrap actual call
   *                 to REST API.
   * @return Callback that will complete once response is received.
   * 
   * @throws IllegalStateException if in 'disposed' state when called(see
   *                               {@link #isDisposed()})
   */
  public synchronized FutureHttpResponse submit(HttpRequest request, Function<HttpRequest, FutureHttpResponse> executor) {
    checkNotDisposed();
    List<RateLimitRule> rateLimits = request.getRateLimits();
    if (CollectionUtils.isEmpty(rateLimits) || throttlingMode == RequestThrottlingMode.NONE) {
      log.debug("No rate limit set, submitting now:,{}", request);
      return executor.apply(request);
    }
    for (RateLimitRule rateLimit: rateLimits) {
      final RateLimitThrottling rlManager = getOrCreateRateLimit(rateLimit);
      FutureHttpResponse queued = rlManager.queued;
      if (queued != null) {
        log.debug("Already has a queued request for {}, request:{} will be submitted again after it completes", 
              rlManager.rateLimitManager.getRule(), request);
        FutureHttpResponse r = new FutureHttpResponse();
        queued.thenRun(() -> submit(request, executor).thenAccept(r::complete));
        return r;
      }
      
      long delay = rlManager.rateLimitManager.requestCall(request.getWeight());
      if (delay > 0) {
        if (throttlingMode == RequestThrottlingMode.BLOCK || (throttlingMode == RequestThrottlingMode.THROTTLE && maxThrottleDelay >=0 && delay > maxThrottleDelay)) {
          return completeWithRateLimitReachedException(request, rateLimit, delay);
        }
         log.debug("Rate limit {} reached, scheduling request:{} for later execution in {}ms", 
              rlManager.rateLimitManager.getRule(), request, delay);
        return queue(request, executor, delay, rlManager);
      }
    }
    log.debug("All request rules checked, submitting now:{}", request);
    return executor.apply(request);
  }
  
  private FutureHttpResponse completeWithRateLimitReachedException(HttpRequest request, RateLimitRule rateLimit, long delayBeforeResubmit) {
    FutureHttpResponse futureResponse = new FutureHttpResponse();
    HttpResponse httpResponse = new HttpResponse();
    httpResponse.setRequest(request);
    httpResponse.setTime(new Date());
    httpResponse.setResponseCode(429);// Too many requests
    httpResponse.setException(new RateLimitReachedException(rateLimit, delayBeforeResubmit));
    futureResponse.complete(httpResponse);
    return futureResponse;
  }
  
  /**
   * @return maximum allowed throttle delay for a request. If a submitted request
   *         should be throttled more than this delay, it will be completed with
   *         an exception instead of being sent. A negative value (default) means
   *         there is no limit for throttling delay.
   */
  public synchronized long getMaxThrottleDelay() {
    return maxThrottleDelay;
  }

  /**
   * @param maxThrottleDelay maximum allowed throttle delay for a request. If a
   *                         submitted request should be throttled more than this
   *                         delay, it will be completed with an exception instead
   *                         of being sent.
   */
  public synchronized void setMaxThrottleDelay(long maxThrottleDelay) {
    this.maxThrottleDelay = maxThrottleDelay;
  }

  /**
   * @return The policy applied when a submitted request breaches some rate
   *         limits. Default is {@link RequestThrottlingMode#toString()}
   */
  public synchronized RequestThrottlingMode getThrottlingMode() {
    return throttlingMode;
  }

  /**
   * @param throttlingMode The policy applied when a submitted request breaches
   *                       some rate limits.
   */
  public synchronized void setThrottlingMode(RequestThrottlingMode throttlingMode) {
    this.throttlingMode = throttlingMode;
  }
  
  private RateLimitThrottling getOrCreateRateLimit(RateLimitRule rateLimit) {
    return rateLimitManagers.computeIfAbsent(rateLimit.getId(), k -> new RateLimitThrottling(rateLimit));
  }
  
  private FutureHttpResponse queue(HttpRequest request, Function<HttpRequest, FutureHttpResponse> executor, long delay, RateLimitThrottling rateLimit) {
    FutureHttpResponse r = new FutureHttpResponse();
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
  
  private void queuedRequestCompleted(RateLimitThrottling rateLimit) {
    rateLimit.queued = null;
  }
  
  private class RateLimitThrottling {
    final RateLimitManager rateLimitManager;
    FutureHttpResponse queued = null;
    
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
