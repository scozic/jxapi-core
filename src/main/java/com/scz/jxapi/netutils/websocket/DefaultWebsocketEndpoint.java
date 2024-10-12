package com.scz.jxapi.netutils.websocket;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.exchange.ExchangeApiEvent;
import com.scz.jxapi.exchange.ExchangeApiObserver;
import com.scz.jxapi.netutils.deserialization.MessageDeserializer;

/**
 * Default implementation of a {@link WebsocketEndpoint}.
 * <p>
 * This class manages the subscriptions to a websocket topic, using a
 * {@link WebsocketManager} to wrap {@link Websocket} and subscribe/unsubscribe
 * to topic, and a {@link MessageDeserializer} to deserialize incoming messages.
 * 
 * @param <M> the type of messages that this endpoint will handle.
 * @see WebsocketEndpoint
 */
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
	}

	@Override
	public String subscribe(WebsocketSubscribeRequest request, WebsocketListener<M> listener) {
		request.setEnpoint(endpointName);
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
		Subscription sub = subscriptionsById.remove(unsubscriptionId);
		if (sub == null) {
			return false;
		}
		if (observer != null) {
			dispatchApiEvent(ExchangeApiEvent.createWebsocketUnsubscribeEvent(sub.request, unsubscriptionId));
		}
		sub.removeListener(unsubscriptionId);
		if (sub.listeners.size() <= 0) {
			subscriptionsByTopic.remove(sub.request.getTopic());
		}
		return true;
	}

	@Override
	public String getEndpointName() {
		return endpointName;
	}
	
	protected String generateSubscriptionId(WebsocketSubscribeRequest request) {
		return String.valueOf(request.getTopic() + "-" + subscriptionCounter++);
	}
	
	protected void dispatchApiEvent(ExchangeApiEvent event) {
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
										   request.getMessageTopicMatcherFactory(), 
										   message -> dispatch(message));
			}
		}
		
		public void removeListener(String subscriptionId) {
			listeners.remove(subscriptionId);
			if (listeners.size() <= 0) {
				websocketManager.unsubscribe(request.getTopic());
			}
		}
		
		private void dispatch(String message) {
			try {
				if (!listeners.isEmpty()) {
					M msg = messageDeserializer.deserialize(message);
					listeners.values().forEach(l -> l.handleMessage(msg));
					if (observer != null) {
						dispatchApiEvent(ExchangeApiEvent.createWebsocketMessageEvent(request, message));
					}
				}
			} catch (Exception ex) {
				String errMsg = "Error while dispatching message [" + message + "]"; 
				log.error(errMsg, ex);
				dispatchApiEvent(ExchangeApiEvent.createWebsocketErrorEvent(new WebsocketException(errMsg, ex)));
			}
			
		}
	}

}
