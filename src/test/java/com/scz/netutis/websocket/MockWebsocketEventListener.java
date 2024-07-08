package com.scz.netutis.websocket;

/**
 * Interface for listening to events from a {@link MockWebsocket}.
 */
public interface MockWebsocketEventListener {

	void handleEvent(MockWebsocketEvent event);
}
