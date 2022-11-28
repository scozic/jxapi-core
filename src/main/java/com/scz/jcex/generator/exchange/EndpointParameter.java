package com.scz.jcex.generator.exchange;

import java.util.List;

import com.scz.jcex.util.EncodingUtil;

/**
 * Part of JSON document describing a crypto exchange API that describes a given field of a request to an endpoint or its response.
 * Such field can be recursive, see {@link EndpointParameterType#STRUCT} or {@link EndpointParameterType#STRUCT_LIST}.
 */
public class EndpointParameter {
	private String name;
	
	private String description;
	
	private EndpointParameterType type;
	
	private Object sampleValue;
	
	private List<EndpointParameter> parameters;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EndpointParameterType getType() {
		return type;
	}

	public void setType(EndpointParameterType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Object getSampleValue() {
		return sampleValue;
	}

	public void setSampleValue(Object sampleValue) {
		this.sampleValue = sampleValue;
	}
	
	/**
	 * @return For a {@link EndpointParameterType#STRUCT} or {@link EndpointParameterType#STRUCT_LIST}, the parameters in nested 'struct'.
	 */
	public List<EndpointParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<EndpointParameter> parameters) {
		this.parameters = parameters;
	}
	
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
}
