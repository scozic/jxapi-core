package com.scz.jxapi.netutils.websocket;

import com.scz.jxapi.exchange.ExchangeApi;
import com.scz.jxapi.netutils.websocket.spring.SpringWebsocket;

/**
 * Default implementation of a {@link WebsocketFactory}.
 * <p>
 * This class creates a new instance of a {@link Websocket} implementation, using a {@link ExchangeApi} instance.
 * 
 * @see WebsocketFactory
 */
public class DefaultWebsocketFactory implements WebsocketFactory {

	@Override
	public Websocket createWebsocket(ExchangeApi exchangeApi) {
		return new SpringWebsocket();
	}

}
