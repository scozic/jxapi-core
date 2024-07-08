package com.scz.netutis.websocket;

import com.scz.jxapi.exchange.ExchangeApi;
import com.scz.jxapi.netutils.websocket.Websocket;
import com.scz.jxapi.netutils.websocket.WebsocketFactory;

public class MockWebsocketFactory implements WebsocketFactory {

	@Override
	public Websocket createWebsocket(ExchangeApi exchangeApi) {
		return new MockWebsocket();
	}

}
