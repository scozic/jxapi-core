package com.scz.jcex.exchanges.kucoin.net;

import java.util.Properties;

import com.scz.jcex.netutils.websocket.AbstractWebsocketEndpointFactory;

public class KucoinFuturesPrivateWebsocketEndpointFactory extends AbstractWebsocketEndpointFactory {

	public static final String BASE_URL = KucoinFuturesPublicWebsocketEndpointFactory.BASE_URL;
	public static final String TOKEN_API_BASE_URL = KucoinFuturesPublicWebsocketEndpointFactory.TOKEN_API_BASE_URL;

	public KucoinFuturesPrivateWebsocketEndpointFactory() {
		super();
	}

	@Override
	public void setProperties(Properties properties) {
		this.websocketManager = new KucoinWebsocketManager(BASE_URL, new KucoinPrivateWebsocketListenKeyApi(properties, TOKEN_API_BASE_URL), true);
	}
}
