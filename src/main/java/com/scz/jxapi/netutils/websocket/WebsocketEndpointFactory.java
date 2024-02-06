package com.scz.jxapi.netutils.websocket;

import java.util.Properties;

import com.scz.jxapi.generator.exchange.ExchangeApiDescriptor;
import com.scz.jxapi.netutils.deserialization.MessageDeserializer;
import com.scz.jxapi.util.HasProperties;

/**
 * Factory for {@link WebsocketEndpoint}. Exchange APIs that provide websocket
 * endpoint must provide an implementation of this interface to create suitable
 * {@link WebsocketEndpoint} instances.
 */
public interface WebsocketEndpointFactory {
	
	/**
	 * 
	 * Called with an API interface implementation generated from an
	 * {@link ExchangeApiDescriptor} instance. Such implementation extends
	 * {@link HasProperties} interface to provide the {@link Properties} containing
	 * exchange specific connection properties, such as API key/password for signing
	 * requests. This instance may be cast to the specific generated API interface
	 * type that carries REST endpoints, in case websocket handshake authentication
	 * involves a token provided by a specific REST API.
	 * 
	 * @param properties
	 */
	default void setApi(HasProperties api) {}

	/**
	 * @param <S> the subscription request
	 * @param <M> the websocket stream message type
	 * @param messageDeserializer websocket message stream deserializer.
	 * @return Suitable {@link WebsocketEndpoint}
	 */
	<S, M> WebsocketEndpoint<S, M> createWebsocketEndpoint(MessageDeserializer<M> messageDeserializer);
}
