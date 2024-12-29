package com.scz.jxapi.exchange;

import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.netutils.rest.FutureRestResponse;
import com.scz.jxapi.netutils.rest.HttpRequestExecutor;
import com.scz.jxapi.netutils.rest.HttpRequestInterceptor;
import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;
import com.scz.jxapi.netutils.rest.ratelimits.RequestThrottler;
import com.scz.jxapi.netutils.rest.ratelimits.RequestThrottlingMode;
import com.scz.jxapi.netutils.websocket.WebsocketManager;
import com.scz.jxapi.util.Disposable;
import com.scz.jxapi.util.HasProperties;

/**
 * Interface for a set of REST and/or Websocket endpoints of an API belonging to
 * an {@link Exchange} wrapper.<br>
 * Actual implmentations will expose methods for calling REST enpoints, and
 * subscribe / unsubscribe methods for Websocket endpoints.
 * 
 * <p>
 * Regarding REST endpoints call methods:
 * <ul>
 * <li>Named as API name
 * <li>Have 0 or 1 parameter providing request data (that can be of any
 * {@link Type}).
 * <li>Return a {@link FutureRestResponse} object to receive the response to
 * REST call asynchronously.
 * <li>Shares the same {@link HttpRequestExecutor} and
 * {@link HttpRequestInterceptor} as other endpoints of this API group
 * <li>Is applied endpoint specific {@link RateLimitRule} throughput
 * limitations, shared limits from API and from {@link Exchange}
 * </ul>
 * <p>
 * Regarding Websocket endpoints
 * <ul>
 * <li>Has a subscribe[apiName] method to subscribe a message listener for a
 * specific topic , with one subscription parameters argument of any
 * {@link Type}
 * <li>Each websocket endpoint shares the same {@link WebsocketManager}
 * <li>Has an unsubscribe[apiName] method to unsubscribe a message l istener
 * from a specific topic.
 * <li>Actual websocket connection is opened when first listener is subscribed.
 * <li>Actual websocket connection is closed when last listener is unsubscribed.
 * <li>Shared {@link WebsocketManager} manages keeping alive connection and
 * automatic reconnection and resubscription to topics upon error.
 * </ul>
 * <p>
 * Observability API (see {@link #subscribeObserver(ExchangeApiObserver)}) can
 * be used to monitor REST endpoint calls, and Websocket subscriptions, received
 * message and errors.<br>
 * Recurring disconnection errors on websocket may indicate network or remote
 * end service outage or bad configuration.
 */
public interface ExchangeApi extends Disposable, HasProperties {
	
	/**
	 * @return Unique API Group name (common to all instances).
	 */
	String getName();

	/**
	 * @return Exchange instance name see {@link Exchange#getName()}
	 */
	String getExchangeName();

	/**
	 * @return Unique exchange name (common to all instances) see
	 *         {@link Exchange#getId()}
	 */
	String getExchangeId();

	/**
	 * Subscribes an observer to be notified of all REST or Websocket events of this
	 * API group endpoints.
	 * 
	 * @param exchangeApiObserver observer that will be notifed of incoming
	 *                            {@link ExchangeApiEvent} events.
	 */
	void subscribeObserver(ExchangeApiObserver exchangeApiObserver);

	/**
	 * Unsubscribes an observer from being notified of all REST or Websocket events
	 * of this API group endpoints.
	 * 
	 * @param exchangeApiObserver observer that will be notifed of incoming
	 *                            {@link ExchangeApiEvent} events.
	 * @return true if observer was subscribed, false otherwise.
	 */
	boolean unsubscribeObserver(ExchangeApiObserver exchangeApiObserver);
	
	/**
	 * Sets the request throttling policy for calls to exposed REST endpoints.
	 * Relevant when some of REST endpoints must enfore rate limit rules
	 * <p>
	 * Warning: If some rate limits are defined at 'exchange' level, the
	 * {@link RequestThrottler} instance is shared between all API groups, changing
	 * this parameter also changes it for other API groups. Instead of using this
	 * method, it is recommended to set this property for whole exchange using
	 * {@link Exchange#setRequestThrottlingMode(RequestThrottlingMode)}
	 * 
	 * @see RateLimitRule
	 * @see RequestThrottler
	 * @param requestThrottlingMode the request throttling policy
	 */
	void setRequestThrottlingMode(RequestThrottlingMode requestThrottlingMode);
	
	/**
	 * 
	 * @return
	 */
	RequestThrottlingMode getRequestThrottlingMode();

	/**
	 * The maximum request throttle delay applied for REST endpoints when rate
	 * limits are breached and some time has to be awaited for before resubmitting a
	 * request. Relevant when some {@link RateLimitRule} are defined. A negative
	 * value means no request throttle delay limit. {@link ExchangeApi}.
	 * <p>
	 * Warning: If some rate limits are defined at 'exchange' level, the
	 * {@link RequestThrottler} instance is shared between all API groups, changing
	 * this parameter also changes it for other API groups. Instead of using this
	 * method, it is recommended to set this property for whole exchange using
	 * {@link Exchange#setMaxRequestThrottleDelay(long)}
	 * 
	 * @param maxRequestThrottleDelay The maximum request throttle delay
	 */
	void setMaxRequestThrottleDelay(long maxRequestThrottleDelay);
	
	/**
	 * @return The maximum request throttle delay applied for REST endpoint APIs
	 *         requests, or a negative value if there are no rate limits set or no max request
	 *         throttle delay limit.
	 */
	long getMaxRequestThrottleDelay();
}
