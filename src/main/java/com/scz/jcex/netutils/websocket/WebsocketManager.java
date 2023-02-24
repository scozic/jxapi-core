package com.scz.jcex.netutils.websocket;

import java.io.IOException;

public interface WebsocketManager {
	
	void connect() throws IOException;
	
	void dispose();
	
	void send(String message) throws IOException;
	
	void registerHandler(String topic, WebsocketMessageTopicMatcher matcher, RawWebsocketMessageHandler messageHandler);
	
	void subscribeErrorHandler(WebsocketErrorHandler websocketErrorHandler);
	
	boolean unsubscribeErrorHandler(WebsocketErrorHandler websocketErrorHandler);
}
