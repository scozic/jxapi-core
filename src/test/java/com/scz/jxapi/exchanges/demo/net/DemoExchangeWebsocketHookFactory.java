package com.scz.jxapi.exchanges.demo.net;

import com.scz.jxapi.exchange.ExchangeApi;
import com.scz.jxapi.netutils.websocket.WebsocketHook;
import com.scz.jxapi.netutils.websocket.WebsocketHookFactory;

public class DemoExchangeWebsocketHookFactory implements WebsocketHookFactory {

	@Override
	public WebsocketHook createWebsocketHook(ExchangeApi exchangeApi) {
		return new DemoExchangeWebsocketHook(exchangeApi.getProperties());
	}

}
