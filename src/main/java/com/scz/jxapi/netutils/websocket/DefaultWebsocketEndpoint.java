package com.scz.jxapi.netutils.websocket;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.netutils.deserialization.MessageDeserializer;
import com.scz.jxapi.observability.ExchangeApiEvent;
import com.scz.jxapi.observability.ExchangeApiObserver;

public class DefaultWebsocketEndpoint<M> implements WebsocketEndpoint<M> {
	
	private final Logger log = LoggerFactory.getLogger(DefaultWebsocketEndpoint.class);
	
	protected final WebsocketManager websocketManager;
	
	protected final MessageDeserializer<M> messageDeserializer;
	protected final Map<String, Subscription> subscriptionsByTopic  = new HashMap<>();
	protected final Map<String, Subscription> subscriptionsById  = new HashMap<>();
	protected final String endpointName;
	protected final ExchangeApiObserver observer;
	
	private int subscriptionCounter = 0;

	public DefaultWebsocketEndpoint(String endpointName,  
									WebsocketManager websocketManager, 
									MessageDeserializer<M> messageDeserializer,
									ExchangeApiObserver observer) {
		this.endpointName = endpointName;
		this.messageDeserializer = messageDeserializer;
		this.websocketManager = websocketManager;
		this.observer = observer;
		if (observer != null) {
			websocketManager.subscribeErrorHandler(error -> dispatchApiEvent(ExchangeApiEvent.createWebsocketErrorEvent(error)));
		}
	}

	@Override
	public String subscribe(WebsocketSubscribeRequest request, WebsocketListener<M> listener) {
		String topic = request.getTopic();
		Subscription sub = subscriptionsByTopic.get(topic);
		if (sub == null ) {
			sub = new Subscription(request);
			subscriptionsByTopic.put(topic, sub);
		}
		String subId = generateSubscriptionId(request);
		sub.addListener(subId, listener);
		subscriptionsById.put(subId, sub);
		if (observer != null) {
			dispatchApiEvent(ExchangeApiEvent.createWebsocketSubscribeEvent(request, subId));
		}
		return subId;
	}

	@Override
	public boolean unsubscribe(String unsubscriptionId) {
		Subscription sub = subscriptionsById.get(unsubscriptionId);
		if (sub == null) {
			return false;
		}
		if (observer != null) {
			dispatchApiEvent(ExchangeApiEvent.createWebsocketUnsubscribeEvent(unsubscriptionId));
		}
		return sub.removeListener(unsubscriptionId);
	}

	@Override
	public String getEndpointName() {
		return endpointName;
	}
	
	protected String generateSubscriptionId(WebsocketSubscribeRequest request) {
		return String.valueOf(request.getTopic() + "-" + subscriptionCounter++);
	}
	
	protected void dispatchApiEvent(ExchangeApiEvent event) {
		event.setEndpointName(endpointName);
		this.observer.handleEvent(event);
	}
	
	private class Subscription {
		final WebsocketSubscribeRequest request;
		final Map<String , WebsocketListener<M>> listeners = new HashMap<>();
		
		public Subscription(WebsocketSubscribeRequest request) {
			this.request = request;
		}
		
		public void addListener(String subscriptionId, WebsocketListener<M> listener) {
			listeners.put(subscriptionId, listener);
			if (listeners.size() == 1) {
				// First subscription
				websocketManager.subscribe(request.getTopic(), 
										   request.getMessageTopicMatcher(), 
										   message -> dispatch(message));
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
					if (observer != null) {
						dispatchApiEvent(ExchangeApiEvent.createWebsocketMessageEvent(endpointName, message));
					}
				}
			} catch (Exception ex) {
				log.error("Error while dispatching message [" + message + "]", ex);
			}
			
		}
	}

}
