package com.scz.jcex.netutils.websocket;

import com.scz.jcex.util.EncodingUtil;

/**
 * Encapsulates a subscription to a websocket feed
 *
 * @param <T> the 'Pojo' containing endpoint specific subscription parameters
 */
public class WebsocketSubscribeRequest<T extends WebsocketSubscribeParameters> {

	private String baseUrl;
	private T parameters;
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
	
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
	
}
