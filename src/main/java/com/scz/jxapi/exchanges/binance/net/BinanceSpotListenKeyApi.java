package com.scz.jxapi.exchanges.binance.net;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.exchanges.binance.gen.spottrading.deserializers.BinanceSpotDeleteListenKeyResponseDeserializer;
import com.scz.jxapi.exchanges.binance.gen.spottrading.deserializers.BinanceSpotKeepAliveListenKeyResponseDeserializer;
import com.scz.jxapi.exchanges.binance.gen.spottrading.deserializers.BinanceSpotListenKeyResponseDeserializer;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceSpotDeleteListenKeyRequest;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceSpotDeleteListenKeyResponse;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceSpotKeepAliveListenKeyRequest;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceSpotKeepAliveListenKeyResponse;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceSpotListenKeyRequest;
import com.scz.jxapi.exchanges.binance.gen.spottrading.pojo.BinanceSpotListenKeyResponse;
import com.scz.jxapi.netutils.rest.RestEndpoint;
import com.scz.jxapi.netutils.rest.RestRequest;

public class BinanceSpotListenKeyApi implements BinanceListenKeyApi {
	
	private static final String USER_DATA_STREAM_URL = "https://api.binance.com/api/v3/userDataStream";

	private static final Logger log = LoggerFactory.getLogger(BinanceSpotListenKeyApi.class);
	
	private static final BinanceSpotListenKeyRequest GET_REQUEST = new BinanceSpotListenKeyRequest();

	private final RestEndpoint<BinanceSpotListenKeyRequest, BinanceSpotListenKeyResponse> createEndpoint;
	private final RestEndpoint<BinanceSpotKeepAliveListenKeyRequest, BinanceSpotKeepAliveListenKeyResponse> keepAliveEndpoint;
	private final RestEndpoint<BinanceSpotDeleteListenKeyRequest, BinanceSpotDeleteListenKeyResponse> deleteEndpoint;
	
	public BinanceSpotListenKeyApi(Properties connectionProperties) {
		BinancePrivateApiRestEndpointFactory fac = new BinancePrivateApiRestEndpointFactory();
		fac.setProperties(connectionProperties);
		createEndpoint = fac.createRestEndpoint(new BinanceSpotListenKeyResponseDeserializer());
		keepAliveEndpoint = fac.createRestEndpoint(new BinanceSpotKeepAliveListenKeyResponseDeserializer());
		deleteEndpoint = fac.createRestEndpoint(new BinanceSpotDeleteListenKeyResponseDeserializer());
	}

	@Override
	public String getListenKey() throws IOException {
		if (log.isDebugEnabled())
		      log.debug("POST spotListenKey > " + GET_REQUEST);
		    BinanceSpotListenKeyResponse response = createEndpoint.call(RestRequest.create(USER_DATA_STREAM_URL, "POST", GET_REQUEST));
		    if (log.isDebugEnabled())
		      log.debug("POST spotListenKey < " + response);
		    return response.getListenKey();
	}

	@Override
	public void keepAliveListenKey(String listenKey) throws IOException {
		BinanceSpotKeepAliveListenKeyRequest request = new BinanceSpotKeepAliveListenKeyRequest();
		request.setListenKey(listenKey);
	    if (log.isDebugEnabled())
		   log.debug("PUT spotKeepAliveListenKey > " + request);
		BinanceSpotKeepAliveListenKeyResponse response = keepAliveEndpoint.call(RestRequest.create(USER_DATA_STREAM_URL, "PUT", request));
		if (log.isDebugEnabled())
		    log.debug("PUT spotKeepAliveListenKey < " + response);
	}

	@Override
	public void deleteListenKey(String listenKey) throws IOException {
		BinanceSpotDeleteListenKeyRequest request = new BinanceSpotDeleteListenKeyRequest();
		request.setListenKey(listenKey);
		 if (log.isDebugEnabled())
		    log.debug("DELETE spotDeleteListenKey > " + request);
		 BinanceSpotDeleteListenKeyResponse response = deleteEndpoint.call(RestRequest.create(USER_DATA_STREAM_URL, "DELETE", request));
		 if (log.isDebugEnabled())
		    log.debug("DELETE spotDeleteListenKey < " + response);
	}
}
