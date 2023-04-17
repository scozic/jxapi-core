package com.scz.jxapi.exchanges.kucoin.net;

import java.util.Properties;

import com.scz.jxapi.netutils.websocket.AbstractWebsocketEndpointFactory;

public class KucoinPublicWebsocketEndpointFactory extends AbstractWebsocketEndpointFactory {

	public static final String BASE_URL = "wss://ws-api-spot.kucoin.com/";
	public static final String TOKEN_API_BASE_URL = "https://api.kucoin.com/api/v1/";

	public KucoinPublicWebsocketEndpointFactory() {
		super();
	}

	@Override
	public void setProperties(Properties properties) {
		this.websocketManager = new KucoinWebsocketManager(BASE_URL, new KucoinPublicWebsocketListenKeyApi(properties, TOKEN_API_BASE_URL), false);
	}

}
