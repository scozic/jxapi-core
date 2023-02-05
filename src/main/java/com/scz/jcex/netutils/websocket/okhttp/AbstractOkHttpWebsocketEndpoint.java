package com.scz.jcex.netutils.websocket.okhttp;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.scz.jcex.generator.exchange.ExchangeApiDescriptor;
import com.scz.jcex.netutils.websocket.DefaultJsonWebsocketMessageDeserializer;
import com.scz.jcex.netutils.websocket.WebsocketEndpoint;
import com.scz.jcex.netutils.websocket.WebsocketListener;
import com.scz.jcex.netutils.websocket.WebsocketMessageDeserializer;
import com.scz.jcex.netutils.websocket.WebsocketSubscribeParameters;
import com.scz.jcex.netutils.websocket.WebsocketSubscribeRequest;

public class AbstractOkHttpWebsocketEndpoint<T extends WebsocketSubscribeParameters, M> implements WebsocketEndpoint<T, M> {
	
	protected final AtomicInteger subscriptionIdGenerator = new AtomicInteger(0);
	
    private WebsocketWatchDog watchDog;

    private final Map<String, OkHttpWebsocketConnection> connections = new HashMap<>();
	private WebsocketMessageDeserializer<M> websocketMessageDeserializer = new DefaultJsonWebsocketMessageDeserializer<>(null);
	protected ExchangeApiDescriptor exchangeApi;

	private boolean reconnectOnFailure = true;

	private int delayBeforeReconnectInSeconds = 3;

	@Override
	public String subscribe(WebsocketSubscribeRequest<T> request, WebsocketListener<M> listener) {
		if (websocketMessageDeserializer == null) {
			throw new IllegalStateException("No WebsocketMessageDeserializer set, cannot handle request:" + request);
		}
		if (watchDog == null) {
            watchDog = new WebsocketWatchDog(isReconnectOnFailure(), getDelayBeforeReconnectInSeconds());
        }
		OkHttpWebsocketConnection connection = new OkHttpWebsocketConnection(request, s -> listener.handleMessage(websocketMessageDeserializer.deserialize(s)), watchDog);
		String subscriptionId = generateSubscriptionId();
		connections.put(subscriptionId, connection);
		connection.connect();
		return subscriptionId;
	}

	@Override
	public void init(ExchangeApiDescriptor exchangeApi) {
		this.exchangeApi = exchangeApi;
	}
	
	protected String generateSubscriptionId() {
		return "websocketSubscription#" + subscriptionIdGenerator.getAndIncrement();
	}

	public WebsocketMessageDeserializer<M> getWebsocketMessageDeserializer() {
		return websocketMessageDeserializer;
	}

	public void setWebsocketMessageDeserializer(WebsocketMessageDeserializer<M> websocketMessageDeserializer) {
		this.websocketMessageDeserializer = websocketMessageDeserializer;
	}
	
	public boolean isReconnectOnFailure() {
		return reconnectOnFailure;
	}


	public void setReconnectOnFailure(boolean reconnectOnFailure) {
		this.reconnectOnFailure = reconnectOnFailure;
	}


	public int getDelayBeforeReconnectInSeconds() {
		return delayBeforeReconnectInSeconds;
	}


	public void setDelayBeforeReconnectInSeconds(int delayBeforeReconnectInSeconds) {
		this.delayBeforeReconnectInSeconds = delayBeforeReconnectInSeconds;
	}

	@Override
	public boolean unsubscribe(String unsubscriptionId) {
		OkHttpWebsocketConnection connection = connections.get(unsubscriptionId);
		if (connection != null) {
			connection.close();
			return true;
		}
		return false;
	}

}
