package com.scz.netutis.websocket;

import com.scz.jxapi.netutils.websocket.AbstractWebsocket;
import com.scz.jxapi.netutils.websocket.WebsocketException;
import com.scz.jxapi.observability.SynchronizedObservable;

public class MockWebsocket extends AbstractWebsocket {
	
	// FIXME use GenericListener
	private final SynchronizedObservable<MockWebsocketEventListener, MockWebsocketEvent> events = 
		 new SynchronizedObservable<MockWebsocketEventListener, MockWebsocketEvent>((l, e) -> l.handleEvent(e));

	@Override
	protected void doConnect() throws WebsocketException {
		events.dispatch(MockWebsocketEvent.createConnectEvent(this));
	}

	@Override
	protected void doDisconnect() throws WebsocketException {
		events.dispatch(MockWebsocketEvent.createDisconnectEvent(this));
	}

	@Override
	protected void doSend(String message) throws WebsocketException {
		events.dispatch(MockWebsocketEvent.createDisconnectEvent(this));
	}

	// TODO Override AbstractWebsocket methods to create suitable event

}
