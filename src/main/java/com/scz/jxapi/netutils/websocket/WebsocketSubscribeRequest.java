package com.scz.jxapi.netutils.websocket;

import com.scz.jxapi.util.EncodingUtil;

/**
 * Encapsulates a subscription to a websocket feed
 *
 * @param <T> the 'Pojo' containing endpoint specific subscription parameters
 */
public class WebsocketSubscribeRequest<T> {
	
	public static <T> WebsocketSubscribeRequest<T> createWebsocketSubscribeReques(T parameters, WebsocketSubscribeRequestTopicSerializer<T> topicSerializer, WebsocketMessageTopicMatcher messageTopicMatcher) {
		WebsocketSubscribeRequest<T> request = new WebsocketSubscribeRequest<>();
		request.parameters = parameters;
		request.messageTopicMatcher = messageTopicMatcher;
		return request;
	}

	private String baseUrl;
	private T parameters;
	private WebsocketSubscribeRequestTopicSerializer<T> topicSerializer;
	private WebsocketMessageTopicMatcher messageTopicMatcher;
	
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String url) {
		this.baseUrl = url;
	}
	
	public T getParameters() {
		return parameters;
	}
	public void setParameters(T parameters) {
		this.parameters = parameters;
	}
	
	public WebsocketMessageTopicMatcher getMessageTopicMatcher() {
		return messageTopicMatcher;
	}
	public void setMessageTopicMatcher(WebsocketMessageTopicMatcher messageTopicMatcher) {
		this.messageTopicMatcher = messageTopicMatcher;
	}
	
	public WebsocketSubscribeRequestTopicSerializer<T> getTopicSerializer() {
		return topicSerializer;
	}
	public void setTopicSerializer(WebsocketSubscribeRequestTopicSerializer<T> topicSerializer) {
		this.topicSerializer = topicSerializer;
	}
	
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
	
}
