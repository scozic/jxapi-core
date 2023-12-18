package com.scz.jxapi.netutils.rest;

import com.scz.jxapi.util.EncodingUtil;

public class RestResponse<A> {
	
	private int httpResponseCode;
	
	private Exception exception;
	
	private A response;

	public int getHttpResponseCode() {
		return httpResponseCode;
	}

	public void setHttpResponseCode(int httpResponseCode) {
		this.httpResponseCode = httpResponseCode;
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
		return exception == null && httpResponseCode == 200;
	}
	
	public String toString() {
		return getClass().getSimpleName() + EncodingUtil.pojoToString(this);
	}

}
