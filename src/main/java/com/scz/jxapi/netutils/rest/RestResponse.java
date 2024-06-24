package com.scz.jxapi.netutils.rest;

import java.util.LinkedHashMap;
import java.util.Map;

import com.scz.jxapi.util.EncodingUtil;
import com.scz.jxapi.util.JsonUtil;

public class RestResponse<A> {
	
	private int httpStatus;
	
	private Exception exception;
	
	private A response;
	
	private HttpResponse httpResponse;
	
	public RestResponse() {
		this(null);
	}
	
	public RestResponse(HttpResponse httpResponse) {
		if (httpResponse != null) {
			this.httpResponse = httpResponse;
			this.httpStatus = httpResponse.getResponseCode();
			this.exception = httpResponse.getException();
		}
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(int httpResponseCode) {
		this.httpStatus = httpResponseCode;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public A getResponse() {
		return response;
	}

	public void setResponse(A response) {
		this.response = response;
	}
	
	public boolean isOk() {
		return exception == null && HttpResponse.isStatusCodeOk(httpStatus);
	}
	
	public HttpResponse getHttpResponse() {
		return httpResponse;
	}

	public void setHttpResponse(HttpResponse httpResponse) {
		this.httpResponse = httpResponse;
	}
	
	public String getEndpoint() {
		if (httpResponse != null) {
			HttpRequest request = httpResponse.getRequest();
			if (request != null) {
				return request.getEndpoint();
			}
		}
		return null;
	}
	
	public String toString() {
		Map<String, Object> fields = new LinkedHashMap<>();
		fields.put("httpStatus", httpStatus);
		fields.put("exception", exception);
		if (response != null) {
			fields.put("response", EncodingUtil.prettyPrintLongString(JsonUtil.pojoToJsonString(response), 512));
		} else if (httpResponse != null) {
			fields.put("body", httpResponse.getBody());
			fields.put("time", httpResponse.getTime());
			fields.put("roundtrip", httpResponse.getRoundTrip());
		}
		return getClass().getSimpleName() + JsonUtil.pojoToJsonString(fields);
	}

}
