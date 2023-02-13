package com.scz.jcex.binance.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jcex.binance.exchange.spot.BinanceSpotApi;
import com.scz.jcex.binance.exchange.spot.BinanceSpotApiImpl;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationRequest;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponse;

public class BinanceExchangeInformationRestAPIDemo {

	private static final Logger log = LoggerFactory.getLogger(BinanceExchangeInformationRestAPIDemo.class);
	
	public static void main(String[] args) {
		try {
			BinanceSpotApi api = new BinanceSpotApiImpl();
			log.info("Sending request...");
			BinanceExchangeInformationResponse response = api.exchangeInformation(new BinanceExchangeInformationRequest());
			log.info("Response received:" + response);
			System.exit(0);
		} catch (Throwable t) {
			t.printStackTrace(System.out);
			System.exit(-1);
		}
	}
}
