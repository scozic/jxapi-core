package com.scz.jxapi.netutils.websocket.mock;
/**
 * Enum for the different types of events that can be sent to a {@link MockWebsocketHookEventListener} by a {@link MockWebsocketHook}.
 */
public enum MockWebsocketHookEventType {

	/**
	 * Event that is sent when the {@link MockWebsocketHook#init()} method has been called.
	 */
	AFTER_INIT,
	BEFORE_CONNECT,
	AFTER_CONNECT,
	BEFORE_DISCONNECT,
	AFTER_DISCONNECT,
	GET_SUBSCRIBE_REQUEST_MESSAGE,
	GET_UNSUBSCRIBE_REQUEST_MESSAGE,
	GET_HEARTBEAT;
}
