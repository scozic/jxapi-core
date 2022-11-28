package com.scz.jcex.generator.exchange;

import java.util.List;

import com.scz.jcex.util.EncodingUtil;

/**
 * Part of a JSON document descriptor that describes a group of REST and or
 * Websocket endpoints. This is a functional grouping of API endpoints: There
 * should be one for market data/public API endpoints and one for
 * trading/private endpoints.
 * Endpoints are expected to be {@link RestEndpoint} and {@link WebsocketEndpoint} lists.
 */
public class ExchangeApi {
	
	private String name;
	
	private String description;
	
	private List<RestEndpoint> restEndpoints;
	
	private List<WebsocketEndpoint> websocketEndpoints;

	public List<RestEndpoint> getRestEndpoints() {
		return restEndpoints;
	}

	public void setRestEndpoints(List<RestEndpoint> restEndpoints) {
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

	public List<WebsocketEndpoint> getWebsocketEndpoints() {
		return websocketEndpoints;
	}

	public void setWebsocketEndpoints(List<WebsocketEndpoint> websocketEndpoints) {
		this.websocketEndpoints = websocketEndpoints;
	}
	
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}

}
