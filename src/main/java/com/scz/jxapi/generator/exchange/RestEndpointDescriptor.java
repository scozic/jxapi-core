package com.scz.jxapi.generator.exchange;

import java.util.List;

import com.scz.jxapi.netutils.rest.HttpMethod;
import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Part of JSON document describing a crypto exchange API, describes a specific REST endpoint.
 * Such endpoints are expecting an HTTP request to given URL endpoint, with given request parameters, and a response should be received.
 * Request and response parameters are detailed as {@link EndpointParameter} lists. 
 */
public class RestEndpointDescriptor {
	private String name;
	
	private String description;
	
	private String url;
	
	private HttpMethod httpMethod;
	
	private EndpointParameter request;
	
	private EndpointParameter response;

	private String urlParameters;
	
	private String urlParametersListSeparator;
	
	private boolean queryParams;
	
	private Integer requestWeight;
	
	private List<RateLimitRule> rateLimits;
 	
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}
	
	public String getUrlParameters() {
		return urlParameters;
	}

	public void setUrlParameters(String urlParameters) {
		this.urlParameters = urlParameters;
	}

	public String getUrlParametersListSeparator() {
		return urlParametersListSeparator;
	}

	public void setUrlParametersListSeparator(String urlParametersListSeparator) {
		this.urlParametersListSeparator = urlParametersListSeparator;
	}
	
	public boolean isQueryParams() {
		return queryParams;
	}

	public void setQueryParams(boolean queryParams) {
		this.queryParams = queryParams;
	}
	
	public List<RateLimitRule> getRateLimits() {
		return rateLimits;
	}

	public void setRateLimits(List<RateLimitRule> rateLimits) {
		this.rateLimits = rateLimits;
	}
	
	public Integer getRequestWeight() {
		return requestWeight;
	}

	public void setRequestWeight(Integer requestWeight) {
		this.requestWeight = requestWeight;
	}
	
	public EndpointParameter getRequest() {
		return request;
	}

	public void setRequest(EndpointParameter request) {
		this.request = request;
	}

	public EndpointParameter getResponse() {
		return response;
	}

	public void setResponse(EndpointParameter response) {
		this.response = response;
	}
	
	@Override
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}

}
