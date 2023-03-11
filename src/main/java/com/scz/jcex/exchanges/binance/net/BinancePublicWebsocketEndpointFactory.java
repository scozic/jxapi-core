package com.scz.jcex.exchanges.binance.net;

import com.scz.jcex.netutils.websocket.AbstractWebsocketEndpointFactory;

public class BinancePublicWebsocketEndpointFactory extends AbstractWebsocketEndpointFactory {
	
	public static final String BASE_URL = "wss://data-stream.binance.com/ws";

	public BinancePublicWebsocketEndpointFactory() {
		super(new BinanceWebsocketManager(BASE_URL));
	}

}
