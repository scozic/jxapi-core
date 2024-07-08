package com.scz.jxapi.netutils.websocket.mock;

/**
 * Interface for listening to events from a {@link MockWebsocket}.
 */
public interface MockWebsocketEventListener {

	void handleEvent(MockWebsocketEvent event);
}
