package com.scz.netutis.websocket;

import com.scz.jxapi.netutils.websocket.RawWebsocketMessageHandler;
import com.scz.jxapi.netutils.websocket.WebsocketErrorHandler;
import com.scz.jxapi.util.EncodingUtil;

/**
 * The MockWebsocketEvent class represents an event that can occur in a mock websocket.
 * It provides static factory methods to create different types of events and access their properties.
 */
public class MockWebsocketEvent {
	
	/**
	 * Creates a CONNECT event for the given mock websocket source.
	 * 
	 * @param source The mock websocket source.
	 * @return The created MockWebsocketEvent object.
	 */
	public static MockWebsocketEvent createConnectEvent(MockWebsocket source) {
		return new MockWebsocketEvent(MockWebsocketEventType.CONNECT, source);
	}
	
	/**
	 * Creates a DISCONNECT event for the given mock websocket source.
	 * 
	 * @param source The mock websocket source.
	 * @return The created MockWebsocketEvent object.
	 */
	public static MockWebsocketEvent createDisconnectEvent(MockWebsocket source) {
		return new MockWebsocketEvent(MockWebsocketEventType.DISCONNECT, source);
	}
	
	/**
	 * Creates a SEND event for the given mock websocket source with the specified message.
	 * 
	 * @param source The mock websocket source.
	 * @param message The message to be sent.
	 * @return The created MockWebsocketEvent object.
	 */
	public static MockWebsocketEvent createSendEvent(MockWebsocket source, String message) {
		MockWebsocketEvent event = new MockWebsocketEvent(MockWebsocketEventType.SEND, source);
		event.setMessage(message);
		return event;
	}
	
	/**
	 * Creates an ADD_MESSAGE_HANDLER event for the given mock websocket source with the specified message handler.
	 * 
	 * @param source The mock websocket source.
	 * @param messageHandler The message handler to be added.
	 * @return The created MockWebsocketEvent object.
	 */
	public static MockWebsocketEvent createAddMessageHandlerEvent(MockWebsocket source, RawWebsocketMessageHandler messageHandler) {
		MockWebsocketEvent event = new MockWebsocketEvent(MockWebsocketEventType.ADD_MESSAGE_HANDLER, source);
		event.setMessageHandler(messageHandler);
		return event;
	}
	
	/**
	 * Creates a REMOVE_MESSAGE_HANDLER event for the given mock websocket source with the specified message handler.
	 * 
	 * @param source The mock websocket source.
	 * @param messageHandler The message handler to be removed.
	 * @return The created MockWebsocketEvent object.
	 */
	public static MockWebsocketEvent createRemoveMessageHandlerEvent(MockWebsocket source, RawWebsocketMessageHandler messageHandler) {
		MockWebsocketEvent event = new MockWebsocketEvent(MockWebsocketEventType.REMOVE_MESSAGE_HANDLER, source);
		event.setMessageHandler(messageHandler);
		return event;
	}
	
	/**
	 * Creates an ADD_ERROR_HANDLER event for the given mock websocket source with the specified error handler.
	 * 
	 * @param source The mock websocket source.
	 * @param errorHandler The error handler to be added.
	 * @return The created MockWebsocketEvent object.
	 */
	public static MockWebsocketEvent createAddErrorHandlerEvent(MockWebsocket source, WebsocketErrorHandler errorHandler) {
		MockWebsocketEvent event = new MockWebsocketEvent(MockWebsocketEventType.ADD_ERROR_HANDLER, source);
		event.setErrorHandler(errorHandler);
		return event;
	}
	
	/**
	 * Creates a REMOVE_ERROR_HANDLER event for the given mock websocket source with the specified error handler.
	 * 
	 * @param source The mock websocket source.
	 * @param errorHandler The error handler to be removed.
	 * @return The created MockWebsocketEvent object.
	 */
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

	/**
	 * Constructs a MockWebsocketEvent object with the specified type and source.
	 * 
	 * @param type The type of the event.
	 * @param source The source of the event.
	 */
	public MockWebsocketEvent(MockWebsocketEventType type, MockWebsocket source) {
		this.type = type;
		this.source = source;
	}

	/**
	 * Returns the type of the event.
	 * 
	 * @return The type of the event.
	 */
	public MockWebsocketEventType getType() {
		return type;
	}

	/**
	 * Returns the source of the event.
	 * 
	 * @return The source of the event.
	 */
	public MockWebsocket getSource() {
		return source;
	}

	/**
	 * Returns the message handler associated with the event.
	 * 
	 * @return The message handler associated with the event.
	 */
	public RawWebsocketMessageHandler getMessageHandler() {
		return messageHandler;
	}

	/**
	 * Sets the message handler for the event.
	 * 
	 * @param messageHandler The message handler to be set.
	 */
	public void setMessageHandler(RawWebsocketMessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

	/**
	 * Returns the error handler associated with the event.
	 * 
	 * @return The error handler associated with the event.
	 */
	public WebsocketErrorHandler getErrorHandler() {
		return errorHandler;
	}

	/**
	 * Sets the error handler for the event.
	 * 
	 * @param errorHandler The error handler to be set.
	 */
	public void setErrorHandler(WebsocketErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}
	
	/**
	 * Returns the message associated with the event.
	 * 
	 * @return The message associated with the event.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message for the event.
	 * 
	 * @param message The message to be set.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Returns a string representation of the MockWebsocketEvent object.
	 * 
	 * @return A string representation of the MockWebsocketEvent object.
	 */
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
}
