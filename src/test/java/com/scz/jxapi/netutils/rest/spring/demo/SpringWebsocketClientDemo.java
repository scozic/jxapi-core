package com.scz.jxapi.netutils.rest.spring.demo;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

import org.glassfish.grizzly.threadpool.ThreadPoolConfig;
import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.container.grizzly.client.GrizzlyClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public class SpringWebsocketClientDemo {
	
	private static final Logger log = LoggerFactory.getLogger(SpringWebsocketClientDemo.class);
	
	private CountDownLatch websocketSessionAvailable = new CountDownLatch(1);
	
	private WebSocketSession webSocketSession;
	
	public SpringWebsocketClientDemo() throws URISyntaxException, InterruptedException, IOException {
		ClientManager clientManager = ClientManager.createClient();
	    clientManager.getProperties().put(GrizzlyClientProperties.SELECTOR_THREAD_POOL_CONFIG, null);
	    clientManager.getProperties().put(GrizzlyClientProperties.WORKER_THREAD_POOL_CONFIG, ThreadPoolConfig.defaultConfig().setCorePoolSize(1).setMaxPoolSize(1).setPoolName("WSDEMO_WORK"));
		StandardWebSocketClient client = new StandardWebSocketClient(clientManager);
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setThreadNamePrefix("WSDEMO");
		taskExecutor.setCorePoolSize(0);
		taskExecutor.setMaxPoolSize(2);
		taskExecutor.setKeepAliveSeconds(5);
		taskExecutor.initialize();
		client.setTaskExecutor(taskExecutor);
		log.info("Doing handshake");
		ListenableFuture<WebSocketSession> futureSession = client.doHandshake(new SpringWebsocketHandlerDemo(), new WebSocketHttpHeaders(), new URI("wss://data-stream.binance.com/ws"));
		futureSession.addCallback(new WebsocketSessionCallback());
		websocketSessionAvailable.await();
		log.info("Done handshake");
		if (webSocketSession == null) {
			throw new IllegalStateException("Failed to initialize websocketSession");
		}
		String topic = "!ticker@arr";
		log.info("Subscribing to:" + topic);
		String request = "{"
				+ "\"method\": \"SUBSCRIBE\","
				+ "\"params\":"
				+ "["
				+ "\""+ topic + "\""
				+ "],"
				+ "\"id\": 1"
				+ "}";
		webSocketSession.sendMessage(new TextMessage(request.getBytes()));
		Thread.sleep(15000L);
		log.info("Closing websocket");
		webSocketSession.close(CloseStatus.NORMAL);
		log.info("Websocket is closed");
	}
	
	public static void main(String[] args) {
		try {
			new SpringWebsocketClientDemo();
		} catch (Throwable t) {
			log.error("Uncaught exception", t);
			System.exit(-1);
		}
	}
	
	private class SpringWebsocketHandlerDemo implements WebSocketHandler {

		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {
			log.info("afterConnectionEstablished:session:" + session);
		}

		@Override
		public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
			log.info("handleMessage:session:" + session + ", message:" + message);
		}

		@Override
		public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
			log.info("handleTransportError:session:" + session + ", throwable:" + exception, exception);
		}

		@Override
		public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
			log.info("afterConnectionClosed:session:" + session + ", closeStatus:" + closeStatus);
		}

		@Override
		public boolean supportsPartialMessages() {
			return false;
		}
		
	}
	
	private class WebsocketSessionCallback implements ListenableFutureCallback<WebSocketSession> {

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

}
