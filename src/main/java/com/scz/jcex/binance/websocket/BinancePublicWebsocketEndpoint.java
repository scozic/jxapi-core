package com.scz.jcex.binance.websocket;

import com.scz.jcex.binance.spotmarketdata.pojo.BinanceAllMarketTickersStreamRequest;
import com.scz.jcex.binance.spotmarketdata.pojo.BinanceAllMarketTickersStreamResponse;
import com.scz.jcex.netutils.websocket.okhttp.AbstractOkHttpWebsocketEndpoint;
import com.scz.jcex.netutils.websocket.okhttp.SubscriptionOptions;

public class BinancePublicWebsocketEndpoint extends AbstractOkHttpWebsocketEndpoint<BinanceAllMarketTickersStreamRequest, BinanceAllMarketTickersStreamResponse> {

	BinancePublicWebsocketEndpoint(SubscriptionOptions options) {
		super(options);
	}

}
