package com.scz.jcex.binance.exchange.spot;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jcex.binance.net.BinancePublicApiRestEndpointFactory;
import com.scz.jcex.binance.spotmarketdata.deserializers.BinanceExchangeInformationResponseDeserializer;
import com.scz.jcex.binance.spotmarketdata.pojo.BinanceExchangeInformationRequest;
import com.scz.jcex.binance.spotmarketdata.pojo.BinanceExchangeInformationResponse;
import com.scz.jcex.netutils.rest.RestEndpoint;
import com.scz.jcex.netutils.rest.RestEndpointFactory;
import com.scz.jcex.netutils.rest.RestRequest;

public class BinanceSpotApiImpl implements BinanceSpotApi {
	
	private static final Logger log = LoggerFactory.getLogger(BinanceSpotApiImpl.class);
	
	public static final String BINANCE_SPOT_API_BASE_URL = "https://data.binance.com/api/v3/";

	private final RestEndpointFactory restEndpointFactory = new BinancePublicApiRestEndpointFactory();
	
	RestEndpoint<BinanceExchangeInformationRequest, BinanceExchangeInformationResponse> binanceExchangeInformationApi = restEndpointFactory.createRestEndpoint(new BinanceExchangeInformationResponseDeserializer());

	@Override
	public BinanceExchangeInformationResponse exchangeInformation(BinanceExchangeInformationRequest request) throws IOException {
		if (log.isDebugEnabled())
			log.debug("GET exchangeInformation > " + request);
		BinanceExchangeInformationResponse response = binanceExchangeInformationApi.call(RestRequest.create(BINANCE_SPOT_API_BASE_URL + "exchangeInfo", "GET", request));
		if (log.isDebugEnabled())
			log.debug("GET exchangeInformation < " + response);
		return response;
	}
}
