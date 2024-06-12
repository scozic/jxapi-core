package com.scz.jxapi.netutils.websocket;

/**
 * Generic interface for a websocket enppoint.
 * Subscriptions are performed with for a given topic and message structure. 
 *
 * @param <M> The message object disseminated by this websocket endpoint
 */
public interface WebsocketEndpoint<M> {

	/**
	 * Subscribes a listener to this websocket feed.
	 * @param request subscription request
	 * @param listener the listener that will receive incoming messages
	 * @return Subscription id, unique identifier that can be used to unsubscribe
	 */
	String subscribe(WebsocketSubscribeRequest request, WebsocketListener<M> listener);
	
	boolean unsubscribe(String unsubscriptionId);
}
