package com.scz.jcex.netutils.websocket;

public interface WebsocketEndpointFactory {

	WebsocketEndpoint<?, ?> createWebsocketEndpoint();
}
