package com.scz.jcex.netutils.websocket;

import com.scz.jcex.netutils.deserialization.MessageDeserializer;

public class DefaultWebsocketEndpointFactory implements WebsocketEndpointFactory {
	
	protected final WebsocketManager websocketManager;
	
	public DefaultWebsocketEndpointFactory(WebsocketManager websocketManager) {
		this.websocketManager = websocketManager;
	}

	@Override
	public <S extends WebsocketSubscribeParameters, M> WebsocketEndpoint<S, M> createWebsocketEndpoint(MessageDeserializer<M> messageDeserializer) {
		return new DefaultWebsocketEndpoint<>(websocketManager, messageDeserializer);
	}

}
