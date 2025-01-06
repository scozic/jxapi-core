package com.scz.jxapi.exchange;

import java.util.List;
import java.util.Properties;

import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;
import com.scz.jxapi.netutils.rest.ratelimits.RequestThrottlingMode;
import com.scz.jxapi.util.Disposable;
import com.scz.jxapi.util.HasProperties;

/**
 * Entry point of a REST/WEBSOCKET API wrapper.<br>
 * Such wrapper is instantiated using specific configuration {@link Properties}
 * and name identifier. Actual implementations will expose wrapper endpoints
 * through one or more {@link ExchangeApi}
 * <p>
 * Every instance has custom name, and id unique for exchange class.
 * Configuration properties are also supplied.
 * <p>
 * Actual implementations will expose getter methods for each
 * {@link ExchangeApi} implementation belonging to this exchange.<br>
 * The idea of exposing endpoint in separate {@link ExchangeApi} groups helps
 * regrouping APIs related to same business feature.
 * 
 * @see ExchangeApi
 */
public interface Exchange extends Disposable, HasProperties {

	/**
	 * @return A name identifying an instance of wrapper.
	 */
	String getName();

	/**
	 * @return Identifier unique for an actual {@link Exchange} implementation
	 *         class.
	 */
	String getId();

	/**
	 * Subscribes an observer to every {@link ExchangeApi} exposed.
	 * 
	 * @param exchangeApiObserver observer that will monitor every endpoint of every
	 *                            {@link ExchangeApi} exposed.
	 * @see ExchangeApi
	 */
	void subscribeObserver(ExchangeApiObserver exchangeApiObserver);

	/**
	 * Unsubscribes an observer from every {@link ExchangeApi} exposed.
	 * 
	 * @param exchangeApiObserver observer to remove
	 * @see ExchangeApi
	 * @return <code>true</code> if observer was actually removed from at least one
	 *         endpoint, <code>false</code> otherwise.
	 */
	boolean unsubscribeObserver(ExchangeApiObserver exchangeApiObserver);

	/**
	 * Returns list of {@link ExchangeApi} instances exposed by this exchange.
	 * A getter method for each {@link ExchangeApi} implementation belonging to this
	 * exchange will be exposed in actual implementations.
	 * 
	 * @return List of {@link ExchangeApi} instances exposed by this exchange.
	 */
	List<ExchangeApi> getApis();
	
	/**
	 * Sets the request throttling policy for all REST requests of every exposed
	 * {@link ExchangeApi}. Relevant when some of REST endpoints must enfore rate
	 * limit rules
	 * 
	 * @see RateLimitRule
	 * @param requestThrottlingMode the request throttling policy
	 * @see ExchangeApi#setRequestThrottlingMode(RequestThrottlingMode)
	 */
	void setRequestThrottlingMode(RequestThrottlingMode requestThrottlingMode);

	/**
	 * Set the max request throttle delay for all REST requests of every exposed
	 * {@link ExchangeApi}.
	 * @param maxRequestThrottleDelay the max request throttle delay for all REST requests
	 * @see ExchangeApi#setMaxRequestThrottleDelay(long)
	 */
	void setMaxRequestThrottleDelay(long maxRequestThrottleDelay);
	
	/**
	 * Sets the request timeout for calls to REST endpoints of every API
	 * @param httpRequestTimeout The HTTP request timeout in ms
	 * @see ExchangeApi#setHttpRequestTimeout(long)
	 */
	void setHttpRequesTimeout(long httpRequestTimeout);

}
