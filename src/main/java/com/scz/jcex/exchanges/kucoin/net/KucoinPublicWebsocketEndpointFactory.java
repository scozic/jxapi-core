package com.scz.jcex.exchanges.kucoin.net;

import com.scz.jcex.netutils.websocket.AbstractWebsocketEndpointFactory;

public class KucoinPublicWebsocketEndpointFactory extends AbstractWebsocketEndpointFactory {
	
	public static final String BASE_URL = "wss://ws-api-spot.kucoin.com/";

	public KucoinPublicWebsocketEndpointFactory() {
		super(new KucoinWebsocketManager(BASE_URL));
	}

}
