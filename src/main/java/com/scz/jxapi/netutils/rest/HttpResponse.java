package com.scz.jxapi.netutils.rest;

import java.util.HashMap;
import java.util.Map;

import com.scz.jxapi.util.EncodingUtil;

public class HttpResponse {

	private int responseCode;
	
	private String body;
	
	private Exception exception;
	
	private final Map<String, String> headers = new HashMap<>();

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}
	
	/**
	 * @return Headers to be sent in request
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * Sets a header 
	 * @param headerName
	 * @param headerValue
	 */
	public void setHeader(String headerName, String headerValue) {
		headers.put(headerName, headerValue);
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + EncodingUtil.pojoToString(this);
	}
}
