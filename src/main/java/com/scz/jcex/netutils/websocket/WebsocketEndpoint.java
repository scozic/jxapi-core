package com.scz.jcex.netutils.websocket;

import com.scz.jcex.generator.exchange.ExchangeApiDescriptor;

/**
 * Generic interface for a websocket enppoint.
 * Subscriptions are performed with for a given topic, using a given request, response to request and message structure. 
 *
 * @param <T> The request object containing fields for subscription
 * @param <M> The message object disseminated by this websocket endpoint
 */
public interface WebsocketEndpoint<T, M> {
	
	void init(ExchangeApiDescriptor exchangeApi);

	/**
	 * Subscribes a listener to this websocket feed.
	 * @param request subscription request
	 * @param listener the listener that will receive incoming messages
	 * @return Subscription id, unique identifier that can be used to unsubscribe
	 */
	String subscribe(WebsocketSubscribeRequest<T> request, WebsocketListener<M> listener);
	
	boolean unsubscribe(String unsubscriptionId);
}
