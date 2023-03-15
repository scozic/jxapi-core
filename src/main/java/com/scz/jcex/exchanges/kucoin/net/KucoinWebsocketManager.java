package com.scz.jcex.exchanges.kucoin.net;

import com.scz.jcex.netutils.websocket.spring.SpringWebsocketManager;

public class KucoinWebsocketManager extends SpringWebsocketManager {

	public KucoinWebsocketManager(String baseUrl) {
		super(baseUrl);
	}

	private static final String HEARTBEAT_REQUEST_TEMPLATE = "{id\":\"%d\", \"type\":\"ping\"}";
	
	private int connectionId = 1;
	
	@Override
	protected String createHeartBeatMessage() {
		return String.format(HEARTBEAT_REQUEST_TEMPLATE, connectionId);
	}

	@Override
	protected String getSubscribeRequestMessage(String topic) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getUnSubscribeRequestMessage(String topic) {
		// TODO Auto-generated method stub
		return null;
	}
}
