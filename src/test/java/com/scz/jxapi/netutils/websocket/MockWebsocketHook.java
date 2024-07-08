package com.scz.jxapi.netutils.websocket;

import java.util.HashMap;
import java.util.Map;

import com.scz.jxapi.observability.GenericObserver;

public class MockWebsocketHook extends GenericObserver<MockWebsocketHookEvent> implements WebsocketHook {
	
	private final Map<String, String> subscribeRequestMessages = new HashMap<>();
	private final Map<String, String> unSubscribeRequestMessages = new HashMap<>();
	private String heartBeatMessage;
	private WebsocketManager websocketManager;

	@Override
	public void afterInit(WebsocketManager websocketManager) {
		this.websocketManager = websocketManager;
		this.handleEvent(MockWebsocketHookEvent.createAfterInitEvent(this, websocketManager));
		WebsocketHook.super.afterInit(websocketManager);
	}

	@Override
	public void beforeConnect(WebsocketManager websocketManager) throws WebsocketException {
		this.websocketManager = websocketManager;
		this.handleEvent(MockWebsocketHookEvent.createBeforeConnectEvent(this, websocketManager));
		WebsocketHook.super.beforeConnect(websocketManager);
	}

	@Override
	public void afterConnect(WebsocketManager websocketManager) throws WebsocketException {
		this.websocketManager = websocketManager;
		this.handleEvent(MockWebsocketHookEvent.createAfterConnectEvent(this, websocketManager));
		WebsocketHook.super.afterConnect(websocketManager);
	}

	@Override
	public void beforeDisconnect(WebsocketManager websocketManager) throws WebsocketException {
		this.websocketManager = websocketManager;
		this.handleEvent(MockWebsocketHookEvent.createBeforeDisconnectEvent(this, websocketManager));
		WebsocketHook.super.beforeDisconnect(websocketManager);
	}

	@Override
	public void afterDisconnect(WebsocketManager websocketManager) throws WebsocketException {
		this.websocketManager = websocketManager;
		this.handleEvent(MockWebsocketHookEvent.createAfterDisconnectEvent(this, websocketManager));
		WebsocketHook.super.afterDisconnect(websocketManager);
	}

	@Override
	public String getSubscribeRequestMessage(String topic) {
		this.handleEvent(MockWebsocketHookEvent.createGetSubscribeRequestMessageEvent(this, topic));
		return subscribeRequestMessages.get(topic);
	}

	@Override
	public String getUnSubscribeRequestMessage(String topic) {
		this.handleEvent(MockWebsocketHookEvent.createGetUnSubscribeRequestMessageEvent(this, topic));
		return unSubscribeRequestMessages.get(topic);
	}

	@Override
	public String getHeartBeatMessage() {
		this.handleEvent(MockWebsocketHookEvent.createGetHeartbeatEvent(this));
		return heartBeatMessage;
	}

	public void setSubscribeRequestMessage(String topic, String subscribeRequestMessage) {
		subscribeRequestMessages.put(topic, subscribeRequestMessage);
	}
	
	public void setUnSubscribeRequestMessage(String topic, String unSubscribeRequestMessage) {
		unSubscribeRequestMessages.put(topic, unSubscribeRequestMessage);
	}

	public WebsocketManager getWebsocketManager() {
		return websocketManager;
	}

	

}
