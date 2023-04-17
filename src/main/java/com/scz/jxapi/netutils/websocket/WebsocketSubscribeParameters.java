package com.scz.jxapi.netutils.websocket;

/**
 * Interface that POJOs for Websocket subscription requests parameters will implement, to
 * properly serialize their eventual fields as URL parameters in CEX specific
 * way
 */
public interface WebsocketSubscribeParameters {

	/**
	 * @return The part of websocket subscription URL that comes after base (domain part). This topic may contain formatted parameters 
	 *  
	 */
	String getTopic();
}
