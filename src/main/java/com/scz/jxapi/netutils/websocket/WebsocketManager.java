package com.scz.jxapi.netutils.websocket;

/**
 * Base interface for websocket connectivity. Actual implementation may use OKHTTP, or Spring over  
 */
public interface WebsocketManager {
	
	void addSystemMessageHandler(String topic, WebsocketMessageTopicMatcher matcher, RawWebsocketMessageHandler messageHandler);
	
	void subscribe(String topic, WebsocketMessageTopicMatcher matcher, RawWebsocketMessageHandler messageHandler);
	
	void unsubscribe(String topic);
	
	void subscribeErrorHandler(WebsocketErrorHandler websocketErrorHandler);
	
	boolean unsubscribeErrorHandler(WebsocketErrorHandler websocketErrorHandler);
	
	void dispose();
}
