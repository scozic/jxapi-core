package com.scz.jcex.netutils.websocket.spring;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.glassfish.grizzly.threadpool.ThreadPoolConfig;
import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.container.grizzly.client.GrizzlyClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import com.scz.jcex.netutils.websocket.AbstractWebsocketManager;

public abstract class SpringWebsocketManager extends AbstractWebsocketManager {
	
	private static final Logger log = LoggerFactory.getLogger(SpringWebsocketManager.class);
	
	protected final String baseUrl;
	
	private ClientManager clientManager;
	
	private ThreadPoolTaskExecutor taskExecutor;
	
	private WebSocketSession webSocketSession;
	
	private int taskExecutorCounter = 0;
	
	private long heartBeatInterval = -1L;
	
	private long  noHeartBeatResponseTimeout = -1L;
	
	protected AtomicLong lastHeartBeatTime = new AtomicLong(0L);
	
	private AtomicBoolean heartBeatTaskCancelled = null;
	
	public SpringWebsocketManager(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	protected abstract String createHeartBeatMessage();

	@Override
	protected void send(String message) throws IOException {
		if (log.isDebugEnabled())
			log.debug("Sending >" + message);
		webSocketSession.sendMessage(new TextMessage(message));
	}

	@Override
	protected void doConnect() throws IOException {

		this.taskExecutor = new ThreadPoolTaskExecutor();
		this.taskExecutor.setThreadNamePrefix(baseUrl + "-" + taskExecutorCounter++);
		this.taskExecutor.setCorePoolSize(0);
		this.taskExecutor.setMaxPoolSize(2);
		this.taskExecutor.setKeepAliveSeconds(5);
		this.taskExecutor.initialize();
		
		this.clientManager = ClientManager.createClient();
		this.clientManager.getProperties().put(GrizzlyClientProperties.SELECTOR_THREAD_POOL_CONFIG, null);
		this.clientManager.getProperties().put(GrizzlyClientProperties.WORKER_THREAD_POOL_CONFIG, ThreadPoolConfig.defaultConfig().setCorePoolSize(1).setMaxPoolSize(1).setPoolName("WSDEMO_WORK"));
		StandardWebSocketClient client = new StandardWebSocketClient(clientManager);
		client.setTaskExecutor(taskExecutor);
		lastHeartBeatTime.set(System.currentTimeMillis());
		log.debug("Performing handshake");
		CountDownLatch websocketSessionAvailable = new CountDownLatch(1);
		ListenableFuture<WebSocketSession> futureSession = client.doHandshake(new SpringWebsocketHandler(this.taskExecutor), new WebSocketHttpHeaders(), getHandShakeURI());
		futureSession.addCallback(new WebsocketSessionCallback(websocketSessionAvailable));
		try {
			websocketSessionAvailable.await();
		} catch (InterruptedException e) {
			log.warn("Interrupted while waiting for websocket handshake");
		}
		if (webSocketSession == null) {
			throw new IllegalStateException("Handshake failed to initialize websocketSession");
		}
		log.debug("Done handshake");
		if (this.heartBeatTaskCancelled != null) {
			this.heartBeatTaskCancelled.set(true);
		}
		this.heartBeatTaskCancelled = new AtomicBoolean(false);
		
		if (heartBeatInterval > 0) {
			scheduleHeartBeatTask(new HeartBeakTask(this.heartBeatTaskCancelled));
		}
	}
	
	protected URI getHandShakeURI() throws IOException {
		try {
			return new URI(baseUrl);
		} catch (URISyntaxException e) {
			throw new IOException("Error creating URI for websocket base URL:" + baseUrl);
		}
	}

	private void scheduleHeartBeatTask(HeartBeakTask heartBeakTask) {
		this.writeExecutor.schedule(heartBeakTask, heartBeatInterval, TimeUnit.MILLISECONDS);
	}

	@Override
	protected void doDisconnect() throws IOException {
		log.info("Closing websocket");
		if (this.heartBeatTaskCancelled != null) {
			this.heartBeatTaskCancelled.set(true);
		}
		webSocketSession.close(CloseStatus.NORMAL);
		clientManager.shutdown();
		taskExecutor.shutdown();
		
		log.info("Websocket is closed");
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

	private class SpringWebsocketHandler implements WebSocketHandler {
		
		private final TaskExecutor dispatchMessageExecutor;
		
		public SpringWebsocketHandler(TaskExecutor dispatchMessageExecutor) {
			this.dispatchMessageExecutor = dispatchMessageExecutor;
		}

		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {
			log.debug("afterConnectionEstablished:session:" + session);
		}

		@Override
		public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
			log.debug("handleMessage:session:" + session + ", message:" + message);
			if (message instanceof TextMessage) {
				TextMessage m  = (TextMessage) message;
				dispatchMessageExecutor.execute(new DispatchTextMessageTask(m));
			} else {
				log.debug("handleMessage:message is not a TextMessage:" + message);
			}
		}

		@Override
		public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
			log.debug("handleTransportError:session:" + session + ", throwable:" + exception, exception);
		}

		@Override
		public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
			log.debug("afterConnectionClosed:session:" + session + ", closeStatus:" + closeStatus);
		}

		@Override
		public boolean supportsPartialMessages() {
			return false;
		}
		
	}
	
	private class WebsocketSessionCallback implements ListenableFutureCallback<WebSocketSession> {
		
		private final CountDownLatch websocketSessionAvailable;

		public WebsocketSessionCallback(CountDownLatch websocketSessionAvailable) {
			this.websocketSessionAvailable = websocketSessionAvailable;
		}

		@Override
		public void onSuccess(WebSocketSession result) {
			log.info("WebsocketSessionCallback:onSuccess:" + result);
			webSocketSession = result;
			websocketSessionAvailable.countDown();
		}

		@Override
		public void onFailure(Throwable ex) {
			log.error("WebsocketSessionCallback:onFailure:" + ex, ex);
			websocketSessionAvailable.countDown();
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
				
				// Raise error if max delay without heartbeat response timeout has elapsed
				long timeElapsedSinceLastHeartBeat = System.currentTimeMillis() - lastHeartBeatTime.get();
				if (noHeartBeatResponseTimeout > 0 && (timeElapsedSinceLastHeartBeat > noHeartBeatResponseTimeout)) {
					onError(new IOException("No heartbeat response since " + timeElapsedSinceLastHeartBeat + "ms"));
				} else {
					if (log.isDebugEnabled())
						log.debug("Sending heartBeat");
					send(createHeartBeatMessage());
					scheduleHeartBeatTask(this);
				}
			} catch (Exception ex) {
				onError(new IOException("Error while sending heartbeat", ex));
			}
			
		}
	}
	
	private class DispatchTextMessageTask implements Runnable {
		
		private final TextMessage textMessage;
		
		public DispatchTextMessageTask(TextMessage textMessage) {
			this.textMessage = textMessage;
		}

		@Override
		public void run() {
			try {
				dispatchMessage(new String(textMessage.asBytes()));
			} catch (Exception ex) {
				log.error("Error while dispatching message [" + new String(textMessage.asBytes()) + "]", ex);
			}
		}
	}
}
