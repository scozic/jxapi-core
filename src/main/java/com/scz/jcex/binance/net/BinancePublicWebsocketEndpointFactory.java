package com.scz.jcex.binance.net;

import com.scz.jcex.netutils.websocket.DefaultWebsocketEndpointFactory;

public class BinancePublicWebsocketEndpointFactory extends DefaultWebsocketEndpointFactory {
	
	public static final String BASE_URL = "wss://data-stream.binance.com/ws";

	public BinancePublicWebsocketEndpointFactory() {
		super(new BinanceWebsocketManager(BASE_URL));
	}

}
