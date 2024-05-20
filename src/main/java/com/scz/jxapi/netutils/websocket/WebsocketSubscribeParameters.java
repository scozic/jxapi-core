package com.scz.jxapi.netutils.websocket;

/**
 * Interface that POJOs for Websocket subscription requests parameters will implement, to
 * properly serialize their eventual fields as URL parameters in CEX specific
 * way
 */
@Deprecated
public interface WebsocketSubscribeParameters {

	/**
	 * @return The part of  
	 *  
	 */
	String getTopic();
}
