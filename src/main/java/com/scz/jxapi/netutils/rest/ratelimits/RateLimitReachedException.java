package com.scz.jxapi.netutils.rest.ratelimits;

import com.scz.jxapi.netutils.rest.HttpRequest;

/**
 * Exception thrown when rate limits have been breached and request cannot be
 * submitted. An {@link HttpRequest} can be completed with such exception if
 * submitted to an API where some rate limits are enforced and throttling mode
 * is either {@value RequestThrottlingMode#BLOCK}, or
 * {@value RequestThrottlingMode#THROTTLE} and a maximum throttle delay has
 * exceeded.
 */
public class RateLimitReachedException extends Exception {
	
	private final long delayBeforeResubmit;

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
