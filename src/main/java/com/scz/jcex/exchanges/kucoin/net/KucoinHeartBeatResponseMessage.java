package com.scz.jcex.exchanges.kucoin.net;

import com.scz.jcex.util.EncodingUtil;

/**
 * Message received on websocket in response to ping (heartbeat). See
 * <a href="https://docs.kucoin.com/#ping">API</a>
 */
public class KucoinHeartBeatResponseMessage {

	public static final String PONG = "pong";

	private String id;
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
}
