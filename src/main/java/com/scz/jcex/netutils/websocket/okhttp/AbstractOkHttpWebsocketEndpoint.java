package com.scz.jcex.netutils.websocket.okhttp;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import com.scz.jcex.generator.exchange.ExchangeApiDescriptor;
import com.scz.jcex.netutils.websocket.WebsocketEndpoint;
import com.scz.jcex.netutils.websocket.WebsocketListener;
import com.scz.jcex.netutils.websocket.WebsocketMessageDeserializer;
import com.scz.jcex.netutils.websocket.WebsocketSubscribeRequest;

public class AbstractOkHttpWebsocketEndpoint<T, M> implements WebsocketEndpoint<T, M> {
	
	protected final AtomicInteger subscriptionIdGenerator = new AtomicInteger(0);
	
	private final SubscriptionOptions options;
    private WebsocketWatchDog watchDog;

    private final Map<String, OkHttpWebsocketConnection> connections = new HashMap<>();
	private WebsocketMessageDeserializer<M> websocketMessageDeserializer;
	protected ExchangeApiDescriptor exchangeApi;
	 

    AbstractOkHttpWebsocketEndpoint(SubscriptionOptions options) {
        this.watchDog = null;
        this.options = Objects.requireNonNull(options);
    }

	@Override
	public String subscribe(WebsocketSubscribeRequest<T> request, WebsocketListener<M> listener) {
		if (websocketMessageDeserializer == null) {
			throw new IllegalStateException("No WebsocketMessageDeserializer set, cannot handle request:" + request);
		}
		if (watchDog == null) {
            watchDog = new WebsocketWatchDog(options);
        }
		OkHttpWebsocketConnection connection = new OkHttpWebsocketConnection(request, s -> listener.handleMessage(websocketMessageDeserializer.deserialize(s)), watchDog);
		String subscriptionId = generateSubscriptionId();
		connections.put(subscriptionId, connection);
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
