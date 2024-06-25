package com.scz.jxapi.exchange.descriptor;

import java.util.List;

import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;
import com.scz.jxapi.util.EncodingUtil;

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
	
	private List<RestEndpointDescriptor> restEndpoints;
	
	private String httpRequestInterceptorFactory;
	
	private String httpRequestExecutorFactory;
	
	private String websocketFactory;
	
	private String websocketHookFactory;
	
	private String websocketUrl;
	
	private List<WebsocketEndpointDescriptor> websocketEndpoints;
	
	private List<RateLimitRule> rateLimits;

	public List<RestEndpointDescriptor> getRestEndpoints() {
		return restEndpoints;
	}

	public void setRestEndpoints(List<RestEndpointDescriptor> restEndpoints) {
		this.restEndpoints = restEndpoints;
	}
	
	public boolean hasRestEndpoints() {
		return restEndpoints != null && !restEndpoints.isEmpty();
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
	
	public boolean hasWebsocketEndpoints() {
		return this.websocketEndpoints != null && !this.websocketEndpoints.isEmpty();
	}
	
	public List<RateLimitRule> getRateLimits() {
		return rateLimits;
	}

	public void setRateLimits(List<RateLimitRule> rateLimits) {
		this.rateLimits = rateLimits;
	}

	public String getHttpRequestInterceptorFactory() {
		return httpRequestInterceptorFactory;
	}

	public void setHttpRequestInterceptorFactory(String httpRequestInterceptorFactory) {
		this.httpRequestInterceptorFactory = httpRequestInterceptorFactory;
	}
	
	public String getHttpRequestExecutorFactory() {
		return httpRequestExecutorFactory;
	}

	public void setHttpRequestExecutorFactory(String httpRequestExecutorFactory) {
		this.httpRequestExecutorFactory = httpRequestExecutorFactory;
	}
	
	public String getWebsocketHookFactory() {
		return websocketHookFactory;
	}

	public void setWebsocketHookFactory(String websocketHookFactory) {
		this.websocketHookFactory = websocketHookFactory;
	}
	
	public String getWebsocketFactory() {
		return websocketFactory;
	}

	public void setWebsocketFactory(String websocketFactory) {
		this.websocketFactory = websocketFactory;
	}
	
	public String getWebsocketUrl() {
		return websocketUrl;
	}

	public void setWebsocketUrl(String websocketUrl) {
		this.websocketUrl = websocketUrl;
	}
	
	@Override
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}

}
