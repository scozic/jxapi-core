package com.scz.jxapi.netutils.websocket.mock;

import com.scz.jxapi.netutils.websocket.WebsocketListener;
import com.scz.jxapi.observability.GenericObserver;

/**
 * Mock implementation of a {@link WebsocketListener}.
 *
 * @param <M> The type of messages to listen to
 */
public class MockWebsocketListener<M> extends GenericObserver<M> implements WebsocketListener<M> {

	@Override
	public void handleMessage(M message) {
		handleEvent(message);
	}

}
