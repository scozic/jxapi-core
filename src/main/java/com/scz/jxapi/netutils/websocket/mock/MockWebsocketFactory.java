package com.scz.jxapi.netutils.websocket.mock;

import com.scz.jxapi.exchange.ExchangeApi;
import com.scz.jxapi.netutils.websocket.Websocket;
import com.scz.jxapi.netutils.websocket.WebsocketFactory;

/**
 * {@link WebsocketFactory} implementation for creating {@link MockWebsocket} instances.
 */
public class MockWebsocketFactory implements WebsocketFactory {

	@Override
	public Websocket createWebsocket(ExchangeApi exchangeApi) {
		return new MockWebsocket();
	}

}
