package com.scz.jcex.generator.exchange;

import java.util.List;

import com.scz.jcex.util.EncodingUtil;

/**
 * Root element of a JSON crypto exchange API descriptor.
 * API will be described in groups of endpoints, as {@link ExchangeApiDescriptor} list. 
 */
public class ExchangeDescriptor {
	
	private String name;
	private String description;
	private String basePackage;
	
	private List<ExchangeApiDescriptor> apis;

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
	
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
}
