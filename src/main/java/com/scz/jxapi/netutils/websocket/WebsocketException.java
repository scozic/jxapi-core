package com.scz.jxapi.netutils.websocket;

/**
 * Exception for an error raised on websocket
 */
public class WebsocketException extends Exception {

	public WebsocketException() {}
	
	/**
	 * @param t The cause of the exception
	 */
	public WebsocketException(Throwable t) {
		this(null, t);
	}
	
	/**
	 * @param message The error message
	 */
	public WebsocketException(String message) {
		this(message, null);
	}
	
	/**
	 * @param message The error message
	 * @param t The cause of the exception
	 */
	public WebsocketException(String message, Throwable t) {
		super(message, t);
	}

}
