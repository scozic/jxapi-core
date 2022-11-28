package com.scz.jcex.generator.exchange;

import java.util.List;

import com.scz.jcex.util.EncodingUtil;

/**
 * Root element of a JSON crypto exchange API descriptor.
 * API will be described in groups of endpoints, as {@link ExchangeApi} list. 
 */
public class ExchangeDescriptor {
	
	private String name;
	private String description;
	
	private List<ExchangeApi> apis;

	public List<ExchangeApi> getApis() {
		return apis;
	}

	public void setApis(List<ExchangeApi> apis) {
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
	
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
}
