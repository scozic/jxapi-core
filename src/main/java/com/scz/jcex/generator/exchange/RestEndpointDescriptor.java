package com.scz.jcex.generator.exchange;

import java.util.List;

import com.scz.jcex.util.EncodingUtil;

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

	public String toString() {
		return EncodingUtil.pojoToString(this);
	}

}
