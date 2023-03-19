package com.scz.jcex.exchanges.kucoin.net;

import java.util.Properties;

import com.scz.jcex.netutils.websocket.AbstractWebsocketEndpointFactory;

public class KucoinPrivateWebsocketEndpointFactory extends AbstractWebsocketEndpointFactory {

	public static final String BASE_URL = "wss://ws-api-spot.kucoin.com/";

	public KucoinPrivateWebsocketEndpointFactory() {
		super();
	}

	@Override
	public void setProperties(Properties properties) {
		this.websocketManager = new KucoinWebsocketManager(BASE_URL, new KucoinPrivateWebsocketListenKeyApi(properties));
	}


}
