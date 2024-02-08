package com.scz.jxapi.netutils.websocket;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.netutils.deserialization.MessageDeserializer;

public class DefaultWebsocketEndpoint<T, M> implements WebsocketEndpoint<T, M> {
	
	private final Logger log = LoggerFactory.getLogger(DefaultWebsocketEndpoint.class);
	
	protected final WebsocketManager websocketManager;
	
	protected final MessageDeserializer<M> messageDeserializer;
	protected final Map<String, Subscription> subscriptionsByTopic  = new HashMap<>();
	protected final Map<String, Subscription> subscriptionsById  = new HashMap<>();
	
	private int subscriptionCounter;

	public DefaultWebsocketEndpoint(WebsocketManager websocketManager, MessageDeserializer<M> messageDeserializer) {
		this.messageDeserializer = messageDeserializer;
		this.websocketManager = websocketManager;
	}

	@Override
	public String subscribe(WebsocketSubscribeRequest<T> request, WebsocketListener<M> listener) {
		String topic = request.getTopic();
		Subscription sub = subscriptionsByTopic.get(topic);
		if (sub == null ) {
			sub = new Subscription(request);
			subscriptionsByTopic.put(topic, sub);
		}
		String subId = generateSubscriptionId(request);
		sub.addListener(subId, listener);
		subscriptionsById.put(subId, sub);
		return subId;
	}
	
	protected String generateSubscriptionId(WebsocketSubscribeRequest<T> request) {
		return String.valueOf(Integer.toHexString(request.hashCode()) + "_" + subscriptionCounter++);
	}

	@Override
	public boolean unsubscribe(String unsubscriptionId) {
		Subscription sub = subscriptionsById.get(unsubscriptionId);
		if (sub == null) {
			return false;
		}
		return sub.removeListener(unsubscriptionId);
	}
	
	private class Subscription {
		final WebsocketSubscribeRequest<T> request;
		final Map<String , WebsocketListener<M>> listeners = new HashMap<>();
		
		public Subscription(WebsocketSubscribeRequest<T> request) {
			this.request = request;
		}
		
		public void addListener(String subscriptionId, WebsocketListener<M> listener) {
			listeners.put(subscriptionId, listener);
			if (listeners.size() == 1) {
				// First subscription
				websocketManager.subscribe(request.getTopic(), request.getMessageTopicMatcher(), message -> dispatch(message));
			}
		}
		
		public boolean removeListener(String subscriptionId) {
			if (listeners.remove(subscriptionId) != null) {
				if (listeners.size() <= 0) {
					websocketManager.unsubscribe(request.getTopic());
				}
				return true;
			}
			return false;
		}
		
		private void dispatch(String message) {
			try {
				if (!listeners.isEmpty()) {
					M msg = messageDeserializer.deserialize(message);
					listeners.values().forEach(l -> l.handleMessage(msg));
				}
			} catch (Exception ex) {
				log.error("Error while dispatching message [" + message + "]", ex);
			}
			
		}
	}

}
