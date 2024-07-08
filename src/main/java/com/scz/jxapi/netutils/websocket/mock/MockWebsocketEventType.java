package com.scz.jxapi.netutils.websocket.mock;

public enum MockWebsocketEventType {
	CONNECT,
	DISCONNECT,
	SEND,
	ADD_MESSAGE_HANDLER,
	REMOVE_MESSAGE_HANDLER,
	ADD_ERROR_HANDLER,
	REMOVE_ERROR_HANDLER
}
