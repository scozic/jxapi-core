package com.scz.jxapi.netutils.websocket;

import com.scz.jxapi.exchange.ExchangeApi;

public class MockWebsocketHookFactory implements WebsocketHookFactory {

	@Override
	public WebsocketHook createWebsocketHook(ExchangeApi exchangeApi) {
		return new MockWebsocketHook();
	}

}
