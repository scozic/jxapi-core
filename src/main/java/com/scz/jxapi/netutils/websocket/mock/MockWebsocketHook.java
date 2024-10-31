package com.scz.jxapi.netutils.websocket.mock;

import java.util.HashMap;
import java.util.Map;

import com.scz.jxapi.netutils.websocket.WebsocketException;
import com.scz.jxapi.netutils.websocket.WebsocketHook;
import com.scz.jxapi.netutils.websocket.WebsocketManager;
import com.scz.jxapi.observability.GenericObserver;

/**
 * The MockWebsocketHook class is a mock implementation of the WebsocketHook interface.
 * It provides hooks for various events in the WebsocketManager lifecycle and allows customization of request messages.
 */
public class MockWebsocketHook extends GenericObserver<MockWebsocketHookEvent> implements WebsocketHook {
	
	private final Map<String, String> subscribeRequestMessages = new HashMap<>();
	private final Map<String, String> unSubscribeRequestMessages = new HashMap<>();
	private String heartBeatMessage;
	private WebsocketManager websocketManager;

	/**
	 * Called after the WebsocketManager is initialized.
	 * 
	 * @param websocketManager The initialized WebsocketManager instance.
	 */
	@Override
	public void init(WebsocketManager websocketManager) {
		this.websocketManager = websocketManager;
		this.handleEvent(MockWebsocketHookEvent.createInitEvent(this, websocketManager));
	}

	/**
	 * Called before the WebsocketManager connects to the server.
	 * 
	 * @throws WebsocketException If an error occurs during the connection process.
	 */
	@Override
	public void beforeConnect() throws WebsocketException {
		this.handleEvent(MockWebsocketHookEvent.createBeforeConnectEvent(this));
	}

	/**
	 * Called after the WebsocketManager successfully connects to the server.
	 * 
	 * @throws WebsocketException If an error occurs after the connection is established.
	 */
	@Override
	public void afterConnect() throws WebsocketException {
		this.handleEvent(MockWebsocketHookEvent.createAfterConnectEvent(this));
	}

	/**
	 * Called before the WebsocketManager disconnects from the server.
	 * 
	 * @throws WebsocketException If an error occurs during the disconnection process.
	 */
	@Override
	public void beforeDisconnect() throws WebsocketException {
		this.handleEvent(MockWebsocketHookEvent.createBeforeDisconnectEvent(this));
	}

	/**
	 * Called after the WebsocketManager is disconnected from the server.
	 * 
	 * @throws WebsocketException If an error occurs after the disconnection is completed.
	 */
	@Override
	public void afterDisconnect() throws WebsocketException {
		this.handleEvent(MockWebsocketHookEvent.createAfterDisconnectEvent(this));
	}

	/**
	 * Retrieves the request message for subscribing to the specified topic.
	 * 
	 * @param topic The topic to subscribe to.
	 * @return The request message for subscribing to the topic.
	 */
	@Override
	public String getSubscribeRequestMessage(String topic) {
		this.handleEvent(MockWebsocketHookEvent.createGetSubscribeRequestMessageEvent(this, topic));
		return subscribeRequestMessages.get(topic);
	}

	/**
	 * Retrieves the request message for unsubscribing from the specified topic.
	 * 
	 * @param topic The topic to unsubscribe from.
	 * @return The request message for unsubscribing from the topic.
	 */
	@Override
	public String getUnSubscribeRequestMessage(String topic) {
		this.handleEvent(MockWebsocketHookEvent.createGetUnSubscribeRequestMessageEvent(this, topic));
		return unSubscribeRequestMessages.get(topic);
	}

	/**
	 * Retrieves the heartbeat message.
	 * 
	 * @return The heartbeat message.
	 */
	@Override
	public String getHeartBeatMessage() {
		this.handleEvent(MockWebsocketHookEvent.createGetHeartbeatEvent(this));
		return heartBeatMessage;
	}

	/**
	 * Sets the heartbeat message.
	 * 
	 * @param heartBeatMessage The heartbeat message to set.
	 */
	public void setHeartBeatMessage(String heartBeatMessage) {
		this.heartBeatMessage = heartBeatMessage;
	}

	/**
	 * Sets the request message for subscribing to the specified topic.
	 * 
	 * @param topic The topic to set the request message for.
	 * @param subscribeRequestMessage The request message for subscribing to the topic.
	 */
	public void setSubscribeRequestMessage(String topic, String subscribeRequestMessage) {
		subscribeRequestMessages.put(topic, subscribeRequestMessage);
	}
	
	/**
	 * Sets the request message for unsubscribing from the specified topic.
	 * 
	 * @param topic The topic to set the request message for.
	 * @param unSubscribeRequestMessage The request message for unsubscribing from the topic.
	 */
	public void setUnSubscribeRequestMessage(String topic, String unSubscribeRequestMessage) {
		unSubscribeRequestMessages.put(topic, unSubscribeRequestMessage);
	}

	/**
	 * Retrieves the WebsocketManager instance associated with this hook.
	 * 
	 * @return The WebsocketManager instance.
	 */
	public WebsocketManager getWebsocketManager() {
		return websocketManager;
	}
}
