package com.scz.jxapi.exchange.descriptor;

import java.util.List;

import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Root element of a JSON Exchange descriptor.<br>
 * This class describes an exchange and its APIs<br>
 * API will be described in groups of endpoints, as {@link ExchangeApiDescriptor} list.<br>
 * Rate limits will be described as {@link RateLimitRule} list. These limits will be applied to all endpoints of each API group of the exchange.
 * 
 * JSON example:
 * <pre>
 * {
 * 		"name": "Binance",
 * 		"ID": "BINANCE",
 * 		"description": "Binance exchange",
 * 		"basePackage": "com.scz.jxapi.exchange.binance",
 * 		"apis": [
 * 			{
 * 				"name": "Spot",
 * 				"description": "Spot trading API",
 * 				"httpRequestExecutorFactory": "com.scz.jxapi.netutils.rest.mock.MockHttpRequestExecutorFactory",
 * 				"httpRequestInterceptorFactory": "com.scz.jxapi.netutils.rest.mock.MockHttpRequestInterceptorFactory",
 * 				"restEndpoints": [
 * 					// RESTendpoints here
 * 				],
 * 				"websocketEndpoints": [
 * 					// Websocket endpoints here
 * 				],
 * 			}
 * 		],
 * 		"rateLimits": [
 * 			{
 * 				"timeFrame": 60000,
 * 		        "maxRequestCount": 1200,
 * 		        "maxTotalWeight": 1200
 * 			}
 * 		]
 * }
 * </pre>
 * @see ExchangeApiDescriptor
 */
public class ExchangeDescriptor {
	
	private String name;
	private String description;
	private String basePackage;
	
	private List<ExchangeApiDescriptor> apis;
	
	private List<RateLimitRule> rateLimits;

	public List<ExchangeApiDescriptor> getApis() {
		return apis;
	}

	public void setApis(List<ExchangeApiDescriptor> apis) {
		this.apis = apis;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}
	
	public List<RateLimitRule> getRateLimits() {
		return rateLimits;
	}

	public void setRateLimits(List<RateLimitRule> rateLimits) {
		this.rateLimits = rateLimits;
	}
	
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
}
