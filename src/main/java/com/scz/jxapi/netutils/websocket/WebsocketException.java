package com.scz.jxapi.netutils.websocket;

/**
 * Exception for an error raised on websocket
 */
public class WebsocketException extends Exception {

	public WebsocketException() {}
	
	public WebsocketException(Throwable t) {
		this(null, t);
	}
	
	public WebsocketException(String message) {
		this(message, null);
	}
	
	public WebsocketException(String message, Throwable t) {
		super(message, t);
	}

}
