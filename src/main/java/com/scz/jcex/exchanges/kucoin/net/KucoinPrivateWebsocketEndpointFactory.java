package com.scz.jcex.exchanges.kucoin.net;

import java.util.Properties;

import com.scz.jcex.netutils.websocket.AbstractWebsocketEndpointFactory;

public class KucoinPrivateWebsocketEndpointFactory extends AbstractWebsocketEndpointFactory {

	public static final String BASE_URL = "wss://ws-api-spot.kucoin.com/";
	public static final String TOKEN_API_BASE_URL = "https://api.kucoin.com/api/v1/";

	public KucoinPrivateWebsocketEndpointFactory() {
		super();
	}

	@Override
	public void setProperties(Properties properties) {
		this.websocketManager = new KucoinWebsocketManager(BASE_URL, new KucoinPrivateWebsocketListenKeyApi(properties, TOKEN_API_BASE_URL), false);
	}


}
