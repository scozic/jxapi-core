package com.scz.jcex.binance.exchange.spot;

import java.io.IOException;

import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationRequest;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponse;

public interface BinanceSpotApi {

	BinanceExchangeInformationResponse exchangeInformation(BinanceExchangeInformationRequest request) throws IOException;
}
