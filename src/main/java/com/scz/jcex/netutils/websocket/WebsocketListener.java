package com.scz.jcex.netutils.websocket;

/**
 * Websocket callback
 * @param <M> the message object disseminated by this websocket stream.
 */
public interface WebsocketListener<M> {
	
	void handleMessage(M message);

}
