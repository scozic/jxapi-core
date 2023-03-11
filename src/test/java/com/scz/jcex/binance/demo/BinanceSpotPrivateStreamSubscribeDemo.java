package com.scz.jcex.binance.demo;

import com.scz.jcex.binance.gen.spottrading.BinanceSpotTradingApi;
import com.scz.jcex.binance.gen.spottrading.BinanceSpotTradingApiImpl;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceBalanceUpdateUserDataStreamRequest;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceExecutionReportUserDataStreamRequest;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceOutboundAccountPositionUserDataStreamRequest;
import com.scz.jcex.util.TestApiProperties;

public class BinanceSpotPrivateStreamSubscribeDemo {

	public static void main(String[] args) {
		try {
//			BinanceAllMarketTickerPublicWebsocketEndpoint endpoint = new BinanceAllMarketTickerPublicWebsocketEndpoint();
//			endpoint.subscribe(new BinanceAllMarketTickersStreamRequest(), m -> System.out.println("Received message[" + m + "]"));
			BinanceSpotTradingApi api = new BinanceSpotTradingApiImpl(TestApiProperties.filterProperties("binance", true));
			api.subscribeBalanceUpdateUserDataStream(new BinanceBalanceUpdateUserDataStreamRequest(), m -> {
				System.out.println("BALANCE_UPDATE:" + m);
			});
			api.subscribeExecutionReportUserDataStream(new BinanceExecutionReportUserDataStreamRequest(), m -> {
				System.out.println("EXECUTION_REPORT:" + m);
			});
			api.subscribeOutboundAccountPositionUserDataStream(new BinanceOutboundAccountPositionUserDataStreamRequest(), m -> {
				System.out.println("OUTBOUNT_ACCOUNT_POSITION:" + m);
			});
		
			
		} catch (Throwable t) {
			t.printStackTrace(System.out);
		}
	}
}
