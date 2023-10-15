package com.scz.jxapi.netutils.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public abstract class AbstractWebsocketManager implements WebsocketManager {
	
	public static final long WRITE_EXECUTOR_KEEP_ALIVE = 30000L;
	
	private static final Logger log = LoggerFactory.getLogger(AbstractWebsocketManager.class);
	
	private final List<WebsocketErrorHandler> errorHandlers = new ArrayList<>();
	
	private final JsonFactory jsonFactory = new JsonFactory();
	
	protected final Map<String, TopicManager> topics = new ConcurrentHashMap<>();
	
	protected final List<TopicManager> systemMessageHandlers = Collections.synchronizedList(new ArrayList<>());
	
	protected final AtomicBoolean connected = new AtomicBoolean(false);
	protected final AtomicBoolean disposed = new AtomicBoolean(false);
	private final AtomicLong messageReceivedCount = new AtomicLong(0);
	
	protected ScheduledExecutorService writeExecutor = null;
	
	private long reconnectDelay = -1L;
	
	private long noMessageTimeout = -1L;
	
	private NoMessageTimeoutTask noMessageTimeoutTask = null;
	
	protected AbstractWebsocketManager() {
		this.writeExecutor = Executors.newSingleThreadScheduledExecutor(); 
	}

	@Override
	public void subscribe(String topic, 
						  WebsocketMessageTopicMatcher matcher,
						  RawWebsocketMessageHandler messageHandler) {
		if (log.isDebugEnabled())
			log.debug("Scheduling subscribe request for topic:" + topic);
		if (topic == null) {
			throw new IllegalArgumentException("null topic");
		}
		writeExecutor.execute(() -> {
			try {
				if (log.isDebugEnabled())
					log.debug("Executing subscribe request for topic:" + topic);
				TopicManager t = topics.get(topic);
				if (t == null) {
					t = new TopicManager(topic, matcher, messageHandler, false);
					topics.put(topic, t);
					String subscribeRequestMessage = getSubscribeRequestMessage(topic);
					// Remark: registered TopicManager before checking connection status and connecting if not already connected.
					// This is because the url provided for handshake may stand for a stream endpoint and message would be disseminated right after handshake. Message handler must be registered before.
					if (!isConnected()) {
						if (log.isDebugEnabled())
							log.debug("Executing subscribe request for topic:" + topic + ": not connected, connecting");
						connect();	
					}
					if (subscribeRequestMessage != null) {
						sendTopicSubscribeRequest(subscribeRequestMessage);
					}
				} else {
					throw new IllegalArgumentException("Already have a subscription for this topic");
				}
				if (log.isDebugEnabled())
					log.debug("DONE Executing subscribe request for topic:" + topic);
			} catch (Exception ex) {
				dispatchWebsocketError(new IOException("Error while subscribing to webscoket for topic [" + topic + "]", ex));
			}
		});
	}
	
	private void sendTopicSubscribeRequest(String subscribeRequestMessage) {
		try {
			if (log.isDebugEnabled())
				log.debug("Sending topic subscribe request:" + subscribeRequestMessage);
			send(subscribeRequestMessage);
		} catch (IOException e) {
			onError(e);
		}
	}
	
	@Override
	public void unsubscribe(String topic) {
		writeExecutor.execute(() -> {
			try {
				TopicManager t = topics.remove(topic);
				if (t != null) {
					send(getUnSubscribeRequestMessage(topic));
				}
			} catch (Exception ex) {
				dispatchWebsocketError(new IOException("Error while unsubscribing from websocket topic [" + topic + "]", ex));
			}
		});
	}
	
	protected final void connect() {
		if (isConnected() || isDisposed()) {
			return;
		}
		if(log.isInfoEnabled())
			log.info("Connecting WS:" + this);
		try {
			doConnect();
			scheduleNoMessageTimeoutTask(this.messageReceivedCount.get());
		} catch (Exception exception) {
			onError(new IOException("Error while connecting websocket", exception));
		}
		connected.set(true);
		if(log.isInfoEnabled())
			log.info("Connected WS:" + this);
	}
	
	public boolean isDisposed() {
		return disposed.get();
	}

	protected final void disconnect() {
		if (!isConnected()) {
			return;
		}
		try {
			if (this.noMessageTimeoutTask != null) {
				this.noMessageTimeoutTask.cancelled.set(true);
			}
			doDisconnect();
		} catch (Exception e) {
			dispatchWebsocketError(new IOException("Error while disconnecting websocket", e));
		}
		connected.set(false);
	}
	
	@Override
	public void dispose() {
		writeExecutor.execute(() -> {
			disconnect();
		});
		
		writeExecutor.shutdown();
		writeExecutor = null;
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
	
	public long getReconnectDelay() {
		return reconnectDelay;
	}

	public void setReconnectDelay(long reconnectDelay) {
		this.reconnectDelay = reconnectDelay;
	}
	
	public long getNoMessageTimeout() {
		return noMessageTimeout;
	}

	public void setNoMessageTimeout(long noMessageTimeout) {
		this.noMessageTimeout = noMessageTimeout;
	}
	
	protected void dispatchWebsocketError(IOException error) {
		errorHandlers.forEach(h -> h.handleWebsocketError(error));
	}
	
	protected void dispatchMessage(String message) {
//		long start = System.nanoTime();
		
//		splitJsonArray(message).forEach(this::dispatchSingleMessage);
		this.dispatchSingleMessage(message);
		
//		this.totalElapsedTimeParsing += System.nanoTime() - start;
//		this.nbMsgProcessed++;
//		if (nbMsgProcessed % 100 == 0) {
//			if (log.isInfoEnabled())
//				log.info("Average time nanos to process 1 message:" + (BigDecimal.valueOf(totalElapsedTimeParsing).divide(BigDecimal.valueOf(nbMsgProcessed), RoundingMode.CEILING)));
//		}
	}
	
	private void dispatchSingleMessage(String message) {
		messageReceivedCount.incrementAndGet();
		List<TopicManager> allTopics = new ArrayList<>(topics.size() + systemMessageHandlers.size());
		allTopics.addAll(systemMessageHandlers);
		allTopics.addAll(topics.values());
		allTopics.forEach(t -> t.matcher.reset());
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
	
//	private List<String> splitJsonArray(String messages) {
//		try {
//			JsonParser jsonParser = jsonFactory.createParser(messages.getBytes());
//			if (jsonParser.nextToken() != JsonToken.START_ARRAY) {
//				return List.of(messages);
//			}
//			
//			ObjectMapper m = new ObjectMapper();
//			ArrayNode array;
//			array = (ArrayNode) m.readTree(messages);
//			List<String> res = new ArrayList<>(array.size());
//			for (Iterator<JsonNode> it = array.elements(); it.hasNext();) {
//				JsonNode node = it.next();
//				res.add(m.writeValueAsString(node));
//			}
//			return res;
//		} catch (IOException e) {
//			log.error("Error while splitting array message:" + messages, e);
//			return List.of();
//		}
//	}
	
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
	
	/**
	 * To be called from {@link #writeExecutor} thread when an error occurred.
	 * Will disconnect websocket, and try reconnect it after reconnect delay.
	 * @param exception
	 */
	protected void onError(IOException exception) {
		log.error("Error raised on Websocket [" + toString() + "]", exception);
		this.dispatchWebsocketError(exception);
		if (!isDisposed() && reconnectDelay > 0) {
			if (log.isInfoEnabled()) {
				log.info("Disconnecting websocket [" +toString() + "] after error");
			}
			
			disconnect();
			if (log.isInfoEnabled()) {
				log.info("Will try to reconnect websocket [" + toString() + "] in " + reconnectDelay + "ms");
			}
			try {
				Thread.sleep(reconnectDelay);
			} catch (InterruptedException e) {
				log.warn("Interrupted while sleeping till reconnect delay has elapsed for websocket [" + toString() + "]");
			}
			try {
				connect();
				resubscribeTopics();
			} catch (IOException e) {
				writeExecutor.execute(() -> onError(e));
			}
		}
	}
	
	private void resubscribeTopics() throws IOException {
		if (log.isInfoEnabled())
			log.info("Resubscribing " + topics.size() + " topics after successful reconnection");
		for (String topic : topics.keySet()) {
			send(getSubscribeRequestMessage(topic));
		}
		if (log.isInfoEnabled())
			log.info("Successfully resubscribed to " + topics.size() + " topics after successful reconnection");
	}
	
	protected abstract void doConnect() throws IOException;
	protected abstract void doDisconnect() throws IOException;
	protected abstract void send(String message) throws IOException;
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
	
	private void scheduleNoMessageTimeoutTask(long lastReceivedMessageCount) {
		if (this.noMessageTimeout > 0) {
			if (this.noMessageTimeoutTask != null) {
				this.noMessageTimeoutTask.cancelled.set(true);
			}
			this.noMessageTimeoutTask = new NoMessageTimeoutTask(lastReceivedMessageCount);
			if (log.isDebugEnabled()) {
				log.debug("Will check in " + this.noMessageTimeout + "ms if received message count has increased, current:" + lastReceivedMessageCount);
			}
			writeExecutor.schedule(noMessageTimeoutTask, this.noMessageTimeout, TimeUnit.MILLISECONDS);
		}
	}
	
	private class NoMessageTimeoutTask implements Runnable {
		
		final AtomicBoolean cancelled = new AtomicBoolean(false);
		
		final long lastReceivedMessageCount;
		public NoMessageTimeoutTask(long lastReceivedMessageCount) {
			this.lastReceivedMessageCount = lastReceivedMessageCount;
		}

		@Override
		public void run() {
			if (isDisposed() || cancelled.get() || !isConnected()) {
				if (log.isDebugEnabled()) {
					log.debug("No executing " + this);
				}
				return;
			}
			long newLastReceivedMessageCount = messageReceivedCount.get();
			try {
				if (log.isDebugEnabled()) {
					log.debug("Checking message count has increased, lastReceivedMessageCount:" + lastReceivedMessageCount + ", current:" + newLastReceivedMessageCount);
				}
				if (newLastReceivedMessageCount <= this.lastReceivedMessageCount) {
					onError(new IOException("No message received last " + noMessageTimeout + "ms, on websocket" + AbstractWebsocketManager.this + " reconnecting websocket"));
				}
			} catch (Exception ex) {
				log.error("Error while running NoMessageTimeout task", ex);
			}
			scheduleNoMessageTimeoutTask(newLastReceivedMessageCount);
		}
		
	}
}
