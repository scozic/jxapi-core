package com.scz.jxapi.netutils.websocket;

import com.scz.jxapi.exchange.ExchangeApi;
import com.scz.jxapi.netutils.websocket.spring.SpringWebsocket;

public class DefaultWebsocketFactory implements WebsocketFactory {

	@Override
	public Websocket createWebsocket(ExchangeApi exchangeApi) {
		return new SpringWebsocket();
	}

}
