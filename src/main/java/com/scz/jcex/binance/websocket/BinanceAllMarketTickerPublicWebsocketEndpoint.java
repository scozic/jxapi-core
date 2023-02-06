package com.scz.jcex.binance.websocket;

import com.scz.jcex.binance.spotmarketdata.deserializers.BinanceAllMarketTickersStreamResponseDeserializer;
import com.scz.jcex.binance.spotmarketdata.pojo.BinanceAllMarketTickersStreamRequest;
import com.scz.jcex.binance.spotmarketdata.pojo.BinanceAllMarketTickersStreamResponse;
import com.scz.jcex.netutils.websocket.DefaultJsonMessageDeserializer;
import com.scz.jcex.netutils.websocket.WebsocketListener;
import com.scz.jcex.netutils.websocket.WebsocketSubscribeRequest;
import com.scz.jcex.netutils.websocket.okhttp.AbstractOkHttpWebsocketEndpoint;

public class BinanceAllMarketTickerPublicWebsocketEndpoint extends AbstractOkHttpWebsocketEndpoint<BinanceAllMarketTickersStreamRequest, BinanceAllMarketTickersStreamResponse> {

	public static final String BINANCE_PUBLIC_WS_BASE_URL = "wss://data-stream.binance.com/ws/";
	
	public BinanceAllMarketTickerPublicWebsocketEndpoint() {
		super();
		setWebsocketMessageDeserializer(new DefaultJsonMessageDeserializer<BinanceAllMarketTickersStreamResponse>(BinanceAllMarketTickersStreamResponse.class));
	}
	
	public void subscribe(BinanceAllMarketTickersStreamRequest request, WebsocketListener<BinanceAllMarketTickersStreamResponse> listener) {
		WebsocketSubscribeRequest<BinanceAllMarketTickersStreamRequest> wsRequest = new WebsocketSubscribeRequest<>();
		wsRequest.setParameters(request);
		wsRequest.setBaseUrl(BINANCE_PUBLIC_WS_BASE_URL);
		this.setWebsocketMessageDeserializer(new BinanceAllMarketTickersStreamResponseDeserializer());
		this.subscribe(wsRequest, listener);
	}

}
