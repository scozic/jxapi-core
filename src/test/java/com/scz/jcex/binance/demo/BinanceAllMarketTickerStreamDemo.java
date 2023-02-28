package com.scz.jcex.binance.demo;

import com.scz.jcex.binance.exchange.spot.BinanceSpotApi;
import com.scz.jcex.binance.exchange.spot.BinanceSpotApiImpl;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceAllMarketTickersStreamRequest;

public class BinanceAllMarketTickerStreamDemo {

	public static void main(String[] args) {
		try {
//			BinanceAllMarketTickerPublicWebsocketEndpoint endpoint = new BinanceAllMarketTickerPublicWebsocketEndpoint();
//			endpoint.subscribe(new BinanceAllMarketTickersStreamRequest(), m -> System.out.println("Received message[" + m + "]"));
			BinanceSpotApi api = new BinanceSpotApiImpl();
			api.subscribeAllMarketsTicker(new BinanceAllMarketTickersStreamRequest(), m -> {
//				 System.out.println("Received message[" + m + "]")
			});
			
		} catch (Throwable t) {
			t.printStackTrace(System.out);
		}
	}
}
