package com.scz.jxapi.netutils.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
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
import com.scz.jxapi.exchange.ExchangeApi;
import com.scz.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatchStatus;
import com.scz.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatcher;
import com.scz.jxapi.netutils.websocket.multiplexing.WebsocketMessageTopicMatcherFactory;

/**
 * Default and sufficient implementation of {@link WebsocketManager} that wraps
 * a {@link Websocket} to manage subscriptions to topics (multiplexing), system
 * message handlers, error handlers, heartbeat and no message timeout features.
 * It can be provided a {@link WebsocketHook} that will take care of API
 * specific handshake, heartbeat and subscription messages.
 * 
 * @see WebsocketManager
 * @see Websocket
 * @see WebsocketHook
 */
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
	private final WebsocketErrorHandler websocketErrorHandler = this::notifyError;
	
	protected final ExchangeApi exchangeApi;
	
	protected final Websocket websocket;

	protected final WebsocketHook websocketHook;
	
	public DefaultWebsocketManager(ExchangeApi exchangeApi, 
								   Websocket websocket, 
								   WebsocketHook websocketHook) {
		this.exchangeApi = exchangeApi;
		this.websocket = websocket;
		this.websocketHook = websocketHook;
		this.writeExecutor = Executors.newSingleThreadScheduledExecutor();
		this.websocket.addErrorHandler(websocketErrorHandler);
		this.websocket.addMessageHandler(rawMessageHandler);
		if (websocketHook != null) {
			websocketHook.init(this);
		}
	}
	
	public long getHeartBeatInterval() {
		return heartBeatInterval;
	}

	public void setHeartBeatInterval(long heartBeatInterval) {
		if (heartBeatInterval > 0 && websocketHook == null) {
			throw new IllegalStateException("Cannot set heartbeat interval to " + heartBeatInterval + " > 0, because no websocket hook is set to provide heartbeat message to send");
		}
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
						  WebsocketMessageTopicMatcherFactory matcherFactory,
						  RawWebsocketMessageHandler messageHandler) {
		if (log.isDebugEnabled())
			log.debug("Scheduling subscribe request for topic:" + topic);
		writeExecutor.execute(() -> {
			try {
				String top = Optional.ofNullable(topic).orElse("");
				if (log.isDebugEnabled())
					log.debug("Executing subscribe request for topic:[" + topic + "]");
				TopicManager t = topics.get(top);
				if (t == null) {
					t = new TopicManager(top, matcherFactory, messageHandler, false);
					topics.put(top, t);
					// Remark: registered TopicManager before checking connection status and connecting if not already connected.
					// This is because the url provided for handshake may stand for a global stream endpoint (no topic/subscription)
					// and message would be disseminated right after handshake. Message handler must be registered before.
					if (!isConnected()) {
						if (log.isDebugEnabled())
							log.debug("Executing subscribe request for topic:[" + top + "]: not connected, connecting");
						connect();	
					}
					sendToTopicSubscription(topic);
				} else {
					throw new IllegalArgumentException("Already have a subscription for this topic");
				}
				if (log.isDebugEnabled())
					log.debug("DONE Executing subscribe request for topic:[" + top + "]");
			} catch (Exception ex) {
				notifyError(new WebsocketException("Error while subscribing to websocket for topic [" + topic + "]", ex));
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
	
	private void unbscribeFromTopic(String topic) throws WebsocketException {
		String unsubscribeRequestMessage = websocketHook == null? null: 
											websocketHook.getUnSubscribeRequestMessage(topic);
		if (unsubscribeRequestMessage != null) {
			if (log.isDebugEnabled())
				log.debug("Sending topic unsubscribe request:" + topic);
			websocket.send(unsubscribeRequestMessage);
		}
	}
	
	private void scheduleHeartBeatTask(HeartBeakTask heartBeakTask) {
		if (log.isDebugEnabled())
			log.debug("Scheduling heartbeat task in " + heartBeatInterval + " ms");
		this.writeExecutor.schedule(heartBeakTask, heartBeatInterval, TimeUnit.MILLISECONDS);
	}
	
	private void scheduleHeartBeatTimeoutTask(HeartBeakTimeoutTask heartBeakTimeoutTask) {
		if (log.isDebugEnabled())
			log.debug("Scheduling heartbeat timeout task in " + noHeartBeatResponseTimeout + " ms " + heartBeatTimeoutTaskCancelled.get());
		this.writeExecutor.schedule(heartBeakTimeoutTask, noHeartBeatResponseTimeout, TimeUnit.MILLISECONDS);
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
	
	protected final void connect() throws WebsocketException {
		if (isConnected() || isDisposed()) {
			return;
		}
		if(log.isInfoEnabled())
			log.info("Connecting WS:" + this);
		try {
			if (websocketHook != null) {
				websocketHook.beforeConnect();
			}
			websocket.connect();
			if (websocketHook != null) {
				websocketHook.afterConnect();
			}
			scheduleNoMessageTimeoutTask(this.messageReceivedCount.get());
			if (heartBeatInterval > 0 || noHeartBeatResponseTimeout > 0) {
				lastHeartBeatTime.set(System.currentTimeMillis());
				if (noHeartBeatResponseTimeout > 0) {
					if (this.heartBeatTimeoutTaskCancelled != null) {
						this.heartBeatTimeoutTaskCancelled.set(true);
					}
					this.heartBeatTimeoutTaskCancelled = new AtomicBoolean(false);
					scheduleHeartBeatTimeoutTask(new HeartBeakTimeoutTask(this.heartBeatTimeoutTaskCancelled));
				}
				if (heartBeatInterval > 0) {
					if (this.heartBeatTaskCancelled != null) {
						this.heartBeatTaskCancelled.set(true);
					}
					this.heartBeatTaskCancelled = new AtomicBoolean(false);
					scheduleHeartBeatTask(new HeartBeakTask(this.heartBeatTaskCancelled));
				}
			}
			
			connected.set(true);
			if(log.isInfoEnabled())
				log.info("Connected WS:" + this);
		} catch (Exception exception) {
			String msg = "Error while connecting websocket";
			if (log.isErrorEnabled())
				log.error(msg, exception);
			throw new WebsocketException(msg, exception);
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
			if (this.heartBeatTimeoutTaskCancelled != null) {
				this.heartBeatTimeoutTaskCancelled.set(true);
			}
			if (websocketHook != null) {
				try {
					websocketHook.beforeDisconnect();
				} catch (Exception e) {
					String errMsg = "Error while calling WebsocketHook#beforeDisconnect";
					log.error(errMsg, e);
					dispatchWebsocketError(new WebsocketException(errMsg, e));
				}
				
			}
 			websocket.disconnect();
 			if (websocketHook != null) {
				websocketHook.afterDisconnect();
			}
			if (log.isInfoEnabled()) {
				log.info("Disconnected " + this);
			}
		} catch (Exception e) {
			String errMsg = "Error while disconnecting websocket";
			log.error(errMsg, e);
			dispatchWebsocketError(new WebsocketException(errMsg, e));
		}
		connected.set(false);
	}
	
	@Override
	public void dispose() {
		if (!disposed.getAndSet(true)) {
			this.websocket.removeMessageHandler(rawMessageHandler);
			writeExecutor.execute(() -> {
				disconnect();
				this.websocket.removeErrorHandler(websocketErrorHandler);
			});
			
			writeExecutor.shutdown();
			writeExecutor = null;
		}
	}
	
	public boolean isConnected() {
		return this.connected.get();
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
	public void addSystemMessageHandler(String topic, WebsocketMessageTopicMatcherFactory matcher, RawWebsocketMessageHandler messageHandler) {
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
		if (isDisposed()) {
			throw new WebsocketException("Disposed");
		}
		if (!isConnected()) {
			connect();
		}
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
	public ExchangeApi getExchangeApi() {
		return this.exchangeApi;
	}

	@Override
	public CompletableFuture<WebsocketException> sendAsync(String msg) {
		CompletableFuture<WebsocketException> res = new CompletableFuture<>();
		if (isDisposed()) {
			res.complete(new WebsocketException(toString() + " is disposed"));
		} else {
			this.writeExecutor.schedule(() -> {
				try {
					send(msg);
					res.complete(null);
				} catch (WebsocketException ex) {
					res.complete(ex);
					onError("Error while sending message:" + msg, ex);
				}
			}, 0L, TimeUnit.MILLISECONDS);
		}
		return res;
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
		List<WebsocketMessageTopicMatcher> topicMatchers = new ArrayList<>(allTopics.size());
		allTopics.forEach(t -> topicMatchers.add(t.matcher.createWebsocketMessageTopicMatcher()));
		try {
			JsonParser jsonParser = jsonFactory.createParser(message.getBytes());
			for (JsonToken tok = jsonParser.nextToken(); tok != null && !allTopics.isEmpty(); tok = jsonParser.nextToken()) {
				if (tok == JsonToken.FIELD_NAME) {
					String fieldName = jsonParser.currentName();
					String value = null;
					switch (jsonParser.nextToken()) {
					case START_OBJECT:
						// Continue searching matching fields in nested structure.
						continue;
					case VALUE_FALSE:
						value = Boolean.FALSE.toString();
						break;
					case VALUE_TRUE:
						value = Boolean.TRUE.toString();
						break;
					default:
						value = jsonParser.getText();
						break;
					}
					
					if (dispatchToMessageTopicMatchers(fieldName, value, allTopics, topicMatchers, message)) {
						break;
					}
				}
			}
 			
		} catch (IOException e) {
			log.error("Error parsing websocket message [" + message + "]", e);
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
		if (!isDisposed()) {
			if (reconnectDelay > 0) {
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
				} catch (WebsocketException e) {
					// Avoid reentrant call, retry in a distinct runnable submitted to executor
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
												List<WebsocketMessageTopicMatcher> topicMatchers, 
												String rawMessage) {
		int i = 0;
		for (Iterator<TopicManager> it = messageHandlers.iterator(); it.hasNext(); i++) {
			TopicManager topic = it.next();
			WebsocketMessageTopicMatchStatus matchResult = topicMatchers.get(i).matches(name, value);
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
			default: // no match
				break;
			}
		}
		return false;
	}

	protected class TopicManager {
		final WebsocketMessageTopicMatcherFactory matcher;
		final RawWebsocketMessageHandler messageHandler;
		final String topic;
		final boolean systemMessage;
		
		public TopicManager(String topic, 
							WebsocketMessageTopicMatcherFactory matcherFactory,
							RawWebsocketMessageHandler messageHandler, 
							boolean systemMessage) {
			this.topic = topic;
			this.matcher = matcherFactory;
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
				// Normally no reachable, but better log exception here or it will missed.
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
				
				String hearBeatMessage = websocketHook.getHeartBeatMessage();
				if (hearBeatMessage == null) {
					String msg = "null heartbeat message provided by websocket hook" 
							+ websocketHook 
							+ " while heartbeat interval is set to " 
							+ heartBeatInterval;
					dispatchWebsocketError(new WebsocketException(msg));
				} else {
					websocket.send(hearBeatMessage);
				}
				if (log.isDebugEnabled()) {
					log.debug("Sending heartbeat:" + hearBeatMessage);
				}
				
				scheduleHeartBeatTask(this);
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
				if (timeElapsedSinceLastHeartBeat > noHeartBeatResponseTimeout) {
					onError("No heartbeat response since " 
											+ timeElapsedSinceLastHeartBeat 
											+ "ms, timeout:" + noHeartBeatResponseTimeout 
											+ " reconnect delay:" + getReconnectDelay(), null);
				} else {
					scheduleHeartBeatTimeoutTask(this);
				}
			} catch (Exception ex) {
				onError("Error while running heartbeat timeout task", ex);
			}
			
		}		
	}
}
