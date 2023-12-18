package com.scz.jxapi.netutils.rest;

import java.util.List;

import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;
import com.scz.jxapi.util.EncodingUtil;

public class RestRequest<T> {
	
	public static <T> RestRequest<T> create(String url, String httpMethod, T request) {
		return create(url, httpMethod, request, null);
	}
	
	public static <T> RestRequest<T> create(String url, String httpMethod, T request, List<RateLimitRule> rateLimits) {
		return create(url, httpMethod, request, rateLimits, 0);
	}
	
	public static <T> RestRequest<T> create(String url, String httpMethod, T request, List<RateLimitRule> rateLimits, int weight) {
		RestRequest<T> r = new RestRequest<>();
		r.setUrl(url);
		r.setHttpMethod(httpMethod);
		r.setRequest(request);
		r.setRateLimits(rateLimits);
		r.setWeight(weight);
		return r;
	}
	
	private String url;
	
	private String httpMethod;
	
	private T request;
	
	private int weight;
	
	private List<RateLimitRule> rateLimits;

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
	
	public int getWeight() {
		return weight;
	}
	
	public List<RateLimitRule> getRateLimits() {
		return rateLimits;
	}

	public void setRateLimits(List<RateLimitRule> rateLimits) {
		this.rateLimits = rateLimits;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String toString() {
		return EncodingUtil.pojoToString(this);
	}

}
