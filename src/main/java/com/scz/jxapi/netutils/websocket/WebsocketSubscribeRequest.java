package com.scz.jxapi.netutils.websocket;

import com.scz.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatcherFactory;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Encapsulates a subscription to a websocket feed
 */
public class WebsocketSubscribeRequest {
	
	public static WebsocketSubscribeRequest create(Object parameters, String topic, WebsocketMessageTopicMatcherFactory messageTopicMatcher) {
		WebsocketSubscribeRequest request = new WebsocketSubscribeRequest();
		request.request = parameters;
		request.topic = topic;
		request.messageTopicMatcher = messageTopicMatcher;
		return request;
	}

	private String baseUrl;
	private Object request;
	private WebsocketMessageTopicMatcherFactory messageTopicMatcher;
	private String topic;
	
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String url) {
		this.baseUrl = url;
	}
	
	public Object getRequest() {
		return request;
	}
	public void setRequest(Object parameters) {
		this.request = parameters;
	}
	
	public WebsocketMessageTopicMatcherFactory getMessageTopicMatcher() {
		return messageTopicMatcher;
	}
	public void setMessageTopicMatcherFactory(WebsocketMessageTopicMatcherFactory messageTopicMatcher) {
		this.messageTopicMatcher = messageTopicMatcher;
	}
	
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
}
