package com.scz.netutis.websocket;

import com.scz.jxapi.netutils.websocket.RawWebsocketMessageHandler;
import com.scz.jxapi.netutils.websocket.WebsocketErrorHandler;
import com.scz.jxapi.util.EncodingUtil;

public class MockWebsocketEvent {
	
	public static MockWebsocketEvent createConnectEvent(MockWebsocket source) {
		return new MockWebsocketEvent(MockWebsocketEventType.CONNECT, source);
	}
	
	public static MockWebsocketEvent createDisconnectEvent(MockWebsocket source) {
		return new MockWebsocketEvent(MockWebsocketEventType.DISCONNECT, source);
	}
	
	public static MockWebsocketEvent createSendEvent(MockWebsocket source, String message) {
		MockWebsocketEvent event = new MockWebsocketEvent(MockWebsocketEventType.SEND, source);
		event.setMessage(message);
		return event;
	}
	
	public static MockWebsocketEvent createAddMessageHandlerEvent(MockWebsocket source, RawWebsocketMessageHandler messageHandler) {
		MockWebsocketEvent event = new MockWebsocketEvent(MockWebsocketEventType.ADD_MESSAGE_HANDLER, source);
		event.setMessageHandler(messageHandler);
		return event;
	}
	
	public static MockWebsocketEvent createRemoveMessageHandlerEvent(MockWebsocket source, RawWebsocketMessageHandler messageHandler) {
		MockWebsocketEvent event = new MockWebsocketEvent(MockWebsocketEventType.REMOVE_MESSAGE_HANDLER, source);
		event.setMessageHandler(messageHandler);
		return event;
	}
	
	public static MockWebsocketEvent createAddErrorHandlerEvent(MockWebsocket source, WebsocketErrorHandler errorHandler) {
		MockWebsocketEvent event = new MockWebsocketEvent(MockWebsocketEventType.ADD_ERROR_HANDLER, source);
		event.setErrorHandler(errorHandler);
		return event;
	}
	
	public static MockWebsocketEvent createRemoveErrorHandlerEvent(MockWebsocket source, WebsocketErrorHandler errorHandler) {
		MockWebsocketEvent event = new MockWebsocketEvent(MockWebsocketEventType.REMOVE_ERROR_HANDLER, source);
		event.setErrorHandler(errorHandler);
		return event;
	}
	
	private final MockWebsocketEventType type;
	private final MockWebsocket source;
	private RawWebsocketMessageHandler messageHandler;
	private WebsocketErrorHandler errorHandler;
	private String message;

	public MockWebsocketEvent(MockWebsocketEventType type, MockWebsocket source) {
		this.type = type;
		this.source = source;
	}

	public MockWebsocketEventType getType() {
		return type;
	}

	public MockWebsocket getSource() {
		return source;
	}

	public RawWebsocketMessageHandler getMessageHandler() {
		return messageHandler;
	}

	public void setMessageHandler(RawWebsocketMessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

	public WebsocketErrorHandler getErrorHandler() {
		return errorHandler;
	}

	public void setErrorHandler(WebsocketErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
}
