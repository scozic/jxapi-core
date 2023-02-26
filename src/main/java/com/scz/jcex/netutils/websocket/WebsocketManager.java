package com.scz.jcex.netutils.websocket;

import java.io.IOException;

/**
 * Base interface for websocket connectivity. Actual implementation may use OKHTTP, or Spring over  
 */
public interface WebsocketManager {
	
	void connect() throws IOException;
	
	void disconnect() throws IOException;
	
	void addSystemMessageHandler(String topic, WebsocketMessageTopicMatcher matcher, RawWebsocketMessageHandler messageHandler);
	
	void subscribe(String topic, WebsocketMessageTopicMatcher matcher, RawWebsocketMessageHandler messageHandler);
	
	boolean unsubscribe(String topic);
	
	void subscribeErrorHandler(WebsocketErrorHandler websocketErrorHandler);
	
	boolean unsubscribeErrorHandler(WebsocketErrorHandler websocketErrorHandler);
}
