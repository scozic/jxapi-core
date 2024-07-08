package com.scz.jxapi.netutils.websocket;

import com.scz.jxapi.observability.GenericObserver;

public class MockWebsocketListener<M> extends GenericObserver<M> implements WebsocketListener<M> {

	@Override
	public void handleMessage(M message) {
		handleEvent(message);
	}

}
