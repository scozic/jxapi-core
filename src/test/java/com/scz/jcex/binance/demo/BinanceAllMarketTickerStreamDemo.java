package com.scz.jcex.binance.demo;

import com.scz.jcex.binance.spotmarketdata.pojo.BinanceAllMarketTickersStreamRequest;
import com.scz.jcex.binance.websocket.BinanceAllMarketTickerPublicWebsocketEndpoint;

public class BinanceAllMarketTickerStreamDemo {

	public static void main(String[] args) {
		try {
			BinanceAllMarketTickerPublicWebsocketEndpoint endpoint = new BinanceAllMarketTickerPublicWebsocketEndpoint();
			endpoint.subscribe(new BinanceAllMarketTickersStreamRequest(), m -> System.out.println("Received message[" + m + "]"));
		} catch (Throwable t) {
			t.printStackTrace(System.out);
		}
	}
}
