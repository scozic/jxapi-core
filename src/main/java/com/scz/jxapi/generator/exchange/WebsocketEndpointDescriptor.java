package com.scz.jxapi.generator.exchange;

import java.util.List;

import com.scz.jxapi.util.EncodingUtil;

/**
 * Part of JSON document describing a crypo exchange API, describes a websocket endpoint where clients subscription can be performed using specified topic and eventual additional parameters.
 * The structure of additional subscription parameters and response format are described as {@link EndpointParameter} lists.
 */
public class WebsocketEndpointDescriptor {
	
	private String name;
	
	private String topic;
	
	private String description;
	
	private String url;
	
	private List<EndpointParameter> parameters;
	
	private List<EndpointParameter> response;
	
	private String topicParametersListSeparator;
	
	private List<WebsocketMessageTopicMatcherFieldDescriptor> messageTopicMatcherFields;
	
	private String responseDataType = CanonicalEndpointParameterTypes.OBJECT.name();
	
	private List<String> requestInterfaces;
	
	private List<String> responseInterfaces;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public List<EndpointParameter> getParameters() {
		return parameters;
	}
	public void setParameters(List<EndpointParameter> parameters) {
		this.parameters = parameters;
	}
	
	public List<EndpointParameter> getResponse() {
		return response;
	}
	public void setResponse(List<EndpointParameter> response) {
		this.response = response;
	}
	
	public String getTopicParametersListSeparator() {
		return topicParametersListSeparator;
	}
	public void setTopicParametersListSeparator(String topicParametersListSeparator) {
		this.topicParametersListSeparator = topicParametersListSeparator;
	}
	
	public List<WebsocketMessageTopicMatcherFieldDescriptor> getMessageTopicMatcherFields() {
		return messageTopicMatcherFields;
	}
	public void setMessageTopicMatcherFields(List<WebsocketMessageTopicMatcherFieldDescriptor> messageTopicMatcherFields) {
		this.messageTopicMatcherFields = messageTopicMatcherFields;
	}
	
	public String getResponseDataType() {
		return responseDataType;
	}
	
	public void setResponseDataType(String responseDataType) {
		this.responseDataType = responseDataType;
	}
	
	/**
	 * @return List of implemented interfaces if generated POJO for this endpoint request
	 *         implements some specific ones. It may be useful for instance if all
	 *         API endpoints provide request with common fields that could be
	 *         extracted to super class.
	 */
	public List<String> getRequestInterfaces() {
		return requestInterfaces;
	}

	/**
	 * @see #getRequestInterfaces()
	 */
	public void setRequestInterfaces(List<String> requestInterfaces) {
		this.requestInterfaces = requestInterfaces;
	}

	/**
	 * @return List of implemented interfaces if generated POJO for this endpoint response
	 *         implements some specific ones. It may be useful for instance if all
	 *         API endpoints provide response with common fields that could be
	 *         extracted to super class.
	 */
	public List<String> getResponseInterfaces() {
		return responseInterfaces;
	}

	/**
	 * @see #getResponseInterfaces()
	 */
	public void setResponseInterfaces(List<String> responseInterfaces) {
		this.responseInterfaces = responseInterfaces;
	}
	
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}	
}
