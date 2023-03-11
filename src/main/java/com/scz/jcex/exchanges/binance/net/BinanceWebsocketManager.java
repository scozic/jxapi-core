package com.scz.jcex.exchanges.binance.net;

import org.apache.commons.lang3.RandomUtils;

import com.scz.jcex.netutils.websocket.spring.SpringWebsocketManager;

public class BinanceWebsocketManager extends SpringWebsocketManager {
	
	private static final String SUBSCRIPTION_REQUEST_TEMPLATE = "{\"method\": \"%s\", \"params\":[\"%s\"],\"id\": %d}";
	
	private int requestIdCounter = RandomUtils.nextInt();

	public BinanceWebsocketManager(String baseUrl) {
		super(baseUrl);
		setReconnectDelay(5000L);
	}

	@Override
	protected String createHeartBeatMessage() {
		return null;
	}

	@Override
	protected String getSubscribeRequestMessage(String topic) {
		return createSubscriptionRequest("SUBSCRIBE", topic);
	}

	@Override
	protected String getUnSubscribeRequestMessage(String topic) {
		return createSubscriptionRequest("UNSUBSCRIBE", topic);
	}

	private String createSubscriptionRequest(String method, String topic) {
		if (requestIdCounter == Integer.MAX_VALUE) {
			requestIdCounter = 0;
		}
		int requestId = requestIdCounter++;
		return String.format(SUBSCRIPTION_REQUEST_TEMPLATE, method, topic, requestId);
	}
	
	
}
