package com.scz.jxapi.netutils.rest;

import java.util.List;

import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;
import com.scz.jxapi.util.EncodingUtil;

public class RestRequest<T> {
	
	public static <T> RestRequest<T> create(String url, String httpMethod, T request) {
		return create(url, httpMethod, request, null, 0, null);
	}
	
	public static <T> RestRequest<T> create(String url, String httpMethod, T request, List<RateLimitRule> rateLimits) {
		return create(url, httpMethod, request, rateLimits, 0, null);
	}
	
	public static <T> RestRequest<T> create(String url, String httpMethod, T request, List<RateLimitRule> rateLimits, int weight) {
		return create(url, httpMethod, request, rateLimits, weight, null);
	}
	
	public static <T> RestRequest<T> create(String url, String httpMethod, T request,  UrlParametersSerializer<T> urlParametersSerializer) {
		return create(url, httpMethod, request, null, 0, urlParametersSerializer);
	}
	
	public static <T> RestRequest<T> create(String url, String httpMethod, T request, List<RateLimitRule> rateLimits, UrlParametersSerializer<T> urlParametersSerializer) {
		return create(url, httpMethod, request, rateLimits, 0, urlParametersSerializer);
	}
	
	public static <T> RestRequest<T> create(String url, String httpMethod, T request, List<RateLimitRule> rateLimits, int weight, UrlParametersSerializer<T> urlParametersSerializer) {
		RestRequest<T> r = new RestRequest<>();
		r.setUrl(url);
		r.setHttpMethod(httpMethod);
		r.setRequest(request);
		r.setRateLimits(rateLimits);
		r.setWeight(weight);
		r.setUrlParametersSerializer(urlParametersSerializer);
		return r;
	}
	
	private String url;
	
	private String httpMethod;
	
	private T request;
	
	private int weight;
	
	private List<RateLimitRule> rateLimits;
	
	private UrlParametersSerializer<T> urlParametersSerializer = null;
 
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
	
	public UrlParametersSerializer<T> getUrlParametersSerializer() {
		return urlParametersSerializer;
	}

	public void setUrlParametersSerializer(UrlParametersSerializer<T> urlParametersExtractor) {
		this.urlParametersSerializer = urlParametersExtractor;
	}

	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
}
