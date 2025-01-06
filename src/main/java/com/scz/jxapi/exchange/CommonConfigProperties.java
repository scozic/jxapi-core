package com.scz.jxapi.exchange;

import java.util.List;

import com.scz.jxapi.exchange.descriptor.ConfigProperty;
import com.scz.jxapi.exchange.descriptor.DefaultConfigProperty;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.netutils.rest.ratelimits.RequestThrottlingMode;

/**
 * Lists every {@link ConfigProperty} that is relevant for any {@link Exchange}
 * generated using JXAPI Java API wrapper. These properties may be relevant only
 * when REST APIs are exposed (for instance
 * {@link #HTTP_REQUEST_TIMEOUT_PROPERTY}).
 */
public interface CommonConfigProperties {
	
	/**
	 * The request timeout for calls to REST endpoints of every API. A negative value means no timeout (discouraged)
	 */
	ConfigProperty  HTTP_REQUEST_TIMEOUT_PROPERTY = DefaultConfigProperty.create(
			"jxapi.httpRequestTimeout", 
			Type.LONG, 
			"The request timeout for calls to REST endpoints of every API. A negative value means no timeout (discouraged)",
			null);
	
	/**
	 * Sets the HTTP request throttling policy in case a rate limit rule is breached, for every exposed ExchangeApi, see enum {@link RequestThrottlingMode}.
	 */
	ConfigProperty  REQUEST_THROTTLING_MODE_PROPERTY = DefaultConfigProperty.create(
			"jxapi.requestThrottlingMode", 
			Type.STRING, 
			"Sets the HTTP request throttling policy in case a rate limit rule is breached, for every exposed ExchangeApi, see enum " + RequestThrottlingMode.class.getName(),
			null);
	
	/**
	 * Set the max HTTP request throttle delay for rate limit rule enforcement, for every exposed ExchangeApi.
	 */
	ConfigProperty  MAX_REQUEST_THROTTLE_DELAY_PROPERTY = DefaultConfigProperty.create(
			"jxapi.maxRequestThrottleDelay", 
			Type.LONG, 
			"Set the max HTTP request throttle delay for rate limit rule enforcement, for every exposed ExchangeApi.",
			null);
	
	/**
	 * List containing all the {@link DefaultConfigProperty} properties of this interface.
	 */
	List<ConfigProperty> ALL = List.of(
			HTTP_REQUEST_TIMEOUT_PROPERTY, 
			REQUEST_THROTTLING_MODE_PROPERTY, 
			MAX_REQUEST_THROTTLE_DELAY_PROPERTY);
	
}
