package com.scz.jxapi.netutils.websocket;

/**
 * Extracts topic from a websocket subscription request parameters.
 * @author Sylvestre COZIC
 *
 * @param <T>
 */
public interface WebsocketSubscribeRequestTopicSerializer<T> {

	/**
	 * @param subscribeRequest Websocket subscription request
	 * @return
	 */
	String getTopic(T subscribeRequest);
}
