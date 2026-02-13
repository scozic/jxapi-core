package org.jxapi.netutils.rest.ratelimits;

import org.jxapi.netutils.rest.HttpRequest;

/**
 * Exception thrown when rate limits have been breached and request cannot be
 * submitted. An {@link HttpRequest} can be completed with such exception if
 * submitted to an API where some rate limits are enforced and throttling mode
 * is either {link RequestThrottlingMode#BLOCK}, or
 * {@link RequestThrottlingMode#THROTTLE} and a maximum throttle delay has
 * exceeded.
 */
public class RateLimitReachedException extends Exception {
  
  private static final long serialVersionUID = -5826543906820128810L;
  /**
   * Delay to wait for before trying to resubmit request
   */
  private final long delayBeforeResubmit;

  /**
   * Constructor
   * @param rateLimit The rate limit rule that has been breached
   * @param delayBeforeResubmit The minimum time to wait for before retrying failed request that breached request rate limit.
   */
  public RateLimitReachedException(RateLimitRule rateLimit, long delayBeforeResubmit) {
    super(String.format("%s has been breached, request cannot be sent before %dms", rateLimit,  delayBeforeResubmit));
    this.delayBeforeResubmit = delayBeforeResubmit;
  }

  /**
   * @return delay to wait for before trying to resubmit request.
   */
  public long getDelayBeforeResubmit() {
    return delayBeforeResubmit;
  }

}
