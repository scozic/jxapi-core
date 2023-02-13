package com.scz.jcex.binance.websocket;

import java.util.List;

import com.scz.jcex.binance.gen.spotmarketdata.deserializers.BinanceAllMarketTickersStreamResponseDeserializer;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceAllMarketTickersStreamRequest;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceAllMarketTickersStreamResponse;
import com.scz.jcex.netutils.deserialization.json.field.StructListFieldDeserializer;
import com.scz.jcex.netutils.websocket.WebsocketListener;
import com.scz.jcex.netutils.websocket.WebsocketSubscribeRequest;
import com.scz.jcex.netutils.websocket.okhttp.AbstractOkHttpWebsocketEndpoint;

public class BinanceAllMarketTickerPublicWebsocketEndpoint extends AbstractOkHttpWebsocketEndpoint<BinanceAllMarketTickersStreamRequest, List<BinanceAllMarketTickersStreamResponse>> {

	public static final String BINANCE_PUBLIC_WS_BASE_URL = "wss://data-stream.binance.com/ws/";
	
	public BinanceAllMarketTickerPublicWebsocketEndpoint() {
		super();
	}
	
	public void subscribe(BinanceAllMarketTickersStreamRequest request, WebsocketListener<List<BinanceAllMarketTickersStreamResponse>> listener) {
		WebsocketSubscribeRequest<BinanceAllMarketTickersStreamRequest> wsRequest = new WebsocketSubscribeRequest<>();
		wsRequest.setParameters(request);
		wsRequest.setBaseUrl(BINANCE_PUBLIC_WS_BASE_URL);
		this.setWebsocketMessageDeserializer(new StructListFieldDeserializer<BinanceAllMarketTickersStreamResponse>(new BinanceAllMarketTickersStreamResponseDeserializer()));
		this.subscribe(wsRequest, listener);
	}

}
