package com.scz.jcex.binance.exchange.spot;

import java.io.IOException;

import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceAllMarketTickersStreamRequest;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceAllMarketTickersStreamResponse;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationRequest;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponse;
import com.scz.jcex.netutils.websocket.WebsocketListener;

public interface BinanceSpotApi {

	BinanceExchangeInformationResponse exchangeInformation(BinanceExchangeInformationRequest request) throws IOException;
	
	String subscribeAllMarketsTicker(BinanceAllMarketTickersStreamRequest request, WebsocketListener<BinanceAllMarketTickersStreamResponse> listener) throws IOException;
	
	boolean unsubscribeAllMarketsTicker(String subscriptionId);
}
