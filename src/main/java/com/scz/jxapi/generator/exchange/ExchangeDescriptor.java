package com.scz.jxapi.generator.exchange;

import java.util.List;

import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;
import com.scz.jxapi.util.JsonUtil;

/**
 * Root element of a JSON Exchange descriptor.
 * API will be described in groups of endpoints, as {@link ExchangeApiDescriptor} list. 
 */
public class ExchangeDescriptor {
	
	private String name;
	private String description;
	private String basePackage;
	
	private List<ExchangeApiDescriptor> apis;
	
	private List<RateLimitRule> rateLimits;

	public List<ExchangeApiDescriptor> getApis() {
		return apis;
	}

	public void setApis(List<ExchangeApiDescriptor> apis) {
		this.apis = apis;
	}

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
	
	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}
	
	public List<RateLimitRule> getRateLimits() {
		return rateLimits;
	}

	public void setRateLimits(List<RateLimitRule> rateLimits) {
		this.rateLimits = rateLimits;
	}
	
	public String toString() {
		return JsonUtil.pojoToString(this);
	}
}
