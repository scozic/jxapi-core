package com.scz.jxapi.netutils.rest;

import com.scz.jxapi.util.EncodingUtil;

public class RestRequest<T> {
	
	public static <T> RestRequest<T> create(String url, String httpMethod, T request) {
		RestRequest<T> r = new RestRequest<>();
		r.setUrl(url);
		r.setHttpMethod(httpMethod);
		r.setRequest(request);
		return r;
	}
	
	private String url;
	
	private String httpMethod;
	
	private T request;

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

	public T getRequest() {
		return request;
	}

	public void setRequest(T request) {
		this.request = request;
	}

	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
}
