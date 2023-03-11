package com.scz.jcex.binance.demo;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jcex.exchanges.binance.gen.spotmarketdata.BinanceSpotMarketDataApi;
import com.scz.jcex.exchanges.binance.gen.spotmarketdata.BinanceSpotMarketDataApiImpl;
import com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationRequest;
import com.scz.jcex.exchanges.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponse;
import com.scz.jcex.util.TestApiProperties;

public class BinanceExchangeInformationRestAPIDemo {

	private static final Logger log = LoggerFactory.getLogger(BinanceExchangeInformationRestAPIDemo.class);
	
	public static void main(String[] args) {
		try {
			BinanceSpotMarketDataApi api = new BinanceSpotMarketDataApiImpl(TestApiProperties.filterProperties("binance", true));
			log.info("Sending request...");
			BinanceExchangeInformationRequest request = new BinanceExchangeInformationRequest();
			request.setSymbols(Arrays.asList("BTCUSDT", "BNBUSDT"));
			BinanceExchangeInformationResponse response = api.exchangeInformation(request);
			log.info("Response received:" + response);
			System.exit(0);
		} catch (Throwable t) {
			t.printStackTrace(System.out);
			System.exit(-1);
		}
	}
}
