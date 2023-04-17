package com.scz.jxapi.exchanges.binance.net;

import java.util.Properties;

import com.scz.jxapi.netutils.websocket.AbstractWebsocketEndpointFactory;

public class BinancePrivateSpotApiWebsocketEndpointFactory extends AbstractWebsocketEndpointFactory {
	
	public static final String BASE_URL = "wss://stream.binance.com:9443/ws";
	
	@Override
	public void setProperties(Properties props) {
		this.websocketManager = new BinancePrivateWebsocketManager(BASE_URL, new BinanceSpotListenKeyApi(props));
	}

}
