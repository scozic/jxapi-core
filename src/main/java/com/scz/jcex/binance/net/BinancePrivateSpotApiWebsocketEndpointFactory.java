package com.scz.jcex.binance.net;

import java.util.Properties;

import com.scz.jcex.binance.gen.spottrading.BinanceSpotTradingApiImpl;
import com.scz.jcex.netutils.websocket.AbstractWebsocketEndpointFactory;

public class BinancePrivateSpotApiWebsocketEndpointFactory extends AbstractWebsocketEndpointFactory {
	
	public static final String BASE_URL = "wss://stream.binance.com:9443/ws";
	
	@Override
	public void setProperties(Properties props) {
		this.websocketManager = new BinancePrivateWebsocketManager(BASE_URL, new BinanceSpotListenKeyApi(new BinanceSpotTradingApiImpl(props)));
	}

}
