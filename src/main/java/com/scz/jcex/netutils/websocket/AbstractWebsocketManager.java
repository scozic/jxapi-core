package com.scz.jcex.netutils.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public abstract class AbstractWebsocketManager implements WebsocketManager {
	
	private static final Logger log = LoggerFactory.getLogger(AbstractWebsocketManager.class);
	
	private final List<WebsocketErrorHandler> errorHandlers = new ArrayList<>();
	
	private final JsonFactory jsonFactory = new JsonFactory();
	
	protected final Map<String, TopicManager> topics = new HashMap<>();
	
	protected final List<TopicManager> systemMessageHandlers = new ArrayList<>();
	
	protected final AtomicBoolean connected = new AtomicBoolean(false);

	@Override
	public void subscribe(String topic, 
						  WebsocketMessageTopicMatcher matcher,
						  RawWebsocketMessageHandler messageHandler) {
		TopicManager t = topics.get(topic);
		if (t == null) {
			t = new TopicManager(topic, matcher, messageHandler, false);
			topics.put(topic, t);
		} else {
			throw new IllegalArgumentException("Already have a subscription for topic [" + topic + "]");
		}
	}
	
	@Override
	public boolean unsubscribe(String topic) {
		TopicManager t = topics.remove(topic);
		if (t != null) {
			try {
				send(getSubscribeRequestMessage(topic));
			} catch (IOException ex) {
				log.error("Error raised while unsubscribing from topic:" + topic);
			}
			
			return true;
		}
		return false;
	}
	
	@Override
	public final void connect() throws IOException {
		if (isConnected()) {
			return;
		}
		doConnect();
		connected.set(true);
	}
	
	public final void disconnect() throws IOException {
		if (!isConnected()) {
			return;
		}
		doDisconnect();
		connected.set(false);
	}
	
	public boolean isConnected() {
		return this.connected.get();
	}

	public boolean removeHandler(String topic) {
		return topics.remove(topic) != null;
	}

	@Override
	public void subscribeErrorHandler(WebsocketErrorHandler websocketErrorHandler) {
		errorHandlers.add(websocketErrorHandler);
	}

	@Override
	public boolean unsubscribeErrorHandler(WebsocketErrorHandler websocketErrorHandler) {
		return errorHandlers.remove(websocketErrorHandler);
	}
	
	@Override
	public void addSystemMessageHandler(String topic, WebsocketMessageTopicMatcher matcher, RawWebsocketMessageHandler messageHandler) {
		this.systemMessageHandlers.add(new TopicManager(topic, matcher, messageHandler, true));
	}
	
	protected void dispatchWebsocketError(IOException error) {
		errorHandlers.forEach(h -> h.handleWebsocketError(error));
	}
	
	protected void dispatchMessage(String message) {
		List<TopicManager> allTopics = new ArrayList<>(topics.size() + systemMessageHandlers.size());
		allTopics.addAll(systemMessageHandlers);
		allTopics.addAll(topics.values());
		allTopics.forEach(t -> t.matcher.reset());
		systemMessageHandlers.forEach(h -> h.matcher.reset());
		try {
			JsonParser jsonParser = jsonFactory.createParser(message.getBytes());
			for (JsonToken tok = jsonParser.nextToken(); tok != null && !allTopics.isEmpty(); tok = jsonParser.nextToken()) {
				if (tok == JsonToken.FIELD_NAME) {
					String fieldName = jsonParser.currentName();
					String value = getNextValue(jsonParser);
					if (dispatchToMessageTopicMatchers(fieldName, value, allTopics, message)) {
						break;
					}
				}
			}
 			
		} catch (IOException e) {
			log.error("Error parsing websocket message [" + message + "]", e);
		} 
	}
	
	private String getNextValue(JsonParser jsonParser) throws IOException {
		switch (jsonParser.nextToken()) {
		case FIELD_NAME:
			throw new IOException("Unexpected FIELD_NAME token type:" + jsonParser.currentName());
		case VALUE_FALSE:
			return Boolean.FALSE.toString();
		case VALUE_NUMBER_FLOAT:
		case VALUE_NUMBER_INT:
		case VALUE_STRING:
			return jsonParser.getText();
		case VALUE_TRUE:
			return Boolean.TRUE.toString();
		default:
			return null;
		}
	}
	
	protected abstract void doConnect() throws IOException;
	protected abstract void doDisconnect() throws IOException;
	protected abstract String getSubscribeRequestMessage(String topic);
	protected abstract String getUnSubscribeRequestMessage(String topic);
	
	private boolean dispatchToMessageTopicMatchers(String name, 
												String value,  
												List<TopicManager> messageHandlers, 
												String rawMessage) {
		for (Iterator<TopicManager> it = messageHandlers.iterator(); it.hasNext();) {
			TopicManager topic = it.next();
			WebsocketMessageTopicMatchStatus matchResult = topic.matcher.matches(name, value);
			switch (matchResult) {
			case MATCHED:
				if (log.isDebugEnabled())
					log.debug("Dispatching message to handler for topic:["+ topic.topic + "  :[" + rawMessage + "]");
				topic.messageHandler.handleWebsocketMessage(rawMessage);
				if (topic.systemMessage) {
					// System message handler matched that message. No need to keep processing to find other handlers.
					return true;
				}
				// Fallback
			case CANT_MATCH:
				it.remove();
				break;
			case NO_MATCH:
				break;
			default:
				throw new IllegalArgumentException("Unexpected WebsocketMessageTopicMatchStatus:" + matchResult);
			}
		}
		return false;
	}
	
	protected class TopicManager {
		final WebsocketMessageTopicMatcher matcher;
		final RawWebsocketMessageHandler messageHandler;
		final String topic;
		final boolean systemMessage;
		
		public TopicManager(String topic, WebsocketMessageTopicMatcher matcher,
				RawWebsocketMessageHandler messageHandler, boolean systemMessage) {
			this.topic = topic;
			this.matcher = matcher;
			this.messageHandler = messageHandler;
			this.systemMessage = systemMessage;
		}
	}
	
//	protected class MessageHandler {
//		final WebsocketMessageTopicMatcher matcher;
//		final RawWebsocketMessageHandler messageHandler;
//		
//		public MessageHandler(WebsocketMessageTopicMatcher matcher, RawWebsocketMessageHandler messageHandler) {
//			this.matcher = matcher;
//			this.messageHandler = messageHandler;
//		}
//	}

	protected abstract void send(String message) throws IOException;
}
