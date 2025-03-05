package com.scz.jxapi.netutils.websocket;

/**
 * Exception for an error raised on websocket
 */
public class WebsocketException extends Exception {
	
	/**
	 * Constructor
	 * @param t The cause of the exception
	 */
	public WebsocketException(Throwable t) {
		this(null, t);
	}
	
	/**
	 * Constructor
	 * @param message The error message
	 */
	public WebsocketException(String message) {
		this(message, null);
	}
	
	/**
	 * Constructor
	 * @param message The error message
	 * @param t The cause of the exception
	 */
	public WebsocketException(String message, Throwable t) {
		super(message, t);
	}

}
