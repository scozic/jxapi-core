package com.scz.jcex.netutils.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public abstract class AbstractWebsocketManager implements WebsocketManager {
	
	private static final Logger log = LoggerFactory.getLogger(AbstractWebsocketManager.class);
	
	private final List<WebsocketErrorHandler> errorHandlers = new ArrayList<>();
	
	private final JsonFactory jsonFactory = new JsonFactory();
	
	private final Map<String, TopicManager> topics = new HashMap<>();

	@Override
	public void dispose() {
		topics.clear();
		errorHandlers.clear();
	}

	@Override
	public void registerHandler(String topic, WebsocketMessageTopicMatcher matcher,
			RawWebsocketMessageHandler messageHandler) {
		TopicManager t = topics.get(topic);
		if (t == null) {
			t = new TopicManager(topic, matcher, messageHandler);
			topics.put(topic, t);
		} else {
			t.messageHandlers.add(messageHandler);
		}
	}
	
	public boolean removeHandler(String topic, RawWebsocketMessageHandler handler) {
		TopicManager t = topics.get(topic);
		if (t != null) {
			if (t.messageHandlers.remove(handler)) {
				if (t.messageHandlers.isEmpty()) {
					topics.remove(topic);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void subscribeErrorHandler(WebsocketErrorHandler websocketErrorHandler) {
		errorHandlers.add(websocketErrorHandler);
	}

	@Override
	public boolean unsubscribeErrorHandler(WebsocketErrorHandler websocketErrorHandler) {
		return errorHandlers.remove(websocketErrorHandler);
	}
	
	protected void dispatchWebsocketError(IOException error) {
		errorHandlers.forEach(h -> h.handleWebsocketError(error));
	}
	
	protected void dispatchMessage(String message) {
		List<TopicManager> allTopics = new ArrayList<>(topics.size());
		topics.values().stream().filter(t -> !t.messageHandlers.isEmpty()).forEach(allTopics::add);
		try {
			JsonParser jsonParser = jsonFactory.createParser(message.getBytes());
			for (JsonToken tok = jsonParser.nextToken(); tok != null && !allTopics.isEmpty(); tok = jsonParser.nextToken()) {
				if (tok == JsonToken.FIELD_NAME) {
					String fieldName = jsonParser.currentName();
					String value = getNextValue(jsonParser);
					dispatchToMessageTopicMatchers(fieldName, value, allTopics, message);
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
	
	private void dispatchToMessageTopicMatchers(String name, String value, List<TopicManager> topics, String rawMessage) {
		for (Iterator<TopicManager> it = topics.iterator(); it.hasNext();) {
			TopicManager topic = it.next();
			WebsocketMessageTopicMatchStatus matchResult = topic.matcher.matches(name, value);
			switch (matchResult) {
			case MATCHED:
				if (log.isDebugEnabled())
					log.debug("Dispatching message to " + topic.messageHandlers.size() + " handlers for topic [" + topic.topic + "]:[" + rawMessage + "]");
				topic.messageHandlers.forEach(t -> t.handleWebsocketMessage(rawMessage));
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
	}
	
	public class TopicManager {
		final String topic;
		final WebsocketMessageTopicMatcher matcher;
		final List<RawWebsocketMessageHandler> messageHandlers = new ArrayList<>();
		
		public TopicManager(String topic, WebsocketMessageTopicMatcher matcher,
				RawWebsocketMessageHandler messageHandler) {
			this.topic = topic;
			this.matcher = matcher;
			this.messageHandlers.add(messageHandler);
		}
	}

}
