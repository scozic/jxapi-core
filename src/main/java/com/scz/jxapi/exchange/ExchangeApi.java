package com.scz.jxapi.exchange;

import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.netutils.rest.FutureRestResponse;
import com.scz.jxapi.netutils.rest.HttpRequestExecutor;
import com.scz.jxapi.netutils.rest.HttpRequestInterceptor;
import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;
import com.scz.jxapi.netutils.websocket.WebsocketManager;
import com.scz.jxapi.observability.ExchangeApiObserver;
import com.scz.jxapi.util.HasProperties;

/**
 * Interface for a set of REST and/or Websocket endpoints of an API belonging to an {@link Exchange}
 * wrapper.</br>
 * Actual implmentations will expose methods for to call REST enpoints, and subscribe / unsubscribe methods for Websocket endpoints.
 * </ul>
 * <p>
 * Regarding REST endpoints call methods:
 * <ul>
 * <li>Named as API name
 * <li>Have 0 or 1 parameter providing request data (that can be of any {@link Type}).
 * <li>Return a {@link FutureRestResponse} object to receive the response to REST call asynchronously.
 * <li>Is bound to same {@link HttpRequestExecutor} and {@link HttpRequestInterceptor}
 * <li>Is applied endpoint specific {@link RateLimitRule} throughput limitations, shared limits from API and from {@link Exchange}
 * </ul>
 * </p>
 * Regarding Websocket endpoints
 * <p>
 * <ul>
 * <li>Has a subscribe[apiName] method to subscribe a message listener for a specific topic , with one subscription parameters argument of any {@link Type}
 * <li>Each websocket endpoint shares the same {@link WebsocketManager}
 * <li>Has an unsubscribe[apiName] method to unsubscribe a message l istener from a specific topic.
 * <li>Actual websocket connection is opened when first listener is subscribed.
 * <li>Actual websocket connection is closed when last listener is unsubscribed. 
 * <li>Shared {@link WebsocketManager} manages keeping alive connection and automatic reconnection and resubscription to topics upon error.
 * </ul>
 * </p>
 * <p>
 * Observability API (see {@link #subscribeObserver(ExchangeApiObserver)}) can be used to monitor REST endpoint calls, and Websocket subscriptions, received message and errors.<br/> 
 * Recurring disconnection errors on websocket may indicate network or remote end service outage or bad configuration.
 */
public interface ExchangeApi extends HasProperties {
	
	/**
	 * @return API group name (unique for every instance of same class)
	 */
	String getName();
	
	/**
	 * @return Name of {@link Exchange} this API group belongs to.
	 */
	String getExchangeName();
	
	String getExchangeId();
	
	void subscribeObserver(ExchangeApiObserver exchangeApiObserver);
	
	boolean unsubscribeObserver(ExchangeApiObserver exchangeApiObserver);

	

}
