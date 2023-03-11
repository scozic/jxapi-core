package com.scz.jcex.binance.net;

import java.io.IOException;

import com.scz.jcex.binance.gen.spottrading.BinanceSpotTradingApi;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceSpotDeleteListenKeyRequest;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceSpotKeepAliveListenKeyRequest;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceSpotListenKeyRequest;

public class BinanceSpotListenKeyApi implements BinanceListenKeyApi {
	
	private final BinanceSpotTradingApi api;

	public BinanceSpotListenKeyApi(BinanceSpotTradingApi api) {
		this.api = api;
	}

	@Override
	public String getListenKey() throws IOException {
		return api.spotListenKey(new BinanceSpotListenKeyRequest()).getListenKey();
	}

	@Override
	public void keepAliveListenKey(String listenKey) throws IOException {
		BinanceSpotKeepAliveListenKeyRequest request = new BinanceSpotKeepAliveListenKeyRequest();
		request.setListenKey(listenKey);
		api.spotKeepAliveListenKey(request);
		
	}

	@Override
	public void deleteListenKey(String listenKey) throws IOException {
		BinanceSpotDeleteListenKeyRequest request = new BinanceSpotDeleteListenKeyRequest();
		request.setListenKey(listenKey);
		api.spotDeleteListenKey(request);
	}

}
