package com.scz.jcex.netutils.websocket;

import com.scz.jcex.netutils.deserialization.MessageDeserializer;

public interface WebsocketEndpointFactory {

	<S extends WebsocketSubscribeParameters, M> WebsocketEndpoint<S, M> createWebsocketEndpoint(String endpoitName, MessageDeserializer<M> messageDeserializer);
}
