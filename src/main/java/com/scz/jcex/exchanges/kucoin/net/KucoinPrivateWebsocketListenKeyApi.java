package com.scz.jcex.exchanges.kucoin.net;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jcex.exchanges.kucoin.gen.spottrading.deserializers.KucoinApplyConnectTokenPrivateResponseDeserializer;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateRequest;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateResponse;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateResponseData;
import com.scz.jcex.exchanges.kucoin.gen.spottrading.pojo.KucoinApplyConnectTokenPrivateResponseDataInstanceServers;
import com.scz.jcex.netutils.rest.RestEndpoint;
import com.scz.jcex.netutils.rest.RestRequest;

public class KucoinPrivateWebsocketListenKeyApi implements KucoinWebsocketListenKeyApi {

	private static final Logger log = LoggerFactory.getLogger(KucoinPrivateWebsocketListenKeyApi.class);
	
	private static final KucoinApplyConnectTokenPrivateRequest APPLY_TOKEN_REQUEST = new KucoinApplyConnectTokenPrivateRequest();
	
	private final KucoinOkHttpPrivateApiRestEndpointFactory restEndpointFactory = new KucoinOkHttpPrivateApiRestEndpointFactory();
	private final RestEndpoint<KucoinApplyConnectTokenPrivateRequest, KucoinApplyConnectTokenPrivateResponse> applyConnectTokenPrivateApi;
	
	public KucoinPrivateWebsocketListenKeyApi(Properties properties) {
		restEndpointFactory.setProperties(properties);
		this.applyConnectTokenPrivateApi = restEndpointFactory.createRestEndpoint(new KucoinApplyConnectTokenPrivateResponseDeserializer());
	}
	
	@Override
	public KucoinWebsocketTokenInfo requestToken() throws IOException {
		if (log.isDebugEnabled())
			log.debug("POST ApplyConnectTokenPrivate > " + APPLY_TOKEN_REQUEST);
		KucoinApplyConnectTokenPrivateResponse response = applyConnectTokenPrivateApi.call(RestRequest.create("https://api.kucoin.com/api/v1/bullet-public", "POST", APPLY_TOKEN_REQUEST));
		if (log.isDebugEnabled())
			log.debug("POST ApplyConnectTokenPrivate < " + response);
		KucoinWebsocketTokenInfo info = new KucoinWebsocketTokenInfo();
		KucoinApplyConnectTokenPrivateResponseData data = response.getData();
		info.setToken(data.getToken());
		if (data.getInstanceServers().size() <= 0)
			throw new IOException("Error in ApplyConnectTokenPrivate response:0 instance servers provided:" + response);
		KucoinApplyConnectTokenPrivateResponseDataInstanceServers serverInstance = data.getInstanceServers().get(0);
		info.setEndpoint(serverInstance.getEndpoint());
		info.setPingInterval(serverInstance.getPingInterval());
		info.setPingTimeout(serverInstance.getPingTimeout());
		return info;
	}

}
