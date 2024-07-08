package com.scz.jxapi.netutils.websocket;

import com.scz.jxapi.exchange.ExchangeApi;

public class MockWebsocketFactory implements WebsocketFactory {

	@Override
	public Websocket createWebsocket(ExchangeApi exchangeApi) {
		return new MockWebsocket();
	}

}
