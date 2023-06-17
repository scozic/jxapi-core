package com.scz.jxapi.netutils.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scz.jxapi.util.EncodingUtil;

/**
 * Generic HTTP request for a REST API call.
 */
public class HttpRequest {

	private String url;
	
	private Map<String, List<String>> headers;
	
	private String httpMethod;
	
	private String body;

	/**
	 * @return full request URL, including request parameters
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url full request URL, including request parameters
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return Headers to be sent in request
	 */
	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	/**
	 * Sets a header 
	 * @param headerName
	 * @param headerValue
	 */
	public void setHeader(String headerName, String headerValue) {
		setHeader(headerName, List.of(headerValue));
	}
	
	/**
	 * Sets a header 
	 * @param headerName
	 * @param headerValue
	 */
	public void setHeader(String headerName, List<String> headerValues) {
		if (headers == null) {
			headers = new HashMap<>();
		}
		headers.put(headerName, headerValues);
	}
	
	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}

	/**
	 * Uppercase name of HTTP method like 'GET' or 'POST'
	 * @return
	 */
	public String getHttpMethod() {
		return httpMethod;
	}

	/**
	 * @param httpMethod Uppercase name of HTTP method like 'GET' or 'POST'
	 */
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	/**
	 * @return body of HTTP request to send
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body body of HTTP request to send
	 */
	public void setBody(String body) {
		this.body = body;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + EncodingUtil.pojoToString(this);
	}
}
