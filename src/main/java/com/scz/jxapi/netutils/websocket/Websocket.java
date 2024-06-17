package com.scz.jxapi.netutils.websocket;

public interface  Websocket {
	
	void connect() throws WebsocketException;
	void disconnect() throws WebsocketException;
	void send(String message) throws WebsocketException;
	void addMessageHandler(RawWebsocketMessageHandler messageHandler);
	boolean removeMessageHandler(RawWebsocketMessageHandler messageHandler);
	void addErrorHandler(WebsocketErrorHandler errorHandler);
	boolean removeErrorHandler(WebsocketErrorHandler errorHandler);
	boolean isConnected();
	void setUrl(String url);
	String getUrl();

}
