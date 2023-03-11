package com.scz.jcex.binance.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jcex.binance.gen.spottrading.BinanceSpotTradingApi;
import com.scz.jcex.binance.gen.spottrading.BinanceSpotTradingApiImpl;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceSpotListenKeyRequest;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceSpotListenKeyResponse;
import com.scz.jcex.util.TestApiProperties;

public class BinanceSpotListenKeyDemo {
	
	private static final Logger log = LoggerFactory.getLogger(BinanceSpotListenKeyDemo.class);

	public static void main(String[] args) {
		try {
			BinanceSpotTradingApi api = new BinanceSpotTradingApiImpl(TestApiProperties.filterProperties("binance", true));
			log.info("Sending request...");
			BinanceSpotListenKeyResponse listenKeyResponse = api.spotListenKey(new BinanceSpotListenKeyRequest());
			log.info("Got listen key:" + listenKeyResponse.getListenKey());
		} catch (Throwable t) {
			t.printStackTrace(System.out);
		}
	}

}
