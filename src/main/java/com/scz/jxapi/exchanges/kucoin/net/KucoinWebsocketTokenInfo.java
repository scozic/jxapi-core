package com.scz.jxapi.exchanges.kucoin.net;

import com.scz.jxapi.util.EncodingUtil;

/**
 * Encapsulates relevant data from response to either
 * <a href="https://docs.kucoin.com/#websocket-feed">bullet-public</a> or <a href="https://docs.kucoin.com/#websocket-feed">bullet-private</a> APIs.
 * 
 * @see KucoinWebsocketListenKeyApi
 */
public class KucoinWebsocketTokenInfo {
	
	private String token;
	private String endpoint;
	private long pingInterval;
	private long pingTimeout;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	public long getPingInterval() {
		return pingInterval;
	}
	public void setPingInterval(long pingInterval) {
		this.pingInterval = pingInterval;
	}
	public long getPingTimeout() {
		return pingTimeout;
	}
	public void setPingTimeout(long pingTimeout) {
		this.pingTimeout = pingTimeout;
	}

	@Override
	public String toString() {
		return EncodingUtil.pojoToJsonString(this);
	}
}
