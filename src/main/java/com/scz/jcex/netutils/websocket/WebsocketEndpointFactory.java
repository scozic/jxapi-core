package com.scz.jcex.netutils.websocket;

import java.util.Properties;

import com.scz.jcex.netutils.deserialization.MessageDeserializer;

/**
 * Factory for {@link WebsocketEndpoint}. Generated exchange wrappers 
 */
public interface WebsocketEndpointFactory {
	
	/**
	 * Called with exchange specific connection properties, such as API key/password
	 * for signing requests.
	 * 
	 * @param properties
	 */
	default void setProperties(Properties properties) {}

	<S extends WebsocketSubscribeParameters, M> WebsocketEndpoint<S, M> createWebsocketEndpoint(MessageDeserializer<M> messageDeserializer);
}
