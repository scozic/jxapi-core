package com.scz.jxapi.exchange.descriptor;

import java.util.List;

import com.scz.jxapi.util.EncodingUtil;

/**
 * Part of JSON document describing a crypo exchange API, describes a websocket endpoint where clients subscription can be performed using specified topic and eventual additional parameters.
 * The structure of additional subscription parameters and response format are described as {@link Field} lists.
 */
public class WebsocketEndpointDescriptor {
	
	private String name;
	
	private String topic;
	
	private String description;
	
	private String url;
	
	private Field request;
	
	private Field message;
	
	private String topicParametersListSeparator;
	
	private List<WebsocketMessageTopicMatcherFieldDescriptor> messageTopicMatcherFields;
	
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
	
	public Field getRequest() {
		return request;
	}
	public void setRequest(Field request) {
		this.request = request;
	}
	
	public Field getMessage() {
		return message;
	}
	public void setMessage(Field message) {
		this.message = message;
	}
	
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
}
