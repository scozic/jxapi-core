package com.scz.jcex.generator.exchange;

import java.util.List;

import com.scz.jcex.util.EncodingUtil;

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
	
	private List<WebsocketEndpointDescriptor> websocketEndpoints;

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
	
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}

	public String getRestEndpointFactory() {
		return restEndpointFactory;
	}

	public void setRestEndpointFactory(String restEndpointFactory) {
		this.restEndpointFactory = restEndpointFactory;
	}

}
