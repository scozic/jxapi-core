package com.scz.jcex.binance.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jcex.binance.gen.spottrading.deserializers.BinanceAccountResponseDeserializer;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceAccountRequest;
import com.scz.jcex.binance.gen.spottrading.pojo.BinanceAccountResponse;
import com.scz.jcex.binance.net.BinancePrivateApiRestEndpointFactory;
import com.scz.jcex.netutils.rest.RestEndpoint;
import com.scz.jcex.netutils.rest.RestRequest;
import com.scz.jcex.util.TestApiProperties;

public class BinanceSpotAccountDemo {
	
	private static final Logger log = LoggerFactory.getLogger(BinanceSpotAccountDemo.class);

	public static void main(String[] args) {
		try {
			BinancePrivateApiRestEndpointFactory fac = new BinancePrivateApiRestEndpointFactory();
			fac.setProperties(TestApiProperties.filterProperties("binance", true));
			RestEndpoint<BinanceAccountRequest, BinanceAccountResponse> endpoint = fac.createRestEndpoint(new BinanceAccountResponseDeserializer());
			log.info("Sending request...");
			BinanceAccountRequest request = new BinanceAccountRequest();
			request.setRecvWindow(60000L);
			request.setTimestamp(System.currentTimeMillis());
			RestRequest<BinanceAccountRequest> restRequest = new RestRequest<>();
			restRequest.setHttpMethod("GET");
			restRequest.setRequest(request);
			restRequest.setUrl("https://api.binance.com/api/v3/account");
			BinanceAccountResponse response = endpoint.call(restRequest);
			log.info("Response received:" + response);
		} catch (Throwable t) {
			t.printStackTrace(System.out);
		}
	}
}
