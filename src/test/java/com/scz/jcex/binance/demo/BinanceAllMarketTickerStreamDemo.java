package com.scz.jcex.binance.demo;

import java.util.Properties;

import com.scz.jcex.binance.gen.spotmarketdata.BinanceSpotMarketData;
import com.scz.jcex.binance.gen.spotmarketdata.BinanceSpotMarketDataImpl;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceAllMarketTickersStreamRequest;

public class BinanceAllMarketTickerStreamDemo {

	public static void main(String[] args) {
		try {
//			BinanceAllMarketTickerPublicWebsocketEndpoint endpoint = new BinanceAllMarketTickerPublicWebsocketEndpoint();
//			endpoint.subscribe(new BinanceAllMarketTickersStreamRequest(), m -> System.out.println("Received message[" + m + "]"));
			BinanceSpotMarketData api = new BinanceSpotMarketDataImpl(new Properties());
			api.subscribeAllMarketTickersStream(new BinanceAllMarketTickersStreamRequest(), m -> {
				 System.out.println("Received message[" + m + "]");
			});
			
		} catch (Throwable t) {
			t.printStackTrace(System.out);
		}
	}
}
