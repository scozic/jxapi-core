package com.scz.jxapi.netutils.websocket;

public interface WebsocketHook {
	
	default void afterInit(WebsocketManager websocketManager) {}
	
	default void beforeConnect(WebsocketManager websocketManager) throws WebsocketException {}
	
	default void afterConnect(WebsocketManager websocketManager) throws WebsocketException {}
	
	default void beforeDisconnect(WebsocketManager websocketManager) throws WebsocketException {}
	
	default void afterDisconnect(WebsocketManager websocketManager) throws WebsocketException {}
	
	default String getSubscribeRequestMessage(String topic) {return null;};
	
	default String getUnSubscribeRequestMessage(String topic) {return null;};
	
	default String getHeartBeatMessage() {return null;};

}
