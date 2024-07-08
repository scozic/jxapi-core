package com.scz.netutis.websocket;

import com.scz.jxapi.exchange.ExchangeApi;
import com.scz.jxapi.netutils.websocket.WebsocketHook;
import com.scz.jxapi.netutils.websocket.WebsocketHookFactory;

public class MockWebsocketHookFactory implements WebsocketHookFactory {

	@Override
	public WebsocketHook createWebsocketHook(ExchangeApi exchangeApi) {
		return new MockWebsocketHook();
	}

}
