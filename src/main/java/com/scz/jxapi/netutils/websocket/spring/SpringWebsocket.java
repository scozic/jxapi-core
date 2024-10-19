package com.scz.jxapi.netutils.websocket.spring;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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

import com.scz.jxapi.netutils.websocket.AbstractWebsocket;
import com.scz.jxapi.netutils.websocket.WebsocketException;

/**
 * Websocket implementation using Spring's {@link StandardWebSocketClient}.
 * <p>
 * Implemntation notes:
 * <ul>
 * <li>Uses internal own {@link ThreadPoolTaskExecutor} for handling websocket messages</li>
 * <li>Uses Grizzly's {@link ClientManager} for websocket connection management</li>
 * <li>Customizes Grizzly's thread pool configuration to use a single worker thread</li>
 * </ul>
 * @see AbstractWebsocket
 * @see StandardWebSocketClient
 */
public class SpringWebsocket extends AbstractWebsocket {
	
	private static final Logger log = LoggerFactory.getLogger(SpringWebsocket.class);

	private static final long CONNECT_TIMEOUT = 30000L;
	
	private ClientManager clientManager;
	
	private ThreadPoolTaskExecutor taskExecutor;
	
	private WebSocketSession webSocketSession;
	
	private int taskExecutorCounter = 0;
	
	public SpringWebsocket() {}

	@Override
	protected void doSend(String message) throws WebsocketException {
		if (log.isDebugEnabled())
			log.debug("Sending >" + message);
		try {
			webSocketSession.sendMessage(new TextMessage(message));
		} catch (IOException e) {
			throw new WebsocketException(toString() + " error occurred while sending message:" + message);
		}
	}

	@Override
	protected void doConnect() throws WebsocketException {
		this.taskExecutor = new ThreadPoolTaskExecutor();
		this.taskExecutor.setThreadNamePrefix(url + "-" + taskExecutorCounter++);
		this.taskExecutor.setCorePoolSize(0);
		this.taskExecutor.setMaxPoolSize(2);
		this.taskExecutor.setKeepAliveSeconds(5);
		this.taskExecutor.initialize();
		this.clientManager = ClientManager.createClient();
		this.clientManager.getProperties().put(GrizzlyClientProperties.SELECTOR_THREAD_POOL_CONFIG, null);
		this.clientManager.getProperties().put(GrizzlyClientProperties.WORKER_THREAD_POOL_CONFIG, ThreadPoolConfig.defaultConfig().setCorePoolSize(1).setMaxPoolSize(1).setPoolName("WSDEMO_WORK"));
		StandardWebSocketClient client = new StandardWebSocketClient(clientManager);
		client.setTaskExecutor(taskExecutor);
		URI uri = getHandShakeURI();
		if (log.isInfoEnabled())
			log.info("Connecting websocket, URI:" + uri);
		CountDownLatch websocketSessionAvailable = new CountDownLatch(1);
		ListenableFuture<WebSocketSession> futureSession = client.doHandshake(new SpringWebsocketHandler(this.taskExecutor), new WebSocketHttpHeaders(), uri);
		futureSession.addCallback(new WebsocketSessionCallback(websocketSessionAvailable));
		boolean sessionAvailable = false;
		try {
			sessionAvailable = websocketSessionAvailable.await(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS );
		} catch (InterruptedException e) {
			log.warn("Interrupted while waiting for websocket handshake");
		}
		if (!sessionAvailable || webSocketSession == null) {
			String handShakeError = "Handshake failed: websocketSession not initialized";
			log.error(handShakeError + ". Disposing resources before reconnection attempt");
			doDisconnect();
			throw new WebsocketException(handShakeError);
		}
		if (log.isDebugEnabled()) {
			log.debug(toString() + ":Done handshake");
		}
	}
	
	protected URI getHandShakeURI() throws WebsocketException {
		URI uri = null;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			throw new WebsocketException("Error creating URI for websocket base URL:" + url);
		}
		String scheme = uri.getScheme();
		if (!"ws".equals(scheme) && !"wss".equals(scheme)) {
			throw new WebsocketException("Invalid scheme: " + scheme + " for:" + uri);
		}
		return uri;
	}



	@Override
	protected void doDisconnect() {
		if (log.isDebugEnabled())
			log.debug("Closing websocket");
		try {
			if (webSocketSession != null && webSocketSession.isOpen()) {
				webSocketSession.close(CloseStatus.NORMAL);
			}
		} catch (IOException e) {
			WebsocketException ex = new WebsocketException("Error disconnecting websocket " + this, e);
			log.error(ex.getMessage(), e);
			dispatchError(ex);
		}
		clientManager.shutdown();
		taskExecutor.shutdown();
		if (log.isDebugEnabled())
			log.debug("Websocket is closed");
	}

	private class SpringWebsocketHandler implements WebSocketHandler {
		
		private final TaskExecutor dispatchMessageExecutor;
		
		public SpringWebsocketHandler(TaskExecutor dispatchMessageExecutor) {
			this.dispatchMessageExecutor = dispatchMessageExecutor;
		}

		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {
			if (log.isDebugEnabled())
				log.debug("afterConnectionEstablished:session:" + session);
		}

		@Override
		public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
			if (log.isDebugEnabled())
				log.debug("handleMessage:session:" + session + ", message:" + message);
			if (message instanceof TextMessage) {
				TextMessage m  = (TextMessage) message;
				dispatchMessageExecutor.execute(new DispatchTextMessageTask(m));
			} else {
				if (log.isDebugEnabled())
					log.debug("handleMessage:message is not a TextMessage:" + message);
			}
		}

		@Override
		public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
			dispatchError(SpringWebsocket.this.toString() + ":handleTransportError:session:" + session, exception);
		}

		@Override
		public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
			if (log.isDebugEnabled())
				log.debug(SpringWebsocket.this.toString() + "afterConnectionClosed:session:" + session + ", closeStatus:" + closeStatus);
			if (closeStatus.getCode() != CloseStatus.NORMAL.getCode()) {
				dispatchError(new WebsocketException("Connection " + session + " closed abormally:" + closeStatus));
			}
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
			dispatchError(SpringWebsocket.this.toString() +  "Error raised on websocket session callback", ex);
			webSocketSession = null;
			websocketSessionAvailable.countDown();
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
				String msg = new String(textMessage.asBytes());
				if (log.isDebugEnabled())
					log.debug("Dispatching message:" + msg);
				dispatchMessage(msg);
			} catch (Exception ex) {
				log.error("Error while dispatching message [" + new String(textMessage.asBytes()) + "]", ex);
			}
		}
	}
}
