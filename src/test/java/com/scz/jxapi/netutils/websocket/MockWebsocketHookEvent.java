package com.scz.jxapi.netutils.websocket;

import com.scz.jxapi.util.EncodingUtil;

public class MockWebsocketHookEvent {

	public static MockWebsocketHookEvent createAfterInitEvent(MockWebsocketHook source, WebsocketManager websocketManager) {
		MockWebsocketHookEvent e = new MockWebsocketHookEvent(MockWebsocketHookEventType.AFTER_INIT, source);
		e.setWebsocketManager(websocketManager);
		return e;
	}

	public static MockWebsocketHookEvent createBeforeConnectEvent(MockWebsocketHook source, WebsocketManager websocketManager) {
		MockWebsocketHookEvent e = new MockWebsocketHookEvent(MockWebsocketHookEventType.BEFORE_CONNECT, source);
		e.setWebsocketManager(websocketManager);
		return e;
	}

	public static MockWebsocketHookEvent createAfterConnectEvent(MockWebsocketHook source, WebsocketManager websocketManager) {
		MockWebsocketHookEvent e = new MockWebsocketHookEvent(MockWebsocketHookEventType.AFTER_CONNECT, source);
		e.setWebsocketManager(websocketManager);
		return e;
	}

	public static MockWebsocketHookEvent createBeforeDisconnectEvent(MockWebsocketHook source, WebsocketManager websocketManager) {
		MockWebsocketHookEvent e = new MockWebsocketHookEvent(MockWebsocketHookEventType.BEFORE_DISCONNECT, source);
		e.setWebsocketManager(websocketManager);
		return e;
	}

	public static MockWebsocketHookEvent createAfterDisconnectEvent(MockWebsocketHook source, WebsocketManager websocketManager) {
		MockWebsocketHookEvent e = new MockWebsocketHookEvent(MockWebsocketHookEventType.AFTER_DISCONNECT, source);
		e.setWebsocketManager(websocketManager);
		return e;
	}

	public static MockWebsocketHookEvent createGetSubscribeRequestMessageEvent(MockWebsocketHook source, String topic) {
		MockWebsocketHookEvent e = new MockWebsocketHookEvent(MockWebsocketHookEventType.GET_SUBSCRIBE_REQUEST_MESSAGE, source);
		e.setTopic(topic);
		return e;
	}

	public static MockWebsocketHookEvent createGetUnSubscribeRequestMessageEvent(MockWebsocketHook source, String topic) {
		MockWebsocketHookEvent e = new MockWebsocketHookEvent(MockWebsocketHookEventType.GET_UNSUBSCRIBE_REQUEST_MESSAGE, source);
		e.setTopic(topic);
		return e;
	}

	public static MockWebsocketHookEvent createGetHeartbeatEvent(MockWebsocketHook source) {
		MockWebsocketHookEvent e = new MockWebsocketHookEvent(MockWebsocketHookEventType.GET_HEARTBEAT, source);
		return e;
	}

	private final MockWebsocketHookEventType type;
	
	private final MockWebsocketHook source;
	
	private WebsocketManager websocketManager;
	
	private String topic;
	
	public MockWebsocketHookEvent(MockWebsocketHookEventType type, MockWebsocketHook source) {
		this.type = type;
		this.source = source;
	}
	
	public MockWebsocketHookEventType getType() {
		return type;
	}

	public MockWebsocketHook getSource() {
		return source;
	}

	public WebsocketManager getWebsocketManager() {
		return websocketManager;
	}

	public void setWebsocketManager(WebsocketManager websocketManager) {
		this.websocketManager = websocketManager;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	@Override
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
}
