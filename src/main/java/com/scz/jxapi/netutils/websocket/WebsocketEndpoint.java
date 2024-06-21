package com.scz.jxapi.netutils.websocket;

/**
 * Generic interface for a websocket enppoint.
 * Subscriptions are performed with for a given topic and message structure. 
 *
 * @param <M> The message object disseminated by this websocket endpoint
 */
public interface WebsocketEndpoint<M> {
	
	String getEndpointName();
}
