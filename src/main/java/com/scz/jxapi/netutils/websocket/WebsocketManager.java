package com.scz.jxapi.netutils.websocket;

import java.io.IOException;

/**
 * 
 */
public interface WebsocketManager {
	
	void addSystemMessageHandler(String topic, WebsocketMessageTopicMatcher matcher, RawWebsocketMessageHandler messageHandler);
	
	void subscribe(String topic, WebsocketMessageTopicMatcher matcher, RawWebsocketMessageHandler messageHandler);
	
	void unsubscribe(String topic);
	
	void subscribeErrorHandler(WebsocketErrorHandler websocketErrorHandler);
	
	boolean unsubscribeErrorHandler(WebsocketErrorHandler websocketErrorHandler);
	
	void dispose();
	
	/**
	 * Sends a message to this manager underlying {@link Websocket}. 
	 * <br/>
	 * This method must be called from dedicated write executor thread, which is the
	 * case in {@link WebsocketHook#beforeConnect(WebsocketManager)},
	 * {@link WebsocketHook#beforeDisconnect(WebsocketManager)},
	 * {@link WebsocketHook#afterConnect(WebsocketManager)},
	 * {@link WebsocketHook#afterDisconnect(WebsocketManager)} methods. 
	 * <br/>
	 * Sending messages from another thread should be done using
	 * {@link #sendAsync(String)} which will write on websocket output
	 * asynchronously from WebsocketManager dedicated write excutor. 
	 * <br/>
	 * A typical use case would be sending a message for authentication handshake
	 * from {@link WebsocketHook#afterConnect(WebsocketManager)}. 
	 * 
	 * @param msg Message to send on underlying Websocket output.
	 * @throws IOException
	 */
	void send(String msg) throws WebsocketException;
	
	void sendAsync(String msg);
	
	void hearbeatReceived();
	
	long getReconnectDelay();

	void setReconnectDelay(long reconnectDelay);
	
	long getNoMessageTimeout();

	void setNoMessageTimeout(long noMessageTimeout);
	
	long getHeartBeatInterval();

	void setHeartBeatInterval(long heartBeatInterval);

	long getNoHeartBeatResponseTimeout();

	void setNoHeartBeatResponseTimeout(long noHeartBeatResponseTimeout);
	
	String getUrl();
	
	void setUrl(String url);
}
