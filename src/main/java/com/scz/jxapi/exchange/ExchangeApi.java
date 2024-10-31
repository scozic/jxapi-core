package com.scz.jxapi.exchange;

import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.netutils.rest.FutureRestResponse;
import com.scz.jxapi.netutils.rest.HttpRequestExecutor;
import com.scz.jxapi.netutils.rest.HttpRequestInterceptor;
import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;
import com.scz.jxapi.netutils.websocket.WebsocketManager;
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
public interface ExchangeApi extends HasProperties {
	
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
}
