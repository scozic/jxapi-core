package com.scz.jcex.exchanges.kucoin.net;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.deserializers.KucoinApplyConnectTokenPublicResponseDeserializer;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicRequest;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicResponse;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicResponseData;
import com.scz.jcex.exchanges.kucoin.gen.spotmarketdata.pojo.KucoinApplyConnectTokenPublicResponseDataInstanceServers;
import com.scz.jcex.netutils.rest.RestEndpoint;
import com.scz.jcex.netutils.rest.RestRequest;

public class KucoinPublicWebsocketListenKeyApi implements KucoinWebsocketListenKeyApi {

	private static final Logger log = LoggerFactory.getLogger(KucoinPublicWebsocketListenKeyApi.class);
	
	private static final KucoinApplyConnectTokenPublicRequest APPLY_TOKEN_REQUEST = new KucoinApplyConnectTokenPublicRequest();
	
	private final KucoinPublicApiRestEndpointFactory restEndpointFactory = new KucoinPublicApiRestEndpointFactory();
	private final RestEndpoint<KucoinApplyConnectTokenPublicRequest, KucoinApplyConnectTokenPublicResponse> applyConnectTokenPublicApi;
	
	public KucoinPublicWebsocketListenKeyApi(Properties properties) {
		restEndpointFactory.setProperties(properties);
		this.applyConnectTokenPublicApi = restEndpointFactory.createRestEndpoint(new KucoinApplyConnectTokenPublicResponseDeserializer());
	}
	
	@Override
	public KucoinWebsocketTokenInfo requestToken() throws IOException {
		if (log.isDebugEnabled())
			log.debug("GET ApplyConnectTokenPublic > " + APPLY_TOKEN_REQUEST);
		KucoinApplyConnectTokenPublicResponse response = applyConnectTokenPublicApi.call(RestRequest.create("https://api.kucoin.com/api/v1/bullet-public", "POST", APPLY_TOKEN_REQUEST));
		if (log.isDebugEnabled())
			log.debug("GET ApplyConnectTokenPublic < " + response);
		KucoinWebsocketTokenInfo info = new KucoinWebsocketTokenInfo();
		KucoinApplyConnectTokenPublicResponseData data = response.getData();
		info.setToken(data.getToken());
		if (data.getInstanceServers().size() <= 0)
			throw new IOException("Error in ApplyConnectTokenPublic response:0 instance servers provided:" + response);
		KucoinApplyConnectTokenPublicResponseDataInstanceServers serverInstance = data.getInstanceServers().get(0);
		info.setEndpoint(serverInstance.getEndpoint());
		info.setPingInterval(serverInstance.getPingInterval());
		info.setPingTimeout(serverInstance.getPingTimeout());
		return info;
	}

}
