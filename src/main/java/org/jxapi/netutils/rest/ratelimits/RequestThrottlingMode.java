package org.jxapi.netutils.rest.ratelimits;

/**
 * Possible behaviors of a {@link RequestThrottler} when a rate limit threshold is reached.
 */
public enum RequestThrottlingMode {
  /**
   * None means no throttling is performed and request is sent regardless of rate
   * limit status, possibly bypassing rate limits.
   */
  NONE,

  /**
   * Block means submitted request will be completed with an exception without
   * being sent when a rate limit is breached. This would be the result of sending
   * request to exchange, but not sending request keeps away possible ban from
   * server.
   */
  BLOCK,

  /**
   * Throttles the request, e.g. waits for minimum delay to elapse so request can
   * be sent without exceeding rate limits before trying to resubmit it.
   */
  THROTTLE

}
