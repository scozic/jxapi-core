package com.scz.jxapi.generator.exchange;

import java.util.List;

import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;
import com.scz.jxapi.util.JsonUtil;

/**
 * Part of JSON document describing a crypto exchange API, describes a specific REST endpoint.
 * Such endpoints are expecting an HTTP request to given URL endpoint, with given request parameters, and a response should be received.
 * Request and response parameters are detailed as {@link EndpointParameter} lists. 
 */
public class RestEndpointDescriptor {
	private String name;
	
	private String description;
	
	private String url;
	
	private String httpMethod;
	
	private List<EndpointParameter> parameters;
	
	private List<EndpointParameter> response;

	private String urlParameters;
	
	private String urlParametersListSeparator;
	
	private boolean queryParams;
	
	private ResponseDataType responseDataType = ResponseDataType.JSON_OBJECT;
	
	private String responseObjectName;
	
	private Integer requestWeight;
	
	private List<RateLimitRule> rateLimits;
	
	private List<String> requestInterfaces;
	
	private List<String> responseInterfaces;
 	
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

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public List<EndpointParameter> getResponse() {
		return response;
	}

	public void setResponse(List<EndpointParameter> response) {
		this.response = response;
	}
	
	public List<EndpointParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<EndpointParameter> parameters) {
		this.parameters = parameters;
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
	
	public ResponseDataType getResponseDataType() {
		return responseDataType;
	}

	public void setResponseDataType(ResponseDataType responseDataType) {
		this.responseDataType = responseDataType;
	}

	public String getResponseObjectName() {
		return responseObjectName;
	}

	public void setResponseObjectName(String responseObjectName) {
		this.responseObjectName = responseObjectName;
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
	
	/**
	 * @return List of implemented interfaces if generated POJO for this endpoint request
	 *         implements some specific ones. It may be useful for instance if all
	 *         API endpoints provide request with common fields that could be
	 *         extracted to super class.
	 */
	public List<String> getRequestInterfaces() {
		return requestInterfaces;
	}

	/**
	 * @see #getRequestInterfaces()
	 */
	public void setRequestInterfaces(List<String> requestInterfaces) {
		this.requestInterfaces = requestInterfaces;
	}

	/**
	 * @return List of implemented interfaces if generated POJO for this endpoint response
	 *         implements some specific ones. It may be useful for instance if all
	 *         API endpoints provide response with common fields that could be
	 *         extracted to super class.
	 */
	public List<String> getResponseInterfaces() {
		return responseInterfaces;
	}

	/**
	 * @see #getResponseInterfaces()
	 */
	public void setResponseInterfaces(List<String> responseInterfaces) {
		this.responseInterfaces = responseInterfaces;
	}

	public String toString() {
		return JsonUtil.pojoToString(this);
	}
}
