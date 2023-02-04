package com.scz.jcex.binance.websocket;

import com.scz.jcex.netutils.websocket.WebsocketEndpoint;
import com.scz.jcex.netutils.websocket.WebsocketEndpointFactory;

public class BinancePublicWebsocketFactory implements WebsocketEndpointFactory {

	@Override
	public WebsocketEndpoint<?, ?> createWebsocketEndpoint(String endpoitName) {
		switch (BinancePublicWebsocketEndpoints.valueOf(endpoitName)) {
		case ALL_MARKET_TICKERS:
				
			break;
		default:
			throw new IllegalArgumentException("Unkown public websocket endpoint name:" + endpoitName);
		}
		return null;
	}

}
