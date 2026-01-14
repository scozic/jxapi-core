package org.jxapi.exchange;

import org.jxapi.netutils.rest.FutureRestResponse;
import org.jxapi.netutils.rest.ratelimits.RateLimitRule;
import org.jxapi.netutils.websocket.WebsocketClient;
import org.jxapi.pojo.descriptor.Type;

/**
 * Interface for a group of of REST and/or Websocket endpoints of an API belonging to
 * an {@link Exchange} wrapper.<br>
 * Actual implmentations will expose methods for calling REST enpoints,
 * subscribe / unsubscribe methods for Websocket endpoints,
 * and constants for identifying endpoint names.
 * 
 * <p>
 * Regarding REST endpoints call methods:
 * <ul>
 * <li>Named as API name
 * <li>Have 0 or 1 parameter providing request data (that can be of any
 * {@link Type}).
 * <li>Return a {@link FutureRestResponse} object to receive the response to
 * REST call asynchronously.
 * <li>Is applied endpoint specific {@link RateLimitRule} throughput
 * limitations, shared limits from {@link Exchange}
 * </ul>
 * <p>
 * Regarding Websocket endpoints
 * <ul>
 * <li>Has a subscribe[apiName] method to subscribe a message listener for a
 * specific topic , with one subscription parameters argument of any
 * {@link Type}
 * <li>Has an unsubscribe[apiName] method to unsubscribe a message l istener
 * from a specific topic.
 * <li>Actual websocket connection is opened when first listener is subscribed.
 * <li>Actual websocket connection is closed when last listener is unsubscribed.
 * <li>Associated {@link WebsocketClient} manages keeping alive connection and
 * automatic reconnection and resubscription to topics upon error.
 * </ul>
 */
public interface ExchangeApi {
 
}
