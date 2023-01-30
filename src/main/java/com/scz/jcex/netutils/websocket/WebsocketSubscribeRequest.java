package com.scz.jcex.netutils.websocket;

import com.scz.jcex.util.EncodingUtil;

/**
 * Encapsulates a subscription to a websocket feed
 *
 * @param <T> the 'Pojo' containing endpoint specific subscription parameters
 */
public class WebsocketSubscribeRequest<T> {

	private String url;
	private String topic;
	private T parameters;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public T getParameters() {
		return parameters;
	}
	public void setParameters(T parameters) {
		this.parameters = parameters;
	}
	
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
	
}
