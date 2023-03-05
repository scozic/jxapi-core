package com.scz.jcex.binance.exchange.spot;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jcex.binance.gen.spotmarketdata.deserializers.BinanceExchangeInformationResponseDeserializer;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationRequest;
import com.scz.jcex.binance.gen.spotmarketdata.pojo.BinanceExchangeInformationResponse;
import com.scz.jcex.binance.net.BinancePublicApiRestEndpointFactory;
import com.scz.jcex.netutils.rest.RestEndpoint;
import com.scz.jcex.netutils.rest.RestEndpointFactory;
import com.scz.jcex.netutils.rest.RestRequest;

/**
 * Implementation of {@link BinanceSpotApi}
 */
public class BinanceSpotApiImpl implements BinanceSpotApi {
	
	private static final Logger log = LoggerFactory.getLogger(BinanceSpotApiImpl.class);
	
	public static final String BINANCE_SPOT_API_BASE_URL = "https://data.binance.com/api/v3/";

	private final RestEndpointFactory restEndpointFactory = new BinancePublicApiRestEndpointFactory();
	
	RestEndpoint<BinanceExchangeInformationRequest, BinanceExchangeInformationResponse> binanceExchangeInformationApi = restEndpointFactory.createRestEndpoint(new BinanceExchangeInformationResponseDeserializer());
	
//	private final WebsocketEndpointFactory publicWebsocketEndpointFactory = new BinancePublicWebsocketEndpointFactory();
	
//	WebsocketEndpoint<BinanceAllMarketTickersStreamRequest, BinanceAllMarketTickersStreamResponse> allMarketTickersStreamWebsocketEndpoint = publicWebsocketEndpointFactory.createWebsocketEndpoint(new BinanceAllMarketTickersStreamResponseDeserializer());

	@Override
	public BinanceExchangeInformationResponse exchangeInformation(BinanceExchangeInformationRequest request) throws IOException {
		if (log.isDebugEnabled())
			log.debug("GET exchangeInformation > " + request);
		BinanceExchangeInformationResponse response = binanceExchangeInformationApi.call(RestRequest.create(BINANCE_SPOT_API_BASE_URL + "exchangeInfo", "GET", request));
		if (log.isDebugEnabled())
			log.debug("GET exchangeInformation < " + response);
		return response;
	}
	
//	@Override
//	public String subscribeAllMarketsTicker(BinanceAllMarketTickersStreamRequest request, WebsocketListener<BinanceAllMarketTickersStreamResponse> listener) throws IOException {
//		WebsocketSubscribeRequest<BinanceAllMarketTickersStreamRequest> websocketSubscribeRequest = new WebsocketSubscribeRequest<>();
//		websocketSubscribeRequest.setBaseUrl(BINANCE_SPOT_API_BASE_URL);
//		websocketSubscribeRequest.setMessageTopicMatcher(new DefaultWebsocketMessageTopicMatcher(WebsocketMessageTopicMatcherField.createList("e", "24hrTicker")));
//		websocketSubscribeRequest.setParameters(request);
//		return allMarketTickersStreamWebsocketEndpoint.subscribe(websocketSubscribeRequest, listener);
//	}
//
//	@Override
//	public boolean unsubscribeAllMarketsTicker(String subscriptionId) {
//		return allMarketTickersStreamWebsocketEndpoint.unsubscribe(subscriptionId);
//		
//	}
}
