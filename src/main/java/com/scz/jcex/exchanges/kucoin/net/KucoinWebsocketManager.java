package com.scz.jcex.exchanges.kucoin.net;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jcex.netutils.websocket.DefaultWebsocketMessageTopicMatcher;
import com.scz.jcex.netutils.websocket.spring.SpringWebsocketManager;

public class KucoinWebsocketManager extends SpringWebsocketManager {
	
	private static final Logger log = LoggerFactory.getLogger(KucoinWebsocketManager.class);
	

	private static final String HEARTBEAT_REQUEST_TEMPLATE = "{\"id\":\"%d\", \"type\":\"ping\"}";
	private static final String SUBSCRIBE_REQUEST_TEMPLATE = "{\"id\": %d, \"type\": \"subscribe\", \"topic\": \"%s\", \"privateChannel\": %s, \"response\": false}";
	private static final String UNSUBSCRIBE_REQUEST_TEMPLATE = "{\"id\": %d, \"type\": \"unsubscribe\", \"topic\": \"%s\", \"privateChannel\": false, \"response\": false}";
	
	private int requestIdCounter = RandomUtils.nextInt();
	private final KucoinWebsocketListenKeyApi listenKeyApi;
	private final String privateChannel;
	
	/** 
	 * @param baseUrl Used for logging purpose (naming thread executors), actual endpoint url used will be the one provided in return of {@link KucoinWebsocketListenKeyApi#requestToken()}
	 * @param listenKeyApi
	 */
	public KucoinWebsocketManager(String baseUrl, KucoinWebsocketListenKeyApi listenKeyApi, boolean privateChannel) {
		super(baseUrl);
		this.listenKeyApi = listenKeyApi;
		this.addSystemMessageHandler("ping", DefaultWebsocketMessageTopicMatcher.create("type", "pong"), m -> handlePongMessage(m));
		this.privateChannel = "" + privateChannel;
	}

	private void handlePongMessage(String m) {
		if (log.isDebugEnabled())
			log.debug("Received 'pong' message:" + m);
		this.lastHeartBeatTime.set(System.currentTimeMillis());
	}
	
	private int connectionIdCounter = 1;
	private int connectionId = 1;
	
	@Override
	protected String createHeartBeatMessage() {
		return String.format(HEARTBEAT_REQUEST_TEMPLATE, generateRequestId());
	}

	@Override
	protected String getSubscribeRequestMessage(String topic) {
		return String.format(SUBSCRIBE_REQUEST_TEMPLATE, generateRequestId(), topic, privateChannel) ;
	}

	@Override
	protected String getUnSubscribeRequestMessage(String topic) {
		return String.format(UNSUBSCRIBE_REQUEST_TEMPLATE, generateRequestId(), topic) ;
	}
	
	private int generateRequestId() {
		return Math.abs(requestIdCounter++);
	}
	
	private int generateConnectionId() {
		return Math.abs(connectionIdCounter++);
	}
	
	@Override
	protected URI getHandShakeURI() throws IOException {
		try {
			KucoinWebsocketTokenInfo tokenInfo = listenKeyApi.requestToken();
			this.connectionId = generateConnectionId();
			this.setHeartBeatInterval(tokenInfo.getPingInterval());
			this.setNoHeartBeatResponseTimeout(tokenInfo.getPingTimeout());
			return new URI(tokenInfo.getEndpoint() + String.format("?token=%s&connectId=%d", tokenInfo.getToken(), this.connectionId));
		} catch (URISyntaxException e) {
			throw new IOException("Error creating URI for websocket base URL:" + baseUrl);
		}
	}
}
