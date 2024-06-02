package com.scz.jxapi.netutils.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scz.jxapi.util.EncodingUtil;

public class HttpResponse {
	
	/**
	 * @return <code>true</code> if status is 2XX [200-299]
	 */
	public static boolean isStatusCodeOk(int httpStatus) {
		return httpStatus / 100 == 2;
	}

	private int responseCode;
	
	private String body;
	
	private Exception exception;
	
	private Map<String, List<String>> headers;
	
	private HttpRequest request;
	
	private Date time;

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
	
	public HttpRequest getRequest() {
		return request;
	}

	public void setRequest(HttpRequest request) {
		this.request = request;
	}
	
	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
	public long getRoundTrip() {
		if (this.time != null && this.request != null && this.request.getTime() != null) {
			return this.time.getTime() - this.request.getTime().getTime();
		}
		return 0L;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + EncodingUtil.pojoToString(this);
	}

}
