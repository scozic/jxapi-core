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

public class DefaultWebsocketManager implements WebsocketManager {
	
	public static final long WRITE_EXECUTOR_KEEP_ALIVE = 30000L;
	
	private static final Logger log = LoggerFactory.getLogger(DefaultWebsocketManager.class); 
	
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
	
	private long heartBeatInterval = -1L;
	
	private long  noHeartBeatResponseTimeout = -1L;
	
	protected AtomicLong lastHeartBeatTime = new AtomicLong(0L);
	
	private AtomicBoolean heartBeatTaskCancelled = null;
	private AtomicBoolean heartBeatTimeoutTaskCancelled = null;
	private final RawWebsocketMessageHandler rawMessageHandler = this::dispatchMessage;
	
	protected final Websocket websocket;

	private final WebsocketHook websocketHook;
	
	public DefaultWebsocketManager(Websocket websocket, WebsocketHook websocketHook) {
		this.websocket = websocket;
		this.websocketHook = websocketHook;
		this.writeExecutor = Executors.newSingleThreadScheduledExecutor();
		this.websocket.addMessageHandler(rawMessageHandler);
		if (websocketHook != null) {
			websocketHook.afterInit(this);
		}
	}
	
	public long getHeartBeatInterval() {
		return heartBeatInterval;
	}

	public void setHeartBeatInterval(long heartBeatInterval) {
		this.heartBeatInterval = heartBeatInterval;
	}

	public long getNoHeartBeatResponseTimeout() {
		return noHeartBeatResponseTimeout;
	}

	public void setNoHeartBeatResponseTimeout(long noHeartBeatResponseTimeout) {
		this.noHeartBeatResponseTimeout = noHeartBeatResponseTimeout;
	}
	
	public WebsocketHook getWebsocketHook() {
		return websocketHook;
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
					// Remark: registered TopicManager before checking connection status and connecting if not already connected.
					// This is because the url provided for handshake may stand for a global stream endpoint (no topic/subscription)
					// and message would be disseminated right after handshake. Message handler must be registered before.
					if (!isConnected()) {
						if (log.isDebugEnabled())
							log.debug("Executing subscribe request for topic:" + topic + ": not connected, connecting");
						connect();	
					}
					sendToTopicSubscription(topic);
				} else {
					throw new IllegalArgumentException("Already have a subscription for this topic");
				}
				if (log.isDebugEnabled())
					log.debug("DONE Executing subscribe request for topic:" + topic);
			} catch (Exception ex) {
				dispatchWebsocketError(new WebsocketException("Error while subscribing to webscoket for topic [" + topic + "]", ex));
			}
		});
	}
	
	private void sendToTopicSubscription(String topic) {
		try {
			String subscribeRequestMessage = websocketHook == null? null: 
												websocketHook.getSubscribeRequestMessage(topic);
			if (subscribeRequestMessage != null) {
				if (log.isDebugEnabled())
					log.debug("Sending topic subscribe request:" + topic);
				websocket.send(subscribeRequestMessage);
			}
		} catch (WebsocketException e) {
			onError(e);
		}
	}
	
	private void unbscribeFromTopic(String topic) {
		try {
			String unsubscribeRequestMessage = websocketHook == null? null: 
												websocketHook.getUnSubscribeRequestMessage(topic);
			if (unsubscribeRequestMessage != null) {
				if (log.isDebugEnabled())
					log.debug("Sending topic subscribe request:" + topic);
				websocket.send(unsubscribeRequestMessage);
			}
		} catch (WebsocketException e) {
			onError(e);
		}
	}
	
	private void scheduleHeartBeatTask(HeartBeakTask heartBeakTask) {
		if (log.isDebugEnabled())
			log.debug("Scheduling heartbeat task in " + heartBeatInterval + " ms");
		this.writeExecutor.schedule(heartBeakTask, heartBeatInterval, TimeUnit.MILLISECONDS);
	}
	
	private void scheduleHeartTimeoutBeatTask() {
		if (log.isDebugEnabled())
			log.debug("Scheduling heartbeat timeout task in " + noHeartBeatResponseTimeout + " ms");
		this.writeExecutor.schedule(new HeartBeakTimeoutTask(heartBeatTaskCancelled), noHeartBeatResponseTimeout, TimeUnit.MILLISECONDS);
	}
	
	@Override
	public void unsubscribe(String topic) {
		writeExecutor.execute(() -> {
			try {
				TopicManager t = topics.remove(topic);
				if (t != null) {
					unbscribeFromTopic(topic);
				}
			} catch (Exception ex) {
				dispatchWebsocketError(new WebsocketException("Error while unsubscribing from websocket topic [" + topic + "]", ex));
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
			if (websocketHook != null) {
				websocketHook.beforeConnect(this);
			}
			websocket.connect();
			if (websocketHook != null) {
				websocketHook.afterConnect(this);
			}
			scheduleNoMessageTimeoutTask(this.messageReceivedCount.get());
			if (this.heartBeatTaskCancelled != null) {
				this.heartBeatTaskCancelled.set(true);
			}
			this.heartBeatTaskCancelled = new AtomicBoolean(false);
			if (this.heartBeatTimeoutTaskCancelled != null) {
				this.heartBeatTimeoutTaskCancelled.set(true);
			}
			this.heartBeatTimeoutTaskCancelled = new AtomicBoolean(false);
			
			if (heartBeatInterval > 0) {
				lastHeartBeatTime.set(System.currentTimeMillis());
				scheduleHeartBeatTask(new HeartBeakTask(this.heartBeatTaskCancelled));
			}
			connected.set(true);
			if(log.isInfoEnabled())
				log.info("Connected WS:" + this);
		} catch (Exception exception) {
			notifyError(new WebsocketException("Error while connecting websocket", exception));
		}
	}
	
	public boolean isDisposed() {
		return disposed.get();
	}

	protected final void disconnect() {
		if (!isConnected()) {
			return;
		}
		try {
			if (log.isInfoEnabled()) {
				log.info("Disconnecting " + this);
			}
			if (this.heartBeatTaskCancelled != null) {
				this.heartBeatTaskCancelled.set(true);
			}
			if (this.noMessageTimeoutTask != null) {
				this.noMessageTimeoutTask.cancelled.set(true);
			}
			if (websocketHook != null) {
				websocketHook.beforeDisconnect(this);
			}
 			websocket.disconnect();
 			if (websocketHook != null) {
				websocketHook.afterDisconnect(this);
			}
			if (log.isInfoEnabled()) {
				log.info("Disconnected " + this);
			}
		} catch (Exception e) {
			dispatchWebsocketError(new WebsocketException("Error while disconnecting websocket", e));
		}
		connected.set(false);
	}
	
	@Override
	public void dispose() {
		this.websocket.removeMessageHandler(rawMessageHandler);
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
	
	public Websocket getWebsocket() {
		return websocket;
	}
	
	@Override
	public void send(String msg) throws WebsocketException {
		websocket.send(msg);
	}

	@Override
	public void hearbeatReceived() {
		if (log.isDebugEnabled())
			log.debug(toString() + ":Received heartbeat response");
		this.lastHeartBeatTime.set(System.currentTimeMillis());
	}
	
	public String getUrl() {
		return websocket.getUrl();
	}
	
	public void setUrl(String url) {
		websocket.setUrl(url);
	}

	@Override
	public void sendAsync(String msg) {
		this.writeExecutor.schedule(() -> {
			try {
				send(msg);
			} catch (Exception ex) {
				onError("Error while sending message:" + msg, ex);
			}
		}, 0L, TimeUnit.MILLISECONDS);
		
	}
	
	protected void dispatchWebsocketError(WebsocketException error) {
		errorHandlers.forEach(h -> h.handleWebsocketError(error));
	}
	
	protected void dispatchMessage(String message) {
		this.dispatchSingleMessage(message);
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
	
	protected void onError(String msg, Throwable t) {
		onError(new WebsocketException(msg, t));
	}
	
	/**
	 * To be called from {@link #writeExecutor} thread when an error occurred.
	 * Will disconnect websocket, and try reconnect it after reconnect delay.
	 * @param exception
	 */
	protected void onError(WebsocketException exception) {
		log.error("Error raised on Websocket [" + toString() + "]", exception);
		this.dispatchWebsocketError(exception);
		if (!isDisposed() && reconnectDelay > 0) {
			if (log.isInfoEnabled()) {
				log.info("Disconnecting websocket [" +toString() + "] after error");
			}
			
			disconnect();
			if (reconnectDelay > 0) {
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
				} catch (WebsocketException e) {
					// Avoid reentrant call, retry in a distinct runnable sublitted to executor
					notifyError(e);
				}
			} else {
				if (log.isWarnEnabled()) {
					log.warn("No reconnect delay set for websocket [" + toString() + "], now disconnected");
				}	
			}
		}
	}
	
	@Override
	public void notifyError(WebsocketException exception) {
		writeExecutor.execute(() -> onError(exception));
	}
	
	private void resubscribeTopics() throws WebsocketException {
		if (log.isInfoEnabled())
			log.info("Resubscribing " + topics.size() + " topics after successful reconnection");
		for (String topic : topics.keySet()) {
			sendToTopicSubscription(topic);
		}
		if (log.isInfoEnabled())
			log.info("Successfully resubscribed to " + topics.size() + " topics after successful reconnection");
	}

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
					onError(new WebsocketException("No message received last " + noMessageTimeout + "ms, on websocket" + DefaultWebsocketManager.this + " reconnecting websocket"));
				}
			} catch (Exception ex) {
				log.error("Error while running NoMessageTimeout task", ex);
			}
			scheduleNoMessageTimeoutTask(newLastReceivedMessageCount);
		}
		
	}
	
	private class HeartBeakTask implements Runnable {
		
		private final AtomicBoolean cancelled;

		public HeartBeakTask(AtomicBoolean cancelled) {
			this.cancelled = cancelled;
		}

		@Override
		public void run() {
			try {
				if (cancelled.get()) {
					log.debug("Not running cancelled heartbeat task");
					return;
				}
				
				if (websocketHook == null) {
					throw new IllegalStateException("No websocket hook set on " 
														+ DefaultWebsocketManager.this.toString() 
														+ " to create heartbeat message");
				}
				String hearBeatMessage = websocketHook.getHeartBeatMessage();
				if (hearBeatMessage == null) {
					log.error("null heartbeat message provided by websocket hook" 
								+ websocketHook 
								+ " while heartbeat interval is set to " 
								+ heartBeatInterval);
				}
				if (log.isDebugEnabled()) {
					log.debug("Sending heartbeat:" + hearBeatMessage);
				}
				websocket.send(hearBeatMessage);
				scheduleHeartBeatTask(this);
				scheduleHeartTimeoutBeatTask();
			} catch (Exception ex) {				
				onError(new WebsocketException("Error while sending heartbeat", ex));
			}
			
		}
	}
	
	private class HeartBeakTimeoutTask implements Runnable {
		
		private final AtomicBoolean cancelled;
		
		HeartBeakTimeoutTask(AtomicBoolean cancelled) {
			this.cancelled = cancelled;
		}

		@Override
		public void run() {
			try {
				if (cancelled.get()) {
					log.debug("Not running cancelled heartbeat task");
					return;
				}
				
				// Raise error if max delay without heartbeat response timeout has elapsed
				long timeElapsedSinceLastHeartBeat = System.currentTimeMillis() - lastHeartBeatTime.get();
				if (noHeartBeatResponseTimeout > 0 && (timeElapsedSinceLastHeartBeat > noHeartBeatResponseTimeout)) {
					onError("No heartbeat response since " 
											+ timeElapsedSinceLastHeartBeat 
											+ "ms, timeout:" + noHeartBeatResponseTimeout 
											+ " reconnect delay:" + getReconnectDelay(), null);
				}
			} catch (Exception ex) {
				onError("Error while running heartbeat timeout task", ex);
			}
			
		}		
	}
}
