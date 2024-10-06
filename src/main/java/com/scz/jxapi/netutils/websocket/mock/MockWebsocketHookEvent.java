package com.scz.jxapi.netutils.websocket.mock;

import com.scz.jxapi.netutils.websocket.WebsocketManager;

/**
 * Event that is sent to a {@link MockWebsocketHook} to notify it of a certain event.
 */
public class MockWebsocketHookEvent {

	/**
	 * Create a new {@link MockWebsocketHookEventType#INIT} event for the given {@link MockWebsocketHook} and {@link WebsocketManager}.
	 * @param source
	 * @param websocketManager
	 * @return
	 */
	public static MockWebsocketHookEvent createInitEvent(MockWebsocketHook source, WebsocketManager websocketManager) {
		MockWebsocketHookEvent res = new MockWebsocketHookEvent(MockWebsocketHookEventType.INIT, source);
		res.setWebsocketManager(websocketManager);
		return res;
	}

	/**
	 * Create a new {@link MockWebsocketHookEventType#BEFORE_CONNECT} event for the given {@link MockWebsocketHook} and {@link WebsocketManager}.
	 * @param source
	 * @param websocketManager
	 * @return
	 */
	public static MockWebsocketHookEvent createBeforeConnectEvent(MockWebsocketHook source) {
		return new MockWebsocketHookEvent(MockWebsocketHookEventType.BEFORE_CONNECT, source);
	}

	/**
	 * Create a new {@link MockWebsocketHookEventType#AFTER_CONNECT} event for the given {@link MockWebsocketHook} and {@link WebsocketManager}.
	 * @param source
	 * @param websocketManager
	 * @return
	 */
	public static MockWebsocketHookEvent createAfterConnectEvent(MockWebsocketHook source) {
		return new MockWebsocketHookEvent(MockWebsocketHookEventType.AFTER_CONNECT, source);
	}

	/**
	 * Create a new {@link MockWebsocketHookEventType#BEFORE_DISCONNECT} event for the given {@link MockWebsocketHook} and {@link WebsocketManager}.
	 * @param source
	 * @param websocketManager
	 * @return
	 */
	public static MockWebsocketHookEvent createBeforeDisconnectEvent(MockWebsocketHook source) {
		return new MockWebsocketHookEvent(MockWebsocketHookEventType.BEFORE_DISCONNECT, source);
	}

	/**
	 * Create a new {@link MockWebsocketHookEventType#AFTER_DISCONNECT} event for the given {@link MockWebsocketHook} and {@link WebsocketManager}.
	 * @param source
	 * @param websocketManager
	 * @return
	 */
	public static MockWebsocketHookEvent createAfterDisconnectEvent(MockWebsocketHook source) {
		return new MockWebsocketHookEvent(MockWebsocketHookEventType.AFTER_DISCONNECT, source);
	}

	/**
	 * Create a new {@link MockWebsocketHookEventType#GET_SUBSCRIBE_REQUEST_MESSAGE} event for the given {@link MockWebsocketHook} and topic.
	 * @param source
	 * @param topic
	 * @return
	 */
	public static MockWebsocketHookEvent createGetSubscribeRequestMessageEvent(MockWebsocketHook source, String topic) {
		MockWebsocketHookEvent e = new MockWebsocketHookEvent(MockWebsocketHookEventType.GET_SUBSCRIBE_REQUEST_MESSAGE, source);
		e.setTopic(topic);
		return e;
	}

	/**
	 * Create a new {@link MockWebsocketHookEventType#GET_UNSUBSCRIBE_REQUEST_MESSAGE} event for the given {@link MockWebsocketHook} and topic.
	 * @param source
	 * @param topic
	 * @return
	 */
	public static MockWebsocketHookEvent createGetUnSubscribeRequestMessageEvent(MockWebsocketHook source, String topic) {
		MockWebsocketHookEvent e = new MockWebsocketHookEvent(MockWebsocketHookEventType.GET_UNSUBSCRIBE_REQUEST_MESSAGE, source);
		e.setTopic(topic);
		return e;
	}

	/**
	 * Create a new {@link MockWebsocketHookEventType#GET_HEARTBEAT_MESSAGE} event for the given {@link MockWebsocketHook}.
	 * @param source
	 * @return
	 */
	public static MockWebsocketHookEvent createGetHeartbeatEvent(MockWebsocketHook source) {
		MockWebsocketHookEvent e = new MockWebsocketHookEvent(MockWebsocketHookEventType.GET_HEARTBEAT_MESSAGE, source);
		return e;
	}

	private final MockWebsocketHookEventType type;
	
	private final MockWebsocketHook source;
	
	private WebsocketManager websocketManager;
	
	private String topic;
	
	/**
	 * Create a new {@link MockWebsocketHookEvent} for the given {@link MockWebsocketHook} and {@link MockWebsocketHookEventType}.
	 * @param type
	 * @param source
	 */
	public MockWebsocketHookEvent(MockWebsocketHookEventType type, MockWebsocketHook source) {
		this.type = type;
		this.source = source;
	}
	
	/**
	 * Retrieve the type of this event.
	 * @return The type of this event.
	 * @see MockWebsocketHookEventType
	 */
	public MockWebsocketHookEventType getType() {
		return type;
	}

	/**
	 * Retrieve the source of this event.
	 * @return The source of this event.
	 * @see MockWebsocketHook
	 */
	public MockWebsocketHook getSource() {
		return source;
	}

	/**
	 * Retrieve the {@link WebsocketManager} associated with this event.
	 * This field is set when type is {@link MockWebsocketHookEventType#INIT}, {@link MockWebsocketHookEventType#BEFORE_CONNECT}, {@link MockWebsocketHookEventType#AFTER_CONNECT}, {@link MockWebsocketHookEventType#BEFORE_DISCONNECT} or {@link MockWebsocketHookEventType#AFTER_DISCONNECT}.
	 * @return The {@link WebsocketManager} associated with this event.
	 */
	public WebsocketManager getWebsocketManager() {
		return websocketManager;
	}

	/**
	 * Set the {@link WebsocketManager} associated with this event.
	 * @param websocketManager The {@link WebsocketManager} to set.
	 */
	public void setWebsocketManager(WebsocketManager websocketManager) {
		this.websocketManager = websocketManager;
	}

	/**
	 * Retrieve the topic associated with this event.
	 * This field is set when type is {@link MockWebsocketHookEventType#GET_SUBSCRIBE_REQUEST_MESSAGE} or {@link MockWebsocketHookEventType#GET_UNSUBSCRIBE_REQUEST_MESSAGE}.
	 * @return The topic associated with this event.
	 */
	public String getTopic() {
		return topic;
	}

	/**
	 * Set the topic associated with this event.
	 * @param topic The topic to set.
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + type + "]";
	}
}
