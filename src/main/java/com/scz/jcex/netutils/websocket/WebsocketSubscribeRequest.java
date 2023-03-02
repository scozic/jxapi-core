package com.scz.jcex.netutils.websocket;

import com.scz.jcex.util.EncodingUtil;

/**
 * Encapsulates a subscription to a websocket feed
 *
 * @param <T> the 'Pojo' containing endpoint specific subscription parameters
 */
public class WebsocketSubscribeRequest<T extends WebsocketSubscribeParameters> {
	
	public static <T extends WebsocketSubscribeParameters> WebsocketSubscribeRequest<T> createWebsocketSubscribeReques(T parameters, WebsocketMessageTopicMatcher messageTopicMatcher) {
		WebsocketSubscribeRequest<T> request = new WebsocketSubscribeRequest<>();
		request.parameters = parameters;
		request.messageTopicMatcher = messageTopicMatcher;
		return request;
	}

	private String baseUrl;
	private T parameters;
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
	
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
	
}
