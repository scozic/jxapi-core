package com.scz.jxapi.generator.exchange;

import java.util.List;

import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;
import com.scz.jxapi.util.JsonUtil;

/**
 * Part of a JSON document descriptor that describes a group of REST and or
 * Websocket endpoints. This is a functional grouping of API endpoints: There
 * should be one for market data/public API endpoints and one for
 * trading/private endpoints.
 * Endpoints are expected to be {@link RestEndpointDescriptor} and {@link WebsocketEndpointDescriptor} lists.
 */
public class ExchangeApiDescriptor {
	
	private String name;
	
	private String description;
	
	private String restEndpointFactory;
	
	private List<RestEndpointDescriptor> restEndpoints;
	
	private String websocketEndpointFactory;
	
	private List<WebsocketEndpointDescriptor> websocketEndpoints;
	
	private List<RateLimitRule> rateLimits;

	public List<RestEndpointDescriptor> getRestEndpoints() {
		return restEndpoints;
	}

	public void setRestEndpoints(List<RestEndpointDescriptor> restEndpoints) {
		this.restEndpoints = restEndpoints;
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

	public List<WebsocketEndpointDescriptor> getWebsocketEndpoints() {
		return websocketEndpoints;
	}

	public void setWebsocketEndpoints(List<WebsocketEndpointDescriptor> websocketEndpoints) {
		this.websocketEndpoints = websocketEndpoints;
	}
	
	public String getRestEndpointFactory() {
		return restEndpointFactory;
	}

	public void setRestEndpointFactory(String restEndpointFactory) {
		this.restEndpointFactory = restEndpointFactory;
	}

	public String getWebsocketEndpointFactory() {
		return websocketEndpointFactory;
	}

	public void setWebsocketEndpointFactory(String websocketEndpointFactory) {
		this.websocketEndpointFactory = websocketEndpointFactory;
	}
	
	public List<RateLimitRule> getRateLimits() {
		return rateLimits;
	}

	public void setRateLimits(List<RateLimitRule> rateLimits) {
		this.rateLimits = rateLimits;
	}
	
	@Override
	public String toString() {
		return JsonUtil.pojoToString(this);
	}

}
